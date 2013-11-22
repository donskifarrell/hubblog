package com.donskifarrell.Hubblog.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.donskifarrell.Hubblog.Interfaces.SelectSiteDialogListener;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 00:07
 */
public class SelectSiteDialogFragment extends DialogFragment {
    private String[] sites;
    private SelectSiteDialogListener callback;
    private int selectedSite;

    public SelectSiteDialogFragment(String[] siteList) {
        sites = siteList;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.select_site_dialog_layout, null));
        builder.setTitle(R.string.select_site_title);
        builder.setPositiveButton(R.string.select_site_positive_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onDialogPositiveClick(selectedSite);
            }
        });
        builder.setNegativeButton(R.string.select_site_negative_btn, null);
        builder.setSingleChoiceItems(sites, -1, new DialogInterface.OnClickListener() {
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
            callback = (SelectSiteDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectSiteDialogListener");
        }
    }
}
