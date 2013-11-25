package com.donskifarrell.Hubblog.Providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.donskifarrell.Hubblog.Interfaces.ActivityDataListener;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
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
public class DatabaseProvider implements LoaderManager.LoaderCallbacks<Cursor> {
    private ActivityDataListener listener;
    private DataProvider centralDataProvider;
    private DatabaseHelper databaseHelper;

    private static final int HUBBLOG_ARTICLE_LOADER = 0;

    private List<Site> sites;

    private static final String whereIdEquals = "_id=";

    @Inject
    public DatabaseProvider(ActivityDataListener activityDataListener, DataProvider dataProvider) {
        databaseHelper = new DatabaseHelper(activityDataListener.getContext(), this);

        sites = new LinkedList<Site>();
        centralDataProvider = dataProvider;

        listener = activityDataListener;
        listener.getSupportLoaderManager().initLoader(HUBBLOG_ARTICLE_LOADER, null, this);
    }

    public long insertArticle(Article article) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues cv = databaseHelper.buildContentValuesFromArticle(article);
        return db.insertOrThrow(DatabaseHelper.ArticleDataModel.TABLE_NAME, DatabaseHelper.ArticleDataModel.COLUMN_TITLE, cv);
    }

    public long insertTag(MetadataTag tag) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = databaseHelper.buildContentValuesFromMetadataTag(tag);
        return db.insertOrThrow(DatabaseHelper.MetadataTagDataModel.TABLE_NAME, DatabaseHelper.MetadataTagDataModel.COLUMN_TAG, cv);
    }

    public void update(Article article) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues cv = databaseHelper.buildContentValuesFromArticle(article);
        db.update(DatabaseHelper.ArticleDataModel.TABLE_NAME, cv, whereIdEquals + article.getId(), null);

        for (MetadataTag tag : article.getMetadataTags()) {
            ContentValues tagCv = databaseHelper.buildContentValuesFromMetadataTag(tag);
            db.update(DatabaseHelper.MetadataTagDataModel.TABLE_NAME, tagCv, whereIdEquals + tag.getTagId(), null);
        }
    }

    /* Data Loading */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        SQLiteCursorLoader cursorLoader;
        String query = "";

        switch (loaderID) {
            case HUBBLOG_ARTICLE_LOADER:
                query = DatabaseHelper.JOIN_ARTICLES_WITH_METADATA_TAGS;
                break;
        }

        cursorLoader = new SQLiteCursorLoader(
                listener.getContext(),
                databaseHelper,
                query,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("HUBBLOG", "CURSOR ID: " + loader.getId() + " > " + DatabaseUtils.dumpCursorToString(cursor));

        switch (loader.getId()) {
            case HUBBLOG_ARTICLE_LOADER:
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    int idx;

                    // Get or create Site
                    Site site = new Site(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_SITE_NAME)));
                    idx = sites.indexOf(site);
                    if (idx == -1) {
                        sites.add(site);
                    } else {
                        site = sites.get(idx);
                    }

                    // Get or create Article
                    Article article = new Article();
                    article.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_ID)));
                    idx = site.getArticles().indexOf(article);
                    if (idx == -1) {
                        article = buildArticleFromCursor(cursor);
                        site.getArticles().add(article);
                    } else {
                        article = site.getArticles().get(idx);
                    }

                    // Get or create MetadataTag
                    MetadataTag tag = new MetadataTag();
                    tag.setTagId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_ARTICLE_ID)));
                    idx = article.getMetadataTags().indexOf(tag);
                    if (idx == -1) {
                        tag = buildMetadataTagFromCursor(cursor);
                        article.getMetadataTags().add(tag);
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

    /* Data Object Building */
    private Article buildArticleFromCursor(Cursor cursor) {
        String siteName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_SITE_NAME));

        Article article = new Article();
        article.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_ID)));
        article.setSiteName(siteName);
        article.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_TITLE)));
        article.setFileTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_FILE_TITLE)));

        int bool = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_IS_DRAFT));
        article.isDraft((bool == 1)? true : false);

        article.setContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_CONTENT)));

        try{
            SimpleDateFormat format = new SimpleDateFormat(DatabaseHelper.ArticleDataModel.DATE_FORMAT);

            Date createdDate = format.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_CREATED_DATE)));
            article.setCreatedDate(createdDate);

            Date modifiedDate = format.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ArticleDataModel.COLUMN_LAST_MODIFIED_DATE)));
            article.setLastModifiedDate(modifiedDate);
        }
        catch (ParseException parseExp) {
            Log.e("HUBBLOG", "Parsing Date Exception");
        }

        return article;
    }

    private MetadataTag buildMetadataTagFromCursor(Cursor cursor) {
        MetadataTag metadataTag = new MetadataTag();
        metadataTag.setArticleId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_ARTICLE_ID)));
        metadataTag.setTagId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_ID)));
        metadataTag.setTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_TAG)));
        return metadataTag;
    }
}
