package com.donskifarrell.Hubblog.Activities.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.donskifarrell.Hubblog.Interfaces.MetadataTagListener;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 18:25
 */
public class MetadataAdapter extends BaseAdapter {
    private Article article;
    private MetadataTagListener callback;
    private final LayoutInflater inflater;
    private final Context context;

    public MetadataAdapter(Fragment articleFragment, Article anArticle) {
        try {
            callback = (MetadataTagListener) articleFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(articleFragment.toString()
                    + " must implement MetadataTagListener");
        }

        context = articleFragment.getActivity();
        inflater = LayoutInflater.from(context);
        article = anArticle;
    }

    public void setArticle(Article anArticle) {
        article = anArticle;
    }

    @Override
    public int getCount() {
        return article.getMetadataTags().size();
    }

    @Override
    public Object getItem(int tagId) {
        if (article.getMetadataTags().containsKey(tagId)) {
            return article.getMetadataTags().get(tagId);
        }
        return null;
    }

    @Override
    public long getItemId(int tagId) {
        return tagId;
    }

    @Override
    public View getView(int position, View metadataTagView, ViewGroup viewGroup) {
        final ViewHolder holder;

        MetadataTag metadataTag = (MetadataTag) getItem(position);
        if (metadataTagView == null) {
            metadataTagView = (LinearLayout) inflater.inflate(R.layout.article_metadata_layout, viewGroup, false);

            holder = new ViewHolder();
            holder.position = position;
            holder.tag = metadataTag;

            holder.metadataText = (EditText) metadataTagView.findViewById(R.id.metadata_tag);
            holder.metadataText.setText(holder.tag.getTag());
            holder.metadataText.addTextChangedListener(new MetaDataTagTextChangedListener(holder.tag));
            holder.metadataText.requestFocus();

            holder.removeTag = (ImageButton) metadataTagView.findViewById(R.id.metadata_remove);
            holder.removeTag.setOnClickListener(new RemoveMetadataTagOnClickListener(holder.tag));

            metadataTagView.setTag(holder);
        } else {
            holder = (ViewHolder) metadataTagView.getTag();
        }

        return metadataTagView;
    }

    private class ViewHolder {
        int position;
        MetadataTag tag;
        EditText metadataText;
        ImageButton removeTag;
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
            callback.removeMetadataTagFromView((View) view.getParent());
        }
    }
}
