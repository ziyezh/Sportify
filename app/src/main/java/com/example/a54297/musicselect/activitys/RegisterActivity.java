package com.example.a54297.musicselect.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;
import com.example.a54297.musicselect.views.InputView;

public class RegisterActivity extends BaseActivity {

    private InputView mInputPhone,mInputPassword,mInputPasswordConfirm;

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
     * 2、保存用户输入的手机号和密码（MD5加密密码）
     * */
    public void onRegisterClick(View v){

        String phone = mInputPhone.getInputStr();
        String password = mInputPassword.getInputStr();
        String passwordconfirm = mInputPasswordConfirm.getInputStr();

        boolean result = UserUtils.registerUser(this,phone,password,passwordconfirm);

        if(!result) return;
        //后退页面
            onBackPressed();
    }
}
