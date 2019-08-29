package com.example.a54297.musicselect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.activities.AlbumListActivity;
import com.example.a54297.musicselect.models.AlbumModel;

import java.util.List;

public class MusicGridAdapter extends RecyclerView.Adapter<MusicGridAdapter.ViewHolder> {

    private Context mContext;
    private List<AlbumModel> mDataSource;

   public MusicGridAdapter(Context context,List<AlbumModel> dataSource){
       mContext = context;
       this.mDataSource = dataSource;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_music,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

       final AlbumModel albumModel = mDataSource.get(i);

        Glide.with(mContext)
                .load(albumModel.getPoster())
                .into(viewHolder.ivIcon2);

        viewHolder.mTvPlayNum.setText(albumModel.getPlayNum());
        viewHolder.mTvName.setText(albumModel.getName());

        //是不是因为自定义ImageView导致图片加载不出来
//        Glide.with(mContext)
//                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562843589372&di=0f0f92993ba23bf128b5d1aae66e0cf8&imgtype=0&src=http%3A%2F%2Fimg6.ph.126.net%2FqGGImIPvJD5o5C7c7lZ-3A%3D%3D%2F2028590157155072446.jpg")
//                .into( viewHolder.ivIcon2);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,AlbumListActivity.class);
                intent.putExtra(AlbumListActivity.ALBUM_ID, albumModel.getAlbumId());
//                intent.putExtra(AlbumListActivity.ALBUM_ID,albumModel.getAlbumId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

       ImageView ivIcon2;
       View itemView;
       TextView mTvPlayNum, mTvName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivIcon2 = itemView.findViewById(R.id.iv_icon2);
            mTvPlayNum = itemView.findViewById(R.id.tv_play_num);
            mTvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
