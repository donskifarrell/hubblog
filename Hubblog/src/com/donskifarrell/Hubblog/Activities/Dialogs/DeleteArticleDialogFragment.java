package com.donskifarrell.Hubblog.Activities.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import com.donskifarrell.Hubblog.Interfaces.DialogListener;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 25/11/13
 * Time: 19:20
 */
public class DeleteArticleDialogFragment extends DialogFragment {
    private DialogListener callback;
    private EditText editText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_delete_article_title);
        builder.setMessage(R.string.dialog_delete_article_message);
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onDeleteArticlePositiveClick();
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