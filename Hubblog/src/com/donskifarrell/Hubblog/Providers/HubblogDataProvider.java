package com.donskifarrell.Hubblog.Providers;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.donskifarrell.Hubblog.Interfaces.RefreshActivityDataListener;
import com.donskifarrell.Hubblog.Providers.Data.Account;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.google.inject.Singleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:03
 */
@Singleton
public class HubblogDataProvider implements DataProvider, LoaderManager.LoaderCallbacks<Cursor> {

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

    private static final int HUBBLOG_ARTICLE_LOADER = 0;
    private static final int HUBBLOG_META_TAG_LOADER = 1;

    public HubblogDataProvider(RefreshActivityDataListener refreshActivityDataListener) {
        listener = refreshActivityDataListener;

        databaseProvider = new DatabaseProvider(listener.getContext());
        fileSystemProvider = new FileSystemProvider();
        gitHubProvider = new GitHubProvider();
        preferencesProvider = new SharedPreferencesProvider();

        sites = new LinkedList<Site>();

        listener.getSupportLoaderManager().initLoader(HUBBLOG_ARTICLE_LOADER, null, this);
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
        if (sites != null && !refreshSites){
            return sites;
        }
        refreshSites = false;

        //sites = dataProvider.loadSites(account);
        return sites;
    }

    public boolean addSite(Site site) {
        //if (dataProvider.saveSite(account, site)) {
            refreshSites = true;
            refreshSiteNames = true;
            return true;
        //} else {
            // todo: feedback to user?
        //    return false;
        //}
    }

    private void buildSites(HashMap<String, List<Article>> sitesMap) {
        for (String key : sitesMap.keySet()) {
            for (Article article : sitesMap.get(key)) {
                Site newSite = new Site(article.getSiteName());
                newSite.addNewArticle(article);
                sites.add(newSite);
            }
        }
    }

    /* Article Data */
    @Override
    public boolean addArticle(Article article) {
        return false;
    }

    public Article buildArticle(Cursor cursor) {
        String siteName = cursor.getString(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_SITE_NAME));

        Article article = new Article();
        article.setId(cursor.getLong(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_ID)));
        article.setSiteName(siteName);
        article.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_TITLE)));
        article.setFileTitle(cursor.getString(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_FILE_TITLE)));

        int bool = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_IS_DRAFT));
        article.isDraft((bool == 1)? true : false);

        article.setContent(cursor.getString(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_CONTENT)));

        try{
            SimpleDateFormat format = new SimpleDateFormat(DatabaseProvider.ArticleDataModel.DATE_FORMAT);

            Date createdDate = format.parse(cursor.getString(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_CREATED_DATE)));
            article.setCreatedDate(createdDate);

            Date modifiedDate = format.parse(cursor.getString(cursor.getColumnIndex(DatabaseProvider.ArticleDataModel.COLUMN_LAST_MODIFIED_DATE)));
            article.setLastModifiedDate(modifiedDate);
        }
        catch (ParseException parseExp) {
            Log.e("HUBBLOG", "Parsing Date Exception");
        }

        return article;
    }

    public MetadataTag buildMetadataTag(Cursor cursor) {
        MetadataTag metadataTag = new MetadataTag();
        metadataTag.setArticleId(cursor.getLong(cursor.getColumnIndex(DatabaseProvider.MetadataTagDataModel.COLUMN_ARTICLE_ID)));
        metadataTag.setTagId(cursor.getLong(cursor.getColumnIndex(DatabaseProvider.MetadataTagDataModel.COLUMN_ID)));
        metadataTag.setTag(cursor.getString(cursor.getColumnIndex(DatabaseProvider.MetadataTagDataModel.COLUMN_TAG)));
        return metadataTag;
    }

    /* Data Loading */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        SQLiteCursorLoader cursorLoader;
        String query = "";

        switch (loaderID) {
            case HUBBLOG_ARTICLE_LOADER:
                query = DatabaseProvider.ArticleDataModel.SELECT_ALL;
                break;
            case HUBBLOG_META_TAG_LOADER:
                query = DatabaseProvider.MetadataTagDataModel.SELECT_ALL;
                break;
        }

        cursorLoader = new SQLiteCursorLoader(
                                listener.getContext(),
                                databaseProvider,
                                query,
                                null
                        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.w("HUBBLOG", "CURSOR ID: " + loader.getId() + " > " + DatabaseUtils.dumpCursorToString(cursor));

        switch (loader.getId()) {
            case HUBBLOG_ARTICLE_LOADER:
                HashMap<String, List<Article>> sitesMap = new HashMap<String, List<Article>>();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Article article = buildArticle(cursor);

                    if (sitesMap.containsKey(article.getSiteName())) {
                        sitesMap.get(article.getSiteName()).add(article);
                    } else {
                        LinkedList<Article> siteArticles = new LinkedList<Article>();
                        siteArticles.add(article);
                        sitesMap.put(article.getSiteName(), siteArticles);
                    }
                }

                for (String key : sitesMap.keySet()) {
                    for (Article article : sitesMap.get(key)) {
                        Site newSite = new Site(article.getSiteName());
                        newSite.addNewArticle(article);
                        sites.add(newSite);
                    }
                }

                break;
            case HUBBLOG_META_TAG_LOADER:
                HashMap<Long, Map<Long, MetadataTag>> metaMap = new HashMap<Long, Map<Long, MetadataTag>>();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    MetadataTag metadataTag = buildMetadataTag(cursor);

                    if (metaMap.containsKey(metadataTag.getArticleId())) {
                        metaMap.get(metadataTag.getArticleId()).put(metadataTag.getTagId(), metadataTag);
                    } else {
                        Map<Long, MetadataTag> articleTags = new HashMap<Long, MetadataTag>();
                        articleTags.put(metadataTag.getTagId(), metadataTag);
                        metaMap.put(metadataTag.getArticleId(), articleTags);
                    }
                }

                for (Site site : sites) {
                    for (Article article : site.getArticles()) {
                        for (long articleId : metaMap.keySet()) {
                            if (article.getId() == articleId) {
                                article.setMetadataTags(metaMap.get(articleId));
                            }
                        }
                    }
                }

                break;
        }

        listener.Refresh(sites);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.w("HUBBLOG", "RESET");
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
