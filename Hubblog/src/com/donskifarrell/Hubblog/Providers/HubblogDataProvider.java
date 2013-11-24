package com.donskifarrell.Hubblog.Providers;

import com.donskifarrell.Hubblog.Interfaces.RefreshActivityDataListener;
import com.donskifarrell.Hubblog.Providers.Data.Account;
import com.donskifarrell.Hubblog.Providers.Data.Article;
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

    protected RefreshActivityDataListener listener;
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

    public HubblogDataProvider(RefreshActivityDataListener refreshActivityDataListener) {
        listener = refreshActivityDataListener;

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
    }

    /* Article Data */
    @Override
    public Article addNewArticle(Site site){
        if (site == null) {
            site = new Site(DEFAULT_NEW_SITE_NAME);
            site.setArticles(new LinkedList<Article>());
            sites.add(site);
        }

        Article newArticle = new Article();
        newArticle.setCreatedDate(new Date());
        newArticle.setLastModifiedDate(new Date());
        newArticle.setSiteName(site.getSiteName());
        newArticle.isDraft(true);
        newArticle.setTitle(DEFAULT_NEW_ARTICLE_TITLE);
        newArticle.createMetadataTag("");

        databaseProvider.insertArticle(newArticle);
        databaseProvider.insertTags(newArticle);
        site.addNewArticle(newArticle);

        return newArticle;
    }
}
