package com.example.a54297.musicselect.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.adapters.SongListAdapter;
import com.example.a54297.musicselect.models.SongModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SelectMusicActivity extends BaseActivity {
    public static final String ALBUM_ID = "albumId";
    private String mAlbumId;
    private RecyclerView mRvList;
    private SongListAdapter mAdapter;
    private List<SongModel> mDataSource;
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
        setContentView(R.layout.activity_album_list);

        mAlbumId = getIntent().getStringExtra(ALBUM_ID);
        new Thread(){
            @Override
            public void run() {
                String path= "http://47.98.168.203:8080/MusicSelectServer/musiclist?status="+mAlbumId;
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

                    //有响应
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            //请求失败
                        }

                        //获取到接口的数据
                        String data = response.body().string();
                        //把数据解析
                        Gson mGson = new Gson();
                        mDataSource = new ArrayList();
                        mDataSource = mGson.fromJson(data,new TypeToken<List<SongModel>>(){}.getType());

                        //通过handler运行initView
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                });
            }
        }.start();
    }

    private void initView() {
        initNavBar(true,"推荐音乐列表",false);
        mRvList = fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new SongListAdapter(this,null,mDataSource);
        mRvList.setAdapter(mAdapter);
    }
}
