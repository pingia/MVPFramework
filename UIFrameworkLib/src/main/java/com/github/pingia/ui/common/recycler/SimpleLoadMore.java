package com.github.pingia.ui.common.recycler;


import com.github.pingia.uiframework.R;

/**
 * author：admin on 2017/9/18.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public class SimpleLoadMore extends BaseLoadMore {
    @Override
    int getLayoutId() {
        return R.layout.pingia_ui_simple_load_more_view;
    }

    @Override
    int getDividerViewId() {
        return R.id.divider_view;
    }

    @Override
    int getLoadingViewId() {
        return R.id.loadMore_loading_view;
    }

    @Override
    int getLoadingNextPageViewId() {
        return R.id.loadMore_load_nextpage_view;
    }

    @Override
    int getLoadingNextPageTextViewId(){
        return R.id.loadMore_load_nextpage_textview;
    }

    @Override
    int getLoadFailedViewId() {
        return R.id.loadMore_load_failed_view;
    }

    @Override
    int getLoadEndedViewId() {
        return R.id.loadMore_load_ended_view;
    }
}
