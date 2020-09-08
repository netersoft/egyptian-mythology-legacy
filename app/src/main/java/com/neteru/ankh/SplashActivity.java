package com.neteru.ankh;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.neteru.ankh.activities.MainActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.Resources;
import com.neteru.ankh.classes.services.MusicService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.yellow), PorterDuff.Mode.SRC_IN);

        TextView label_1 = findViewById(R.id.label_1),
                 label_2 = findViewById(R.id.label_2);
        Typeface cambria = Typeface.createFromAsset(getAssets(),  "fonts/cambria.ttf"),
                 papyrus = Typeface.createFromAsset(getAssets(),  "fonts/PAPYRUS.TTF");
        Typeface boldCambria = Typeface.create(cambria, Typeface.BOLD),
                 boldPapyrus = Typeface.create(papyrus, Typeface.BOLD);
        label_1.setTypeface(boldCambria);
        label_2.setTypeface(boldPapyrus);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("firstLaunch", true)){

            PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit()
                    .putString("lang", getResources().getString(R.string.lang))
                    .apply();

            Resources.getInstance(SplashActivity.this).initQuiz();

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit()
                        .putBoolean("firstLaunch", false)
                        .putBoolean("mainOperator", true)
                        .putBoolean("aboutOperator", true)
                        .putBoolean("instructionsOperator", true)
                        .putBoolean("playOperator", true)
                        .putBoolean("settingsOperator", true)
                        .putBoolean("statsOperator", true)
                        .putBoolean("DocSectionsOperator", true)
                        .putBoolean("GameOverOperator", true)
                        .putBoolean("GodsDocOperator", true)
                        .putBoolean("CosmogoniesDocOperator", true)
                        .putBoolean("MythsDocOperator", true)
                        .apply();

                //start service and play music
                startService(new Intent(SplashActivity.this, MusicService.class));
                startActivityForResult(new Intent(SplashActivity.this, MainActivity.class), AppUtilities.DELAY);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }, AppUtilities.DELAY / 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //start service and play music
        stopService(new Intent(SplashActivity.this, MusicService.class));
    }
}
