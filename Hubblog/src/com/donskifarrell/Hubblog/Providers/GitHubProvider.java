package com.donskifarrell.Hubblog.Providers;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import com.donskifarrell.Hubblog.Interfaces.ActivityDataListener;
import com.github.mobile.Persistence.AccountDataManager;
import com.github.mobile.Persistence.OrganizationLoader;
import com.github.mobile.Utils.UserComparator;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.egit.github.core.User;

import java.util.Collections;
import java.util.List;


/**
 * User: donski
 * Date: 01/10/13
 * Time: 19:49
 */
public class GitHubProvider implements LoaderCallbacks<List<User>> {
    private ActivityDataListener listener;
    private List<User> orgs = Collections.emptyList();

    @Inject
    private AccountDataManager accountDataManager;

    @Inject
    private Provider<UserComparator> userComparatorProvider;

    public GitHubProvider(ActivityDataListener activityDataListener) {
        listener = activityDataListener;

        listener.getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int i, Bundle bundle) {
        return new OrganizationLoader((Activity)listener, accountDataManager, userComparatorProvider);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> listLoader, List<User> orgs) {
        this.orgs = orgs;
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
        // Ignore
    }

    public List<User> getUsers() {
        return orgs;
    }
}