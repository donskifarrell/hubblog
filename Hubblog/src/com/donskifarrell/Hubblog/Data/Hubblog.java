package com.donskifarrell.Hubblog.Data;

import android.app.Application;
import android.content.SharedPreferences;
import com.donskifarrell.Hubblog.Providers.FileSystem;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:17
 */
@Singleton
public class Hubblog {
    @Inject
    private Application application;

    @Inject
    private FileSystem fileSystem;

    private final static String PREFS_ACCOUNTS = "Accounts";
    private final static String PREF_ACCOUNT_NAME = "AccountName";
    private final static String PREF_ACCOUNT_USER = "Username";
    private final static String PREF_ACCOUNT_PASS = "Password";

    private List<Account> accounts;
    private List<Site> sites;
    private String[] accountsTitles;
    private String[] siteTitles;

    private boolean refreshAccounts;
    private boolean refreshAccountTitles;
    private boolean refreshSites;
    private boolean refreshSiteTitles;

    public List<Account> getAccounts() {
        if (accounts != null && !refreshAccounts){
            return accounts;
        }
        refreshAccounts = false;

        Set<String> accountList = null;
        accounts = new LinkedList<Account>();

        // Restore preferences
        SharedPreferences prefAccounts = application.getSharedPreferences(PREFS_ACCOUNTS, 0);
        accountList = prefAccounts.getStringSet(PREFS_ACCOUNTS, accountList);

        if (accountList != null) {
            for (String accName : accountList) {
                Account account = new Account();
                SharedPreferences prefAccount = application.getSharedPreferences(accName, 0);
                account.setAccountName(prefAccount.getString(PREF_ACCOUNT_NAME, ""));
                account.setUsername(prefAccount.getString(PREF_ACCOUNT_USER, ""));
                account.setPassword(prefAccount.getString(PREF_ACCOUNT_PASS, ""));

                accounts.add(account);
            }
        }

        return accounts;
    }

    public String[] getAccountsTitleList() {
        if (accountsTitles != null && !refreshAccountTitles){
            return accountsTitles;
        }
        refreshAccountTitles = false;

        List<String> titles = new LinkedList<String>();
        for (Account account : this.getAccounts()){
            titles.add(account.getAccountName());
        }

        return accountsTitles = titles.toArray(new String[0]);
    }

    public void addAccount(Account account) {
        Set<String> accountList = null;
        SharedPreferences prefAccounts = application.getSharedPreferences(PREFS_ACCOUNTS, 0);
        accountList = prefAccounts.getStringSet(PREFS_ACCOUNTS, accountList);

        if (accountList == null) {
            accountList = new LinkedHashSet<String>();
        }
        accountList.add(account.getAccountName());

        SharedPreferences.Editor accountsEditor = prefAccounts.edit();
        accountsEditor.putStringSet(PREFS_ACCOUNTS, accountList);
        accountsEditor.commit();

        SharedPreferences settings = application.getSharedPreferences(account.getAccountName(), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, account.getAccountName());
        editor.putString(PREF_ACCOUNT_USER, account.getUsername());
        editor.putString(PREF_ACCOUNT_PASS, account.getPassword());
        editor.commit();

        refreshAccounts = true;
        refreshAccountTitles = true;
    }

    // todo: trigger github call to get latest updates to sites
    public List<Site> getSites() {
        if (sites != null && !refreshSites){
            return sites;
        }
        refreshSites = false;

        sites = new LinkedList<Site>();
        for (Account account : this.getAccounts()){
            sites.addAll(fileSystem.getSites(account.getAccountName()));
        }

        return sites;
    }

    public String[] getSitesTitleList() {
        if (siteTitles != null && !refreshSiteTitles){
            return siteTitles;
        }
        refreshSiteTitles = false;

        List<String> titles = new LinkedList<String>();
        titles.add("All");

        for (Site site : this.getSites()){
            titles.add(site.getSiteName());
        }

        return siteTitles = titles.toArray(new String[0]);
    }

    public void addSite(Site site) {
        fileSystem.saveSite(site);

        refreshSites = true;
        refreshSiteTitles = true;
    }

    public void addPostToSite(Site site, Post post) {
        fileSystem.savePost(site, post);

        refreshSites = true;
    }
}
