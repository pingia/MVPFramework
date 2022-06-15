package com.github.pingia.ui.framework.base;

import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.ui.framework.architecture.IPresenterProvider;
import com.github.pingia.ui.framework.architecture.IView;


/**
 * author：admin on 2017/8/18.
 * mail:zengll@hztxt.com.cn
 * function:    封装一个基础的mvp相关的fragment，fragment在视图创建完毕之后负责Presenter生成，并用该presenter绑定自身；视图销毁时进行解绑
 */
public abstract class BaseMvpFragment<P extends IPresenter> extends BaseToolbarFragment implements IView, IPresenterProvider<P> {
    private P mPresenter;

    @Override
    protected void doOnEachViewCreated() {
        if(null == mPresenter) {
            mPresenter = onCreatePresenter();
        }
        mPresenter.attachView(this);
    }

    public void onDestroyView(){
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void showError(Throwable e) {

    }

    protected P getPresenter(){
        return this.mPresenter;
    }
}
