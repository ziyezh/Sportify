package com.example.a54297.musicselect.views;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridSpaceItemDecoration(int space,RecyclerView parent){
        mSpace = space;
        getRecyclerViewOffsets(parent);
    }

    /**
     *
     * @param outRect  Item的矩形边界
     * @param view  ItemView
     * @param parent    RecycleView
     * @param state RecycleView的状态
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mSpace;

//        if(parent.getChildLayoutPosition(view)%3 == 0){
//            outRect.left = 0;
//        }

       LinearLayout.LayoutParams layoutParams =(LinearLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.leftMargin = -mSpace;
        parent.setLayoutParams(layoutParams);
    }

    private void getRecyclerViewOffsets (RecyclerView parent){
        LinearLayout.LayoutParams layoutParams =(LinearLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.leftMargin = -mSpace;
        parent.setLayoutParams(layoutParams);
    }
}
