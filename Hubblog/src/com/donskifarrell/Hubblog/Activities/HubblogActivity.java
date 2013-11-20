package com.donskifarrell.Hubblog.Activities;

import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.donskifarrell.Hubblog.Data.Post;
import com.donskifarrell.Hubblog.R;
import shared.ui.actionscontentview.ActionsContentView;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 20/11/13
 * Time: 12:04
 */
public class HubblogActivity extends BaseActivity {

    public void showArticle(Post post) {
        currentArticleTitle = post.getTitle();
        actionsContentView.showContent();

        // load edit article fragment and generate html content
        Toast.makeText(
                this,
                post.getTitle() + " showing!",
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
