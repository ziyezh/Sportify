package com.example.a54297.musicselect.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a54297.musicselect.Help.RealmHelp;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.models.SongModel;
import com.example.a54297.musicselect.views.PlayMusicView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayMusicActivity extends BaseActivity{

    public static final String MUSIC_ID = "musicId";

    private ImageView mIvBg;
    private TextView mTvName, mTvAuthor;
    private PlayMusicView mPlayMusicView;
    private String mMusicId;
    private SongModel mMusicModel;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        //隐藏statusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mMusicId = getIntent().getStringExtra(MUSIC_ID);

        new Thread(){
            @Override
            public void run() {
                String path = "http://47.98.168.203:8080/MusicSelectServer/playmusic?id=" + mMusicId;
                final OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(path)
                        .build();
                //开启一个异步请求
                client.newCall(request).enqueue(new Callback() {
                    //请求失败
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            //请求失败
                        }
                        //获取到接口的数据
                        String data = response.body().string();
                        //把数据解析
                        Gson mGson = new Gson();
                        mMusicModel = mGson.fromJson(data,new TypeToken<SongModel>(){}.getType());

                        //通过handler运行initView
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);

                    }
                });
            }
        }.start();
    }

    private void initView(){
        mIvBg = fd(R.id.iv_bg);
        mTvName = fd(R.id.tv_name);
        mTvAuthor = fd(R.id.tv_author);
        //glide-transformations 高斯模糊
        Glide.with(this)
                .load(mMusicModel.getPoster())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());

        mPlayMusicView = fd(R.id.play_music_view);
        mPlayMusicView.setMusic(mMusicModel);
        mPlayMusicView.playMusic();
    }

    /**
     * 后退按钮点击事件
     * @param view 后退按钮
     */
    public void onBackClick(View view){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayMusicView.destroy();
    }
}
