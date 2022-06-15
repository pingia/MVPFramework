package com.github.pingia.ui.framework.base;

import android.view.Menu;
import android.view.MenuItem;

/**
 * author：admin on 2017/8/17.
 * mail:zengll@hztxt.com.cn
 * function: 一个用来控制标题栏（toolbar）的接口
 */
public interface ToolbarInterface {
    void hideToolBar();
    void hideToolBarBackIcon();
    void doTitleRefresh(MenuItem item);
    void onInitMenu(Menu menu);
    int getToolBarMenuResId();
}
