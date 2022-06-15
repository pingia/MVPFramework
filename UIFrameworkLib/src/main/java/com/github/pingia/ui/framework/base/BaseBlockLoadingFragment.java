package com.github.pingia.ui.framework.base;

import android.content.Context;

import com.github.pingia.ui.common.widget.BlockLoadingView;
import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.uiframework.R;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function: 实现一个在加载数据时可能会阻塞用户的fragment，一般是加载时显示一个对话框或为一个菊花的加载对话框
 */

public abstract class BaseBlockLoadingFragment<P extends IPresenter> extends BaseMvpFragment<P> implements ILoadView {
    private BlockLoadingView mBlockLoadingView;
    private boolean mCancelLoadingEnabled = true; //是否支持取消加载对话框,默认开启

    public void onAttach(Context context){
        super.onAttach(context);
        mBlockLoadingView = new BlockLoadingView(context,onCreateLoadingMessage());
    }

    @Override
    public void showLoading() {
        mBlockLoadingView.show(mCancelLoadingEnabled);
    }

    @Override
    public void hideLoading() {
        mBlockLoadingView.dismiss();
    }

    @Override
    public void showError(Throwable e) {
        super.showError(e);
    }

    /**
     * 显示在加载对话框中的文字，默认显示加载中...，可重载满足页面的自定义需求
     * @return
     */
    public String onCreateLoadingMessage(){
        return getString(R.string.loading);
    }


    public void setCancelLoadingEnabled(boolean enabled){
        mCancelLoadingEnabled = enabled;
    }
}
