package com.example.a54297.musicselect.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.a54297.musicselect.Help.MediaPlayHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.activitys.WelcomeActivity;
import com.example.a54297.musicselect.models.MusicModel;


public class MusicService extends Service {

    public static final int NOTIFICATION_ID = 1;

    private MediaPlayHelp mMediaPlayHelp;
    private MusicModel mMusicModel;

    public MusicService() {
    }

    public class MusicBind extends Binder{
        public void setMusic(MusicModel musicModel){
            mMusicModel = musicModel;
            startForeground();
        }

        public void playMusic(){
            if( mMediaPlayHelp.getPath() != null &&  mMediaPlayHelp.getPath().equals(mMusicModel.getPath())){
                mMediaPlayHelp.start();
            }else {
                mMediaPlayHelp.setPath(mMusicModel.getPath());
                mMediaPlayHelp.setOnMediaPlayerHelperListener(new MediaPlayHelp.onMediaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSelf();
                    }
                });
            }
        }

        public void stopMusic(){
            mMediaPlayHelp.pause();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
     return new MusicBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayHelp = MediaPlayHelp.getInstance(this);
    }

    private void startForeground (){

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,WelcomeActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(mMusicModel.getName())
                .setContentText(mMusicModel.getAuthor())
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID,notification);

    }

}
