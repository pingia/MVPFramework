package com.github.pingia.ui.common.recycler;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zenglulin on 2017/10/11.
 */

public class HorizontalSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public HorizontalSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildLayoutPosition(view);
        if(pos !=0) {
            outRect.right = space;
        }

    }
}
