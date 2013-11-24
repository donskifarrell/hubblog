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
public class DatabaseProvider implements LoaderManager.LoaderCallbacks<Cursor> {
    private RefreshActivityDataListener listener;
    private DataProvider centralDataProvider;
    private DatabaseHelper databaseHelper;

    private static final int HUBBLOG_ARTICLE_LOADER = 0;
    private static final int HUBBLOG_META_TAG_LOADER = 1;

    private List<Site> sites;

    private static final String whereIdEquals = "_id=";
    public static final String JOIN_ARTICLES_WITH_METADATA_TAGS =
            "SELECT * FROM " + DatabaseHelper.ArticleDataModel.TABLE_NAME +
            " LEFT OUTER JOIN " + DatabaseHelper.MetadataTagDataModel.TABLE_NAME +
            " ON " +
                    DatabaseHelper.ArticleDataModel.TABLE_NAME + "." + DatabaseHelper.ArticleDataModel.COLUMN_ID + "=" +
                    DatabaseHelper.MetadataTagDataModel.TABLE_NAME + "." + DatabaseHelper.MetadataTagDataModel.COLUMN_ID;

    @Inject
    public DatabaseProvider(RefreshActivityDataListener refreshActivityDataListener, DataProvider dataProvider) {
        databaseHelper = new DatabaseHelper(refreshActivityDataListener.getContext(), this);
        bootstrapDB();

        sites = new LinkedList<Site>();
        centralDataProvider = dataProvider;

        listener = refreshActivityDataListener;
        listener.getSupportLoaderManager().initLoader(HUBBLOG_ARTICLE_LOADER, null, this);
        listener.getSupportLoaderManager().initLoader(HUBBLOG_META_TAG_LOADER, null, this);
    }

    public void insertArticle(Article article) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.ArticleDataModel.DATE_FORMAT);
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_TITLE, article.getTitle());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_FILE_TITLE, article.getFileTitle());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_CONTENT, article.getContent());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_IS_DRAFT, article.isDraft());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_SITE_NAME, article.getSiteName());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_CREATED_DATE, dateFormat.format(article.getCreatedDate()));
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_LAST_MODIFIED_DATE, dateFormat.format(article.getLastModifiedDate()));
        long result = db.insertOrThrow(DatabaseHelper.ArticleDataModel.TABLE_NAME, DatabaseHelper.ArticleDataModel.COLUMN_TITLE, cv);

        if (result == -1) {
            // todo: error - report to user?
        } else {
            article.setId(result);
        }
    }

    public void insertTags(Article article) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (MetadataTag tag : article.getMetadataTags().values()) {
            ContentValues tagCv = new ContentValues();
            tagCv.put(DatabaseHelper.MetadataTagDataModel.COLUMN_ARTICLE_ID, article.getId());
            tagCv.put(DatabaseHelper.MetadataTagDataModel.COLUMN_TAG, tag.getTag());
            long result = db.insertOrThrow(DatabaseHelper.MetadataTagDataModel.TABLE_NAME, DatabaseHelper.MetadataTagDataModel.COLUMN_TAG, tagCv);

            if (result == -1) {
                // todo: error - report to user?
            } else {
                tag.setTagId(result);
            }
        }
    }

    public void update(Article article) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DatabaseHelper.ArticleDataModel.DATE_FORMAT);
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_TITLE, article.getTitle());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_FILE_TITLE, article.getFileTitle());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_CONTENT, article.getContent());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_IS_DRAFT, article.isDraft());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_SITE_NAME, article.getSiteName());
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_CREATED_DATE, dateFormat.format(article.getCreatedDate()));
        cv.put(DatabaseHelper.ArticleDataModel.COLUMN_LAST_MODIFIED_DATE, dateFormat.format(article.getLastModifiedDate()));

        db.update(DatabaseHelper.ArticleDataModel.TABLE_NAME, cv, whereIdEquals + article.getId(), null);

        for (MetadataTag tag : article.getMetadataTags().values()) {
            ContentValues tagCv = new ContentValues();
            tagCv.put(DatabaseHelper.MetadataTagDataModel.COLUMN_ARTICLE_ID, article.getId());
            tagCv.put(DatabaseHelper.MetadataTagDataModel.COLUMN_TAG, tag.getTag());
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
                query = DatabaseHelper.ArticleDataModel.SELECT_ALL;
                break;
            case HUBBLOG_META_TAG_LOADER:
                query = DatabaseHelper.MetadataTagDataModel.SELECT_ALL;
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

    private MetadataTag buildMetadataTag(Cursor cursor) {
        MetadataTag metadataTag = new MetadataTag();
        metadataTag.setArticleId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_ARTICLE_ID)));
        metadataTag.setTagId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_ID)));
        metadataTag.setTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MetadataTagDataModel.COLUMN_TAG)));
        return metadataTag;
    }





    /* Bootstrap code below here */
    EnglishNumberToWords numberToWords;

    private void bootstrapDB() {
        numberToWords = new EnglishNumberToWords();

        for (int siteCount = 0; siteCount < 4; siteCount++){
            for (int postCount = 0; postCount < 8; postCount++){
                String siteName = "THE SITE " + numberToWords.convertLessThanOneThousand(siteCount).toUpperCase();
                Article article = createArticle(siteName, postCount);

                for (int tagCount = 0; tagCount < 4; tagCount++) {
                    article.createMetadataTag("TAG: " + numberToWords.convertLessThanOneThousand(tagCount).toUpperCase());
                }

                insertArticle(article);
                insertTags(article);
            }
        }
    }

    private Article createArticle(String siteName, int idx){
        Article article = new Article();
        article.setSiteName(siteName);
        article.setTitle("Article " + numberToWords.convertLessThanOneThousand(idx));
        article.setCreatedDate(new Date());
        article.setLastModifiedDate(new Date());
        article.setContent("## Heading2 for article " + numberToWords.convertLessThanOneThousand(idx) +
                "\\n\\n **" + article.getFileTitle() + "**");

        if (idx % 2 == 0) {
            article.isDraft(false);
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
