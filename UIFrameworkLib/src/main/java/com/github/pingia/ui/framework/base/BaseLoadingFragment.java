package com.github.pingia.ui.framework.base;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.uiframework.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author：admin on 2017/8/25.
 * mail:zengll@hztxt.com.cn
 * function: 实现一个不阻塞的加载fragment，在加载时显示加载样式，加载成功后会显示正常的页面
 */
public abstract class BaseLoadingFragment<P extends IPresenter> extends BaseMvpFragment<P> implements IEmptyLoadView {
    //是否开启手势下拉刷新列表数据，默认关闭，
    private boolean swipeRefreshEnabled = false;

    private SwipeRefreshLayout mRefreshLayout;

    private RelativeLayout emptyRootLayout;
    private RelativeLayout errorRootLayout;
    private RelativeLayout loadingLayout;
    private LinearLayout contentFrame;

    private Unbinder mContentViewBinder;
    private LayoutInflater mInflater;

    private View mNewEmptyView;
    private int mNewEmptyImgResId;
    private CharSequence mNewEmptyText;

    private View mNewErrorView;
    private int mNewErrorImgResId;
    private CharSequence mNewErrorText;

    private View contentView;


    /**
     * 控制当前fragment能否下拉刷新，
     * 在fragment视图创建之前 设置
     * @param enabled   false：不能  true:能  默认能
     */
    protected void setSwipeRefreshEnable(boolean enabled){
        swipeRefreshEnabled =  enabled;
        if(null != mRefreshLayout) {
            mRefreshLayout.setEnabled(enabled);
        }
    }

    public void onResume(){
        super.onResume();
        if(null == mContentViewBinder){
            if(null != contentView) {
                mContentViewBinder = ButterKnife.bind(this, contentView);
            }
        }

    }

    @Override
    protected void initView(View view) {
        mInflater = LayoutInflater.from(getContext());
        mRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.CYAN);
        mRefreshLayout.setEnabled(swipeRefreshEnabled);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        emptyRootLayout =  view.findViewById(R.id.empty_root_layout);
        errorRootLayout = view.findViewById(R.id.error_root_layout);
        loadingLayout = view.findViewById(R.id.loading_layout);
        contentFrame = view.findViewById(R.id.content_frame);
        contentFrame.removeAllViews();

        if(null == contentView) {
            contentView = getRealContentView(mInflater);
        }

        if(null == mContentViewBinder) {
            mContentViewBinder = ButterKnife.bind(this, contentView);
        }

        initRealContentView(contentView);
        contentFrame.addView(contentView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initEmptyViewLayout();
        initErrorViewLayout();

    }

    public void initEmptyViewLayout(){
        if(null != mNewEmptyView) {
            emptyRootLayout.removeAllViews();
            emptyRootLayout.addView(mNewEmptyView,new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        ImageView emptyIv =  emptyRootLayout.findViewById(getEmptyViewImgViewId());
        TextView emptyTv =  emptyRootLayout.findViewById(getEmptyViewTextViewId());

        setEmptyInfo(emptyIv, emptyTv);
    }

    public void initErrorViewLayout(){
        if(null != mNewErrorView) {
            errorRootLayout.removeAllViews();
            errorRootLayout.addView(mNewErrorView,new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }


        ImageView errorIv =  errorRootLayout.findViewById(getErrorViewImgViewId());
        TextView errorTv =  errorRootLayout.findViewById(getErrorViewTextViewId());

        if(null != errorIv) {
            if (mNewErrorImgResId != 0) {
                errorIv.setImageResource(mNewErrorImgResId);
            }
        }

        if(null != errorTv) {
            if (null != mNewErrorText) {
                errorTv.setText(mNewErrorText);
            }
        }
    }

    protected void setEmptyInfo(ImageView emptyIv, TextView emptyTv){
        if(null != emptyIv) {
            if (mNewEmptyImgResId != 0) {
                emptyIv.setImageResource(mNewEmptyImgResId);
            }
        }

        if(null != emptyTv) {
            if (null != mNewEmptyText) {
                emptyTv.setText(mNewEmptyText);
            }
        }
    }

    protected void setErrorInfo(ImageView errorIv, TextView errorTv){
        if(null != errorIv) {
            if (mNewErrorImgResId != 0) {
                errorIv.setImageResource(mNewErrorImgResId);
            }
        }

        if(null != errorTv) {
            if (null != mNewErrorText) {
                errorTv.setText(mNewErrorText);
            }
        }
    }


    @Override
    protected boolean bindRootViewOrNot(){
        return false;       //不绑定根视图，防止子类声明了@BindView注解的组件，在初始化根视图的时候报异常；为什么不绑定根视图，因为我们这里要的实际视图内容是getRealContentView方法返回的
    }

    protected abstract void initRealContentView(View contentView);
    public abstract View getRealContentView(LayoutInflater inflater);

    @Override
    public void showLoading() {
        disableSwipeRefresh();
        contentFrame.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    @CallSuper
    public void showContent() {
        finishSwipeRefresh();
        hideLoading();
        emptyRootLayout.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.GONE);
        contentFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        super.showError(e);
        disableSwipeRefresh();
        hideLoading();
        contentFrame.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.VISIBLE);

        errorRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

    }

    @Override
    public void showEmpty() {
        finishSwipeRefresh();
        hideLoading();
        contentFrame.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.VISIBLE);
    }

    public void setEmptyView(View emptyView){
        mNewEmptyView = emptyView;
    }

    public View getEmptyView(){
        return mNewEmptyView;
    }


    public void setEmptyImgResId(int imgResId){
        mNewEmptyImgResId = imgResId;
    }

    public void setEmptyText(CharSequence emptyText){
       mNewEmptyText = emptyText;
    }

    public void  setErrorView(View errorView){
        mNewErrorView = errorView;
    }

    public View getErrorView(){
        return mNewErrorView;
    }

    public void setErrorImgResId(int imgResId){
        mNewErrorImgResId = imgResId;
    }

    public void setErrorText(CharSequence errorText){
        mNewErrorText = errorText;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.pingia_ui_fragment_base_loading, null);
    }

    protected int getEmptyViewImgViewId(){
        return R.id.empty_img;
    }

    protected  int getEmptyViewTextViewId(){
        return R.id.empty_desc;
    }

    protected int getErrorViewImgViewId(){
        return R.id.error_img;
    }

    protected  int getErrorViewTextViewId(){
        return R.id.error_desc;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null  != mContentViewBinder){
            mContentViewBinder.unbind();
        }

        mContentViewBinder = null;
    }

    protected void finishSwipeRefresh(){
        if(mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setEnabled(swipeRefreshEnabled);
        }
    }


    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onSwipeRefreshing();
            if (null != mRefreshLayout) {
                mRefreshLayout.setEnabled(false);
                mRefreshLayout.setRefreshing(true);
            }
            refresh();


        }
    };

    public void refresh(){
        super.refresh();
        finishSwipeRefresh();
    }

    protected void onSwipeRefreshing() {
        //do nothing.
    }

    protected void disableSwipeRefresh() {
        if (null != mRefreshLayout) {
            mRefreshLayout.setEnabled(false);
        }
    }

}
