package com.github.pingia.ui.framework.base.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BaseWebViewClient extends WebViewClient {
	private String[] mWhites;
	private TextView mTitleView;
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		return super.shouldInterceptRequest(view, url);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		handler.proceed();
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return super.shouldOverrideUrlLoading(view, url);
	}
	
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    	super.onPageStarted(view, url, favicon);
    }
	
    public void onPageFinished(WebView view, String url) {
    	super.onPageFinished(view, url);

		if(null != mTitleView){
			if(!TextUtils.isEmpty(view.getTitle()) && !url.contains(view.getTitle())){
				mTitleView.setText(view.getTitle());
			}
		}
    }

    public void setWhites(String[] whites){
		this.mWhites = whites;
	}

	public void setTitleView(TextView titleView){
		this.mTitleView  = titleView;
	}
	
}
