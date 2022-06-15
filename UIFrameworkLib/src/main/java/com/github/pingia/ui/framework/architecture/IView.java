package com.github.pingia.ui.framework.architecture;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function:mvp模式中的View抽象接口
 */

public interface IView {
    /**
     * 用于提示一些通用的错误
     * 比如登录超时等
     * @param e
     */
    void showError(Throwable e);
}