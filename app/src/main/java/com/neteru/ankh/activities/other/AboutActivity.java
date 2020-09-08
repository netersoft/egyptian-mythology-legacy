package com.neteru.ankh.activities.other;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neteru.ankh.R;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.Typewriter;
import com.neteru.ankh.classes.services.MusicService;

public class AboutActivity extends AnkhBaseActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        Typewriter app_name = findViewById(R.id.app_name),
                   credits = findViewById(R.id.credits);
        TextView copyright = findViewById(R.id.copyright);
        ImageView contact = findViewById(R.id.contact),
                  share = findViewById(R.id.share),
                  rate = findViewById(R.id.rate);

        String app_info = getString(R.string.app_name) + "\n" + getString(R.string.app_version) + "\n" + getString(R.string.credits);
        app_name.setText(app_info);

        credits.animateText(getString(R.string.about_1) + "\n\n" + getString(R.string.about_2) + "\n\n" + getString(R.string.about_3) );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            copyright.setText(Html.fromHtml(getString(R.string.copyright),Html.FROM_HTML_MODE_COMPACT));
        }else{
            copyright.setText(Html.fromHtml(getString(R.string.copyright)));
        }
        copyright.setMovementMethod(LinkMovementMethod.getInstance());

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtilities.getInstance(AboutActivity.this).startClickSong();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_msg, ":\n\n https://play.google.com/store/apps/details?id="+getPackageName()+"\n\n"));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_title)));

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtilities.getInstance(AboutActivity.this).startClickSong();
                Intent sendIntent = new Intent(
                        Intent.ACTION_SEND);
                String subject = getResources().getString(R.string.app_name) + " - Feedback";
                StringBuilder body = AppUtilities.getInstance(AboutActivity.this).getInformation(false);
                sendIntent.setType("message/rfc822");
                sendIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { "neterustudio@gmail.com" });
                sendIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendIntent.setType("message/rfc822");
                startActivityForResult(sendIntent, 4320);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                }else{

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                }

                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +getPackageName())));
                }

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("aboutOperator", false).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("aboutOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(AboutActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("aboutOperator", true)){

            stopService(new Intent(AboutActivity.this, MusicService.class));

        }else {
            editor.putBoolean("aboutOperator", true).apply();
        }
    }
}
