package com.donskifarrell.Hubblog.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
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
    private static Drawable editIcon;
    private static Drawable tickIcon;

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
                holder.articleTitle.setCompoundDrawables(getEditIcon(convertView.getContext()), null, null, null);
            }
            else {
                holder.articleTitle.setCompoundDrawables(getTickIcon(convertView.getContext()), null, null, null);
            }

            holder.articleTitle.setText(post.getTitle());
            holder.articleTitle.setOnClickListener(new AdapterView.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Toast.makeText(
                            view.getContext(),
                            ((TextView)view).getText(),
                            Toast.LENGTH_LONG).show();
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
        TextView articleTitle;
    }
}
