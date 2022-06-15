package com.github.pingia.ui.framework.base;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pingia.ui.common.listener.OnScrolledToEndListener;
import com.github.pingia.ui.common.listener.RecycleViewScrollToBottomListener;
import com.github.pingia.ui.common.recycler.BaseListVerticalDividerItemDecoration;
import com.github.pingia.ui.common.recycler.BaseLoadMore;
import com.github.pingia.ui.common.recycler.BaseRecycleAdapter;
import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.uiframework.R;
import com.github.pingia.uiframework.R2;

import java.util.List;

import butterknife.BindView;

/**
 * author：admin on 2017/8/22.
 * mail:zengll@hztxt.com.cn
 * function:    可显示加载progress的list切片，需要指定一个初始化带listview或recycleview的布局
 */
public abstract class BaseLoadingListFragment<P extends IPresenter,RA extends BaseRecycleAdapter> extends BaseLoadingFragment<P> implements IListLoadView {

    @BindView(R2.id.swipe_target)
    RecyclerView mRecycleView;

    @BindView(R2.id.title_framelayout)
    FrameLayout titleFramelayout;

    private RA mRecycleAdapter;



    //是否开启分页加载，有些列表页面不需要分页，可以通过这个开关控制，默认开启
    private boolean isLoadMoreEnabled = true;

    //是否显示"全部数据加载完毕"，默认显示
    private boolean showLoadMoreEnded = true;

    //是否开启滑动到底部加载更多，如果不开启，那么只能通过点击才能加载下一页数据,默认开启
    private boolean mScrollToLoadMoreEnabled = true;

    private int mPageIndex = 0;     //当前页索引，从0开始

    private int mPageSize = 10;        //分页数据大小，默认10条，每个列表页可自定义
    public View getRealContentView(LayoutInflater inflater){
        return inflater.inflate(R.layout.pingia_ui_fragment_base_list,null);
    }

    protected View getTitleView(LayoutInflater inflater){
        return null;
    }

    protected abstract RA onCreateRecycleAdapter();

    public RA getRecycleAdapter(){
        return this.mRecycleAdapter;
    }


    protected void setShowLoadMoreEnded(boolean showEnded){
        this.showLoadMoreEnded = showEnded;
    }

    protected void setLoadMoreEnable(boolean enabled){
        isLoadMoreEnabled = enabled;
    }

    protected void setScrollToLoadMoreEnabled(boolean enable){
        mScrollToLoadMoreEnabled = enable;
    }

    /**
     * 设置分页数据大小
     * @param pageSize
     */
    protected void setPageSize(int pageSize){
        this.mPageSize = pageSize;
    }

    protected int getPageSize(){
        return this.mPageSize;
    }

    protected int getCurrentPageIndex(){
        return this.mPageIndex;
    }

    public void initEmptyViewLayout(){
        if(null != getEmptyView()) {
            ImageView emptyIv = getEmptyView().findViewById(getEmptyViewImgViewId());
            TextView emptyTv = getEmptyView().findViewById(getEmptyViewTextViewId());

            setEmptyInfo(emptyIv, emptyTv);
        }
    }

    public void initErrorViewLayout(){
        if(null != getErrorView()) {
            ImageView errorIv = getErrorView().findViewById(getErrorViewImgViewId());
            TextView errorTv = getErrorView().findViewById(getErrorViewTextViewId());

            setErrorInfo(errorIv, errorTv);
        }
    }

