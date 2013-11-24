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
    private boolean refreshSites;
    private boolean refreshSiteNames;


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
        refreshSites = true;
        refreshSiteNames = true;
    }

    /* Article Data */
    @Override
    public boolean addArticle(Article article) {
        // add default metadata??
        // insert into DB
        Site site;
        if (!sites.contains(article.getSiteName())) {
            site = new Site(article.getSiteName());
            List<Article> newArticles = new LinkedList<Article>();
            newArticles.add(article);
            site.setArticles(newArticles);
        } else {
            site = sites.get(sites.indexOf(article.getSiteName()));
            site.getArticles().add(article);
        }

        databaseProvider.insert(article);

        return false;
    }



/*
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
    }*/


    /* Bootstrap code below here */
/*
    EnglishNumberToWords numberToWords;

    private void bootstrap() {
        numberToWords = new EnglishNumberToWords();

        Account acc = new Account();
        acc.setAccountName("TestAccount1");
        acc.setUsername("TestUsername");
        acc.setPassword("TestPassword");
        hubblog.setAccount(acc);

        for (int siteCount = 0; siteCount < 4; siteCount++){
            Site site = createSite(siteCount);
            hubblog.addSite(site);

            for (int postCount = 0; postCount < 8; postCount++){
                Article article = createPost(site, postCount);
                hubblog.addArticle(article);
            }
        }
    }

    private Site createSite(int idx){
        Site site = new Site();
        site.setSiteName("THE SITE " + numberToWords.convertLessThanOneThousand(idx).toUpperCase());

        return site;
    }

    private Article createPost(Site site, int idx){
        Article article = new Article();
        article.setSiteName(site.getSiteName());
        article.setTitle("Article " + numberToWords.convertLessThanOneThousand(idx));
        article.setCreatedDate(new Date());
        article.setContent("## Heading2 for article " + numberToWords.convertLessThanOneThousand(idx) +
                "\\n\\n **" + article.getFileTitle() + "**");

        if (idx % 2 == 0) {
            article.setIsDraft(false);
        }

        return article;
    }

    private class EnglishNumberToWords {

        private final String[] tensNames = { "", " ten", " twenty",
                " thirty", " forty", " fifty", " sixty", " seventy", " eighty",
                " ninety" };

        private final String[] numNames = { "", " one", " two", " three",
                " four", " five", " six", " seven", " eight", " nine", " ten",
                " eleven", " twelve", " thirteen", " fourteen", " fifteen",
                " sixteen", " seventeen", " eighteen", " nineteen" };

        private String convertLessThanOneThousand(int number) {
            String soFar;

            if (number % 100 < 20) {
                soFar = numNames[number % 100];
                number /= 100;
            } else {
                soFar = numNames[number % 10];
                number /= 10;

                soFar = tensNames[number % 10] + soFar;
                number /= 10;
            }
            if (number == 0)
                return soFar;
            return numNames[number] + " hundred" + soFar;
        }
    }*/
}
