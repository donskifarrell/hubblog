package com.donskifarrell.Hubblog.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:17
 */
public class Site {
    private String siteName;
    private String accountName;
    private List<Post> posts;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public String[] getPostsTitleList() {
        List<String> titles = new LinkedList<String>();
        for (Post post : this.getPosts()){
            titles.add(post.getTitle());
        }
        return titles.toArray(new String[0]);
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
