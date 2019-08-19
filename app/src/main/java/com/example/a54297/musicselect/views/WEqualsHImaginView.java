package com.example.a54297.musicselect.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;



public class WEqualsHImaginView extends AppCompatImageView {
    public WEqualsHImaginView(Context context) {
        super(context);
    }

    public WEqualsHImaginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WEqualsHImaginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

//      int width = MeasureSpec.getSize(widthMeasureSpec);
//      int mode = MeasureSpec.getMode(widthMeasureSpec);
//
    }
}
