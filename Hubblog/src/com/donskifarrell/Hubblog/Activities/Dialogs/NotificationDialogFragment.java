package com.donskifarrell.Hubblog.Activities.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 26/11/13
 * Time: 00:34
 */
public class NotificationDialogFragment extends DialogFragment {
    private int titleId;
    private int messageId;

    public NotificationDialogFragment(int titleId, int messageId){
        this.titleId = titleId;
        this.messageId = messageId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.ok_btn, null);

        return builder.create();
    }
}
