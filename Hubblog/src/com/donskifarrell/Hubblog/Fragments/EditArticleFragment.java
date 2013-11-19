package com.donskifarrell.Hubblog.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.donskifarrell.Hubblog.Adapters.ArticleWebViewAdapter;
import com.donskifarrell.Hubblog.Interfaces.ArticleWebViewJsInterface;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 19:37
 */
public class EditArticleFragment extends RoboSherlockFragment {
    private ArticleWebViewAdapter articleWebViewAdapter;
    private WebView browser;
    private boolean isReady = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View preview = inflater.inflate(R.layout.edit_article_layout, container, false);
        browser = (WebView) preview.findViewById(R.id.article_web_view);

        setupWebViewClient();

        isReady = true;
        return preview;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isReady) {
            if (articleWebViewAdapter == null){
                setupWebViewClient();
            }

            articleWebViewAdapter.triggerUpdate();
        }
    }

    private void setupWebViewClient() {
        //browser.getSettings().setLoadWithOverviewMode(true);
/*        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setBuiltInZoomControls(true);*/
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new ArticleWebViewJsInterface(this), "previewInterface");
        browser.loadUrl("file:///android_asset/article_webclient/preview.html");

        articleWebViewAdapter = new ArticleWebViewAdapter(browser);
        browser.setWebViewClient(articleWebViewAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            //callback = (OnUpdatePreviewListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUpdatePreviewListener");
        }
    }
}
