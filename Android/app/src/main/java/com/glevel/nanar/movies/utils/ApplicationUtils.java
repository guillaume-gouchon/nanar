package com.glevel.nanar.movies.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glevel.nanar.movies.R;

public class ApplicationUtils {

    public static final String PREFS_NB_LAUNCHES = "nb_launches";
    public static final String PREFS_RATE_DIALOG_IN = "rate_dialog_in";
    public static final int NB_LAUNCHES_RATE_DIALOG_APPEARS = 5;

    public static void showRateDialogIfNeeded(final Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        if (prefs.getInt(PREFS_RATE_DIALOG_IN, NB_LAUNCHES_RATE_DIALOG_APPEARS) == 0) {
            final Editor editor = prefs.edit();

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = (View) inflater.inflate(R.layout.dialog_rate, null);

            // Create and show custom alert dialog
            final Dialog dialog = new Dialog(activity, R.style.Dialog);
            dialog.setContentView(view);

            ((TextView) view.findViewById(R.id.message)).setText(activity.getString(R.string.rate_message, activity.getString(R.string.app_name)));
            ((Button) view.findViewById(R.id.cancelButton)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putInt(PREFS_RATE_DIALOG_IN, -1);
                    editor.commit();
                    dialog.dismiss();
                }
            });
            ((Button) view.findViewById(R.id.neutralButton)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putInt(PREFS_RATE_DIALOG_IN, NB_LAUNCHES_RATE_DIALOG_APPEARS);
                    editor.commit();
                    dialog.dismiss();
                }
            });
            ((Button) view.findViewById(R.id.okButton)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putInt(PREFS_RATE_DIALOG_IN, -1);
                    editor.commit();
                    rateTheApp(activity);
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            // Remove padding from parent
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.setPadding(0, 0, 0, 0);

            dialog.show();
        }
    }

    public static void rateTheApp(Activity activity) {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
        activity.startActivity(goToMarket);
    }

    public static void contactSupport(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.mail_address)});
        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.contact_title) + " " + context.getString(R.string.app_name));
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.contact_support_via)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.no_mail_client), Toast.LENGTH_LONG).show();
        }
    }

    public static void share(Activity activity, String subject, String text, int image) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getString(R.string.share_via)));
    }

}