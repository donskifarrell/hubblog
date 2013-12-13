package com.donskifarrell.Hubblog.Activities;

import android.content.Intent;
import android.os.Bundle;
import com.donskifarrell.Hubblog.GitHub.Accounts.AccountAuthenticatorService;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 12/12/13
 * Time: 11:38
 */
public class StartupActivity extends RoboSherlockFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(getApplicationContext(), AccountAuthenticatorService.class);
        startService(serviceIntent);
    }

    @Override
    public void onStart() {
        super.onStart();

        finish();
    }
}
