package com.donskifarrell.Hubblog.Data;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:17
 */
public class Account {
    private String accountName;
    private String username;
    private String password;
    private String authKey;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
