package com.neteru.ankh.activities.other;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.neteru.ankh.R;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.Typewriter;
import com.neteru.ankh.classes.adapters.ScoresAdapter;
import com.neteru.ankh.classes.databases.DbManager;
import com.neteru.ankh.classes.models.Scores;
import com.neteru.ankh.classes.services.MusicService;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AnkhBaseActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private List<Scores> scoresList = new ArrayList<>();
    private RecyclerView recycler;
    private Typewriter bScore;
    private TextView noScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        bScore = findViewById(R.id.bestScore);
        noScore = findViewById(R.id.noScore);
        Button
                delete = findViewById(R.id.delete),
                progress = findViewById(R.id.progress);
        recycler = findViewById(R.id.scoreRecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recycler.setLayoutManager(layoutManager);

        loadScores();

        bScore.animateText(getString(R.string.best_score)+
                PreferenceManager.getDefaultSharedPreferences(StatsActivity.this)
                        .getInt("bestScore", 0));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtilities.getInstance(StatsActivity.this).startClickSong();

                AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this, R.style.CustomDialogTheme);
                builder
                    .setTitle(R.string.confirm_title)
                    .setMessage(R.string.confirm_msg)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DbManager dbManager = new DbManager(StatsActivity.this);
                            dbManager.clearAccountTable();
                            editor.putInt("bestScore", 0).apply();

                            bScore.animateText(getString(R.string.best_score)+
                                    PreferenceManager.getDefaultSharedPreferences(StatsActivity.this)
                                            .getInt("bestScore", 0));

                            loadScores();

                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();

            }
        });

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtilities.getInstance(StatsActivity.this).startClickSong();

                LayoutInflater inflater = LayoutInflater.from(StatsActivity.this);
                @SuppressLint("InflateParams") View graphView = inflater.inflate(R.layout.stats_model, null);

                LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>();
                GraphView graph = graphView.findViewById(R.id.graph);
                graph.setTitle(getString(R.string.scores_progress));
                graph.getViewport().setScrollable(true);
                graph.getViewport().setScrollableY(true);
                graph.setTitleColor(R.color.black);
                graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
                graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                graph.getGridLabelRenderer().setHumanRounding(true);

                if (scoresList != null){
                    for (int i = 0; i < scoresList.size(); i++){
                        graphSeries.appendData(new DataPoint(i, scoresList.get(i).getScore()), true, scoresList.size());
                    }

                    graphSeries.setColor(R.color.goldenYellow);
                    graphSeries.setDrawDataPoints(true);
                    graphSeries.setDataPointsRadius(21);
                    graphSeries.setDrawBackground(true);
                    graphSeries.setBackgroundColor(R.color.black);
                    graphSeries.setThickness(2);

                    graphSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {
                            Toast.makeText(StatsActivity.this, "Score : "+dataPoint.getY(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    graph.addSeries(graphSeries);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this, R.style.CustomDialogTheme);
                builder
                        .setCancelable(true)
                        .setView(graphView)
                        .show();
            }
        });

    }

    private void loadScores(){
        getScores();

        ScoresAdapter scoresAdapter = new ScoresAdapter(scoresList, R.layout.scores_model, StatsActivity.this);

        recycler.setAdapter(scoresAdapter);
    }

    private void getScores(){
        DbManager dbManager = new DbManager(StatsActivity.this);
        scoresList = dbManager.db_readAllScores();

        if (scoresList.isEmpty()){
            noScore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("statsOperator", false).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("statsOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(StatsActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("statsOperator", true)){

            stopService(new Intent(StatsActivity.this, MusicService.class));

        }else {
            editor.putBoolean("statsOperator", true).apply();
        }
    }
}
