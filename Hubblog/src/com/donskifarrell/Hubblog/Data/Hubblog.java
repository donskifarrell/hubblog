package com.donskifarrell.Hubblog.Data;

import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.donskifarrell.Hubblog.Providers.HubblogDataProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.LinkedList;
import java.util.List;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:17
 */
@Singleton
public class Hubblog {
    @Inject protected DataProvider dataProvider;

    private Account account;
    private List<Site> sites;
    private String[] siteNames;

    private boolean refreshAccount;
    private boolean refreshSites;
    private boolean refreshSiteNames;

    public Account getAccount() {
        if (account != null && !refreshAccount){
            return account;
        }
        refreshAccount = false;

        account = dataProvider.loadAccount();
        return account;
    }

    public void setAccount(Account account) {
        if (dataProvider.saveAccount(account)) {
            refreshAccount = true;
        } else {
            // todo: feedback to user?
        }
    }

    public List<Site> getSites() {
        if (sites != null && !refreshSites){
            return sites;
        }
        refreshSites = false;

        sites = dataProvider.loadSites(account);
        return sites;
    }

    public void addSite(Site site) {
        if (dataProvider.saveSite(account, site)) {
            refreshSites = true;
            refreshSiteNames = true;
        } else {
            // todo: feedback to user?
        }
    }

    public String[] getSiteNameList() {
        if (siteNames != null && !refreshSiteNames){
            return siteNames;
        }
        refreshSiteNames = false;

        List<String> titles = new LinkedList<String>();

        for (Site site : this.getSites()){
            titles.add(site.getSiteName());
        }

        return siteNames = titles.toArray(new String[0]);
    }

    public void addArticle(Article article) {
        if (dataProvider.saveArticle(article)) {
            refreshSites = true;
        } else {
            // todo: feedback to user?
        }
    }
}
