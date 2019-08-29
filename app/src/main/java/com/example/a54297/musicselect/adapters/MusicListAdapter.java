package com.example.a54297.musicselect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a54297.musicselect.R;
import com.example.a54297.musicselect.activities.PlayMusicActivity;
import com.example.a54297.musicselect.models.MusicModel;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>{

    private Context mContext;
    private View mItemView;
    private RecyclerView mRv;
    private boolean isCalculationRvHeight;
    private List<MusicModel> mDataSource;

    public MusicListAdapter(Context context, RecyclerView recyclerView,List<MusicModel> dataSource){
        mContext = context;
        mRv = recyclerView;
        this.mDataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_music,viewGroup,false);
        return new ViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            setRecyclerViewHeight();

            final MusicModel musicModel = mDataSource.get(i);

            Glide.with(mContext)
                    .load(musicModel.getPoster())
                    .into( viewHolder.ivIcon1);

            viewHolder.tvName.setText(musicModel.getName());
            viewHolder.tvAuthor.setText(musicModel.getAuthor());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,PlayMusicActivity.class);
                    intent.putExtra(PlayMusicActivity.MUSIC_ID,musicModel.getMusicId());
                    mContext.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    private void setRecyclerViewHeight(){
        if(isCalculationRvHeight || mRv ==null) {
            return;
        }
        isCalculationRvHeight = true;
        //获取itemview高度
      RecyclerView.LayoutParams itemViewLp = (RecyclerView.LayoutParams) mItemView.getLayoutParams();
      int itemCount = getItemCount();
      int recyclerViewHeight =  itemViewLp.height*itemCount;
      LinearLayout.LayoutParams rvLp =  (LinearLayout.LayoutParams) mRv.getLayoutParams();
      rvLp.height = recyclerViewHeight;
      mRv.setLayoutParams(rvLp);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        ImageView ivIcon1;
        TextView tvName,tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivIcon1 = itemView.findViewById(R.id.iv_icon1);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
