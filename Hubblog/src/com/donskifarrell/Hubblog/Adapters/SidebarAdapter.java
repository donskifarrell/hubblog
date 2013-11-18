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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sidebar_list_layout, parent, false);

            Site site = sites.get(position);

            LinearLayout header_layout = (LinearLayout) convertView.findViewById(R.id.header_title);
            TextView header = (TextView) header_layout.findViewById(R.id.title);
            header.setText(site.getSiteName());

            ListView articlesList = (ListView) convertView.findViewById(R.id.articles);
            articlesList.setAdapter(new ArticlesAdapter(context, site));
            articlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long flags) {
                    //showArticle(position);
                }
            });

        }

        return convertView;
    }
}
