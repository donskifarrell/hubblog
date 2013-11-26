package com.donskifarrell.Hubblog.Activities.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.donskifarrell.Hubblog.Interfaces.DialogListener;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 25/11/13
 * Time: 18:09
 */
public class EditArticleTitleDialogFragment extends DialogFragment {
    private DialogListener callback;
    private String articleTitle;
    private EditText editText;

    public EditArticleTitleDialogFragment(String title) {
        articleTitle = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout dialog = (LinearLayout) inflater.inflate(R.layout.dialog_edit_article_title_layout, null);

        editText = (EditText) dialog.findViewById(R.id.edit_article_title_text);
        editText.setText(articleTitle);

        builder.setView(dialog);
        builder.setTitle(R.string.dialog_edit_article_title);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onChangeArticleTitlePositiveClick(editText.getText().toString());
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
