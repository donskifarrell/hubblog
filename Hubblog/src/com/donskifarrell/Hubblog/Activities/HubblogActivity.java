package com.donskifarrell.Hubblog.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.donskifarrell.Hubblog.Activities.Adapters.SidebarAdapter;
import com.donskifarrell.Hubblog.Activities.Adapters.TabsAdapter;
import com.donskifarrell.Hubblog.Activities.Dialogs.*;
import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.donskifarrell.Hubblog.Interfaces.ActivityDataListener;
import com.donskifarrell.Hubblog.Interfaces.DialogListener;
import com.donskifarrell.Hubblog.Providers.Data.Article;
import com.donskifarrell.Hubblog.Providers.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.OnSidebarListItemSelected;
import com.donskifarrell.Hubblog.Providers.HubblogDataProvider;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.viewpagerindicator.UnderlinePageIndicator;
import shared.ui.actionscontentview.ActionsContentView;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 17:48
 */
public class HubblogActivity extends RoboSherlockFragmentActivity
                             implements OnSidebarListItemSelected,
                                        DialogListener,
                                        ActivityDataListener {

    private static final String STATE_POSITION = "state:layout_id";
    private static final int EDIT_ARTICLE_TAB_POSITION = 0;
    private static final int EDIT_MARKDOWN_TAB_POSITION = 1;
    private static final int COMMIT_ARTICLE_TAB_POSITION = 2;

    protected DataProvider hubblog;
    protected TabsAdapter tabsAdapter;
    protected UnderlinePageIndicator pageIndicator;
    protected ActionsContentView actionsContentView;
    protected Article currentArticle;
    protected String currentArticleTitle;
    protected String currentArticleSubTitle;

    private ListView sidebarList;
    private SidebarAdapter sidebarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_layout);

        hubblog = new HubblogDataProvider(this);
        createActionBar();
        createTabPager();
        createSidebar();

        // todo: launch proper article or a help one?
        // TODO :NEED TO AS IT WILL BREAK LOTS OF THINGS!
        currentArticle = new Article();
        currentArticle.setTitle("CHANGE!");
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
        currentArticle = article;
        currentArticleTitle = article.getTitle();
        tabsAdapter.setArticle(article);

        pageIndicator.setCurrentItem(EDIT_ARTICLE_TAB_POSITION);
        actionsContentView.showContent();

        // load edit article fragment and generate html content
        Toast.makeText(this ,article.getTitle() + " showing!", Toast.LENGTH_LONG).show();
    }

    /* Action Bar Methods */
    private void createActionBar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentArticleTitle = this.getResources().getString(R.string.app_name);
        setActionBarTitle(currentArticleTitle);

        currentArticleSubTitle = this.getResources().getString(R.string.edit_article_subtitle);
        setActionBarSubTitle(currentArticleSubTitle);

        setActionBarHomeIcon(false);
    }

    public void setActionBarHomeIcon(boolean show) {
        getSupportActionBar().setDisplayShowHomeEnabled(show);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setActionBarSubTitle(String subTitle) {
        getSupportActionBar().setSubtitle(subTitle);
    }

    /* Action Bar Sub Menu Methods*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                if (actionsContentView.isActionsShown()) {
                    actionsContentView.showContent();
                } else {
                    actionsContentView.showActions();
                }
                break;

            case (R.id.menu_refresh_item):
                Toast.makeText(this , "Refresh!", Toast.LENGTH_LONG).show();
                break;

            case (R.id.menu_item_invert_display):
                Toast.makeText(this , "Invert!", Toast.LENGTH_LONG).show();
                break;

            case (R.id.menu_item_change_title):
                DialogFragment editArticleTitleDialog = new EditArticleTitleDialogFragment(currentArticle.getTitle());
                editArticleTitleDialog.show(getSupportFragmentManager(), "EditArticleTitleDialogFragment");
                break;

            case (R.id.menu_item_delete_article):
                DialogFragment deleteArticleDialog = new DeleteArticleDialogFragment();
                deleteArticleDialog.show(getSupportFragmentManager(), "DeleteArticleDialogFragment");
                break;

            case (R.id.menu_item_add_site):
                DialogFragment addSiteDialog = new AddSiteDialogFragment();
                addSiteDialog.show(getSupportFragmentManager(), "AddSiteDialogFragment");
                break;

            case (R.id.menu_item_default_tags):
                Toast.makeText(this , "Tags!", Toast.LENGTH_LONG).show();
                break;

            case (R.id.menu_item_about):
                DialogFragment aboutDialog = new AboutDialogFragment();
                aboutDialog.show(getSupportFragmentManager(), "AboutDialogFragment");
                break;
        }

        return super.onOptionsItemSelected(item);
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

        sidebarList = (ListView) sidebar_layout.findViewById(R.id.sidebar_list);
        sidebarAdapter = new SidebarAdapter(this, hubblog);
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
                    setActionBarHomeIcon(false);
                } else {
                    setActionBarTitle(getResources().getString(R.string.sidebar_open_title));
                    setActionBarSubTitle(getResources().getString(R.string.sidebar_open_subtitle));
                    setActionBarHomeIcon(true);
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
                        DialogFragment addSiteDialog = new AddSiteDialogFragment();
                        addSiteDialog.show(getSupportFragmentManager(), "AddSiteDialogFragment");
                        break;
                    case 1:
                        addAndShowArticle(0);
                        break;
                    default:
                        DialogFragment selectSiteDialog = new SelectSiteDialogFragment(hubblog.getSiteNames());
                        selectSiteDialog.show(getSupportFragmentManager(), "SelectSiteDialogFragment");
                        break;
                }
            }
        };
    }

    public void addAndShowArticle(int selectedSite) {
        Article article = hubblog.addNewArticle(hubblog.getSites().get(selectedSite));
        showArticle(article);
    }

    @Override
    public void Refresh() {
        // todo: load last article?

        sidebarAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public DataProvider getDataProvider() {
        return hubblog;
    }

    public void onSelectSitePositiveClick(int selectedSite) {
        Article article = hubblog.addNewArticle(hubblog.getSites().get(selectedSite));
        showArticle(article);
    }

    public void onAddNewSitePositiveClick(String siteName) {
        Site site = new Site(siteName);
        hubblog.getSites().add(site);

        addAndShowArticle(hubblog.getSites().indexOf(site));
    }

    @Override
    public void onSetDefaultTags() {
    }

    @Override
    public void onChangeArticleTitlePositiveClick(String newArticleTitle) {
        currentArticle.setTitle(newArticleTitle);
        setActionBarTitle(newArticleTitle);
    }

    @Override
    public void onDeleteArticlePositiveClick() {
        Toast.makeText(this , "Delete!", Toast.LENGTH_LONG).show();
    }
}