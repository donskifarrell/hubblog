package com.donskifarrell.Hubblog.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.donskifarrell.Hubblog.Adapters.MetadataAdapter;
import com.donskifarrell.Hubblog.Data.MetadataTag;
import com.donskifarrell.Hubblog.Interfaces.RemoveMetadataTagListener;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 11:07
 */
public class CommitArticleFragment extends BasePageFragment
                                   implements RemoveMetadataTagListener {
    private MetadataAdapter metadataAdapter;

    private LayoutInflater layoutInflater;
    private ViewGroup viewGroup;
    private View commitArticle;
    private LinearLayout metadataTagList;
    private ImageButton addMetadataButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        viewGroup = container;

        commitArticle = inflater.inflate(R.layout.commit_article_layout, container, false);

        metadataTagList = (LinearLayout) commitArticle.findViewById(R.id.metadata_tag_list);

        if (metadataAdapter == null)
            metadataAdapter = new MetadataAdapter(this, article);

        addAllMetadataTagsToView();

        addMetadataButton = (ImageButton) commitArticle.findViewById(R.id.add_metadata_tag);
        addMetadataButton.setOnClickListener(getAddMetadataTagOnClickListener());

        isReady = true;
        return commitArticle;
    }

    @Override
    public void removeMetadataTagFromView(View view) {
        metadataTagList.removeView(view);
    }

    @Override
    public void triggerPageUpdate() {
        if (!isReady)
            return;

        metadataTagList.removeAllViews();
        metadataAdapter.setArticle(article);
        addAllMetadataTagsToView();
    }

    private void addAllMetadataTagsToView(){
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

    private void addNewMetadataTag(){
        MetadataTag newTag = article.createMetadataTag("");
        View view = metadataAdapter.getView(newTag.getId(), null, this.viewGroup);
        metadataTagList.addView(view, metadataTagList.getChildCount());
    }

    private View.OnClickListener getAddMetadataTagOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMetadataTag();
            }
        };
    }
}
