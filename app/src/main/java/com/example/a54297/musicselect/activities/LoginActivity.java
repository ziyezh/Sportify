package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;
import com.example.a54297.musicselect.views.InputView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends BaseActivity {

    private InputView mInputPhone, mInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView(){
        initNavBar(false,"登录",false);

        mInputPhone = fd(R.id.input_phone);
        mInputPassword = fd(R.id.input_password);
    }

    /**
     * 跳转注册点击事件
     * @param v 立即注册TextView
     */
    public void  onRegisterClick(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void onCommitClick(View v){
        final String phone = mInputPhone.getInputStr();
        final String password = mInputPassword.getInputStr();

        /**
         * 1. 检测输入是否为有效的手机号
         * 2. 检测是否输入密码
         */
        if(!UserUtils.validateLogin(this,phone,password)){
            return;
        }
        new Thread(){
            @Override
            public void run() {
                //向服务器发送请求
                String path="http://47.98.168.203:8080/MusicSelectServer/login?uname="+phone+"&upwd="+password;
                String str="";
                try{
                    URL url = new URL(path);
                    URLConnection conn = url.openConnection();
                    InputStream input = conn.getInputStream();
                    byte [] by = new byte[1024];
                    int len = 0;
                    while((len=input.read(by))!=-1){
                        str = new String(by,0,len);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                if(str.equals("0")){
                    //为0表示要注册的手机号已经存在
                    //之后添加Toast提示
                    Log.i("===",str);
                }
                else if(str.equals("-1")){
                    //为-1表示数据库增加操作不成功
                    //之后添加Toast提示
                    Log.i("===",str);
                }
                else if(str.equals("1")){
                    //为1表示注册成功
                    //回到登陆界面
                    //之后添加Toast提示
                    Log.i("===",str);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    //数据库连接不成功，服务器代码有问题
                    //之后添加Toast提示
                    Log.i("===", str);
                }
            }
        }.start();
    }
}
