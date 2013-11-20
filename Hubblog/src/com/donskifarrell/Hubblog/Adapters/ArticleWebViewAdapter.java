package com.donskifarrell.Hubblog.Adapters;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 19/11/13
 * Time: 21:09
 */
public class ArticleWebViewAdapter extends WebViewClient {
    private WebView webView;

    public ArticleWebViewAdapter(WebView view) {
        webView = view;
    }

    @Override
    public void onPageFinished(WebView view, String url){
        webView.loadUrl("javascript:window.Article.showToast('test');");
    }

    public void triggerUpdate(){
        webView.loadUrl("javascript:showToast();");
    }
}