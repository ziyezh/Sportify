package com.example.a54297.musicselect.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.a54297.musicselect.sensorDetector.holdingDetector;
import com.example.a54297.musicselect.Help.UserHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.a54297.musicselect.sensorDetector.movingDetector;

public class MeActivity extends BaseActivity {

    private TextView mTvUser;
    private TextView lightView;
    private TextView orienView;
    private TextView accelerometerView;

    private SensorManager sensorManager;
    private Sensor lightSensor;//光传感器
    private Sensor accelerometerSensor;//加速度传感器
    private Sensor magneticSensor;//磁场传感器
    private Sensor proximitySensor;//近程传感器

    private float[] Ro = new float[9];
    private float[] orientation = new float[3];//方向角
    private float[] geomagnetic = null;//磁场
    private float[] gravity = null;//加速度
    private float proximity;//距离
    private float light;//光强
    private float accelerometer = 0;
    private movingDetector move = new movingDetector(MeActivity.this);
    private holdingDetector holding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        sensorManager=(SensorManager) this.getSystemService(SENSOR_SERVICE);        //传感器服务
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);               //指定传感器
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        holding = new holdingDetector(orientation,proximity,light);
        lightView = (TextView) fd(R.id.lightView);
        accelerometerView = (TextView) fd(R.id.accelerometerView);
        orienView = (TextView) fd(R.id.orienView);

        initView();
    }

    private void initView(){
        initNavBar(true,"个人中心",false);

        mTvUser = fd(R.id.tv_user);
        mTvUser.setText("用户名："+ UserHelp.getInstance().getPhone());
    }

    public void onChangeClick(View v){
        startActivity(new Intent(this,ChangePasswordActivity.class));
    }

    public void onLogoutClick(View v){
        UserUtils.logout(this);
    }

    private SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //注意这个改变方法，会因为传感器只改变而执行
            int sensorType = sensorEvent.sensor.getType();
            float[] value = sensorEvent.values;
            switch (sensorType) {

                case Sensor.TYPE_LIGHT:
                    light = value[0];
                    holding.setLight(light);
                    lightView.setText("light value" + light);
                    //value包含3个值，x,y,z但对与光线传感器只有第一个值
                    break;
                case Sensor.TYPE_ACCELEROMETER: {

                    gravity = value;
                    accelerometer = Accelerometer(value);
                    break;
                }
                case Sensor.TYPE_PROXIMITY:
                    proximity = value[0];
                    holding.setProximity(proximity);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    geomagnetic = value;
                    break;
            }

            StringBuffer oMessage = new StringBuffer();
                Orientation(orientation);
                oMessage.append("\n计算所得方位角：");
                oMessage.append(orientation[0]);
                oMessage.append("\n计算所得俯仰角：");
                oMessage.append(orientation[1]);
                oMessage.append("\n计算所得翻转角：");
                oMessage.append(orientation[2]);

            int holdStyle = holding.holdingStyle();
            oMessage.append("\nstyle：");
            oMessage.append(holdStyle);
            orienView.setText(oMessage.toString());
            StringBuffer accelerometerMessage = new StringBuffer();
            accelerometerMessage.append("\n计算所得总加速度：");
            accelerometerMessage.append(accelerometer);
            accelerometerMessage.append("\n显示用户如今运动状态：");

            int check = move.inputValue(accelerometer,holdStyle);
            switch (check) {
                case 0:
                    accelerometerMessage.append("静止");
                    break;
                case 1:
                    accelerometerMessage.append("散步");
                    break;
                case 2:
                    accelerometerMessage.append("快走");
                    break;
                case 3:
                    accelerometerMessage.append("慢跑");
                    break;
                case 4:
                    accelerometerMessage.append("快跑");
                    break;
                default:
                    accelerometerMessage.append("...");
                    break;
            }

            accelerometerView.setText(accelerometerMessage.toString());


        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected  void onResume(){                                 //注意状态方法
        super.onResume();
        sensorManager.registerListener( sensorEventListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);      //注册传感器
        sensorManager.registerListener( sensorEventListener,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener( sensorEventListener,magneticSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener( sensorEventListener,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected  void onPause() {                      //注意状态方法
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);           //取消注册          }
    }

    //计算合加速度
    public float Accelerometer(float[] value){
        double temp_x = (float)value[0];
        double temp_y = (float)value[1];
        double temp_z = (float)value[2];
        double result = Math.sqrt(Math.pow(temp_x,2.0) + Math.pow(temp_y,2.0) + Math.pow(temp_z,2.0));

        return (float)result;
    }

    //计算方向角
    public void Orientation(float[] value) {
        if(gravity != null && geomagnetic != null){
            if(SensorManager.getRotationMatrix(Ro,null,gravity,geomagnetic))
                SensorManager.getOrientation(Ro,value);
                value[0] = (float) ((360f+value[0]*180f/Math.PI)%360);
                value[1] = (float) Math.toDegrees(value[1]);
                value[2] = (float) Math.toDegrees(value[2]);
                holding.setOrien(value);
                Log.i("====","===Orien[0]==="+value[0]+"===value[1]"+value[1]+"value[2]"+value[2]);
                System.out.println("=======Orien[0]==="+value[0]+"===value[1]"+value[1]+"value[2]"+value[2]);
        }

    }
}
