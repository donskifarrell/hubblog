package com.donskifarrell.Hubblog.Activities;

import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Interfaces.ArticleContentUpdateListener;
import com.donskifarrell.Hubblog.R;
import shared.ui.actionscontentview.ActionsContentView;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 20/11/13
 * Time: 12:04
 */
public class HubblogActivity extends BaseActivity
                             implements ArticleContentUpdateListener {

    private static final int EDIT_ARTICLE_TAB_POSITION = 0;
    private static final int EDIT_MARKDOWN_TAB_POSITION = 1;

    public void showArticle(Article article) {
        currentArticleTitle = article.getTitle();
        pageIndicator.setCurrentItem(EDIT_ARTICLE_TAB_POSITION);

        tabsAdapter.setArticle(article);
        actionsContentView.showContent();

        // load edit article fragment and generate html content
        Toast.makeText(
                this,
                article.getTitle() + " showing!",
                Toast.LENGTH_LONG).show();
    }

    public void triggerArticlePageUpdate(){
        // markdown page calls this to get article page to update
        tabsAdapter.EditArticle().triggerArticleUpdate();
    }

    public void triggerMarkdownPageUpdate(){
        // article page calls this to get markdown page to update
        tabsAdapter.EditMarkdown().triggerMarkdownUpdate();
    }

    protected ViewPager.OnPageChangeListener getPageChangeListener(){
        return new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case EDIT_ARTICLE_TAB_POSITION:
                        currentArticleSubTitle = getResources().getString(R.string.edit_article_subtitle);
                        setActionBarSubTitle(currentArticleSubTitle);
                        break;
                    case EDIT_MARKDOWN_TAB_POSITION:
                        currentArticleSubTitle = getResources().getString(R.string.edit_markdown_subtitle);
                        setActionBarSubTitle(currentArticleSubTitle);
                        break;
                    default:
                        setActionBarSubTitle("");
                        break;
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {
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
