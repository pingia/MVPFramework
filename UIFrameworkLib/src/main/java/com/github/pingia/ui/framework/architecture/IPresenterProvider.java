package com.github.pingia.ui.framework.architecture;


import androidx.annotation.NonNull;

/**
 * author：admin on 2017/8/18.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public interface IPresenterProvider<P extends IPresenter> {
    @NonNull
    P onCreatePresenter();
}
