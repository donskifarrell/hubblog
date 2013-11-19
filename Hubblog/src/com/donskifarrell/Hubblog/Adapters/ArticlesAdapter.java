package com.donskifarrell.Hubblog.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.donskifarrell.Hubblog.Data.Post;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 20:26
 */
public class ArticlesAdapter extends BaseAdapter {
    private final Site site;
    private final LayoutInflater inflater;

    public ArticlesAdapter(Context context, Site aSite) {
        inflater = LayoutInflater.from(context);
        site = aSite;
    }

    @Override
    public int getCount() {
        return site.getPosts().size();
    }

    @Override
    public Object getItem(int position) {
        return site.getPosts().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sidebar_list_item, parent, false);

            holder = new ViewHolder();
            holder.articleTitle = (TextView) convertView.findViewById(R.id.sidebar_item);

            Post post = site.getPosts().get(position);
            final Drawable icon;
            if (post.getIsDraft()) {
                icon = convertView.getContext().getResources().getDrawable(R.drawable.edit);
            } else {
                icon = convertView.getContext().getResources().getDrawable(R.drawable.checkmark);
            }


            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            holder.articleTitle.setCompoundDrawables(icon, null, null, null);
            holder.articleTitle.setDrawingCacheBackgroundColor(Color.BLUE);
            holder.articleTitle.setText(post.getTitle());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView articleTitle;
    }
}
