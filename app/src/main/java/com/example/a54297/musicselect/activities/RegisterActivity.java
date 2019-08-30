package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;
import com.example.a54297.musicselect.views.InputView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RegisterActivity extends BaseActivity {

    private InputView mInputPhone,mInputPassword,mInputPasswordConfirm;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    Toast.makeText(RegisterActivity.this, "手机号已注册", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                    break;
                case -2:
                    Toast.makeText(RegisterActivity.this, "连接数据库失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView(){

        initNavBar(true,"注册",false);

        mInputPhone = fd(R.id.input_phone);
        mInputPassword = fd(R.id.input_password);
        mInputPasswordConfirm = fd(R.id.input_password_confirm);
    }

    /**
     * 注册按钮点击事件
     * 1、用户输入合法性验证
     *      1用户输入手机号是否合法
     *      2用户是否已经输入密码以及确定密码，并且这两次输入是否一样
     *      3用户当前输入手机号是否已经注册
     * 2、保存用户输入的手机号和密码
     * */
    public void onRegisterClick(View v){

        final String phone = mInputPhone.getInputStr();
        final String password = EncryptUtils.encryptMD5ToString(mInputPassword.getInputStr());
        String passwordConfirm = EncryptUtils.encryptMD5ToString(mInputPasswordConfirm.getInputStr());

        /**
         * 1. 检测输入是否为有效的手机号
         * 2. 检测密码和确认密码是都一致
         */
        boolean result = UserUtils.registerUser(this,phone,password,passwordConfirm);

        if(!result) {
            return;
        }
        new Thread(){
            @Override
            public void run() {
                //向服务器发送请求
                String path="http://47.98.168.203:8080/MusicSelectServer/register?uname="+phone+"&upwd="+password;
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
                    //Log.i("===",str);
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else if(str.equals("-1")){
                    //为-1表示数据库增加操作不成功
                    //Log.i("===",str);
                    Message msg = new Message();
                    msg.what = -1;
                    handler.sendMessage(msg);
                }
                else if(str.equals("1")){
                    //为1表示注册成功
                    //Log.i("===",str);

                    //发出注册成功提示信息
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    //回到登陆界面
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    //数据库连接不成功，服务器代码有问题
                    //Log.i("===", str);
                    Message msg = new Message();
                    msg.what = -2;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
