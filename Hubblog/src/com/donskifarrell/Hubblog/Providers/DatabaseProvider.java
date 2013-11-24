package com.donskifarrell.Hubblog.Providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import com.google.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 23/11/13
 * Time: 15:12
 */
public class DatabaseProvider extends SQLiteOpenHelper implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DATABASE_NAME = "hubblog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String JOIN_ARTICLES_WITH_METADATA_TAGS =
            "SELECT * FROM " + ArticleDataModel.TABLE_NAME +
            " LEFT OUTER JOIN " + MetadataTagDataModel.TABLE_NAME +
            " ON " +
                    ArticleDataModel.TABLE_NAME + "." + ArticleDataModel.COLUMN_ID + "=" +
                    MetadataTagDataModel.TABLE_NAME + "." + MetadataTagDataModel.COLUMN_ID;

    @Inject
    public DatabaseProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

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
