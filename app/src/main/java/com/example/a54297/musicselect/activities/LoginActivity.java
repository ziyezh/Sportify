package com.example.a54297.musicselect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.example.a54297.musicselect.Help.RealmHelp;
import com.example.a54297.musicselect.Help.UserHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.SPUtils;
import com.example.a54297.musicselect.utils.UserUtils;
import com.example.a54297.musicselect.views.InputView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends BaseActivity {

    private InputView mInputPhone, mInputPassword;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(LoginActivity.this, "手机号未注册", Toast.LENGTH_LONG).show();
                    break;
                case -2:
                    Toast.makeText(LoginActivity.this, "查询数据库失败", Toast.LENGTH_LONG).show();
                    break;
                case -3:
                    Toast.makeText(LoginActivity.this, "连接数据库失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

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
        final String password = EncryptUtils.encryptMD5ToString(mInputPassword.getInputStr());

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
                if(str.equals("-1")){
                    //为-1表示手机号不存在
                    //Log.i("===",str);
                    Message msg = new Message();
                    msg.what = -1;
                    handler.sendMessage(msg);
                }
                else if(str.equals("0")){
                    //为0表示要登陆的手机号存在但密码不正确
                    //Log.i("===",str);
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else if(str.equals("1")){
                    //为1表示登陆成功
                    //Log.i("===",str);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    //保存登录标记
                    SPUtils.saveUser(LoginActivity.this,phone);
                    //保存用户信息
                    UserHelp.getInstance().setPhone(phone);

                    //保存音乐源数据,目前还是用Realm数据库，之后需要更改
                    RealmHelp realmHelp = new RealmHelp();
                    realmHelp.setMusicSource(LoginActivity.this);
                    realmHelp.close();

                    //来到主界面
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(str.equals("-2")){
                    //为-2表示数据库操作不成功
                    //Log.i("===",str);
                    Message msg = new Message();
                    msg.what = -2;
                    handler.sendMessage(msg);
                }
                else {
                    //-3表示数据库连接不成功
                    //Log.i("===", str);
                    Message msg = new Message();
                    msg.what = -3;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
