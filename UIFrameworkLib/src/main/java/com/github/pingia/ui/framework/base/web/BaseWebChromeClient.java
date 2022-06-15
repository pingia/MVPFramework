package com.github.pingia.ui.framework.base.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Description:
 * Created by zenglulin@youxiang.com
 * <p>
 * Date: 2022/5/23
 */
public class BaseWebChromeClient extends WebChromeClient {
    private TextView mTitleView;
    public void setTitleView(TextView titleView){
        this.mTitleView  = titleView;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        //如果需要显示web页面的title，则显示web的title
        if(this.mTitleView != null){
            this.mTitleView.setText(title);
        }
    }
}
