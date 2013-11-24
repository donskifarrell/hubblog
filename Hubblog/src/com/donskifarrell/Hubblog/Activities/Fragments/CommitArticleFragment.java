package com.donskifarrell.Hubblog.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.donskifarrell.Hubblog.Activities.Adapters.MetadataAdapter;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;
import com.donskifarrell.Hubblog.Interfaces.MetadataTagListener;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 11:07
 */
public class CommitArticleFragment extends BasePageFragment
                                   implements MetadataTagListener {
    private MetadataAdapter metadataAdapter;
    private ViewGroup viewGroup;
    private LinearLayout metadataTagList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = container;

        View commitArticle = inflater.inflate(R.layout.commit_article_layout, container, false);

        metadataTagList = (LinearLayout) commitArticle.findViewById(R.id.metadata_tag_list);

        if (metadataAdapter == null)
            metadataAdapter = new MetadataAdapter(this, article);

        ImageButton addMetadataButton = (ImageButton) commitArticle.findViewById(R.id.add_metadata_tag);
        addMetadataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMetadataTag();
            }
        });

        Button saveAsDraftButton = (Button) commitArticle.findViewById(R.id.save_as_draft);
        saveAsDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveArticleAsDraft();
            }
        });

        Button saveAsLiveButton = (Button) commitArticle.findViewById(R.id.save_as_live);
        saveAsLiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveArticleAsLive();
            }
        });

        isReady = true;
        return commitArticle;
    }

    @Override
    public void removeMetadataTagFromView(View view) {
        metadataTagList.removeView(view);
    }

    @Override
    public void triggerPageUpdate() {
        if (!isReady || article == null)
            return;

        metadataTagList.removeAllViews();
        metadataAdapter.setArticle(article);
        addAllMetadataTagsToView();
    }

    private void addAllMetadataTagsToView() {
        if (article.getLastTagIdInMap() == 0) {
            addNewMetadataTag();
        } else {
            for (int idx = 0; idx < article.getLastTagIdInMap(); idx++) {
                if (article.getMetadataTags().containsKey(idx)) {
                    View item = metadataAdapter.getView(idx, null, this.viewGroup);
                    metadataTagList.addView(item);
                }
            }
        }
    }

    private void addNewMetadataTag() {
        if (article == null) return;

        MetadataTag newTag = article.createMetadataTag("");
        View view = metadataAdapter.getView((int)newTag.getTagId(), null, this.viewGroup);
        metadataTagList.addView(view, metadataTagList.getChildCount());
    }

    private void saveArticleAsDraft() {
        if (article == null) return;

    }

    private void saveArticleAsLive() {
        if (article == null) return;

        article.isDraft(false);
    }
}
