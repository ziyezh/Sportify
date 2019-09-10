package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.sensorDetector.holdingDetector;
import com.example.a54297.musicselect.sensorDetector.movingDetector;

public class RecommendActivity extends BaseActivity {


    //当前状态 0 静止  1 走路  2 快走  3 慢跑  4 快跑
    final Animation rotateAnimation1 = new RotateAnimation(0f,1800f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    final Animation rotateAnimation2 = new RotateAnimation(0f,-1800f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    private int presentState = 0;
    private String[] states = new String[5];
    private TextView view4;//view1,view2,

    private int time=0,activityState=0;
    //time: 0夜晚  1上午/清晨  2中午/下午  3傍晚
    //activaty  0静止  1慢走 2快走 3慢跑 4疾跑  修改函数为void input(int time,int activity);
    private LinearLayout linear2;
    private FrameLayout frame1,frame3;
    private ImageView stateView,imageStart,imageLoading1,imageLoading2,imageloading0;
    private Random random = new Random();

    //传感器
    private SensorManager sensorManager;
    private Sensor lightSensor;//光传感器
    private Sensor accelerometerSensor;//加速度传感器
    private Sensor magneticSensor;//磁场传感器
    private Sensor proximitySensor;//近程传感器
    //传感器数据
    private float[] Ro = new float[9];
    private float[] orientation = new float[3];//方向角
    private float[] geomagnetic = null;//磁场
    private float[] gravity = null;//加速度
    private float proximity;//距离
    private float light;//光强
    private float accelerometer = 0;
    private movingDetector move = new movingDetector(RecommendActivity.this);
    private holdingDetector holding;
    //运动状态 结果
    private int finalResult=0;
    //是否更新传感器
    boolean isUp=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        //传感器管理器及各类传感器定义
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //手机握持状态检测器
        holding = new holdingDetector(orientation, proximity, light);
//        lightView = fd(R.id.lightView);
//        accelerometerView = fd(R.id.accelerometerView);
//        holdingView = fd(R.id.holdingView);

        initData();
        initView();

        {//onstartclick函数
            imageStart.setVisibility(View.INVISIBLE);
            imageloading0.setVisibility(View.VISIBLE);
            imageLoading1.setVisibility(View.VISIBLE);
            imageLoading2.setVisibility(View.VISIBLE);

            rotateAnimation1.setDuration(5000);						//持续时间
            imageLoading1.setAnimation(rotateAnimation1);					//设置动画
            rotateAnimation1.startNow();

            rotateAnimation2.setDuration(5000);
            imageLoading2.setAnimation(rotateAnimation2);
            rotateAnimation2.startNow();

            isUp=true;
        }//onstartclick函数 结束
    }

    private void initData(){
        isUp=true;
        long r1 = System.currentTimeMillis();
        random.setSeed(r1);
        //种子
        Calendar calendar = Calendar.getInstance();
        int temp_time = calendar.get(Calendar.HOUR_OF_DAY);
        if(temp_time>=20&&temp_time<6){
            //0夜晚  1上午/清晨  2中午/下午  3傍晚
            time=0;
        }else if(temp_time>=6&&temp_time<10){
            time=1;
        }else if(temp_time>=10&&temp_time<17){
            time=2;
        }else if(temp_time>=17&&temp_time<20){
            time=3;
        }else {
            time =0;
        }
        System.out.println("time值："+time);
        states[0]="休闲";
        states[1]="散步";
        states[2]="步行";
        states[3]="运动";
        states[4]="跑步";
//        view1=findViewById(R.id.textView1);
//        view2=findViewById(R.id.textView2);
        stateView=findViewById(R.id.state);
//        switch1=findViewById(R.id.switch1);
        view4=findViewById(R.id.textView4);
        imageStart=findViewById(R.id.start);
        imageLoading1=findViewById(R.id.loading1);
        imageLoading2=findViewById(R.id.loading2);
        imageloading0=findViewById(R.id.loading0);
        frame1=findViewById(R.id.frame1);
        ////设置
        linear2=findViewById(R.id.linear2);
        //检测结果
        frame3=findViewById(R.id.frame3);
        //开始检测
    }

    private void initView(){

        initNavBar(true,"实时推荐",false);
//        view1.setVisibility(View.INVISIBLE);
//        view2.setVisibility(View.INVISIBLE);
//        switch1.setVisibility(View.INVISIBLE);

        stateView.setImageResource(R.drawable.morning_peace1);
        frame1.setVisibility(View.INVISIBLE);
        linear2.setVisibility(View.INVISIBLE);
        frame3.setVisibility(View.VISIBLE);
        imageloading0.setVisibility(View.INVISIBLE);
        imageLoading1.setVisibility(View.INVISIBLE);
        imageLoading2.setVisibility(View.INVISIBLE);


    }
    void input(int Time,int Activities){
        time=Time;
        activityState=Activities;
    }

    void setPic(){

        switch (time){
            case 0://0夜晚===================
            {
                switch(activityState){
                    case 0:{
                        int temp_r=random.nextInt()%3;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.night_peace1);
                        }
                        else if(temp_r==1) {
                            stateView.setImageResource(R.drawable.night_peace2);
                        }
                        else{ stateView.setImageResource(R.drawable.night_peace3);}
                        break;
                    }
                    case 1:{
                        stateView.setImageResource(R.drawable.night_walk);
                        break;
                    }
                    case 2:{
                        stateView.setImageResource(R.drawable.night_walk);
                        break;
                    }
                    case 3:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.night_run1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.night_run2);
                        }
                        break;
                    }
                    case 4:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.night_run1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.night_run2);
                        }
                        break;
                    }
                }
                break;
            }
            case 1://1上午/清晨=============
            {
                switch(activityState){
                    case 0:{
                        int temp_r=random.nextInt()%3;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.morning_peace1);
                        }
                        else if(temp_r==1) {
                            stateView.setImageResource(R.drawable.morning_peace2);
                        }
                        else{ stateView.setImageResource(R.drawable.morning_peace3);}
                        break;
                    }
                    case 1:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.morning_walk1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.morning_walk2);
                        }
                        break;
                    }
                    case 2:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.morning_walk1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.morning_walk2);
                        }
                        break;
                    }
                    case 3:{
                        stateView.setImageResource(R.drawable.morning_walk_run);
                        break;
                    }
                    case 4:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.morning_walk_run);
                        }
                        else {
                            stateView.setImageResource(R.drawable.morning_run);
                        }
                        break;
                    }
                }
                break;
            }
            case 2://2中午/下午==============
            {
                switch(activityState){
                    case 0:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.noon_peace1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.noon_peace2);
                        }
                        break;
                    }
                    case 1:{
                        stateView.setImageResource(R.drawable.noon_walk);
                        break;
                    }
                    case 2:{
                        stateView.setImageResource(R.drawable.noon_walk);
                        break;
                    }
                    case 3:{
                        int temp_r=random.nextInt()%3;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.noon_run1);
                        }
                        else if(temp_r==1) {
                            stateView.setImageResource(R.drawable.noon_run2);
                        }
                        else{ stateView.setImageResource(R.drawable.noon_run3);}
                        break;
                    }
                    case 4:{
                        int temp_r=random.nextInt()%3;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.noon_run1);
                        }
                        else if(temp_r==1) {
                            stateView.setImageResource(R.drawable.noon_run2);
                        }
                        else{ stateView.setImageResource(R.drawable.noon_run3);}
                        break;
                    }
                }
                break;
            }
            case 3://3傍晚=================
            {
                switch(activityState){
                    case 0:{
                        stateView.setImageResource(R.drawable.evening_peace);
                        break;
                    }
                    case 1:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.evening_walk1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.evening_walk2);
                        }
                        break;
                    }
                    case 2:{
                        int temp_r=random.nextInt()%2;
                        if(temp_r==0) {
                            stateView.setImageResource(R.drawable.evening_walk1);
                        }
                        else {
                            stateView.setImageResource(R.drawable.evening_walk2);
                        }
                        break;
                    }
                    case 3:{
                        stateView.setImageResource(R.drawable.evening_run);
                        break;
                    }
                    case 4:{
                        stateView.setImageResource(R.drawable.evening_run);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void onChangeDown(View v){
        //改变状态：前一个  同时
        presentState--;
        if(presentState<0){presentState=4;}

        input(time,presentState);
        setPic();
        view4.setText(states[presentState]);

    }

    public void onChangeUp(View v){
        //改变状态：下一个  同时
        presentState++;
        if(presentState>4) {
            presentState=0;
        }

        input(time,presentState);
        setPic();
        view4.setText(states[presentState]);

    }

    public void onStartClick(View v){
        imageStart.setVisibility(View.INVISIBLE);
        imageloading0.setVisibility(View.VISIBLE);
        imageLoading1.setVisibility(View.VISIBLE);
        imageLoading2.setVisibility(View.VISIBLE);
        frame3.setVisibility(View.VISIBLE);

        isUp=true;
        rotateAnimation1.setDuration(5000);						//持续时间
        imageLoading1.setAnimation(rotateAnimation1);					//设置动画
        rotateAnimation1.startNow();

        rotateAnimation2.setDuration(5000);
        imageLoading2.setAnimation(rotateAnimation2);
        rotateAnimation2.startNow();
    }

    public void onLoading(View v){
        if(rotateAnimation2.hasEnded()){//
            System.out.println("----rotateAnimation2.hasEnded()");
            isUp=false;
            presentState=finalResult;
            System.out.println("finalresult值:"+finalResult);
            input(time,presentState);
            setPic();
            frame3.setVisibility(View.INVISIBLE);
            linear2.setVisibility(View.VISIBLE);
            frame1.setVisibility(View.VISIBLE);
        }else {//rotateAnimation2.hasStarted()
//            rotateAnimation1.cancel();
//            rotateAnimation2.cancel();
            //rotateAnimation2.start();
            isUp=true;
            imageLoading1.setAnimation(null);
            imageLoading2.setAnimation(null);
            imageStart.setVisibility(View.VISIBLE);
            imageloading0.setVisibility(View.INVISIBLE);
            imageLoading1.setVisibility(View.INVISIBLE);
            imageLoading2.setVisibility(View.INVISIBLE);
            frame3.setVisibility(View.INVISIBLE);
        }
    }  public void toMusic(View v){
        Intent intent = new Intent(this,SelectMusicActivity.class);
        intent.putExtra(SelectMusicActivity.ALBUM_ID, String.valueOf(presentState));

        System.out.println("----toMusic3 presentState"+presentState);
        this.startActivity(intent);
        finish();
        System.out.println("finalresult----="+finalResult);
    }//-------------------------------------end--------------------------------------

    //传感器数据
    //监听传感器数据变化
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        //数值变化
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //注意这个改变方法，会因为传感器值改变而执行
            //记录传感器类型及数据
            StringBuffer accelerometerMessage = new StringBuffer();
            int sensorType = sensorEvent.sensor.getType();
            float[] value = sensorEvent.values;

            switch (sensorType) {
                //光传感器，录入手机握持状态监控器
                case Sensor.TYPE_LIGHT:
                    light = value[0];
                    holding.setLight(light);
//                    lightView.setText("light value" + light);
                    //value包含3个值，x,y,z但对与光线传感器只有第一个值
                    break;
                //加速度传感器，记录三轴数据，计算合加速度
                case Sensor.TYPE_ACCELEROMETER:
                    gravity = value;
                    accelerometer = Accelerometer(gravity);
                    break;
                //近程出传感器，录入手机握持状态监控器
                case Sensor.TYPE_PROXIMITY:
                    proximity = value[0];
                    //value只有一个值，并且近为0、远为5、单位cm
                    holding.setProximity(proximity);
                    break;
                //磁场传感器，记录值，帮助计算Orientation[]
                case Sensor.TYPE_MAGNETIC_FIELD:
                    geomagnetic = value;
                    break;
            }

            //计算方向角orientation（0方向角，1俯仰角，2翻转角）
            Orientation(orientation);
            //计算合加速度

            accelerometerMessage.append("合加速度：" + accelerometer);
            //计算手机握持状态
            int holdStyle = holding.holdingStyle();
//            holdingView.setText("\n手机握持状态：" + holdStyle);

            int check = move.inputValue(accelerometer, holdStyle);
            if(isUp){
                finalResult=check;
                System.out.println("finalresult---- "+finalResult);
            }
            if(isUp&&rotateAnimation2.hasEnded()){
                isUp=false;
                presentState=finalResult;
                System.out.println("结束判断");
                input(time,presentState);
                setPic();
                frame3.setVisibility(View.INVISIBLE);
                linear2.setVisibility(View.VISIBLE);
                frame1.setVisibility(View.VISIBLE);
                view4.setText(states[presentState]);
            }

            switch (check) {
                case 0:
                    Log.i("====", "===静止===");
                    accelerometerMessage.append("\n运动状态： 静止");
                    break;
                case 1:
                    Log.i("====", "===走===");
                    accelerometerMessage.append("\n运动状态： 走");
                    break;
                case 2:
                    Log.i("====", "===快走===");
                    accelerometerMessage.append("\n运动状态： 快走");
                    break;
                case 3:
                    Log.i("====", "===慢跑===");
                    accelerometerMessage.append("\n运动状态： 慢跑");
                    break;
                case 4:
                    Log.i("====", "===跑===");
                    accelerometerMessage.append("\n运动状态： 跑");
                    break;
                case 6:
                    Log.i("====", "===摇晃===");
                    accelerometerMessage.append("\n运动状态： 摇晃");
                    break;
                default:
                    Log.i("====", "===加载===");
                    accelerometerMessage.append("\n运动状态： 加载");
                    break;
            }
//            accelerometerView.setText(accelerometerMessage.toString());
        }

        //精度变化
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onResume() {                                 //注意状态方法
        super.onResume();
        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);      //注册传感器
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {                      //注意状态方法
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);           //取消注册
    }

    //计算合加速度
    public float Accelerometer(float[] value) {
        double temp_x = (float) value[0];
        double temp_y = (float) value[1];
        double temp_z = (float) value[2];
        double result = Math.sqrt(Math.pow(temp_x, 2.0) + Math.pow(temp_y, 2.0) + Math.pow(temp_z, 2.0));

        return (float) result;
    }

    //计算方向角
    public void Orientation(float[] value) {
        if (gravity != null && geomagnetic != null) {
            if (SensorManager.getRotationMatrix(Ro, null, gravity, geomagnetic)) {
                SensorManager.getOrientation(Ro, value);
                value[0] = (float) ((360f + value[0] * 180f / Math.PI) % 360);
                value[1] = (float) Math.toDegrees(value[1]);
                value[2] = (float) Math.toDegrees(value[2]);
                holding.setOrien(value);
                Log.i("====", "===Orien[0]===" + value[0] + "===value[1]" + value[1] + "value[2]" + value[2]);
                System.out.println("=======Orien[0]===" + value[0] + "===value[1]" + value[1] + "value[2]" + value[2]);
            }
        }
    }
}
