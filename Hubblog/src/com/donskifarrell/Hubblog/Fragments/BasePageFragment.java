package com.donskifarrell.Hubblog.Fragments;

import com.donskifarrell.Hubblog.Data.Article;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 20/11/13
 * Time: 13:39
 */
public abstract class BasePageFragment extends RoboSherlockFragment {
    protected Article article;
    protected boolean isReady = false;

    public abstract void triggerPageUpdate();

    public void setArticle(Article anArticle) {
        article = anArticle;
        triggerPageUpdate();
    }

    public void setArticleContent(String content) {
        article.setContent(content);
    }
}
