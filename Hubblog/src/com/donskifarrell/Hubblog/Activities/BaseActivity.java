package com.donskifarrell.Hubblog.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.donskifarrell.Hubblog.Adapters.ArticlesAdapter;
import com.donskifarrell.Hubblog.Data.Account;
import com.donskifarrell.Hubblog.Data.Post;
import com.donskifarrell.Hubblog.Data.Site;
import com.donskifarrell.Hubblog.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo: remove
        bootstrap();

        setContentView(R.layout.base_layout);

        addSitesToSidebar();

        final int selectedPosition;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(STATE_POSITION, 0);
        } else {
            selectedPosition = 0;
        }

        showArticle(selectedPosition);
    }

    private void addSitesToSidebar(){
        LayoutInflater sidebar_list_inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.sidebar_layout);

        for (Site site : hubblog.getSites()){
            View sidebar_list = sidebar_list_inflater.inflate(R.layout.sidebar_list_layout, null);

            LinearLayout header_layout = (LinearLayout) sidebar_list.findViewById(R.id.header_title);
            TextView header = (TextView) header_layout.findViewById(R.id.title);
            header.setText(site.getSiteName());

            ListView articlesList = (ListView) sidebar_list.findViewById(R.id.articles);
            articlesList.setAdapter(new ArticlesAdapter(this, site));
            articlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long flags) {
                    showArticle(position);
                }
            });

            relativeLayout.addView(sidebar_list);
        }
    }

    private void showArticle(int position){

    }

    private void bootstrap() {
        Account acc = new Account();
        acc.setAccountName("TestAccount1");
        acc.setUsername("TestUsername");
        acc.setPassword("TestPassword");
        hubblog.addAccount(acc);

        Site site = new Site();
        site.setAccountName(acc.getAccountName());
        site.setSiteName("TestSiteName");
        hubblog.addSite(site);

        Post post = new Post();
        post.setSite(site.getSiteName());
        post.setTitle("TestPost1 - Site 1");
        post.setCreatedDate(new Date());
        post.setContent("# Heading1 - post 1");
        hubblog.addPostToSite(site, post);

        post = new Post();
        post.setSite(site.getSiteName());
        post.setTitle("TestPost2 - Site 1");
        post.setCreatedDate(new Date());
        post.setContent("## Heading2 - post 2");
        hubblog.addPostToSite(site, post);

        site.setAccountName(acc.getAccountName());
        site.setSiteName("TestSiteName 2");
        hubblog.addSite(site);

        post = new Post();
        post.setSite(site.getSiteName());
        post.setTitle("TestPost3 - Site 2");
        post.setCreatedDate(new Date());
        post.setContent("### Heading3 - post 3");
        hubblog.addPostToSite(site, post);

        post = new Post();
        post.setSite(site.getSiteName());
        post.setTitle("TestPost2 - Site 2");
        post.setCreatedDate(new Date());
        post.setContent("#### Heading4 - post 4");
        hubblog.addPostToSite(site, post);
    }
}