package com.donskifarrell.Hubblog.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.MenuItem;
import com.donskifarrell.Hubblog.Adapters.SidebarAdapter;
import com.donskifarrell.Hubblog.Adapters.TabsAdapter;
import com.donskifarrell.Hubblog.Data.Account;
import com.donskifarrell.Hubblog.Data.Article;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.Interfaces.OnSidebarListItemSelected;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.viewpagerindicator.UnderlinePageIndicator;
import roboguice.RoboGuice;
import shared.ui.actionscontentview.ActionsContentView;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 17:48
 */
public abstract class BaseActivity extends RoboSherlockFragmentActivity
                                   implements OnSidebarListItemSelected {
    @Inject
    public com.donskifarrell.Hubblog.Data.Hubblog hubblog;

    private static final String STATE_POSITION = "state:layout_id";

    protected TabsAdapter tabsAdapter;
    protected UnderlinePageIndicator pageIndicator;
    protected ActionsContentView actionsContentView;
    protected String currentArticleTitle;
    protected String currentArticleSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo: remove
        //bootstrap();

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

    private void createActionBar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentArticleTitle = this.getResources().getString(R.string.app_name);
        setActionBarTitle(currentArticleTitle);

        currentArticleSubTitle = this.getResources().getString(R.string.edit_article_subtitle);
        setActionBarSubTitle(currentArticleSubTitle);
    }

    private void createTabPager() {
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(tabsAdapter);
        pager.setOffscreenPageLimit(3);

        pageIndicator = (UnderlinePageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setOnPageChangeListener(getPageChangeListener());
        pageIndicator.setViewPager(pager);
    }

    protected abstract ViewPager.OnPageChangeListener getPageChangeListener();

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

    protected abstract ActionsContentView.OnActionsContentListener getSidebarListener();
    protected abstract View.OnClickListener getSidebarAddNewListener();

    /* Bootstrap code below here */

    EnglishNumberToWords numberToWords;

    private void bootstrap() {
        numberToWords = new EnglishNumberToWords();

        Account acc = new Account();
        acc.setAccountName("TestAccount1");
        acc.setUsername("TestUsername");
        acc.setPassword("TestPassword");
        hubblog.setAccount(acc);

        for (int siteCount = 0; siteCount < 4; siteCount++){
            Site site = createSite(siteCount);
            hubblog.addSite(site);

            for (int postCount = 0; postCount < 8; postCount++){
                Article article = createPost(site, postCount);
                hubblog.addArticle(article);
            }
        }
    }

    private Site createSite(int idx){
        Site site = new Site();
        site.setSiteName("THE SITE " + numberToWords.convertLessThanOneThousand(idx).toUpperCase());

        return site;
    }

    private Article createPost(Site site, int idx){
        Article article = new Article();
        article.setSiteName(site.getSiteName());
        article.setTitle("Article " + numberToWords.convertLessThanOneThousand(idx));
        article.setCreatedDate(new Date());
        article.setContent("## Heading2 for article " + numberToWords.convertLessThanOneThousand(idx) +
                           "\\n\\n **" + article.getFileTitle() + "**");

        if (idx % 2 == 0) {
            article.setIsDraft(false);
        }

        return article;
    }

    private class EnglishNumberToWords {

        private final String[] tensNames = { "", " ten", " twenty",
                " thirty", " forty", " fifty", " sixty", " seventy", " eighty",
                " ninety" };

        private final String[] numNames = { "", " one", " two", " three",
                " four", " five", " six", " seven", " eight", " nine", " ten",
                " eleven", " twelve", " thirteen", " fourteen", " fifteen",
                " sixteen", " seventeen", " eighteen", " nineteen" };

        private String convertLessThanOneThousand(int number) {
            String soFar;

            if (number % 100 < 20) {
                soFar = numNames[number % 100];
                number /= 100;
            } else {
                soFar = numNames[number % 10];
                number /= 10;

                soFar = tensNames[number % 10] + soFar;
                number /= 10;
            }
            if (number == 0)
                return soFar;
            return numNames[number] + " hundred" + soFar;
        }
    }
}