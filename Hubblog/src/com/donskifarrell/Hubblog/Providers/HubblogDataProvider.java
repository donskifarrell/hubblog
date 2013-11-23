package com.donskifarrell.Hubblog.Providers;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.donskifarrell.Hubblog.Data.Account;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:03
 */
@Singleton
public class HubblogDataProvider implements DataProvider, LoaderManager.LoaderCallbacks<Cursor> {

    @Inject protected Context context;
    @Inject protected FragmentActivity activity;

    @Inject protected DatabaseProvider databaseProvider;
    @Inject protected FileSystemProvider fileSystemProvider;
    @Inject protected GitHubProvider gitHubProvider;
    @Inject protected SharedPreferencesProvider preferencesProvider;

    private Account account;
    private List<Article> articles;

    private static final int HUBBLOG_ARTICLE_LOADER = 0;

    public HubblogDataProvider() {
        activity.getSupportLoaderManager().initLoader(HUBBLOG_ARTICLE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        String query = "SELECT * FROM " + DatabaseProvider.ArticleDataModel.TABLE_NAME;
        SQLiteCursorLoader cursorLoader = new SQLiteCursorLoader(
                context,
                databaseProvider,
                query,
                null
        );

        return cursorLoader;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.w("HUBBLOG", "FINI");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.w("HUBBLOG", "RESET");
    }





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


    /* Bootstrap code below here */

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
    }
}
