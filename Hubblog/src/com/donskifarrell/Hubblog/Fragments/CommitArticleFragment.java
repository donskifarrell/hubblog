package com.donskifarrell.Hubblog.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 11:07
 */
public class CommitArticleFragment extends BasePageFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View commitArticle = inflater.inflate(R.layout.commit_article_layout, container, false);


        isReady = true;
        return commitArticle;
    }

    @Override
    public void triggerPageUpdate() {
        //if (isReady)
    }

}
