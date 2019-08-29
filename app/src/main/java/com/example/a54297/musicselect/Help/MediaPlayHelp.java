package com.example.a54297.musicselect.Help;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class MediaPlayHelp {
    private static MediaPlayHelp instance;

    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private onMediaPlayerHelperListener onMediaPlayerHelperListener;
    private String mPath;

    public void setOnMediaPlayerHelperListener(MediaPlayHelp.onMediaPlayerHelperListener onMediaPlayerHelperListener) {
        this.onMediaPlayerHelperListener = onMediaPlayerHelperListener;
    }

    public static MediaPlayHelp getInstance(Context context){

        if (instance == null){
            synchronized (MediaPlayHelp.class){
                if(instance == null){
                    instance = new MediaPlayHelp(context);
                }
            }
        }

        return instance;
    }
    private  MediaPlayHelp (Context context){
        mContext = context;
        mMediaPlayer = new MediaPlayer();
    }

    /**
     *  1、setpath：播放音乐的地址
     *  2、start： 播放音乐
     *  3、path: 暂停播放
     */
    public void setPath(String path){
        /**
         * 1、音乐正在播放：重置音乐播放状态
         * 2、设置播放音乐路径
         * 3、准备播放
         */


        //1、音乐正在播放或切换了音乐：重置音乐播放状态
        if(mMediaPlayer.isPlaying()||!path.equals(mPath)){
            mMediaPlayer.reset();
        }
        mPath = path;

        //2、设置播放音乐路径
     try {
         mMediaPlayer.setDataSource(mContext, Uri.parse(path));
     }catch(IOException e){
            e.printStackTrace();
     }

     //3、准备播放
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(onMediaPlayerHelperListener !=null){
                    onMediaPlayerHelperListener.onPrepared(mp);
                }
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(onMediaPlayerHelperListener !=null){
                    onMediaPlayerHelperListener.onCompletion(mp);
                }
            }
        });

    }

    public String  getPath(){
        return mPath;
    }

    public void start(){
        if(mMediaPlayer.isPlaying()) {
            return;
        }
        mMediaPlayer.start();
    }

    public void pause(){
        mMediaPlayer.pause();
    }

    public interface onMediaPlayerHelperListener{
        void onPrepared(MediaPlayer mp);
        void onCompletion(MediaPlayer mp);
    }
}
