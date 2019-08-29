package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a54297.musicselect.Help.UserHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.SPUtils;
import com.example.a54297.musicselect.utils.UserUtils;
import com.example.a54297.musicselect.views.InputView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ChangePasswordActivity extends BaseActivity {

    private InputView mOldPassword, mPassword, mPasswordConfirm;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Toast.makeText(ChangePasswordActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(ChangePasswordActivity.this, "原密码不正确", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(ChangePasswordActivity.this, "密码修改失败", Toast.LENGTH_LONG).show();
                    break;
                case -2:
                    Toast.makeText(ChangePasswordActivity.this, "连接数据库失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }
    private void initView(){
        initNavBar(true,"修改密码",false);

        mOldPassword = fd(R.id.input_old_password);
        mPassword = fd(R.id.input_password);
        mPasswordConfirm = fd(R.id.input_password_confirm);
    }

    public void onChangePasswordClick(View v){
        final String oldPassword = mOldPassword.getInputStr();
        final String password = mPassword.getInputStr();
        String passwordConfirm = mPasswordConfirm.getInputStr();

       boolean result = UserUtils.changePassword(this,oldPassword,password,passwordConfirm);
       if(!result){
           return;
       }
        new Thread(){
            @Override
            public void run() {
                //向服务器发送请求
                String path="http://47.98.168.203:8080/MusicSelectServer/change?uname="+ UserHelp.getInstance().getPhone()+"&upwd="+password+"&oldupwd="+oldPassword;
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
                if(str.equals("-1")){
                    Log.i("===",str);
                    //为-1表示数据库修改操作不成功
                    Message msg = new Message();
                    msg.what = -1;
                    handler.sendMessage(msg);
                }
                else if(str.equals("0")){
                    Log.i("===",str);
                    //为0表示原密码不正确
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else if(str.equals("1")){
                    Log.i("===",str);
                    //为1表示修改密码成功
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    UserUtils.logout(ChangePasswordActivity.this);
                }
                else {
                    Log.i("===", str);
                    //数据库连接不成功
                    Message msg = new Message();
                    msg.what = -2;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
