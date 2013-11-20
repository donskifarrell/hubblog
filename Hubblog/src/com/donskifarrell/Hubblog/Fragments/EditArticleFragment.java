package com.donskifarrell.Hubblog.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.donskifarrell.Hubblog.Adapters.ArticleWebViewAdapter;
import com.donskifarrell.Hubblog.Interfaces.ArticleWebViewJsInterface;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 19:37
 */
public class EditArticleFragment extends BasePageFragment {
    private ArticleWebViewAdapter articleWebViewAdapter;
    private WebView browser;
    private boolean isReady = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articleLayout = inflater.inflate(R.layout.edit_article_layout, container, false);
        browser = (WebView) articleLayout.findViewById(R.id.article_web_view);

        setupWebViewClient();

        isReady = true;
        return articleLayout;
    }

    public void triggerPageUpdate() {
        articleWebViewAdapter.triggerArticleUpdate();
    }

    public String getArticleContent() {
        return article.getContent();
    }

    public void setArticleContent(String content) {
        article.setContent(content);
        callback.triggerMarkdownPageUpdate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isReady) {
            if (articleWebViewAdapter == null){
                setupWebViewClient();
            }
            triggerPageUpdate();
        }
    }

    private void setupWebViewClient() {
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new ArticleWebViewJsInterface(this), "Article");
        browser.loadUrl("file:///android_asset/article_webclient/preview.html");

        articleWebViewAdapter = new ArticleWebViewAdapter(browser);
        browser.setWebViewClient(articleWebViewAdapter);
    }
}
