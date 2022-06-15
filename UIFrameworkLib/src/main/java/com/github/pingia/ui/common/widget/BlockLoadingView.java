package com.github.pingia.ui.common.widget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * author：admin on 2017/8/24.
 * mail:zengll@hztxt.com.cn
 * function:  对正在加载对话框做一层封装，以后可方便替换加载对话框的样式
 */
public class BlockLoadingView {
    private AlertDialog mLoadingDialog;

    public BlockLoadingView(Context context, String message) {
        mLoadingDialog = new ProgressDialog(context);
        mLoadingDialog.setMessage(message);
    }

    public void show() {
        //默认允许被取消
       show(true);
    }

    public void show(boolean cancelable){
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.show();
    }
    public void dismiss() {
        mLoadingDialog.dismiss();
    }
}
