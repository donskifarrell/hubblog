package com.donskifarrell.Hubblog.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
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
        return site.getPostsTitleList().length;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sidebar_list_item, parent, false);

            TextView articleTitle = (TextView) convertView.findViewById(R.id.sidebar_item);

            articleTitle.setText(site.getPostsTitleList()[position]);
        }

        return convertView;
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sidebar_list_item, parent, false);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.sidebar_item);

            holder.text.setText(site.getPostsTitleList()[position]);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

*/
/*        final Drawable icon = convertView.getContext().getResources().getDrawable(R.drawable.ic_link_evernote);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        holder.text.setCompoundDrawables(icon, null, null, null);*//*

        holder.text.setText(site.getPostsTitleList()[position]);

        return convertView;
    }
*/

    private static class ViewHolder {
        TextView text;
    }
}