    @CallSuper
    protected void initRealContentView (View rootView){
        setSwipeRefreshEnable(true);    //list默认开启下拉刷新
        titleFramelayout.removeAllViews();
        View titleView = getTitleView(LayoutInflater.from(rootView.getContext()));
        if(titleView != null) {
            titleFramelayout.addView(titleView);
        }


        mRecycleAdapter = onCreateRecycleAdapter();
        mRecycleAdapter.setLoadMoreEnable(isLoadMoreEnabled);
        mRecycleAdapter.setScrollToLoadMoreEnable(mScrollToLoadMoreEnabled);
        mRecycleAdapter.setLoadMoreRequestListener(listener);
        mRecycleAdapter.setOnEmptyLayoutVisibleListener(new BaseRecycleAdapter.OnEmptyLayoutVisibleListener() {
            @Override
            public void onEmptyVisible(boolean visible) {
                titleFramelayout.setVisibility(visible ? View.GONE: View.VISIBLE);      //有数据的时候不显示title，无数据的时候才显示title
            }
        });

        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));   //因为我们要的是竖向的列表，所以这里采用线性布局
        mRecycleView.setAdapter(mRecycleAdapter);
        mRecycleAdapter.bindRecycleView(mRecycleView);
        setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.pingia_ui_empty_view,null));

        if(needItemDecoration()) {
            BaseListVerticalDividerItemDecoration dividerItemDecoration = new BaseListVerticalDividerItemDecoration(getContext());
            Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), getItemDecorationResId());
            dividerItemDecoration.setDrawable(dividerDrawable);
            mRecycleView.addItemDecoration(dividerItemDecoration);
        }

        mRecycleView.addOnScrollListener(new RecycleViewScrollToBottomListener(scrollToListBottomListener));
    }

    /**
     * 返回分割线颜色,给一个默认的drawable，可自定义
     * @return
     */
    protected int getItemDecorationResId(){
        return android.R.drawable.divider_horizontal_dark;
    }

    /**
     * 在initRealContentView之前调用
     */
    public void disableLoadMore(){
        setShowLoadMoreEnded(false);
        setLoadMoreEnable(false);
        setScrollToLoadMoreEnabled(false);
    }

    public void disablePullToRefresh(){
        setSwipeRefreshEnable(false);
    }

    /**
     * 是否需要列表item之间的分隔线，默认需要，可覆写该函数
     * @return
     */
    protected boolean needItemDecoration(){
        return true;
    }

    public void refresh(){  //刷新的时候清除所有list数据，重新请求第一页数据
        getRecycleAdapter().clear();
        requestFirstPageData();
    }

    protected abstract void requestListData(int pageIndex,int pageSize);

    protected void requestFirstPageData(){
        mPageIndex = 0;
        if(mRecycleAdapter.getDatas().size() == 0){
            showLoading();  //如果还没加载到任何数据，显示一个全屏加载动画
        }else{
        }
        requestListData(mPageIndex,getPageSize());
    }

    @Override
    public void showContent(List data) {
        super.showContent();
        finishSwipeRefresh();
        mRecycleAdapter.setLoadMoreEnable(isLoadMoreEnabled);

        if(null == data  || data.isEmpty()) {
            if(mPageIndex == 0 ){
                showEmpty();
            }else{
                getRecycleAdapter().loadMoreEnded(showLoadMoreEnded);
            }
        }else{
            if(mPageIndex == 0){
                getRecycleAdapter().setListData(data);
                if(mRecycleView != null) {
                    mRecycleView.scrollToPosition(0);
                }
            }else {
                getRecycleAdapter().appendData(data);
            }

            if(data.size() < mPageSize){    //如果上拉加载的数据项个数小于分页大小，说明数据已全部加载完毕
                getRecycleAdapter().loadMoreEnded(showLoadMoreEnded);
            }else{
                getRecycleAdapter().loadMoreSuccess();
            }
        }

    }

    @Override
    public void showError(Throwable e){
        finishSwipeRefresh();
        mRecycleAdapter.setLoadMoreEnable(isLoadMoreEnabled);

        if(mPageIndex == 0){    //加载错误的时候，如果是第一页,直接展示一个全屏的错误；否则通过loadMore展示加载失败
            super.showError(e);
        }else{
            getRecycleAdapter().loadMoreFailed();
        }
    }



    private BaseRecycleAdapter.OnLoadMoreRequestListener listener = new BaseRecycleAdapter.OnLoadMoreRequestListener() {
        @Override
        public void onRequestLoadMore() {
            disableSwipeRefresh();   //下拉加载前先设置不可手势下拉
            mPageIndex = mPageIndex +1;
            requestListData(mPageIndex,mPageSize);

        }
    };

    private OnScrolledToEndListener scrollToListBottomListener = new OnScrolledToEndListener() {
        @Override
        public void scrollToEnd() {
            if(null == mRecycleView){
                return;
            }
            mRecycleView.post(new Runnable() {
                @Override
                public void run() {
                    if(isLoadMoreEnabled && mScrollToLoadMoreEnabled ) {
                        //当开启了上拉加载和滑动加载下一页标志，同时当前是加载完毕或初始状态时，滑动到一页底部自动加载下一页数据
                        BaseLoadMore loadMore = mRecycleAdapter.getLoadMore();
                        if(loadMore.getLoadState() == BaseLoadMore.STATE_NORMAL) {
                            if (mRecycleAdapter.getDatas().size() < mPageSize) {
                                mRecycleAdapter.loadMoreEnded(showLoadMoreEnded);
                            } else {
                                mRecycleAdapter.notifyLoadMoreToLoading();
                                mRecycleAdapter.startLoadMore();
                            }
                        }
                    }
                }
            });

        }
    };

    protected void onSwipeRefreshing() {
        if(isLoadMoreEnabled) {
            mRecycleAdapter.setLoadMoreEnable(!isLoadMoreEnabled);
        }
    }

    /**
     * 设置emptyView,必须在setAdapter之后
     * @param emptyView
     */
    public void setEmptyView(View emptyView){
        super.setEmptyView(emptyView);
        mRecycleAdapter.setEmptyView(emptyView);
    }

    @Override
    public void showEmpty() {
        mRecycleAdapter.clear();
        showContent();
    }

}
