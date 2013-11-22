package com.donskifarrell.Hubblog.Data;

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

    public String getSiteName() {
        return siteName.trim();
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        sortArticleByDraft();
    }

    public void addNewArticle(Article article) {
        this.articles.add(article);
        sortArticleByDraft();
    }

    private void sortArticleByDraft(){
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
}
