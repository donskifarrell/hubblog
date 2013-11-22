package com.donskifarrell.Hubblog.Providers;

import android.app.Application;
import com.donskifarrell.Hubblog.Data.Account;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:03
 */
@Singleton
public class HubblogDataProvider implements DataProvider {
    @Inject protected Application application;
    @Inject protected FileSystemProvider fileSystemProvider;
    @Inject protected GitHubProvider gitHubProvider;
    @Inject protected SharedPreferencesProvider preferencesProvider;

    public Account loadAccount() {
        return preferencesProvider.getAccount();
    }

    public boolean saveAccount(Account account) {
        return preferencesProvider.setAccount(account);
    }

    public List<Site> loadSites(Account account) {
        // Load from file system immediately but start async task to get GitHub data
        // If account details are blank, just load filesystem

        List<Site> sites = fileSystemProvider.buildSitesFromStorage();

        // todo: Github sync to get latest data from github - maybe use setting to determine when this is done?
        // todo: Github data needs to be saved to storage as well

        return sites;
    }

    public boolean saveSite(Account account, Site site) {
        fileSystemProvider.serialiseSiteToStorage(site);

        // todo: surround in try/catch.
        // todo: Github async to create new repo for site - maybe use setting to determine when this is done?
        return true;
    }

    public boolean saveArticle(Article article) {
        fileSystemProvider.serialiseArticleToStorage(article);

        // todo: surround in try/catch.
        // todo: Github async
        return true;
    }
}
