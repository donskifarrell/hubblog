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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articleLayout = inflater.inflate(R.layout.edit_article_layout, container, false);
        browser = (WebView) articleLayout.findViewById(R.id.article_web_view);

        setupWebViewClient();

        isReady = true;
        return articleLayout;
    }

    public void triggerPageUpdate() {
        if (isReady && article != null)
            articleWebViewAdapter.triggerArticleUpdate();
    }

    public String getArticleContent() {
        // should really be passed in as part of triggerArticleUpdate() JS call.
        // Think there was an issue with formatting text tho..
        return article.getContent();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            triggerPageUpdate();
        }
    }

    private void setupWebViewClient() {
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new ArticleWebViewJsInterface(this), "Article");
        browser.loadUrl("file:///android_asset/article_webclient/hubblog.html");

        articleWebViewAdapter = new ArticleWebViewAdapter(browser);
        browser.setWebViewClient(articleWebViewAdapter);
    }
}
