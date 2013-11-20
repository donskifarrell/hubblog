package com.donskifarrell.Hubblog.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 19:37
 */
public class EditMarkdownFragment extends RoboSherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.edit_markdown_layout, container, false);
        scrollView.setSmoothScrollingEnabled(true);

        return scrollView;
    }

}
