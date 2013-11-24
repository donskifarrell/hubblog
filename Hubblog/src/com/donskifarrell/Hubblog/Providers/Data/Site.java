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
/*    private List<Long> articleIds;
    private Long lastArticleId;*/

    public Site(String name) {
        siteName = name;
        articles = new LinkedList<Article>();
/*        articleIds = new LinkedList<Long>();
        lastArticleId = (long) 0;*/
    }

    public String getSiteName() {
        return siteName.trim();
    }

/*    public List<Long> getArticleIds() {
        return articleIds;
    }

    public Long getLastArticleId() {
        return lastArticleId;
    }*/

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        createListOfArticleIds();
        sortArticlesByDraft();
    }

    public void addNewArticle(Article article) {
        this.articles.add(article);
        createListOfArticleIds();
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

    private void createListOfArticleIds() {
/*        articleIds.clear();

        for (Article article : articles) {
            articleIds.add(article.getId());
            if (article.getId() > lastArticleId){
                lastArticleId = article.getId();
            }
        }*/
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this.getSiteName() == null) {
            return that == null;
        }
        return this.getSiteName().equals(that);
    }
}
