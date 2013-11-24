package com.donskifarrell.Hubblog.Providers.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:16
 */
public class Article implements Serializable {
    private long id;
    private String siteName;
    private String title;
    private String fileTitle;
    private boolean isDraft = true;
    private List<MetadataTag> metadataTags;
    private String content;
    private Date createdDate;
    private Date lastModifiedDate;

    public Article() {
        metadataTags = new LinkedList<MetadataTag>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName.trim();
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getTitle() {
        return title.trim();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileTitle() {
        // To be used by Jekyll the file format is:
        // YEAR-MONTH-DAY-title.md
        // All whitespace is converted to '-'

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName= getTitle();
        fileName = fileName.replaceAll("[^a-zA-Z ]", "-");
        fileName = fileName.trim().replaceAll(" +", " ");
        fileName = fileName.replace(" ", "-");

        fileTitle =  dateFormat.format(getCreatedDate()) + "-" + fileName + ".22md";
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void isDraft(boolean draft) {
        isDraft = draft;
    }

    public List<MetadataTag> getMetadataTags() {
        return metadataTags;
    }

    public void setMetadataTags(List<MetadataTag> metadataTags) {
        this.metadataTags = metadataTags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }

        Article anArticle = (Article) that;
        return this.getId() == anArticle.getId();
    }

/*
    public MetadataTag createMetadataTag(String tag) {
        MetadataTag metadataTag = new MetadataTag();
        metadataTag.setArticleId(getId());
        metadataTag.setTag(tag);

        metadataTags.put((long)lastTagId, metadataTag);

        lastTagId++;
        return metadataTag;
    }

    public boolean updateMetadataTag(MetadataTag tag) {
        if (metadataTags.containsKey(tag.getTagId())) {
            metadataTags.get(tag.getTagId()).setTag(tag.getTag());
            return true;
        }

        return false;
    }

    public boolean removeMetadataTag(MetadataTag tag) {
        if (metadataTags.containsKey(tag.getTagId())) {
            metadataTags.remove(tag.getTagId());
            return true;
        }

        return false;
    }*/
}
