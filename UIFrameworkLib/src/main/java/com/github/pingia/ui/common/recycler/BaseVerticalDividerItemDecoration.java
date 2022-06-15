package com.github.pingia.ui.common.recycler;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;


public class BaseVerticalDividerItemDecoration extends VerticalDividerItemDecoration {

    public BaseVerticalDividerItemDecoration(Context context) {
        super(context);
    }

    protected boolean isFixedViewType(RecyclerView.Adapter adapter, int adapterPosition) {
        int itemViewType = adapter.getItemViewType(adapterPosition);
        return itemViewType == BaseRecycleAdapter.HEADER_VIEW_TYPE
                || itemViewType == BaseRecycleAdapter.FOOTER_VIEW_TYPE
                || itemViewType == BaseRecycleAdapter.LOAD_MORE_VIEW_TYPE;
    }
}

