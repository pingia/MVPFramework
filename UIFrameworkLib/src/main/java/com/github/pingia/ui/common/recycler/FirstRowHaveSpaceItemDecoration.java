package com.github.pingia.ui.common.recycler;


import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FirstRowHaveSpaceItemDecoration extends VerticalSpacesItemDecoration{

    private int mSpace;
    public FirstRowHaveSpaceItemDecoration(int space) {
        super(space);
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = mSpace;

    }
}
