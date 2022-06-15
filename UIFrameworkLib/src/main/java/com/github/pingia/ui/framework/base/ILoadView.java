package com.github.pingia.ui.framework.base;

import com.github.pingia.ui.framework.architecture.IView;

/**
 * Created by zenglulin on 2017/8/18.
 *
 *
 */

public interface ILoadView extends IView {
    void showLoading();
    void hideLoading();
}
