package com.github.pingia.ui.framework.base;

import android.os.Bundle;

import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.ui.framework.architecture.IPresenterProvider;
import com.github.pingia.ui.framework.architecture.IView;


/**
 * author：admin on 2017/8/18.
 * mail:zengll@hztxt.com.cn
 * function:    封装一个基础的mvp相关的activity，初始化activity的时候负责Presenter生成，并用该presenter绑定自身；acvitity销毁的时候进行解绑
 * 注意：所有该类的子类 在初始化activity的时候都必须调用基类的方法
 */
public abstract class BaseMvpToolBarActivity<P extends IPresenter> extends BaseToolBarActivity implements IView, IPresenterProvider<P> {

    private P mPresenter;

    @Override
    public void onActivityInit(Bundle savedInstanceState) {
        mPresenter = onCreatePresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showError(Throwable e) {

    }

    protected P getPresenter(){
        return this.mPresenter;
    }

}
