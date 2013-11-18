package com.donskifarrell.Hubblog.Activities;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 17:48
 */
public class BaseActivity extends RoboSherlockFragmentActivity {
    @Inject
    public com.donskifarrell.Hubblog.Data.Hubblog hubblog;

}