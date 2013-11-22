package com.donskifarrell.Hubblog.Activities;

import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.Fragments.SelectSiteDialogFragment;
import com.donskifarrell.Hubblog.Interfaces.SelectSiteDialogListener;
import com.donskifarrell.Hubblog.R;
import shared.ui.actionscontentview.ActionsContentView;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 20/11/13
 * Time: 12:04
 */
public class HubblogActivity extends BaseActivity
                             implements SelectSiteDialogListener {

    private static final int EDIT_ARTICLE_TAB_POSITION = 0;
    private static final int EDIT_MARKDOWN_TAB_POSITION = 1;
    private static final int COMMIT_ARTICLE_TAB_POSITION = 2;

    public void showArticle(Article article) {
        currentArticleTitle = article.getTitle();
        tabsAdapter.setArticle(article);

        pageIndicator.setCurrentItem(EDIT_ARTICLE_TAB_POSITION);
        actionsContentView.showContent();

        // load edit article fragment and generate html content
        Toast.makeText(
                this,
                article.getTitle() + " showing!",
                Toast.LENGTH_LONG).show();
    }

    protected ViewPager.OnPageChangeListener getPageChangeListener(){
        return new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                switch (position){

                    case EDIT_ARTICLE_TAB_POSITION:
                        currentArticleSubTitle = getResources().getString(R.string.edit_article_subtitle);
                        setActionBarSubTitle(currentArticleSubTitle);
                        tabsAdapter.EditArticle().triggerPageUpdate();
                        break;

                    case EDIT_MARKDOWN_TAB_POSITION:
                        currentArticleSubTitle = getResources().getString(R.string.edit_markdown_subtitle);
                        setActionBarSubTitle(currentArticleSubTitle);
                        tabsAdapter.EditMarkdown().triggerPageUpdate();
                        break;

                    case COMMIT_ARTICLE_TAB_POSITION:
                        currentArticleSubTitle = getResources().getString(R.string.commit_article_subtitle);
                        setActionBarSubTitle(currentArticleSubTitle);
                        tabsAdapter.CommitArticle().triggerPageUpdate();
                        break;

                    default:
                        setActionBarSubTitle("Unknown Page!");
                        break;
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {
                // todo: put update code here? or below? might give better look and feel..
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        };
    }

    protected ActionsContentView.OnActionsContentListener getSidebarListener() {
        return new ActionsContentView.OnActionsContentListener() {
            @Override
            public void onContentStateChanged(ActionsContentView v, boolean isContentShown) {
            }

            @Override
            public void onContentStateInAction(ActionsContentView v, boolean isContentShowing) {
                if (isContentShowing){
                    setActionBarTitle(currentArticleTitle);
                    setActionBarSubTitle(currentArticleSubTitle);
                } else {
                    setActionBarTitle(getResources().getString(R.string.sidebar_open_title));
                    setActionBarSubTitle(getResources().getString(R.string.sidebar_open_subtitle));
                }
            }
        };
    }

    @Override
    protected View.OnClickListener getSidebarAddNewListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (hubblog.getSites().size()) {
                    case 0:
                        // show add site dialog
                        break;
                    case 1:
                        addNewArticleToSite(hubblog.getSites().get(0));
                        break;
                    default:
                        DialogFragment dialog = new SelectSiteDialogFragment(hubblog.getSiteNameList());
                        dialog.show(getSupportFragmentManager(), "SelectSiteDialogFragment");
                        break;
                }
            }
        };
    }

    @Override
    public void onDialogPositiveClick(int selectedSite) {
        addNewArticleToSite(hubblog.getSites().get(selectedSite));
    }

    public void addNewArticleToSite(Site site){
        Article newArticle = new Article();
        newArticle.setCreatedDate(new Date());
        newArticle.setSiteName(site.getSiteName());
        newArticle.setIsDraft(true);
        newArticle.setTitle(getResources().getString(R.string.default_article_title));

        site.addNewArticle(newArticle);

        showArticle(newArticle);
    }
}
