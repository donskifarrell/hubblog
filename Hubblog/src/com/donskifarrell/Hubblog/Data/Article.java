package com.donskifarrell.Hubblog.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:16
 */
public class Article implements Serializable {
    private String title;
    private String site;
    private String content;
    private boolean isDraft = true;
    private Date createdDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean draft) {
        isDraft = draft;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
