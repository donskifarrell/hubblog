package com.donskifarrell.Hubblog.Interfaces;

import com.donskifarrell.Hubblog.Providers.Data.Account;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;
import com.donskifarrell.Hubblog.Providers.Data.Site;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:04
 */
public interface DataProvider {
    public void getGitHubDetails();

    public Account getAccountDetails();
    public boolean setAccountDetails(Account account);
    public boolean assertAccountDetails();

    public List<String> getSiteNames();
    public List<Site> getSites();
    public void setSites(List<Site> sites);

    public Article addNewArticle(Site site);
    public void removeArticle(Article article);

    public MetadataTag addNewMetadataTag(Article article);
    public void removeMetadataTag(MetadataTag tag);
}
