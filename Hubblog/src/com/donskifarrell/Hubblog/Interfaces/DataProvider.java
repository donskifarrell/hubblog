package com.donskifarrell.Hubblog.Interfaces;

import com.donskifarrell.Hubblog.Providers.Data.Account;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.Site;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:04
 */
public interface DataProvider {
    public Account getAccountDetails();
    public boolean setAccountDetails(Account account);

    public List<String> getSiteNames();
    public List<Site> getSites();
    public boolean addSite(Site site);

    public boolean addArticle(Article article);
}
