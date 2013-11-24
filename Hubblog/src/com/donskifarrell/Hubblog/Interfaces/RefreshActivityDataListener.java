package com.donskifarrell.Hubblog.Interfaces;

import android.content.Context;
import android.support.v4.app.LoaderManager;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 23/11/13
 * Time: 23:12
 */
public interface RefreshActivityDataListener<T> {
    public Context getContext();
    public LoaderManager getSupportLoaderManager();
    public void Refresh(T data);
}
