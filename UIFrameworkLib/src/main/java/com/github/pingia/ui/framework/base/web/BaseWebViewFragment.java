package com.github.pingia.ui.framework.base.web;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.github.pingia.ui.common.IntentConstant;
import com.github.pingia.ui.framework.base.BaseToolbarFragment;
import com.github.pingia.ui.framework.base.IEmptyLoadView;
import com.github.pingia.uiframework.R;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseWebViewFragment extends BaseToolbarFragment implements IEmptyLoadView, IWebPageLoad {

    private BaseWebView webview;

    @Override
    protected void initView(View view) {
        webview = view.findViewById(R.id.webview);
        WebViewClient client2 = webview.getWebViewClient2();
        WebChromeClient chromeClient2 = webview.getChromeClient2();

        if(null != getArguments()){
            boolean showTitleBar = getArguments().getBoolean(IntentConstant. KEY_SHOW_TITLE_BAR, true);
            boolean showWebTitle = getArguments().getBoolean(IntentConstant. KEY_SHOW_WEB_TITLE, true);

            if(!showTitleBar){
                hideToolBar();
            }

            if(showWebTitle){
                if(client2 instanceof BaseWebViewClient) {
                    ((BaseWebViewClient)client2).setTitleView(mToolbarTitleTextView);
                }

                if(chromeClient2 instanceof  BaseWebChromeClient){
                    ((BaseWebChromeClient)chromeClient2).setTitleView(mToolbarTitleTextView);
                }
            }
        }


        loadPage();
    }

    private void loadPage(){
        //设置一些白名单。一些公共的头和公共的参数
        // URL地址
        if(null != getArguments()) {
            String url = getArguments().getString(IntentConstant.KEY_URL);
            Serializable headersSerializable = getArguments().getSerializable(IntentConstant.KEY_HEADERS);
            Map<String, String> headers = null;
            if(null != headersSerializable){
                 headers = (Map<String, String>)headersSerializable;
            }

            if (url == null) {
                url = "about:blank";
            }

            webview.loadUrl(url,headers);
        }
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_webview, null);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void onPageStart(String url) {

    }

    @Override
    public void onPageComplete(String url) {

    }

    @Override
    public void onPageOverridingLoadingUrl(String url) {
    }
}
