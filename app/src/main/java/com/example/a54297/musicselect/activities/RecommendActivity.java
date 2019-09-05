package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import com.example.a54297.musicselect.R;

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

    private FrameLayout frame1,frame2,frame3;
    private ImageView stateView,imageStart,imageLoading1,imageLoading2,imageloading0;
    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

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
        }//onstartclick函数 结束
    }

    private void initData(){
        long r1 = System.currentTimeMillis();

        random.setSeed(r1);
        states[0]="安静";
        states[1]="散步";
        states[2]="快走";
        states[3]="运动";
        states[4]="快跑";
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
        frame2=findViewById(R.id.frame2);
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
        frame2.setVisibility(View.INVISIBLE);
        frame3.setVisibility(View.VISIBLE);
        imageloading0.setVisibility(View.INVISIBLE);
        imageLoading1.setVisibility(View.INVISIBLE);
        imageLoading2.setVisibility(View.INVISIBLE);


    }

    void setPic(int Time,int Activities){
        time=Time;
        activityState=Activities;
        switch (time){
            //0夜晚
            case 0:
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
                    default:
                        break;
                }
                break;
            }
            //1上午/清晨
            case 1:
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
                    default:
                        break;
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
                    default:
                        break;
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
                    default:
                        break;
                }
                break;
            }
            default:
                break;
        }
    }

    public void onChangeDown(View v){
        //改变状态：前一个  同时
        presentState--;
        if(presentState<0){
            presentState=4;
        }
        setPic(0,presentState);
        view4.setText(states[presentState]);

    }

    public void onChangeUp(View v){
        //改变状态：下一个  同时
        presentState++;
        if(presentState>4) {
            presentState=0;
        }

        setPic(0,presentState);
        view4.setText(states[presentState]);

    }

    public void onStartClick(View v){
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
    }

    public void onLoading(View v){
        if(rotateAnimation2.hasEnded()){//
            frame3.setVisibility(View.INVISIBLE);
            frame2.setVisibility(View.VISIBLE);
            frame1.setVisibility(View.VISIBLE);
        }else {//rotateAnimation2.hasStarted()
//            rotateAnimation1.cancel();
//            rotateAnimation2.cancel();
            //rotateAnimation2.start();
            imageLoading1.setAnimation(null);
            imageLoading2.setAnimation(null);
            imageStart.setVisibility(View.VISIBLE);
            imageloading0.setVisibility(View.INVISIBLE);
            imageLoading1.setVisibility(View.INVISIBLE);
            imageLoading2.setVisibility(View.INVISIBLE);
        }
    }
    public void toMusic(View v){
        Intent intent = new Intent(this,SelectMusicActivity.class);
        intent.putExtra(SelectMusicActivity.ALBUM_ID, "2");
        this.startActivity(intent);
        finish();
    }
}
