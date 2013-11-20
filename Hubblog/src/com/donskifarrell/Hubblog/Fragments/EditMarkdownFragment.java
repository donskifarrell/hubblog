package com.donskifarrell.Hubblog.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 19:37
 */
public class EditMarkdownFragment extends BasePageFragment {
    private EditText editMarkdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.edit_markdown_layout, container, false);
        scrollView.setSmoothScrollingEnabled(true);

        editMarkdown = (EditText) scrollView.findViewById(R.id.edit_markdown);
        editMarkdown.addTextChangedListener(getTextChangedListener());

        isReady = true;
        return scrollView;
    }

    public void triggerPageUpdate() {
        if (isReady)
            editMarkdown.setText(article.getContent());
    }

    private TextWatcher getTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // when edit text changes send the updated markdown to activity to update webview
                // maybe only do this on view change though?
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setArticleContent(editable.toString());
            }
        };
    }
}
