package com.donskifarrell.Hubblog.Activities;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.MenuItem;
import com.donskifarrell.Hubblog.Adapters.SidebarAdapter;
import com.donskifarrell.Hubblog.Data.Account;
import com.donskifarrell.Hubblog.Data.Post;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import shared.ui.actionscontentview.ActionsContentView;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 18/11/13
 * Time: 17:48
 */
public class BaseActivity extends RoboSherlockFragmentActivity {
    @Inject
    public com.donskifarrell.Hubblog.Data.Hubblog hubblog;

    private static final String STATE_POSITION = "state:layout_id";
    private ActionBarDrawerToggle drawerToggle;
    private ActionsContentView actionsContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo: remove
        bootstrap();

        setContentView(R.layout.base_layout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionsContentView = (ActionsContentView) findViewById(R.id.base_layout);
        actionsContentView.setOnActionsContentListener(getSidebarListener());

        LinearLayout sidebar_layout = (LinearLayout) findViewById(R.id.sidebar_layout);
        ListView sidebarList = (ListView) sidebar_layout.findViewById(R.id.sidebar_list);

        SidebarAdapter sidebarAdapter = new SidebarAdapter(this, hubblog.getSites());
        sidebarList.setAdapter(sidebarAdapter);

        actionsContentView.showActions();

        final int selectedPosition;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(STATE_POSITION, 0);
        } else {
            selectedPosition = 0;
        }

        showArticle(selectedPosition);
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

    private void showArticle(int position){

    }

    private ActionsContentView.OnActionsContentListener getSidebarListener(){
        return new ActionsContentView.OnActionsContentListener() {
            @Override
            public void onContentStateChanged(ActionsContentView v, boolean isContentShown) {
            }

            @Override
            public void onContentStateInAction(ActionsContentView v, boolean isContentShowing) {
                if (isContentShowing){
                    getSupportActionBar().setTitle(R.string.app_name);
                } else {
                    getSupportActionBar().setTitle(R.string.sidebar_open);
                }
            }
        };
    }

    /* Bootstrap code below here */

    EnglishNumberToWords numberToWords;

    private void bootstrap() {
        numberToWords = new EnglishNumberToWords();

        Account acc = new Account();
        acc.setAccountName("TestAccount1");
        acc.setUsername("TestUsername");
        acc.setPassword("TestPassword");
        hubblog.addAccount(acc);

        for (int siteCount = 0; siteCount < 4; siteCount++){
            Site site = createSite(acc, siteCount);
            hubblog.addSite(site);

            for (int postCount = 0; postCount < 8; postCount++){
                Post post = createPost(site, postCount);
                hubblog.addPostToSite(site, post);
            }
        }
    }

    private Site createSite(Account account, int idx){
        Site site = new Site();
        site.setAccountName(account.getAccountName());
        site.setSiteName("THE SITE " + numberToWords.convertLessThanOneThousand(idx));

        return site;
    }

    private Post createPost(Site site, int idx){
        Post post = new Post();
        post.setSite(site.getSiteName());
        post.setTitle("A post " + numberToWords.convertLessThanOneThousand(idx));
        post.setCreatedDate(new Date());
        post.setContent("## Heading2 for post " + numberToWords.convertLessThanOneThousand(idx));

        if (idx % 2 == 0) {
            post.setIsDraft(false);
        }

        return post;
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

        public String convert(long number) {
            // 0 to 999 999 999 999
            if (number == 0) {
                return "zero";
            }

            String snumber = Long.toString(number);

            // pad with "0"
            String mask = "000000000000";
            DecimalFormat df = new DecimalFormat(mask);
            snumber = df.format(number);

            // XXXnnnnnnnnn
            int billions = Integer.parseInt(snumber.substring(0, 3));
            // nnnXXXnnnnnn
            int millions = Integer.parseInt(snumber.substring(3, 6));
            // nnnnnnXXXnnn
            int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
            // nnnnnnnnnXXX
            int thousands = Integer.parseInt(snumber.substring(9, 12));

            String tradBillions;
            switch (billions) {
                case 0:
                    tradBillions = "";
                    break;
                case 1:
                    tradBillions = convertLessThanOneThousand(billions) + " billion ";
                    break;
                default:
                    tradBillions = convertLessThanOneThousand(billions) + " billion ";
            }
            String result = tradBillions;

            String tradMillions;
            switch (millions) {
                case 0:
                    tradMillions = "";
                    break;
                case 1:
                    tradMillions = convertLessThanOneThousand(millions) + " million ";
                    break;
                default:
                    tradMillions = convertLessThanOneThousand(millions) + " million ";
            }
            result = result + tradMillions;

            String tradHundredThousands;
            switch (hundredThousands) {
                case 0:
                    tradHundredThousands = "";
                    break;
                case 1:
                    tradHundredThousands = "one thousand ";
                    break;
                default:
                    tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                            + " thousand ";
            }
            result = result + tradHundredThousands;

            String tradThousand;
            tradThousand = convertLessThanOneThousand(thousands);
            result = result + tradThousand;

            // remove extra spaces!
            return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
        }
    }
}