package com.donskifarrell.Hubblog.Providers;

import com.donskifarrell.Hubblog.Interfaces.ActivityDataListener;
import com.donskifarrell.Hubblog.Providers.Data.Account;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.google.inject.Singleton;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:03
 */
@Singleton
public class HubblogDataProvider implements DataProvider {

    protected ActivityDataListener listener;
    protected DatabaseProvider databaseProvider;
    protected FileSystemProvider fileSystemProvider;
    protected GitHubProvider gitHubProvider;
    protected SharedPreferencesProvider preferencesProvider;

    private Account account;
    private List<Site> sites;
    private List<String> siteNames;

    private boolean refreshAccount;
    private boolean refreshSiteNames;

    private final String DEFAULT_NEW_ARTICLE_TITLE = "Untitled Article";
    private final String DEFAULT_NEW_SITE_NAME = "UnassignedSite";

    public HubblogDataProvider(ActivityDataListener activityDataListener) {
        listener = activityDataListener;

        sites = new LinkedList<Site>();

        preferencesProvider = new SharedPreferencesProvider();
        databaseProvider = new DatabaseProvider(listener, this);
        fileSystemProvider = new FileSystemProvider();
        gitHubProvider = new GitHubProvider();
    }

    /* Account Data */
    public Account getAccountDetails() {
        if (account != null && !refreshAccount){
            return account;
        }
        refreshAccount = false;

        account = preferencesProvider.getAccount();
        return account;
    }

    public boolean setAccountDetails(Account account) {
        if (preferencesProvider.setAccount(account)) {
            refreshAccount = true;
            return true;
        } else {
            // todo: feedback to user?
            return false;
        }
    }

    /* Site Data */
    @Override
    public List<String> getSiteNames() {
        if (siteNames != null && !refreshSiteNames){
            return siteNames;
        }
        refreshSiteNames = false;

        List<String> titles = new LinkedList<String>();

        for (Site site : getSites()){
            titles.add(site.getSiteName());
        }

        return siteNames = titles;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> siteList) {
        sites = siteList;
        refreshSiteNames = true;
        listener.Refresh();
    }

    /* Article Data */
    @Override
    public Article addNewArticle(Site site){
        if (site == null) {
            site = new Site(DEFAULT_NEW_SITE_NAME);
            sites.add(site);
        }

        Article newArticle = new Article();
        newArticle.setCreatedDate(new Date());
        newArticle.setLastModifiedDate(new Date());
        newArticle.setSiteName(site.getSiteName());
        newArticle.isDraft(true);
        newArticle.setTitle(DEFAULT_NEW_ARTICLE_TITLE);
        newArticle.setContent("");

        long articleId = databaseProvider.insertArticle(newArticle);
        if (articleId == -1) {
            // todo: error report?
        } else {
            newArticle.setId(articleId);
        }

        List<MetadataTag> tags = new LinkedList<MetadataTag>();
        MetadataTag newTag = new MetadataTag();
        newTag.setArticleId(articleId);
        newTag.setTag("");

        long tagId = databaseProvider.insertTag(newTag);
        if (tagId == -1) {
            // todo: error - report to user?
        } else {
            newTag.setTagId(tagId);
        }

        tags.add(newTag);
        newArticle.setMetadataTags(tags);
        site.addNewArticle(newArticle);

        return newArticle;
    }

    @Override
    public void removeArticle(Article article) {
    }

    @Override
    public MetadataTag addNewMetadataTag(Article article) {
        MetadataTag newTag = new MetadataTag();
        newTag.setArticleId(article.getId());
        newTag.setTag("");

        long tagId = databaseProvider.insertTag(newTag);
        if (tagId == -1) {
            // todo: error - report to user?
        } else {
            newTag.setTagId(tagId);
        }

        article.getMetadataTags().add(newTag);
        return newTag;
    }

    @Override
    public void removeMetadataTag(MetadataTag tag) {
    }
}
