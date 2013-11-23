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
    private String title;
    private String fileTitle;
    private String site;
    private Map<Integer, MetadataTag> metadata;
    private int lastTagId;
    private String content;
    private boolean isDraft = true;
    private Date createdDate;

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Date lastModifiedDate;

    public Article() {
        metadata = new HashMap<Integer, MetadataTag>();
        lastTagId = 0;
    }

    public Map<Integer, MetadataTag> getMetadataTags() {
        return metadata;
    }

    public int getLastTagIdInMap() {
        return lastTagId;
    }

    public MetadataTag createMetadataTag(String tag) {
        MetadataTag metadataTag = new MetadataTag();
        metadataTag.setId(lastTagId);
        metadataTag.setTag(tag);

        metadata.put(lastTagId, metadataTag);

        lastTagId++;
        return metadataTag;
    }

    public boolean updateMetadataTag(MetadataTag tag) {
        if (metadata.containsKey(tag.getId())) {
            metadata.get(tag.getId()).setTag(tag.getTag());
            return true;
        }

        return false;
    }

    public boolean removeMetadataTag(MetadataTag tag) {
        if (metadata.containsKey(tag.getId())) {
            metadata.remove(tag.getId());
            return true;
        }

        return false;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean draft) {
        isDraft = draft;
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
        String fileTitle = getTitle();
        fileTitle = fileTitle.replaceAll("[^a-zA-Z ]", "-");
        fileTitle = fileTitle.trim().replaceAll(" +", " ");
        fileTitle = fileTitle.replace(" ", "-");

        return dateFormat.format(getCreatedDate()) + "-" + fileTitle + ".22md";
    }

    public String getSiteName() {
        return site.trim();
    }

    public void setSiteName(String site) {
        this.site = site;
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
}