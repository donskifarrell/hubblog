package com.donskifarrell.Hubblog.Providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    private static final String AS = " AS ";
    private static final String DOT = ".";
    private static final String COMMA = ", ";

    /* AHHHHHH */
    public static final String JOIN_ARTICLES_WITH_METADATA_TAGS =
            "SELECT " +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_TITLE + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_FILE_TITLE + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_SITE_NAME + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_CONTENT + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_CREATED_DATE + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_LAST_MODIFIED_DATE + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_IS_DRAFT + COMMA +
                    ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_ID + COMMA +
                    MetadataTagDataModel.TABLE_NAME + DOT + MetadataTagDataModel.COLUMN_ID + AS + MetadataTagDataModel.COLUMN_TAG_ID_ALIAS + COMMA +
                    MetadataTagDataModel.TABLE_NAME + DOT + MetadataTagDataModel.COLUMN_TAG + COMMA +
                    MetadataTagDataModel.TABLE_NAME + DOT + MetadataTagDataModel.COLUMN_ARTICLE_ID + COMMA +
                    "FROM " + ArticleDataModel.TABLE_NAME +
                    " LEFT OUTER JOIN " + MetadataTagDataModel.TABLE_NAME +
                    " ON " +
                        ArticleDataModel.TABLE_NAME + DOT + ArticleDataModel.COLUMN_ID + "=" +
                        MetadataTagDataModel.TABLE_NAME + DOT + MetadataTagDataModel.COLUMN_ID;

    public DatabaseHelper(Context context, DatabaseProvider databaseProvider) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.databaseProvider = databaseProvider;
    }

    /* Database Maintenance */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticleDataModel.CREATE_TABLE);
        db.execSQL(MetadataTagDataModel.CREATE_TABLE);

        bootstrapDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseProvider.class.getName(),
                "Upgrading database from version " + oldVersion +
                        " to " + newVersion + ", which will destroy all old data");

        // todo: handle database upgrade?
    }

    public ContentValues buildContentValuesFromArticle(Article article) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ArticleDataModel.DATE_FORMAT);
        ContentValues cv = new ContentValues();
        cv.put(ArticleDataModel.COLUMN_TITLE, article.getTitle());
        cv.put(ArticleDataModel.COLUMN_FILE_TITLE, article.getFileTitle());
        cv.put(ArticleDataModel.COLUMN_CONTENT, article.getContent());
        cv.put(ArticleDataModel.COLUMN_IS_DRAFT, article.isDraft());
        cv.put(ArticleDataModel.COLUMN_SITE_NAME, article.getSiteName());
        cv.put(ArticleDataModel.COLUMN_CREATED_DATE, dateFormat.format(article.getCreatedDate()));
        cv.put(ArticleDataModel.COLUMN_LAST_MODIFIED_DATE, dateFormat.format(article.getLastModifiedDate()));

        return cv;
    }

    public ContentValues buildContentValuesFromMetadataTag(MetadataTag metadataTag) {
        ContentValues cv = new ContentValues();
        cv.put(MetadataTagDataModel.COLUMN_ARTICLE_ID, metadataTag.getArticleId());
        cv.put(MetadataTagDataModel.COLUMN_TAG, metadataTag.getTag());

        return cv;
    }

    /* Data Models */
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
        public static final String COLUMN_TAG_ID_ALIAS = "tag_id";

        public static final String CREATE_TABLE =
                "CREATE TABLE " +
                        TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER primary key autoincrement, " +
                        COLUMN_ARTICLE_ID + " INTEGER not null, " +
                        COLUMN_TAG + " TEXT not null);";

        public static final String SELECT_ALL =
                "SELECT * FROM " + TABLE_NAME;
    }


    /* Bootstrap code below here */
    EnglishNumberToWords numberToWords;

    private void bootstrapDB(SQLiteDatabase db) {
        numberToWords = new EnglishNumberToWords();

        for (int siteCount = 0; siteCount < 4; siteCount++){
            for (int postCount = 0; postCount < 8; postCount++){
                String siteName = "THE SITE " + numberToWords.convertLessThanOneThousand(siteCount).toUpperCase();
                Article article = createArticle(siteName, postCount);
                List<MetadataTag> tags = new LinkedList<MetadataTag>();
                article.setMetadataTags(tags);
                db.insertOrThrow(ArticleDataModel.TABLE_NAME, ArticleDataModel.COLUMN_TITLE, buildContentValuesFromArticle(article));

                for (int tagCount = 0; tagCount < 4; tagCount++) {
                    MetadataTag tag = new MetadataTag();
                    tag.setArticleId(article.getId());
                    tag.setTag("TAG: " + numberToWords.convertLessThanOneThousand(tagCount).toUpperCase());
                    db.insertOrThrow(MetadataTagDataModel.TABLE_NAME, MetadataTagDataModel.COLUMN_TAG, buildContentValuesFromMetadataTag(tag));
                    tags.add(tag);
                }
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
