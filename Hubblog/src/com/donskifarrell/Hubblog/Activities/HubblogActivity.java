package com.donskifarrell.Hubblog.Activities;

import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.R;
import shared.ui.actionscontentview.ActionsContentView;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 20/11/13
 * Time: 12:04
 */
public class HubblogActivity extends BaseActivity {

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
}
