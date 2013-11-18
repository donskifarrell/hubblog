package com.donskifarrell.Hubblog.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;


/**
 * User: donski
 * Date: 01/10/13
 * Time: 19:49
 */
public class GitHub extends AsyncTask<String, Void, String> {

    private Activity activity;
    private ProgressDialog progressDialog;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog = new ProgressDialog(this.activity);
        this.progressDialog.setMessage("Starting task....");
        this.progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        RepositoryService service = new RepositoryService();
        String repos = new String();

        try {
            for (Repository repo : service.getRepositories("defunkt")){
                repos += "\n * TEST";
                repos += repo.getName() + " Watchers: " + repo.getWatchers();
            }

            return repos;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
/*        TextView view = (TextView) this.activity.findViewById(R.id.textView);

        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        view.setText(result);*/
    }
}