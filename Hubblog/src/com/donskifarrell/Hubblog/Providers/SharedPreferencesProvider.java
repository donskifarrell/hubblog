package com.donskifarrell.Hubblog.Providers;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.donskifarrell.Hubblog.Providers.Data.Account;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 11:15
 */
public class SharedPreferencesProvider {
    private Context context;

    private final static String PREF_ACCOUNT = "Account";
    private final static String PREF_ACCOUNT_NAME = "AccountName";
    private final static String PREF_ACCOUNT_USER = "Username";
    private final static String PREF_ACCOUNT_PASS = "Password";

    public SharedPreferencesProvider(Context aContext) {
        context = aContext;
    }

    public Account getAccount() {
        Account account = new Account();
        SharedPreferences prefAccount = context.getSharedPreferences(PREF_ACCOUNT, 0);
        account.setAccountName(prefAccount.getString(PREF_ACCOUNT_NAME, ""));
        account.setUsername(prefAccount.getString(PREF_ACCOUNT_USER, ""));
        account.setPassword(prefAccount.getString(PREF_ACCOUNT_PASS, ""));
        return account;
    }

    public boolean setAccount(Account account) {
        SharedPreferences settings = context.getSharedPreferences(PREF_ACCOUNT, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, account.getAccountName());
        editor.putString(PREF_ACCOUNT_USER, account.getUsername());
        editor.putString(PREF_ACCOUNT_PASS, account.getPassword());
        editor.commit();

        return true;
    }
}
