package com.donskifarrell.Hubblog.Providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 24/11/13
 * Time: 17:17
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private DatabaseProvider databaseProvider;
    private static final String DATABASE_NAME = "hubblog.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context, DatabaseProvider databaseProvider) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.databaseProvider = databaseProvider;
    }

    /* Database Maintenance */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(SiteDataModel.CREATE_TABLE);
        db.execSQL(ArticleDataModel.CREATE_TABLE);
        db.execSQL(MetadataTagDataModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseProvider.class.getName(),
                "Upgrading database from version " + oldVersion +
                        " to " + newVersion + ", which will destroy all old data");

        // todo: handle database upgrade?
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
