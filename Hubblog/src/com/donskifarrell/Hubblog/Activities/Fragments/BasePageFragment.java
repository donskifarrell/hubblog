package com.donskifarrell.Hubblog.Activities.Fragments;

import android.app.Activity;
import com.donskifarrell.Hubblog.Interfaces.ActivityDataListener;
import com.donskifarrell.Hubblog.Interfaces.MetadataTagListener;
import com.donskifarrell.Hubblog.Providers.Data.Article;
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
    protected ActivityDataListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (ActivityDataListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityDataListener");
        }
    }

    public abstract void triggerPageUpdate();

    public void setArticle(Article anArticle) {
        article = anArticle;
        triggerPageUpdate();
    }

    public void setArticleContent(String content) {
        if (article == null) return;

        article.setContent(content);
    }
}
