package com.donskifarrell.Hubblog.Activities.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import com.donskifarrell.Hubblog.Interfaces.DialogListener;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 00:07
 */
public class AddSiteDialogFragment extends DialogFragment {
    private DialogListener callback;
    private String siteName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // todo:
        builder.setView(inflater.inflate(R.layout.select_site_dialog_layout, null));
        builder.setTitle(R.string.dialog_select_site_title);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onAddNewSitePositiveClick(siteName);
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, null);

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
