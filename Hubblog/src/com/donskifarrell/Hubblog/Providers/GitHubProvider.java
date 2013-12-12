package com.donskifarrell.Hubblog.Providers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * User: donski
 * Date: 01/10/13
 * Time: 19:49
 */
public class GitHubProvider extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progressDialog;

    public GitHubProvider(Context aContext) {
        context = aContext;
    }

    @Override
    protected void onPreExecute() {
/*        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("Starting task....");
        this.progressDialog.show();*/
    }

    @Override
    protected String doInBackground(String... urls) {
/*

        GitHubClient client = new GitHubClient();
        //String userName = client.setOAuth2Token("17a8e55bb38d1b9d1ece").getUser();
        String userName = client.setCredentials("donskifarrell", ).getUser();


        OAuthService oAuthService = new OAuthService(client);
        try {
            for (Authorization authorization : oAuthService.getAuthorizations()) {
                authorization.getApp();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int scopeCount = 2;
        List<String> scopes = new LinkedList<String>();
        scopes.add("repo");
        try {
            oAuthService.setScopes(scopeCount, scopes);
        } catch (IOException e) {
            e.printStackTrace();
        }


        RepositoryService service = new RepositoryService(client);
        try {
            List<Repository> repositories = service.getRepositories();
            String repos = new String();

            for (Repository repo : repositories) {
                repos += "\n Name: " + repo.getName().toString() + " - Created: " + repo.getCreatedAt().toString();
            }

            return repos;
        } catch (IOException e) {
            return e.getMessage();
        }

*/
return "";

/*        RepositoryService service = new RepositoryService();
        String repos = new String();

        try {
            for (Repository repo : service.getRepositories("defunkt")){
                repos += "\n * TEST";
                repos += repo.getName() + " Watchers: " + repo.getWatchers();
            }

            return repos;
        } catch (Exception e) {
            return e.getMessage();
        }*/
    }

    @Override
    protected void onPostExecute(String result) {
/*        TextView view = (TextView) this.activity.findViewById(R.id.textView);

        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        view.setText(result);*/
    }

/*    GitHubClient client = new GitHubClient();
    client.setOAuth2Token("17a8e55bb38d1b9d1ece");
    RepositoryService service = new RepositoryService();
    try {
        List<Repository> repositories = service.getRepositories();

        for (Repository repo : repositories) {
            String ah = "Name: " + repo.getName().toString() + " - Created: " + repo.getCreatedAt().toString();
            System.out.println(ah);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }*/


       /* OAuthService service = new OAuthService();
        //service.getClient().setCredentials("donskifarrell", );

        int scopeCount = 2;
        List<String> scopes = new LinkedList<String>();
        scopes.add("repo");
        try {
            service.setScopes(scopeCount, scopes);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Authorization auth = new Authorization();
        auth.setScopes(Arrays.asList("repo"));
        try {
            auth = service.createAuthorization(auth);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String token = auth.getToken();*/

/*        RepositoryService rservice = new RepositoryService();
        for (Repository repo : rservice.getRepositories(user))
            System.out.println(MessageFormat.format(format, count++,
                    repo.getName(), repo.getCreatedAt()));*/

}