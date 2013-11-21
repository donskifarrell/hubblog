package com.donskifarrell.Hubblog.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.donskifarrell.Hubblog.Data.MetadataTag;
import com.donskifarrell.Hubblog.R;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 11:07
 */
public class CommitArticleFragment extends BasePageFragment {
    private LayoutInflater layoutInflater;
    private ViewGroup viewGroup;
    private View commitArticle;
    private LinearLayout metadataTagList;
    private ImageButton addMetadataButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        viewGroup = container;

        //if (commitArticle == null)
            commitArticle = inflater.inflate(R.layout.commit_article_layout, container, false);

        //if (metadataTagList == null)
            metadataTagList = (LinearLayout) commitArticle.findViewById(R.id.metadata_tag_list);

        //if (addMetadataButton == null) {
            addMetadataButton = (ImageButton) commitArticle.findViewById(R.id.add_metadata_tag);
            addMetadataButton.setOnClickListener(getAddMetadataTagOnClickListener());
        //}

        // Awful logic..
        if (!isReady)
            triggerPageUpdate();

        isReady = true;
        return commitArticle;
    }

    @Override
    public void triggerPageUpdate() {
        if (!isReady)
            return;

        if (article.getMetadataTags().size() == 0 && metadataTagList.getChildCount() == 0) {
            addNewMetadataTag(article.createMetadataTag(""));
        } else {
            for (Map.Entry<Integer, MetadataTag> tagEntry : article.getMetadataTags().entrySet()) {
                addNewMetadataTag(tagEntry.getValue());
            }
        }
    }

    private void addNewMetadataTag(MetadataTag tag){
        LinearLayout newMetadataTag = (LinearLayout) layoutInflater.inflate(R.layout.article_metadata_layout, viewGroup, false);

        EditText tagText = (EditText) newMetadataTag.findViewById(R.id.metadata_tag);
        tagText.setText(tag.getTag());
        tagText.addTextChangedListener(new MetaDataTagTextChangedListener(tag));

        ImageButton removeTag = (ImageButton) newMetadataTag.findViewById(R.id.metadata_remove);
        removeTag.setOnClickListener(new RemoveMetadataTagOnClickListener(tag));

        metadataTagList.addView(newMetadataTag, metadataTagList.getChildCount());
    }

    private View.OnClickListener getAddMetadataTagOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMetadataTag(article.createMetadataTag(""));
            }
        };
    }

    private class MetaDataTagTextChangedListener implements TextWatcher {
        private MetadataTag tag;

        public MetaDataTagTextChangedListener(MetadataTag aTag) {
            tag = aTag;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            tag.setTag(editable.toString());
            article.updateMetadataTag(tag);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
    }

    private class RemoveMetadataTagOnClickListener implements View.OnClickListener {
        private MetadataTag tag;

        public RemoveMetadataTagOnClickListener(MetadataTag aTag){
                tag = aTag;
        }

        @Override
        public void onClick(View view) {
            article.removeMetadataTag(tag);
            metadataTagList.removeView((View) view.getParent());
        }
    }
}
