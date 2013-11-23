package com.donskifarrell.Hubblog.Activities.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.OnSidebarListItemSelected;
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
    private static Drawable editIcon;
    private static Drawable tickIcon;
    private Context context;
    private OnSidebarListItemSelected callback;

    public ArticlesAdapter(Context aContext, Site aSite) {
        context = aContext;
        inflater = LayoutInflater.from(context);
        site = aSite;

        try {
            callback = (OnSidebarListItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUpdatePreviewListener");
        }
    }

    @Override
    public int getCount() {
        return site.getArticles().size();
    }

    @Override
    public Object getItem(int position) {
        return site.getArticles().get(position);
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

            Article article = (Article) getItem(position);
            if (article.isDraft()) {
                holder.articleTitle.setCompoundDrawables(getEditIcon(convertView.getContext()), null, null, null);
            }
            else {
                holder.articleTitle.setCompoundDrawables(getTickIcon(convertView.getContext()), null, null, null);
            }

            holder.article = article;
            holder.articleTitle.setText(article.getTitle());
            holder.articleTitle.setOnClickListener(new AdapterView.OnClickListener() {

                @Override
                public void onClick(View view) {
                    callback.showArticle(holder.article);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private static Drawable getEditIcon(Context context){
        if (editIcon == null) {
            editIcon = context.getResources().getDrawable(R.drawable.edit_glow);
            editIcon.setBounds(0, 0, editIcon.getIntrinsicWidth(), editIcon.getIntrinsicHeight());
        }
        return editIcon;
    }

    private static Drawable getTickIcon(Context context){
        if (tickIcon == null) {
            tickIcon = context.getResources().getDrawable(R.drawable.checkmark);
            tickIcon.setBounds(0, 0, tickIcon.getIntrinsicWidth(), tickIcon.getIntrinsicHeight());
        }
        return tickIcon;
    }

    private class ViewHolder {
        Article article;
        TextView articleTitle;
    }
}
