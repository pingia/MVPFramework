package com.github.pingia.ui.framework.architecture;


import androidx.annotation.UiThread;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function: mvp模式中的presenter
 */
public  interface IPresenter<V extends IView>{


        @UiThread
        void attachView(V view);

        @UiThread
        void detachView();

}
