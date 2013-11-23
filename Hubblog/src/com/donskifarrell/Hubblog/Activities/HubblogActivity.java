package com.donskifarrell.Hubblog.Activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.MenuItem;
import com.donskifarrell.Hubblog.Activities.Adapters.SidebarAdapter;
import com.donskifarrell.Hubblog.Activities.Adapters.TabsAdapter;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.Hubblog;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.donskifarrell.Hubblog.Activities.Fragments.SelectSiteDialogFragment;
import com.donskifarrell.Hubblog.Interfaces.OnSidebarListItemSelected;
import com.donskifarrell.Hubblog.Interfaces.SelectSiteDialogListener;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.viewpagerindicator.UnderlinePageIndicator;
import shared.ui.actionscontentview.ActionsContentView;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 17:48
 */
public class HubblogActivity extends RoboSherlockFragmentActivity
                             implements OnSidebarListItemSelected, SelectSiteDialogListener {
    @Inject protected Hubblog hubblog;

    private static final String STATE_POSITION = "state:layout_id";
    private static final int EDIT_ARTICLE_TAB_POSITION = 0;
    private static final int EDIT_MARKDOWN_TAB_POSITION = 1;
    private static final int COMMIT_ARTICLE_TAB_POSITION = 2;

    protected TabsAdapter tabsAdapter;
    protected UnderlinePageIndicator pageIndicator;
    protected ActionsContentView actionsContentView;
    protected String currentArticleTitle;
    protected String currentArticleSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_layout);

        createActionBar();
        createTabPager();
        createSidebar();

        // todo: launch proper article or a help one?
        //showArticle(hubblog.getSites().get(0).getArticles().get(0));

        final int selectedPosition;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(STATE_POSITION, 0);
        } else {
            // todo: show help article?
            selectedPosition = 0;
        }
    }

    /* Article Methods */
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

    public void addNewArticleToSite(Site site){
        Article newArticle = new Article();
        newArticle.setCreatedDate(new Date());
        newArticle.setSiteName(site.getSiteName());
        newArticle.setIsDraft(true);
        newArticle.setTitle(getResources().getString(R.string.default_article_title));

        site.addNewArticle(newArticle);

        showArticle(newArticle);
    }

    /* Action Bar Methods */
    private void createActionBar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentArticleTitle = this.getResources().getString(R.string.app_name);
        setActionBarTitle(currentArticleTitle);

        currentArticleSubTitle = this.getResources().getString(R.string.edit_article_subtitle);
        setActionBarSubTitle(currentArticleSubTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            if (actionsContentView.isActionsShown()) {
                actionsContentView.showContent();
            } else {
                actionsContentView.showActions();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setActionBarSubTitle(String subTitle) {
        getSupportActionBar().setSubtitle(subTitle);
    }

    /* Tab Navigation Methods */
    private void createTabPager() {
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(tabsAdapter);
        pager.setOffscreenPageLimit(3);

        pageIndicator = (UnderlinePageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setOnPageChangeListener(getPageChangeListener());
        pageIndicator.setViewPager(pager);
    }

    private ViewPager.OnPageChangeListener getPageChangeListener(){
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

    /* Side Bar Methods */
    private void createSidebar() {
        actionsContentView = (ActionsContentView) findViewById(R.id.base_layout);
        actionsContentView.setOnActionsContentListener(getSidebarListener());

        LinearLayout sidebar_layout = (LinearLayout) findViewById(R.id.sidebar_layout);
        Button addNew = (Button) sidebar_layout.findViewById(R.id.sidebar_add_new);
        addNew.setOnClickListener(getSidebarAddNewListener());

        ListView sidebarList = (ListView) sidebar_layout.findViewById(R.id.sidebar_list);
        SidebarAdapter sidebarAdapter = new SidebarAdapter(this, hubblog.getSites());
        sidebarList.setAdapter(sidebarAdapter);
    }

    private ActionsContentView.OnActionsContentListener getSidebarListener() {
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

    private View.OnClickListener getSidebarAddNewListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (hubblog.getSites().size()) {
                    case 0:
                        // todo: show add site dialog
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

    public void onDialogPositiveClick(int selectedSite) {
        addNewArticleToSite(hubblog.getSites().get(selectedSite));
    }
}