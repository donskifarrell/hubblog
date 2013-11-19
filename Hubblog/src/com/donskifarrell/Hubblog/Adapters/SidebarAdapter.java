package com.donskifarrell.Hubblog.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 22:30
 */
public class SidebarAdapter extends BaseAdapter {
    private List<Site> sites;
    private final LayoutInflater inflater;
    private final Context context;

    public SidebarAdapter(Context theContext, List<Site> siteList){
        context = theContext;
        inflater = LayoutInflater.from(context);
        sites = siteList;
    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int position) {
        return sites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();

            if (holder.position == position)
                return convertView;
        }

        return createView(position, parent);
    }

    private View createView(int position, ViewGroup parent){
        final ViewHolder holder;
        View convertView = inflater.inflate(R.layout.sidebar_list_layout, parent, false);

        Site site = sites.get(position);

        holder = new ViewHolder();
        holder.position = position;

        holder.headerLayout = (LinearLayout) convertView.findViewById(R.id.header_title);
        holder.headerTitle = (TextView) holder.headerLayout.findViewById(R.id.title);
        holder.headerTitle.setText(site.getSiteName());

        holder.articlesList = (ListView) convertView.findViewById(R.id.articles);
        holder.articlesList.setAdapter(new ArticlesAdapter(context, site));
        holder.articlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long flags) {
                Toast.makeText( context,
                        "Junk",
                        Toast.LENGTH_LONG).show();
            }
        });

        convertView.setTag(holder);
        return convertView;
    }

    private class ViewHolder {
        private int position;
        private LinearLayout headerLayout;
        private TextView headerTitle;
        private ListView articlesList;
    }
}
