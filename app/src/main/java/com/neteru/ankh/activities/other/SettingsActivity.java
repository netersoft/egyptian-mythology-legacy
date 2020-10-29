package com.neteru.ankh.activities.other;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.neteru.ankh.R;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.LoadingDialog;
import com.neteru.ankh.classes.Resources;
import com.neteru.ankh.classes.services.MusicService;
import com.neteru.ankh.classes.utils.async.BaseTask;
import com.neteru.ankh.classes.utils.async.TaskRunner;

public class SettingsActivity extends AnkhBaseActivity {
    private AlertDialog dialog;
    private CharSequence[] languages;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setResult(0);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        SwitchCompat switchSong = findViewById(R.id.switchSong),
                     switchMusic = findViewById(R.id.switchMusic);

        RelativeLayout langLayout = findViewById(R.id.langSetting);
        TextView currentLang = findViewById(R.id.selectLang);
        languages = new CharSequence[]{getString(R.string.french), getString(R.string.english)};

        String lang = preferences.getString("lang", getString(R.string.lang));

        if (lang == null) lang = getString(R.string.lang);

        currentLang.setText(lang.equals("en") ? R.string.english : R.string.french);

        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_arrow_drop_down_white_24dp);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.yellow));
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            currentLang.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }

        langLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lang = preferences.getString("lang", getString(R.string.lang));

                if (lang == null) lang = getString(R.string.lang);

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.CustomDialogTheme)
                        .setCancelable(true)
                        .setTitle(R.string.changeLang)
                        .setSingleChoiceItems(languages, lang.equals("fr") ? 0 : 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                editor
                                        .putString("lang", i == 0 ? "fr" : "en")
                                        .putBoolean("settingsOperator", false)
                                        .putBoolean("db_rewriter", true)
                                        .apply();

                                dialog.dismiss();

                                setResult(1);
                                finish();
                                startActivity(getIntent());
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });

        switchSong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                AppUtilities.getInstance(SettingsActivity.this).startClickSong();
                if (b){
                    preferences.edit().putBoolean("switchSong", true).apply();
                }else{
                    preferences.edit().putBoolean("switchSong", false).apply();
                }
            }
        });

        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                AppUtilities.getInstance(SettingsActivity.this).startClickSong();
                if (b){
                    preferences.edit().putBoolean("switchMusic", true).apply();

                    startService(new Intent(SettingsActivity.this, MusicService.class));

                }else {
                    preferences.edit().putBoolean("switchMusic", false).apply();

                    stopService(new Intent(SettingsActivity.this, MusicService.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("settingsOperator", false).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("settingsOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(SettingsActivity.this, MusicService.class));

        if (preferences.getBoolean("db_rewriter", false)){
            new TaskRunner().executeAsync(new ResetDB(SettingsActivity.this));

            editor.putBoolean("db_rewriter", false).apply();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("settingsOperator", true)){

            stopService(new Intent(SettingsActivity.this, MusicService.class));

        }else {
            editor.putBoolean("settingsOperator", true).apply();
        }
    }

    static class ResetDB extends BaseTask<Object>{

        private LoadingDialog loadingDialog;
        private Context context;
        private boolean result;

        ResetDB(Context ctx){
            context = ctx;
            loadingDialog = new LoadingDialog(context);
        }

        @Override
        public void setUiForLoading() {

            result = true;
            loadingDialog.show();

        }

        @Override
        public Object call() {

            if (Resources.getInstance(context).clearQuizTable()){

                Resources.getInstance(context).initQuiz();

            }else{

                result = false;

            }

            return null;

        }

        @Override
        public void setDataAfterLoading(Object o) {

            if (loadingDialog.isShowing()){ loadingDialog.dismiss(); }
            if (!result){ Toast.makeText(context, R.string.error_occured, Toast.LENGTH_SHORT).show(); }

        }

    }
}
