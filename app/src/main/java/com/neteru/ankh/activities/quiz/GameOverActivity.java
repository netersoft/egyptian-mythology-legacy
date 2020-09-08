package com.neteru.ankh.activities.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neteru.ankh.R;
import com.neteru.ankh.activities.MainActivity;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.Typewriter;
import com.neteru.ankh.classes.databases.DbManager;
import com.neteru.ankh.classes.models.Scores;
import com.neteru.ankh.classes.services.MusicService;

public class GameOverActivity extends AnkhBaseActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        DbManager dbManager = new DbManager(GameOverActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        boolean finish = getIntent().getBooleanExtra("finish", false);
        int score = getIntent().getIntExtra("score", 0);

        Typewriter scoreView = findViewById(R.id.yourScore),
                   quizFinishTxt = findViewById(R.id.quizFinish);
        TextView congratTxt = findViewById(R.id.congrat),
                 recordLabel = findViewById(R.id.recordLabel);
        ImageView replay = findViewById(R.id.replay),
                  stats = findViewById(R.id.stats),
                  main = findViewById(R.id.main);

        scoreView.animateText("Score : "+score);
        dbManager.db_insertScore(new Scores(score));

        if (score > preferences.getInt("bestScore", 0)){
            editor.putInt("bestScore", score).apply();
            recordLabel.setVisibility(View.VISIBLE);
        }

        if (finish){

            congratTxt.setVisibility(View.VISIBLE);
            congratTxt.setText(R.string.congratulations);
            quizFinishTxt.setVisibility(View.VISIBLE);
            quizFinishTxt.animateText(getString(R.string.quiz_finish_txt));

        }else {
            if (score > 0){

                congratTxt.setVisibility(View.VISIBLE);
                congratTxt.setText(R.string.bravo);
                quizFinishTxt.setVisibility(View.GONE);

            }
        }

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("GameOverOperator", false).apply();
                AppUtilities.getInstance(GameOverActivity.this).startClickSong();

                Intent i = new Intent(GameOverActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("redirect", "play");
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("GameOverOperator", false).apply();
                AppUtilities.getInstance(GameOverActivity.this).startClickSong();

                Intent i = new Intent(GameOverActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("GameOverOperator", false).apply();
                AppUtilities.getInstance(GameOverActivity.this).startClickSong();

                Intent i = new Intent(GameOverActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("redirect", "stats");
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

    }

    @Override
    public void onBackPressed() {
        editor.putBoolean("GameOverOperator", false).apply();

        Intent i = new Intent(GameOverActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("GameOverOperator", false).apply();

        Intent i = new Intent(GameOverActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(GameOverActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("GameOverOperator", true)){

            stopService(new Intent(GameOverActivity.this, MusicService.class));

        }else {
            editor.putBoolean("GameOverOperator", true).apply();
        }
    }
}
