package com.example.a54297.musicselect.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a54297.musicselect.Help.UserHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.utils.UserUtils;

public class MeActivity extends BaseActivity {

    private TextView mTvUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        initView();
    }

    private void initView(){
        initNavBar(true,"个人中心",false);

        mTvUser = fd(R.id.tv_user);
        mTvUser.setText("用户名："+ UserHelp.getInstance().getPhone());
    }

    public void onChangeClick(View v){
        startActivity(new Intent(this,ChangePasswordActivity.class));
    }

    public void onLogoutClick(View v){
        UserUtils.logout(this);
    }

}
