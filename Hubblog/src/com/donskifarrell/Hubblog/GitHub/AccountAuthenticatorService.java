package com.donskifarrell.Hubblog.GitHub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 10/12/13
 * Time: 20:15
 */
public class AccountAuthenticatorService extends Service {

    private static AccountAuthenticator AUTHENTICATOR;

    public IBinder onBind(Intent intent) {
        return intent.getAction().equals(ACTION_AUTHENTICATOR_INTENT) ?
                getAuthenticator().getIBinder() : null;
    }

    private AccountAuthenticator getAuthenticator() {
        if (AUTHENTICATOR == null)
            AUTHENTICATOR = new AccountAuthenticator(this);
        return AUTHENTICATOR;
    }
}