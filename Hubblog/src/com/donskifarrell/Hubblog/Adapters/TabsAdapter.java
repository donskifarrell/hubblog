package com.donskifarrell.Hubblog.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Fragments.EditArticleFragment;
import com.donskifarrell.Hubblog.Fragments.EditMarkdownFragment;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 19/11/13
 * Time: 20:50
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[] { "Edit Article", "Edit Markdown" };
    private int tabCount = TAB_TITLES.length;
    private EditArticleFragment editArticleFragment;
    private EditMarkdownFragment editMarkdownFragment;

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return EditArticle();
        }

        if (position == 1){
            return EditMarkdown();
        }

        return null;
    }

    public void setArticle(Article article) {
        EditArticle().setArticle(article);
        EditMarkdown().setArticle(article);
    }

    public EditArticleFragment EditArticle() {
        if (editArticleFragment == null) {
            editArticleFragment = new EditArticleFragment();
        }
        return editArticleFragment;
    }

    public EditMarkdownFragment EditMarkdown() {
        if (editMarkdownFragment == null) {
            editMarkdownFragment = new EditMarkdownFragment();
        }
        return editMarkdownFragment;
    }

    public String getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
