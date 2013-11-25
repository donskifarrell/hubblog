package com.donskifarrell.Hubblog.Activities.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.donskifarrell.Hubblog.R;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 25/11/13
 * Time: 20:55
 */
public class AboutDialogFragment extends DialogFragment {
    private static final String url = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=WX48B6TLH773Q&lc=GB&item_name=Hubblog&item_number=hubblog&currency_code=GBP&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        LinearLayout about = (LinearLayout) inflater.inflate(R.layout.dialog_about_layout, null);

        ImageButton donate = (ImageButton) about.findViewById(R.id.donate_button);
        donate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        builder.setView(about);
        builder.setTitle(R.string.dialog_about_title);
        builder.setPositiveButton(R.string.ok_btn, null);

        return builder.create();
    }
}
