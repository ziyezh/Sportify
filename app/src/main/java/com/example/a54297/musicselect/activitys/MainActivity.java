package com.example.a54297.musicselect.activitys;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a54297.musicselect.Help.RealmHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.adapters.MusicGridAdapter;
import com.example.a54297.musicselect.adapters.MusicListAdapter;
import com.example.a54297.musicselect.models.MusicSourceModel;
import com.example.a54297.musicselect.views.GridSpaceItemDecoration;
//import com.example.a54297.musicselect.sensorDetector.movingDetector;
//import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private RecyclerView mRvGrid, mRvList;
    private MusicGridAdapter mGridAdapter;
    private MusicListAdapter mListAdaper;
    private RealmHelp mRealHelp;
    private MusicSourceModel mMusicSourceModel;
    //private SensorManager sensorManager;
    //private Sensor accelerometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

    }

    private void initData(){
        mRealHelp = new RealmHelp();
        mMusicSourceModel = mRealHelp.getMusicSource();
//        mRealHelp.close();
    }

    private void initView(){
        initNavBar(false,"SRTP音乐",true);
        //sensorManager=(SensorManager) this.getSystemService(SENSOR_SERVICE);        //传感器服务
        //accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);               //指定传感器

        mRvGrid = fd(R.id.rv_grid);
        mRvGrid.setLayoutManager(new GridLayoutManager(this,3));
        mRvGrid.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize),mRvGrid));
        mRvGrid.setNestedScrollingEnabled(false);
        mGridAdapter = new MusicGridAdapter(this,mMusicSourceModel.getAlbum());
        mRvGrid.setAdapter(mGridAdapter);

        /**
         * 1、假如已知列表高度，可以直接在布局中把Rycycle高度布局定义上
         * 2、不知道的话要手动计算
         */


        mRvList = fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRvGrid.setNestedScrollingEnabled(false);
        mListAdaper = new MusicListAdapter(this,mRvList,mMusicSourceModel.getHot());
        mRvList.setAdapter(mListAdaper);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealHelp.close();
    }


/*
    private SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {        //注意这个改变方法，会因为传感器只改变而执行
            float[] temp= sensorEvent.values;
            float accelerometer = Accelerometer(temp);
            movingDetector move = new movingDetector();
            switch (move.inputValue(accelerometer)) {
                case 0:Toast.makeText(MainActivity.this,"resting",Toast.LENGTH_LONG).show();
                break;
                case 1:Toast.makeText(MainActivity.this,"walking",Toast.LENGTH_LONG).show();
                break;
                case 2:Toast.makeText(MainActivity.this,"slow running",Toast.LENGTH_LONG).show();
                break;
                case 3:Toast.makeText(MainActivity.this,"running",Toast.LENGTH_LONG).show();
                break;
                default:Toast.makeText(MainActivity.this,"wait",Toast.LENGTH_LONG).show();
                break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public float Accelerometer(float[] value){
        double temp_x = (float)value[0];
        double temp_y = (float)value[1];
        double temp_z = (float)value[2];
        double result = Math.sqrt(Math.pow(temp_x,2.0) + Math.pow(temp_y,2.0) + Math.pow(temp_z,2.0));

        return (float)result;
    }

    @Override
    protected  void onResume(){                                 //注意状态方法
        super.onResume();
        sensorManager.registerListener( sensorEventListener,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);      //注册传感器
    }

    @Override
    protected  void onPause() {                      //注意状态方法
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);           //取消注册          }
    }

*/
}
