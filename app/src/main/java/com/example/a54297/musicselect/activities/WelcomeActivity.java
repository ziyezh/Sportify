package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其他任务就会中止运行，建议使用ScheduleExecutorService")
public class WelcomeActivity extends BaseActivity {
    @SuppressWarnings("建议使用ScheduleExecutorService代替Timer")
    private Timer mTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    @SuppressWarnings("建议使用ScheduleExecutorService代替Timer")
    private void init(){
        final boolean isLogin = UserUtils.validateUserLogin(this);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //toLogin();
                if(isLogin){
                    toMain();
                }else {
                    toLogin();
                }
            }
        },3*1000);
    }

    private void toMain(){
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void toLogin(){
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
