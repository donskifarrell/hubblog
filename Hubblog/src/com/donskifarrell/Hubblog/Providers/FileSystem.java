package com.donskifarrell.Hubblog.Providers;

import android.app.Application;
import android.os.Environment;
import com.donskifarrell.Hubblog.Data.Post;
import com.donskifarrell.Hubblog.Data.Site;
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
public class FileSystem {
    @Inject
    private Application application;

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWritable = false;
    private String state = Environment.getExternalStorageState();
    private final static String TAG = "Hubblog :: " + FileSystem.class.getSimpleName();

    public List<Site> getSites(String accountName){
        // get site names and all posts in each site
        File accountPath = null;
        File storagePath = application.getExternalFilesDir(null);
        List<Site> sites = new LinkedList<Site>();

        if (storagePath.exists()) {
            accountPath = new File(storagePath + "/" + accountName);
        }

        if (accountPath.exists()) {
            File[] files = accountPath.listFiles();
            for (int idx = 0; idx < files.length; ++idx) {
                File file = files[idx];
                Site site = new Site();

                if (file.isDirectory()) {
                    site.setSiteName(file.getName());
                    site.setAccountName(accountName);
                    site.setPosts(getPosts(file));
                    sites.add(site);
                }
            }
        }

        return sites;
    }

    public void saveSite(Site site){
        File storagePath = application.getExternalFilesDir(null);
        if (storagePath.exists()) {
            Ln.i(TAG, "Save Site: "
                    + storagePath.getAbsolutePath()
                    + "/" + site.getAccountName()
                    + "/" + site.getSiteName());

            File sitePath = new File(
                    storagePath.getAbsolutePath()
                    + "/" + site.getAccountName()
                    + "/" + site.getSiteName());

            if (!sitePath.exists()) {
                if (!sitePath.mkdirs()) {
                    Ln.e(TAG, "Problem creating site folder: " + sitePath);
                }
            }
        }
    }

    private List<Post> getPosts(File site) {
        List<Post> posts = new LinkedList<Post>();

        if (site.exists()) {
            File[] files = site.listFiles();
            for (int idx = 0; idx < files.length; ++idx) {
                File file = files[idx];

                if (file.isFile()) {
                    FileInputStream fileInputStream = null;
                    ObjectInputStream objectInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file.getPath());
                        objectInputStream = new ObjectInputStream(fileInputStream);

                        posts.add((Post) objectInputStream.readObject());
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

        return posts;
    }

    public void savePost(Site site, Post post){
        File storagePath = application.getExternalFilesDir(null);
        if (storagePath.exists()) { // todo: handle creation of storage area?
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream= null;
            try {
                Ln.i(TAG, "Save Post: "
                        + storagePath.getAbsolutePath()
                        + "/" + site.getAccountName()
                        + "/" + site.getSiteName()
                        + "/" + post.getTitle()
                        + ".md");

                fileOutputStream = new FileOutputStream(storagePath.getAbsolutePath()
                        + "/" + site.getAccountName()
                        + "/" + site.getSiteName()
                        + "/" + post.getTitle()
                        + ".md");
                objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(post);
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
