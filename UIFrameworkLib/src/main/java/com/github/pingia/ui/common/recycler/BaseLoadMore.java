package com.github.pingia.ui.common.recycler;


import com.github.pingia.uiframework.R;

/**
 * author：admin on 2017/9/18.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public abstract class BaseLoadMore {
    private static final int DEFAULT_LOAD_TIP_TEXT_RES_ID = R.string.click_pullup_loadmore;
    private static final int ONLY_CLICK_LOAD_TIP_TEXT_RES_ID = R.string.click_load_next;

    public static final int STATE_NORMAL = 0;   //正常状态，未触发上拉加载
    public static final int STATE_LOADING = 1;      //上拉加载中...
    public static final int STATE_LOAD_FAILED = 2;  //上拉加载失败，
    public static final int STATE_LOAD_ENDED = 3;       //所有数据全部加载完毕

    private int mState = STATE_NORMAL;
    private boolean mShowLoadMoreEnd = true;     //  数据全部加载完成后，是否显示加载完毕，默认展示
    private boolean mScrollToLoadMoreEnabled = false;   //是否展示“点击加载下一页”的

    public void setLoadMoreEndShow(boolean show){
        mShowLoadMoreEnd = show;
    }

    public void setScrollToLoadMoreEnable(boolean enable){
        mScrollToLoadMoreEnabled = enable;
    }

    public boolean isLoadMoreEndShow(){
        return this.mShowLoadMoreEnd;
    }

    public boolean isScrollToLoadMoreEnabled(){
        return this.mScrollToLoadMoreEnabled;
    }


    public void setLoadState(int state){
        this.mState = state;
    }

    public int getLoadState(){
        return this.mState;
    }

    public void convert(BaseRecycleViewHolder holder){
        switch (this.mState){
            case STATE_NORMAL:
                holder.setVisible(getLoadingViewId(), false);
                holder.setVisible(getLoadingNextPageViewId(), true);
                holder.setVisible(getLoadFailedViewId(),false);
                holder.setVisible(getLoadEndedViewId(),false);
                holder.setText(getLoadingNextPageTextViewId(),getLoadingNextPageStringResId(isScrollToLoadMoreEnabled()));
                break;
            case STATE_LOADING:
                holder.setVisible(getLoadingViewId(), true);
                holder.setVisible(getLoadingNextPageViewId(), false);
                holder.setVisible(getLoadFailedViewId(),false);
                holder.setVisible(getLoadEndedViewId(),false);
                break;
            case STATE_LOAD_FAILED:
                holder.setVisible(getLoadingViewId(), false);
                holder.setVisible(getLoadingNextPageViewId(), false);
                holder.setVisible(getLoadFailedViewId(),true);
                holder.setVisible(getLoadEndedViewId(),false);
                break;
            case STATE_LOAD_ENDED:
                holder.setVisible(getLoadingViewId(), false);
                holder.setVisible(getLoadingNextPageViewId(), false);
                holder.setVisible(getLoadFailedViewId(),false);
                holder.setVisible(getLoadEndedViewId(),true);
                break;
        }
    }

    abstract int getLayoutId();

    abstract int getDividerViewId();
    abstract int getLoadingViewId();
    abstract int getLoadingNextPageViewId();
    abstract int getLoadingNextPageTextViewId();
    abstract int getLoadFailedViewId();
    abstract int getLoadEndedViewId();
    protected int getLoadingNextPageStringResId(boolean allowScrollToLoadMore ){
        if(allowScrollToLoadMore){
            return DEFAULT_LOAD_TIP_TEXT_RES_ID;
        }else{
            return ONLY_CLICK_LOAD_TIP_TEXT_RES_ID;
        }
    }

}
