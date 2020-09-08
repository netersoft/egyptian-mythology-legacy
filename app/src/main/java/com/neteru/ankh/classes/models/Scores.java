package com.neteru.ankh.classes.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused")
@DatabaseTable(tableName = "scores")
public class Scores {
    @DatabaseField(columnName = "date", canBeNull = false)
    private String date;
    @DatabaseField(columnName = "score", canBeNull = false)
    private int score;
    @DatabaseField(generatedId = true)
    private int id;

    public Scores(){}

    public Scores(int sc){
        Date thisDate = new Date();
        SimpleDateFormat formatted = new SimpleDateFormat("E dd.MM.yyyy '-' HH:mm", Locale.US);

        score = sc;
        date = formatted.format(thisDate);
    }

    public String getDate(){
        return date;
    }

    public int getScore(){
        return score;
    }

    public int getId(){
        return id;
    }

}
