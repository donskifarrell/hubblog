package com.donskifarrell.Hubblog.Interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 00:36
 */
public interface DialogListener {
    public void onSelectSitePositiveClick(int selectedSite);
    public void onAddNewSitePositiveClick(String siteName);
    public void onSetDefaultTags();

    public void onChangeArticleTitlePositiveClick(String newArticleTitle);
    public void onDeleteArticlePositiveClick();
}
