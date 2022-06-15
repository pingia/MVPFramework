package com.github.pingia.ui.framework.base;


import com.github.pingia.ui.framework.architecture.IPresenter;

/**
 * <p>文件描述：一个非阻塞的fragment。<p>
 * <p>作者: zengll@jrrcapital.com<p>
 * <p>创建时间：2018/9/1<p>
 */
public abstract class BaseNoBlockLoadingFragment<P extends IPresenter> extends BaseMvpFragment<P> implements ILoadView{

    @Override
    public void showLoading() {
        //do nothing
    }

    @Override
    public void hideLoading() {
        //do nothing
    }

}
