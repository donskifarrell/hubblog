package com.donskifarrell.Hubblog.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 22:30
 */
public class SidebarAdapter extends BaseAdapter {
    private DataProvider hubblog;
    private final LayoutInflater inflater;
    private final Context context;

    public SidebarAdapter(Context theContext, DataProvider dataProvider){
        context = theContext;
        inflater = LayoutInflater.from(context);
        hubblog = dataProvider;
    }

    @Override
    public int getCount() {
        return hubblog.getSites().size();
    }

    @Override
    public Object getItem(int position) {
        return hubblog.getSites().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Site site = (Site) getItem(position);

        if (convertView == null) {
            convertView = createView(site, position, convertView, parent);
        } else {
            holder = (ViewHolder) convertView.getTag();

            if (holder.position != position) {
                convertView = createView(site, position, convertView, parent);
            }
        }

        return convertView;
    }

    private View createView(Site site, int position, View convertView, ViewGroup parent){
        convertView = inflater.inflate(R.layout.sidebar_list_layout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.position = position;

        holder.headerLayout = (LinearLayout) convertView.findViewById(R.id.header_title);
        holder.headerTitle = (TextView) holder.headerLayout.findViewById(R.id.title);
        holder.headerTitle.setText(site.getSiteName());

        ArticlesAdapter articlesAdapter = new ArticlesAdapter(context, site);
        holder.articlesList = new TextView[articlesAdapter.getCount()];

        for (int idx = 0; idx < articlesAdapter.getCount(); idx++) {
            View item = articlesAdapter.getView(idx, null, null);
            holder.articlesList[idx] = (TextView) item;
            ((LinearLayout)convertView).addView(item);
        }

        convertView.setTag(holder);
        return convertView;
    }

    private class ViewHolder {
        private int position;
        private LinearLayout headerLayout;
        private TextView headerTitle;
        private TextView[] articlesList;
    }
}
