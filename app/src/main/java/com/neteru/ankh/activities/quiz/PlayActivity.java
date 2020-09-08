package com.neteru.ankh.activities.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neteru.ankh.R;
import com.neteru.ankh.activities.MainActivity;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.LoadingDialog;
import com.neteru.ankh.classes.databases.DbManager;
import com.neteru.ankh.classes.models.Quiz;
import com.neteru.ankh.classes.services.MusicService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayActivity extends AnkhBaseActivity {
    private DbManager dbManager;
    private List<Quiz> quizList;
    private int position, score, life, increase;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView ankh_1, ankh_2, ankh_3;
    private TextView timer, question, scoreView;
    private Button answer_1, answer_2, answer_3, answer_4;
    private Button[] butList;
    private CountDownTimer downTimer;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        dbManager = new DbManager(PlayActivity.this);
        position = 0; score = 0; increase = 0; life = 3;

        timer = findViewById(R.id.timer);
        question = findViewById(R.id.question);
        ankh_1 = findViewById(R.id.ankh_1);
        ankh_2 = findViewById(R.id.ankh_2);
        ankh_3 = findViewById(R.id.ankh_3);
        answer_1 = findViewById(R.id.answer_1);
        answer_2 = findViewById(R.id.answer_2);
        answer_3 = findViewById(R.id.answer_3);
        answer_4 = findViewById(R.id.answer_4);
        scoreView = findViewById(R.id.score);
        RelativeLayout bloc_1 = findViewById(R.id.bloc_1),
                       bloc_2 = findViewById(R.id.bloc_2);

        bloc_1.bringToFront(); bloc_2.bringToFront();

        butList = new Button[]{answer_1, answer_2, answer_3, answer_4};

        new recupTask(PlayActivity.this).execute();

        answer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(0);
            }
        });

        answer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(1);
            }
        });

        answer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(2);
            }
        });

        answer_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(3);
            }
        });
    }

    public void getQuizList() {
        quizList = dbManager.db_readAllQuiz();
        if (quizList != null) {
            Collections.shuffle(quizList);
        }
    }

    public void next(){

        if (position < quizList.size()) {

            Quiz quiz = quizList.get(position);
            String[] temp = new String[]{quiz.getFirstProposal(),
                    quiz.getSecondProposal(),
                    quiz.getThirdProposal(),
                    quiz.getFourthProposal()};
            List<String> tempList = Arrays.asList(temp);
            Collections.shuffle(tempList);
            temp = tempList.toArray(new String[0]);

            String q = quiz.getQuestion().trim()+" ?";
            answer_1.setText(temp[0]);
            answer_2.setText(temp[1]);
            answer_3.setText(temp[2]);
            answer_4.setText(temp[3]);
            question.setText(q);
            scoreView.setText(getString(R.string.score, score));

            position += 1;

            downTimer = new CountDownTimer(21000, 1000) {

                public void onTick(long millisUntilFinished) {
                    AppUtilities.getInstance(PlayActivity.this).startClickSong();
                    long t = millisUntilFinished / 1000;

                    editor.putLong("timerValue", t).apply();
                    timer.setText(String.valueOf(t));

                }

                public void onFinish() {
                    increase = 0;
                    manageLife();
                }

            };
            downTimer.start();

        }else {
            editor.putBoolean("playOperator", false).apply();

            Intent i = new Intent(PlayActivity.this, GameOverActivity.class);
            i.putExtra("score", score)
             .putExtra("finish", true);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public void manageLife(){
        switch (life){
            case 1:
                ankh_1.setVisibility(View.INVISIBLE); life -= 1;

                editor.putBoolean("playOperator", false).apply();

                Intent i = new Intent(PlayActivity.this, GameOverActivity.class);
                i.putExtra("score", score)
                 .putExtra("finish", false);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case 2:
                ankh_2.setVisibility(View.INVISIBLE); life -= 1;
                next();
                break;

            case 3:
                ankh_3.setVisibility(View.INVISIBLE); life -= 1;
                next();
                break;
        }
    }

    public void checkAnswer(final int i){

        AppUtilities.getInstance(PlayActivity.this).startClickSong();
        downTimer.cancel();

        if (butList[i].getText().toString().equals(quizList.get(position - 1).getAnswer())) {

            score += preferences.getLong("timerValue", 0) * 10;
            scoreView.setText(getString(R.string.score, score));

            if (life < 3) {
                if (increase < 5) {

                    increase += 1;

                } else {
                    switch (life) {
                        case 1:
                            ankh_2.setVisibility(View.VISIBLE);
                            break;

                        case 2:
                            ankh_3.setVisibility(View.VISIBLE);
                            break;
                    }
                    increase = 0;
                    life += 1;
                }
            }

            butList[i].setBackgroundResource(R.drawable.button_quiz_true);

            startBlinkAnim(butList[i]);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    butList[i].clearAnimation();
                    butList[i].setBackgroundResource(R.drawable.button);
                    next();
                }
            }, AppUtilities.DELAY / 2);

        } else {
            increase = 0;

            butList[i].setBackgroundResource(R.drawable.button_quiz_false);
            for (final Button but : butList) {
                if (but.getText().toString().equals(quizList.get(position - 1).getAnswer())) {

                    but.setBackgroundResource(R.drawable.button_quiz_true);

                    startBlinkAnim(but);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            butList[i].clearAnimation();
                            butList[i].setBackgroundResource(R.drawable.button);
                            but.clearAnimation();
                            but.setBackgroundResource(R.drawable.button);
                            manageLife();
                        }
                    }, AppUtilities.DELAY / 2);
                }
            }
        }

    }

    public void startBlinkAnim(final Button button){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        button.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("playOperator", false).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("playOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(PlayActivity.this, MusicService.class));

        if (!preferences.getBoolean("secondPlayOperator", true)){

            Intent i = new Intent(PlayActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
    }

    @Override
    protected void onStop() {
        downTimer.cancel();

        if (preferences.getBoolean("playOperator", true)){

            stopService(new Intent(PlayActivity.this, MusicService.class));
            editor.putBoolean("secondPlayOperator", false).apply();

        }else {
            editor.putBoolean("playOperator", true).apply();
        }

        super.onStop();
    }

    @SuppressLint("StaticFieldLeak")
    class recupTask extends AsyncTask<String, Void, Void>{
        private Context context;
        private LoadingDialog loadingDialog;

        recupTask(Context ctx){
            context = ctx;
            loadingDialog = new LoadingDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            getQuizList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (loadingDialog.isShowing()) { loadingDialog.dismiss(); }
            next();
        }
    }
}
