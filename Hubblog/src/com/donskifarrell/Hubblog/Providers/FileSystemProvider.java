package com.donskifarrell.Hubblog.Providers;

import android.app.Application;
import android.os.Environment;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.util.Ln;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 12:46
 */
@Singleton
public class FileSystemProvider {
    @Inject
    private Application application;

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWritable = false;
    private String state = Environment.getExternalStorageState();
    private final static String TAG = "Hubblog :: " + FileSystemProvider.class.getSimpleName();

    public List<Site> buildSitesFromStorage(){
        File storagePath = application.getExternalFilesDir(null);
        List<Site> sites = new LinkedList<Site>();

        if (storagePath.exists()) {
            File[] fsItems = storagePath.listFiles();
            for (int idx = 0; idx < fsItems.length; ++idx) {
                File fsItem = fsItems[idx];

                if (fsItem.isDirectory()) {
                    Site site = new Site(fsItem.getName());
                    site.setArticles(buildArticlesForSite(fsItem));
                    sites.add(site);
                }
            }
        }

        return sites;
    }

    public void serialiseSiteToStorage(Site site){
        File storagePath = application.getExternalFilesDir(null);
        if (storagePath.exists()) {
            Ln.i(TAG, "Save Site: "
                    + storagePath.getAbsolutePath()
                    + "/" + site.getSiteName());

            File sitePath = new File(
                    storagePath.getAbsolutePath()
                    + "/" + site.getSiteName());

            if (!sitePath.exists()) {
                if (!sitePath.mkdirs()) {
                    Ln.e(TAG, "Problem creating site folder: " + sitePath);
                }
            }
        }
    }

    private List<Article> buildArticlesForSite(File site) {
        List<Article> articles = new LinkedList<Article>();

        if (site.exists()) {
            File[] fsItems = site.listFiles();
            for (int idx = 0; idx < fsItems.length; ++idx) {
                File fsItem = fsItems[idx];

                if (fsItem.isFile()) {
                    FileInputStream fileInputStream = null;
                    ObjectInputStream objectInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(fsItem.getPath());
                        objectInputStream = new ObjectInputStream(fileInputStream);

                        articles.add((Article) objectInputStream.readObject());
                        objectInputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return articles;
    }

    public void serialiseArticleToStorage(Article article){
        File storagePath = application.getExternalFilesDir(null);
        if (storagePath.exists()) { // todo: handle creation of storage area?
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream= null;
            try {
                Ln.i(TAG, "Save Article: "
                        + storagePath.getAbsolutePath()
                        + "/" + article.getSiteName()
                        + "/" + article.getFileTitle());

                fileOutputStream = new FileOutputStream(storagePath.getAbsolutePath()
                        + "/" + article.getSiteName()
                        + "/" + article.getFileTitle());

                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(article);
                objectOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getExternalStorageState(){
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWritable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWritable = false;
        }
    }
}
