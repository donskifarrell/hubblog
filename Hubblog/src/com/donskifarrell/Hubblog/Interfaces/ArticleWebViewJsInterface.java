package com.donskifarrell.Hubblog.Interfaces;

import android.widget.Toast;
import com.donskifarrell.Hubblog.Fragments.EditArticleFragment;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 19/11/13
 * Time: 21:11
 */
public class ArticleWebViewJsInterface {
    EditArticleFragment editArticleFragment;

    public ArticleWebViewJsInterface(EditArticleFragment fragment) {
        editArticleFragment = fragment;
    }

/*    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(editArticleFragment.getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public String getMarkdown() {
        return editArticleFragment.callback.getLatestMarkdown();
    }*/
}
