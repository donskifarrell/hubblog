package com.donskifarrell.Hubblog.Providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.donskifarrell.Hubblog.Interfaces.RefreshActivityDataListener;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.google.inject.Inject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 23/11/13
 * Time: 15:12
 */
public class DatabaseProvider extends SQLiteOpenHelper
                              implements LoaderManager.LoaderCallbacks<Cursor> {
    private RefreshActivityDataListener listener;
    private DataProvider centralDataProvider;

    private static final String DATABASE_NAME = "hubblog.db";
    private static final int DATABASE_VERSION = 1;

    private static final int HUBBLOG_ARTICLE_LOADER = 0;
    private static final int HUBBLOG_META_TAG_LOADER = 1;

    private List<Site> sites;

    public static final String JOIN_ARTICLES_WITH_METADATA_TAGS =
            "SELECT * FROM " + ArticleDataModel.TABLE_NAME +
            " LEFT OUTER JOIN " + MetadataTagDataModel.TABLE_NAME +
            " ON " +
                    ArticleDataModel.TABLE_NAME + "." + ArticleDataModel.COLUMN_ID + "=" +
                    MetadataTagDataModel.TABLE_NAME + "." + MetadataTagDataModel.COLUMN_ID;

    @Inject
    public DatabaseProvider(RefreshActivityDataListener refreshActivityDataListener, DataProvider dataProvider) {
        super(refreshActivityDataListener.getContext(), DATABASE_NAME, null, DATABASE_VERSION);

        sites = new LinkedList<Site>();
        centralDataProvider = dataProvider;

        listener = refreshActivityDataListener;
        listener.getSupportLoaderManager().initLoader(HUBBLOG_ARTICLE_LOADER, null, this);
        listener.getSupportLoaderManager().initLoader(HUBBLOG_META_TAG_LOADER, null, this);
    }

    public void insert(Article article) {
        // insert into DB
    }

    /* Database Maintenance */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(SiteDataModel.CREATE_TABLE);
        db.execSQL(ArticleDataModel.CREATE_TABLE);
        db.execSQL(MetadataTagDataModel.CREATE_TABLE);

        /* Todo: remove*/
        SimpleDateFormat dateFormat = new SimpleDateFormat(ArticleDataModel.DATE_FORMAT);
        ContentValues cv = new ContentValues();
        cv.put(ArticleDataModel.COLUMN_TITLE, "TestTile");
        cv.put(ArticleDataModel.COLUMN_FILE_TITLE, "fileTitleTest");
        cv.put(ArticleDataModel.COLUMN_CONTENT, "TestContent");
        cv.put(ArticleDataModel.COLUMN_IS_DRAFT, 1);
        cv.put(ArticleDataModel.COLUMN_SITE_NAME, "TestSiteName");
        cv.put(ArticleDataModel.COLUMN_CREATED_DATE, dateFormat.format(new Date()));
        cv.put(ArticleDataModel.COLUMN_LAST_MODIFIED_DATE, dateFormat.format(new Date()));
        db.insertOrThrow(ArticleDataModel.TABLE_NAME, ArticleDataModel.COLUMN_TITLE, cv);

        ContentValues meta = new ContentValues();
        meta.put(MetadataTagDataModel.COLUMN_ARTICLE_ID, 1);
        meta.put(MetadataTagDataModel.COLUMN_TAG, "TEST META TAG 1");
        db.insertOrThrow(MetadataTagDataModel.TABLE_NAME, MetadataTagDataModel.COLUMN_TAG, meta);

        ContentValues meta2 = new ContentValues();
        meta2.put(MetadataTagDataModel.COLUMN_ARTICLE_ID, 1);
        meta2.put(MetadataTagDataModel.COLUMN_TAG, "TEST META TAG 2");
        db.insertOrThrow(MetadataTagDataModel.TABLE_NAME, MetadataTagDataModel.COLUMN_TAG, meta2);

        ContentValues meta3 = new ContentValues();
        meta3.put(MetadataTagDataModel.COLUMN_ARTICLE_ID, 1);
        meta3.put(MetadataTagDataModel.COLUMN_TAG, "TEST META TAG 3");
        db.insertOrThrow(MetadataTagDataModel.TABLE_NAME, MetadataTagDataModel.COLUMN_TAG, meta3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseProvider.class.getName(),
              "Upgrading database from version " + oldVersion +
              " to " + newVersion + ", which will destroy all old data");

        // todo: handle database upgrade?
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
                this,
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

        centralDataProvider.setSites(sites);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.w("HUBBLOG", "RESET");
    }

    /* Data Object Buidling */
    private Article buildArticle(Cursor cursor) {
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

    private MetadataTag buildMetadataTag(Cursor cursor) {
        MetadataTag metadataTag = new MetadataTag();
        metadataTag.setArticleId(cursor.getLong(cursor.getColumnIndex(DatabaseProvider.MetadataTagDataModel.COLUMN_ARTICLE_ID)));
        metadataTag.setTagId(cursor.getLong(cursor.getColumnIndex(DatabaseProvider.MetadataTagDataModel.COLUMN_ID)));
        metadataTag.setTag(cursor.getString(cursor.getColumnIndex(DatabaseProvider.MetadataTagDataModel.COLUMN_TAG)));
        return metadataTag;
    }

    /* Data Models */
    public static class SiteDataModel {
        public static final String TABLE_NAME = "sites";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_SITE_NAME = "site_name";

        public static final String CREATE_TABLE =
                "CREATE TABLE " +
                        TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER primary key autoincrement, " +
                        COLUMN_SITE_NAME + " TEXT not null);";
    }

    public static class ArticleDataModel {
        public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String TABLE_NAME = "articles";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_SITE_NAME = "site_name";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_FILE_TITLE = "file_title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_IS_DRAFT = "is_draft";
        public static final String COLUMN_CREATED_DATE = "created_date";
        public static final String COLUMN_LAST_MODIFIED_DATE = "last_modified_date";

        public static final String CREATE_TABLE =
                "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER primary key autoincrement, " +
                COLUMN_SITE_NAME + " TEXT, " +
                COLUMN_TITLE + " TEXT not null, " +
                COLUMN_FILE_TITLE + " TEXT not null, " +
                COLUMN_CONTENT + " TEXT not null, " +
                COLUMN_IS_DRAFT + " INTEGER not null, " +
                COLUMN_CREATED_DATE + " DATETIME not null, " +
                COLUMN_LAST_MODIFIED_DATE + " DATETIME not null);";

        public static final String SELECT_ALL =
                "SELECT * FROM " + TABLE_NAME;
    }

    public static class MetadataTagDataModel {
        public static final String TABLE_NAME = "metadata";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ARTICLE_ID = "article_id";
        public static final String COLUMN_TAG = "tag";

        public static final String CREATE_TABLE =
                "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER primary key autoincrement, " +
                COLUMN_ARTICLE_ID + " INTEGER not null, " +
                COLUMN_TAG + " TEXT not null);";

        public static final String SELECT_ALL =
                "SELECT * FROM " + TABLE_NAME;
    }
}
