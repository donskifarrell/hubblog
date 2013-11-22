package com.donskifarrell.Hubblog.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:17
 */
public class Site {
    private String siteName;
    private String accountName;
    private List<Article> articles;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public String[] getPostsTitleList() {
        List<String> titles = new LinkedList<String>();
        for (Article article : this.getArticles()){
            titles.add(article.getTitle());
        }
        return titles.toArray(new String[0]);
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addNewArticle(Article article) {
        this.articles.add(article);
    }
}
