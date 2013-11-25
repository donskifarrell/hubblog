package com.donskifarrell.Hubblog.Providers.Data;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:17
 */
public class Site {
    private String siteName;
    private List<Article> articles;

    public Site(String name) {
        siteName = name;
        articles = new LinkedList<Article>();
    }

    public String getSiteName() {
        return siteName.trim();
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void addNewArticle(Article article) {
        this.articles.add(article);
        sortArticlesByDraft();
    }

    private void sortArticlesByDraft() {
        Collections.sort(articles, new Comparator<Article>() {
            public int compare(Article a1, Article a2) {
                if (!a1.isDraft() && a2.isDraft())
                    return 1;
                if ((a1.isDraft() && a2.isDraft()) || (!a1.isDraft() && !a2.isDraft()))
                    return 0;
                if (a1.isDraft() && !a2.isDraft())
                    return -1;
                return 0;
            }
        });
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }

        Site aSite = (Site) that;
        return this.getSiteName().equals(aSite.getSiteName());
    }
}
