package com.donskifarrell.Hubblog.Fragments;

import android.app.Activity;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Interfaces.ArticleContentUpdateListener;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 20/11/13
 * Time: 13:39
 */
public abstract class BasePageFragment extends RoboSherlockFragment {
    protected ArticleContentUpdateListener callback;
    protected Article article;

    public abstract void triggerPageUpdate();

    public void setArticle(Article anArticle) {
        article = anArticle;
        triggerPageUpdate();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (ArticleContentUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ArticleContentUpdateListener");
        }
    }
}
