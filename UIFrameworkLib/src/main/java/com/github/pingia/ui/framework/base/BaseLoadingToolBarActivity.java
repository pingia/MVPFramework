package com.github.pingia.ui.framework.base;

import android.os.Bundle;

import com.github.pingia.ui.common.widget.BlockLoadingView;
import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.uiframework.R;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function: 实现一个在acvitiy之上显示的全局加载对话框样式，内部可以直接用dialog来显示
 */
public abstract class BaseLoadingToolBarActivity<P extends IPresenter> extends BaseMvpToolBarActivity<P> implements ILoadView {
    private BlockLoadingView mBlockLoadingView;
    private boolean mCancelLoadingEnabled = true; //是否支持取消加载对话框,默认开启
    @Override
    public void onActivityInit(Bundle savedInstanceState) {
        super.onActivityInit(savedInstanceState);
        mBlockLoadingView = new BlockLoadingView(this, onCreateLoadingMessage());
    }

    @Override
    public void showLoading() {
        //用展示正在加载的ui显示,初始化dialog
        mBlockLoadingView.show(mCancelLoadingEnabled);
    }

    @Override
    public void hideLoading() {
        mBlockLoadingView.dismiss();
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
