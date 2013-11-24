package com.donskifarrell.Hubblog.Interfaces;

import android.content.Context;
import android.support.v4.app.LoaderManager;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 23/11/13
 * Time: 23:12
 */
public interface ActivityDataListener {
    public Context getContext();
    public DataProvider getDataProvider();
    public LoaderManager getSupportLoaderManager();
    public void Refresh();
}
