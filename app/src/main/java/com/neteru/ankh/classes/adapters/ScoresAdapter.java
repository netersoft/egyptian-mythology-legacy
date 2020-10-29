package com.neteru.ankh.classes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neteru.ankh.R;
import com.neteru.ankh.classes.models.Scores;

import java.util.List;

@SuppressWarnings("unused")
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.MyViewHolder> {
    private int rowLayout;
    private List<Scores> scoresList;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, score;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            score = view.findViewById(R.id.score);
        }

    }

    public ScoresAdapter(List<Scores> scores, int row, Context ctx){
        rowLayout = row;
        scoresList = scores;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Scores score = scoresList.get(position);

        holder.score.setText(String.valueOf(score.getScore()));
        holder.date.setText(score.getDate());

    }

    @Override
    public int getItemCount() {
        return scoresList.size();
    }
}
