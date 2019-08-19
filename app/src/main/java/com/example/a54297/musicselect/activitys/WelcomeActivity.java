package com.example.a54297.musicselect.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {
    private Timer mTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init(){
        final boolean isLogin = UserUtils.validateUserLogin(this);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
//                Log.e("WelcomeActivity","当前线程为："+ Thread.currentThread());
//                跳转到MainActivity
//                toMain();
                toLogin();
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
