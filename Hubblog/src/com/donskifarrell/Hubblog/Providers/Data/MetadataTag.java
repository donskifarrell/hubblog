package com.donskifarrell.Hubblog.Providers.Data;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 16:51
 */
public class MetadataTag {
    private long tagId;
    private long articleId;
    private String tag;

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getTag() {
        return tag.trim();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
