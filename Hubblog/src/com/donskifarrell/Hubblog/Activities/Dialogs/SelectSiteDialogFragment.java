package com.donskifarrell.Hubblog.Activities.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.donskifarrell.Hubblog.Interfaces.DialogListener;
import com.donskifarrell.Hubblog.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 00:07
 */
public class SelectSiteDialogFragment extends DialogFragment {
    private List<String> sites;
    private DialogListener callback;
    private int selectedSite;

    public SelectSiteDialogFragment(List<String> siteList) {
        sites = siteList;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_select_site_layout, null));
        builder.setTitle(R.string.dialog_select_site_title);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onSelectSitePositiveClick(selectedSite);
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, null);
        builder.setSingleChoiceItems(sites.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int site) {
                selectedSite = site;
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }
}
