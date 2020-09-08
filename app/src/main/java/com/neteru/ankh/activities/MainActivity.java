package com.neteru.ankh.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.neteru.ankh.R;
import com.neteru.ankh.activities.documentation.DocSectionsActivity;
import com.neteru.ankh.activities.other.AboutActivity;
import com.neteru.ankh.activities.other.SettingsActivity;
import com.neteru.ankh.activities.other.StatsActivity;
import com.neteru.ankh.activities.quiz.InstructionsActivity;
import com.neteru.ankh.activities.quiz.PlayActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.LangOperator;
import com.neteru.ankh.classes.Typewriter;
import com.neteru.ankh.classes.services.MusicService;
import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.OnClickButtonListener;
import com.vorlonsoft.android.rate.StoreType;
import com.vorlonsoft.android.rate.Time;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.neteru.ankh.classes.Constants.EMPTY;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LangOperator.setLang(newBase, PreferenceManager.getDefaultSharedPreferences(newBase).getString("lang", "en"))));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        CardView card = findViewById(R.id.card);
        final Typewriter title = findViewById(R.id.title);
        Button
                doc = findViewById(R.id.doc),
                play = findViewById(R.id.play),
                stats = findViewById(R.id.stats),
                settings = findViewById(R.id.settings),
                about = findViewById(R.id.about);

        intentRedirector();
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("mainOperator", false).apply();

                AppUtilities.getInstance(MainActivity.this).startClickSong();
                startActivity(new Intent(MainActivity.this, DocSectionsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("mainOperator", false).apply();

                AppUtilities.getInstance(MainActivity.this).startClickSong();
                startActivity(new Intent(MainActivity.this, InstructionsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("mainOperator", false).apply();

                AppUtilities.getInstance(MainActivity.this).startClickSong();
                startActivity(new Intent(MainActivity.this, StatsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor
                      .putBoolean("mainOperator", false)
                      .putBoolean("db_rewriter", false)
                      .apply();

                AppUtilities.getInstance(MainActivity.this).startClickSong();
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), AppUtilities.DELAY);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("mainOperator", false).apply();

                AppUtilities.getInstance(MainActivity.this).startClickSong();
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Animation cardAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        card.startAnimation(cardAnimation);
        cardAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                title.animateText(getString(R.string.main_menu));

                final ImageView ankh = findViewById(R.id.ankh);
                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ankh.setVisibility(View.VISIBLE);
                ankh.startAnimation(slideUp);

                slideUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                        ankh.startAnimation(shake);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        setAppRateSystem();

    }

    private void intentRedirector() {
        if (getIntent().getExtras() == null) return;

        if (getIntent().hasExtra("redirect")){
            switch (getIntent().getExtras().getString("redirect", "")){
                case "play":
                    editor
                            .putBoolean("mainOperator", false)
                            .putBoolean("secondPlayOperator", true)
                            .apply();

                    startActivity(new Intent(MainActivity.this, PlayActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;

                case "stats":
                    editor.putBoolean("mainOperator", false).apply();

                    startActivity(new Intent(MainActivity.this, StatsActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialogTheme);
        builder
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        editor.putBoolean("mainOperator", true).apply();
                        finish();

                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("mainOperator", true).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(MainActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("mainOperator", true)){

            stopService(new Intent(MainActivity.this, MusicService.class));

        }else {
            editor.putBoolean("mainOperator", true).apply();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){ recreate(); }
    }

    /**
     * Initialisation du système de notation
     */
    private void setAppRateSystem() {

        AppRate.with(this)
                .setThemeResId(R.style.CustomDialogTheme)
                .setTitle(EMPTY)
                .setStoreType(StoreType.GOOGLEPLAY)
                .setTimeToWait(Time.DAY, (short) 10) // default is 10 days, 0 means install millisecond, 10 means app is launched 10 or more time units later than installation
                .setLaunchTimes((byte) 10)          // default is 10, 3 means app is launched 3 or more times
                .setRemindTimeToWait(Time.DAY, (short) 1) // default is 1 day, 1 means app is launched 1 or more time units after neutral button clicked
                .setRemindLaunchesNumber((byte) 0)  // default is 0, 1 means app is launched 1 or more times after neutral button clicked
                .setSelectedAppLaunches((byte) 1)   // default is 1, 1 means each launch, 2 means every 2nd launch, 3 means every 3rd launch, etc
                .setShowLaterButton(true)           // default is true, true means to show the Neutral button ("Remind me later").
                .setVersionCodeCheck(false)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version code is installed
                .setVersionNameCheck(false)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version name is installed
                .setDebug(false)                    // default is false, true is for development only, true ensures that the Rate Dialog will be shown each time the app is launched
                .setOnClickButtonListener(new OnClickButtonListener() {
                    @Override
                    public void onClickButton(byte which) {
                        Log.d(MainActivity.this.getLocalClassName(), Byte.toString(which));
                    }
                })
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

    }
}
