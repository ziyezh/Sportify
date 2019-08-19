package com.example.a54297.musicselect.views;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a54297.musicselect.Help.MediaPlayHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.models.MusicModel;
import com.example.a54297.musicselect.services.MusicService;

import java.security.PublicKey;

import io.realm.annotations.Required;

public class PlayMusicView extends FrameLayout {

    Context mContext;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private View mView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvIcon, mIvNeedle,mIvPlay;
    private boolean isPlaying,isBindService;
    private MusicModel mMusicModel;
    private Animation mPlayMusicAnim, mPlayNeedleAnim, mStopNeedleAnim;

    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init (Context context){
      mContext = context;
      mView =  LayoutInflater.from(mContext).inflate(R.layout.play_music,this,false);
      mFlPlayMusic = mView.findViewById(R.id.fl_play_music);
      mFlPlayMusic.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
              trigger();
          }
      });
      mIvIcon = mView.findViewById(R.id.iv_icon1);
      mIvNeedle = mView.findViewById(R.id.iv_needle);
      mIvPlay = mView.findViewById(R.id.iv_play);


        /**
         * 1、定义所需要执行的动画
         *  1、光盘转动的动画
         *  2、指针指向光盘的动画
         *  3、指针离开光盘的动画
         * 2、startAnimation 执行动画
         */

       mPlayMusicAnim = AnimationUtils.loadAnimation(mContext,R.anim.play_music_anim);
       mPlayNeedleAnim  = AnimationUtils.loadAnimation(mContext,R.anim.play_needle_anim);
       mStopNeedleAnim  = AnimationUtils.loadAnimation(mContext,R.anim.stop_needle_anim);


       addView(mView);


    }

//   切换播放状态
    private void trigger(){
        if(isPlaying){
            stopMusic();
        }else {
            playMusic();
        }
    }


    public void playMusic(){ ;
        isPlaying = true;
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

       startMusicService();

    }

    public void stopMusic(){
        isPlaying = false;
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);
        mIvPlay.setVisibility(View.VISIBLE);
        if(mMusicBind !=null)
            mMusicBind.stopMusic();
}

//光盘显示的音乐封面图片
    public void setMusicIcon(){
//            Glide.with(mContext)
//                    .load(icon)
//                    .into(mIvIcon);
        if(mContext !=null)
            Glide.with(mContext)
                    .load(mMusicModel.getPoster())
                    .into(mIvIcon);
    }

    public void setMusic(MusicModel music){
        mMusicModel = music;
        setMusicIcon();
    }
/**
 * 启动音乐服务
 */

        private void startMusicService(){
            if(mServiceIntent ==null){
                mServiceIntent = new Intent(mContext,MusicService.class);
                mContext.startService(mServiceIntent);
            }else {
                mMusicBind.playMusic();
            }
            if(!isBindService){
                isBindService = true;
                mContext.bindService(mServiceIntent,conn,Context.BIND_AUTO_CREATE);
            }
        }

        public void destroy(){

            if(isBindService){
                isBindService = false;
                mContext.unbindService(conn);
            }
        }

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMusicBind = (MusicService.MusicBind) service;
                mMusicBind.setMusic(mMusicModel);
                mMusicBind .playMusic();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

        };
}
