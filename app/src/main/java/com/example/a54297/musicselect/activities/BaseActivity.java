package com.example.a54297.musicselect.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a54297.musicselect.R;

public class BaseActivity extends Activity {

    private ImageView mIvBack, mIvMe,mIvHeart,mIvMeMain;
    private TextView mTvTitle,mTvTitleMain;

    /**
     * findviewbyid的缩写函数
     * @param id findviewbyid所需参数
     * @param <T> findviewbyid泛型
     * @return findviewbyid函数
     */
    protected  <T extends View> T fd(@IdRes int id){
        return findViewById(id);
    }

    /**
     * 初始化navigationBar
     * @param isShowBack 是否展示后退键
     * @param title 标题
     * @param isShowMe 是否展示“我”的界面点击按钮
     */
    protected  void initNavBar(boolean isShowBack,String title,boolean isShowMe){
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = fd(R.id.tv_title);
        mIvMe = fd(R.id.iv_me);

        mIvBack.setVisibility(isShowBack?View.VISIBLE:View.GONE);
        mIvMe.setVisibility(isShowMe?View.VISIBLE:View.GONE);
        mTvTitle.setText(title);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this,MeActivity.class));
            }
        });
    }

    protected  void initNavBarMain(boolean isShowHeart,String title,boolean isShowMe){
        mIvHeart = findViewById(R.id.iv_heart);
        mTvTitleMain = fd(R.id.tv_titlemain);
        mIvMeMain = fd(R.id.iv_memain);

        mIvHeart.setVisibility(isShowHeart?View.VISIBLE:View.GONE);
        mIvMeMain.setVisibility(isShowMe?View.VISIBLE:View.GONE);
        mTvTitleMain.setText(title);

        mIvHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this,RecommendActivity.class));
            }
        });
        mIvMeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this,MeActivity.class));
            }
        });
    }

}
