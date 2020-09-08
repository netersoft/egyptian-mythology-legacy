package com.neteru.ankh.activities.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neteru.ankh.R;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.Typewriter;
import com.neteru.ankh.classes.services.MusicService;

public class InstructionsActivity extends AnkhBaseActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        Button forPlay = findViewById(R.id.letsPlay);
        TextView instructions = findViewById(R.id.instructions);
        Typewriter title = findViewById(R.id.title);

        String txt = getResources().getString(R.string.instructions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            instructions.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT));
        }else{
            instructions.setText(Html.fromHtml(txt));
        }

        title.animateText(getResources().getString(R.string.quiz));
        title.setCharacterDelay(225);

        forPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor
                        .putBoolean("instructionsOperator", false)
                        .putBoolean("secondPlayOperator", true)
                        .apply();

                AppUtilities.getInstance(InstructionsActivity.this).startClickSong();
                startActivityForResult(new Intent(InstructionsActivity.this, PlayActivity.class), AppUtilities.DELAY);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("instructionsOperator", false).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("instructionsOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(InstructionsActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("instructionsOperator", true)){

            stopService(new Intent(InstructionsActivity.this, MusicService.class));

        }else {
            editor.putBoolean("instructionsOperator", true).apply();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
    }
}
