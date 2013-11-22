package com.donskifarrell.Hubblog.Data;

import java.io.Serializable;
import java.util.*;

/**
 * User: donski
 * Date: 22/10/13
 * Time: 11:16
 */
public class Article implements Serializable {
    private String title;
    private String site;
    private Map<Integer, MetadataTag> metadata;
    private int lastTagId;
    private String content;
    private boolean isDraft = true;
    private Date createdDate;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
