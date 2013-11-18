package com.donskifarrell.Hubblog.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.donskifarrell.Hubblog.Adapters.ArticlesAdapter;
import com.donskifarrell.Hubblog.Adapters.SidebarAdapter;
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

        LinearLayout sidebar_layout = (LinearLayout) findViewById(R.id.sidebar_layout);
        ListView sidebarList = (ListView) sidebar_layout.findViewById(R.id.sidebar_list);
        sidebarList.setAdapter(new SidebarAdapter(this, hubblog.getSites()));

        final int selectedPosition;
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(STATE_POSITION, 0);
        } else {
            selectedPosition = 0;
        }

        showArticle(selectedPosition);
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
        site.setSiteName("THE FIRST SITE");
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
        site.setSiteName("SECOND SITE");
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