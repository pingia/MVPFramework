package com.github.pingia.ui.framework.base.web;

public interface IWebPageLoad {
    void onUrlLoadPrepared(String url);
    void onPageStart(String url);
    void onPageComplete(String url);
    void onPageOverridingLoadingUrl(String url);
}
