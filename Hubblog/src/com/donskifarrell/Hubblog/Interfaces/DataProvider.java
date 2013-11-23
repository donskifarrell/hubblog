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
    public Account loadAccount();
    public boolean saveAccount(Account account);

    public List<Site> loadSites(Account account);
    public boolean saveSite(Account account, Site site);

    public boolean saveArticle(Article article);
}
