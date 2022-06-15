package com.github.pingia.ui.framework.base.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeLog;
import com.smallbuer.jsbridge.core.BridgeTiny;
import com.smallbuer.jsbridge.core.IWebView;
import com.smallbuer.jsbridge.core.OnBridgeCallback;

import java.util.HashMap;
import java.util.Map;


public class BaseWebView extends WebView implements IWebView {
    private BridgeTiny bridgeTiny;
    private Map<String, BridgeHandler> mLocalMessageHandlers = new HashMap<>();

    private String TAG = "BaseWebView";

    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;
    private IWebPageLoad iPageLoadListener;

    public BaseWebView(Context context) {
        this(context,null);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        bridgeTiny = new BridgeTiny(this);
        initWebViewSettings();

        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");
        removeJavascriptInterface("searchBoxJavaBridge");

        //允许webview 能聚焦，这也是保证webview能响应input标签点击弹出键盘的前提，
        // 如果调用requestfocus函数,那么，一进入webview，如果页面有Input标签，会聚焦到第一个Input标签之上。
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setWebViewClient2(client);
        this.setWebChromeClient2(chromeClient);     // TODO: 2017/10/24 大坑，不写这句话，远程js无法调用我的
    }

    private void initWebViewSettings(){
        WebSettings dd = getSettings();
        dd.setJavaScriptEnabled(true);
        dd.setBuiltInZoomControls(true);
        dd.setSavePassword(false);
        dd.setSaveFormData(false);
//        dd.setJavaScriptCanOpenWindowsAutomatically(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            //Enum for specifying the text size.
            // SMALLEST is 50% SMALLER is 75% NORMAL is 100% LARGER is 150% LARGEST is 200%
            dd.setTextZoom(100);
        }else{
            //This method was deprecated in API level 14.  Use setTextZoom(int) instead.
            dd.setTextSize(WebSettings.TextSize.NORMAL);        //把webview的字体大小固定为默认字体大小（系统--字体大小--正常）
        }

        dd.setUseWideViewPort(true);//關鍵點
        dd.setLoadWithOverviewMode(true);
        dd.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            dd.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        dd.setBlockNetworkImage(false);

        dd.setDomStorageEnabled(true);
        dd.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = this.getContext().getCacheDir().getAbsolutePath();
        dd.setAppCachePath(appCachePath);
        dd.setAllowFileAccess(true);
        dd.setAppCacheEnabled(true);
    }

    private void setWebViewClient2(WebViewClient client2){
        mWebViewClient = client2;
        this.setWebViewClient(client2);
    }

    private void setWebChromeClient2(WebChromeClient client2){
        mWebChromeClient = client2;
        this.setWebChromeClient(client2);
    }

    private void setPageLoadListener(IWebPageLoad listener){
        this.iPageLoadListener = listener;
    }

    public WebViewClient getWebViewClient2(){
        return mWebViewClient;
    }

    public WebChromeClient getChromeClient2(){
        return mWebChromeClient;
    }

    private final WebViewClient client = new BaseWebViewClient() {
        /**
         * prevent system browser from launching when web page loads
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if(null != iPageLoadListener) {
                iPageLoadListener.onPageOverridingLoadingUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            if(null != iPageLoadListener) {
                iPageLoadListener.onPageComplete(s);
            }
            bridgeTiny.webViewLoadJs((IWebView) webView);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(null != iPageLoadListener) {
                iPageLoadListener.onPageStart(url);
            }
        }

    };

    private final WebChromeClient chromeClient = new BaseWebChromeClient(){
        @Override
        public boolean onJsPrompt(WebView webView, String url,String message, String defaultValue, JsPromptResult jsPromptResult) {
            BridgeLog.d(TAG,"message->"+message);
            bridgeTiny.onJsPrompt(BaseWebView.this,message);
            //don't delete this line
            jsPromptResult.confirm("do");
            return true;
        }
    };

    @Override
    public void destroy() {
        super.destroy();
        bridgeTiny.freeMemory();
    }

    @Override
    public void addHandlerLocal(String handlerName, BridgeHandler bridgeHandler) {
        mLocalMessageHandlers.put(handlerName,bridgeHandler);
    }

    @Override
    public Map<String, BridgeHandler> getLocalMessageHandlers() {
        return mLocalMessageHandlers;
    }

    @Override
    public void evaluateJavascript(String var1,@Nullable Object object) {
        if(object == null){
            super.evaluateJavascript(var1, null);
            return;
        }
        super.evaluateJavascript(var1, (ValueCallback<String>) object);
    }

    @Override
    public void callHandler(String handlerName, Object data, OnBridgeCallback responseCallback) {
        bridgeTiny.callHandler(handlerName,data,responseCallback);
    }

    @Override
    public void loadUrl(String url) {
        this.loadUrl(url,null);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders){
        if(iPageLoadListener != null){
            iPageLoadListener.onUrlLoadPrepared(url);
        }
        super.loadUrl(url,additionalHttpHeaders);
    }

}
