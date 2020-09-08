package com.neteru.ankh.classes.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.neteru.ankh.classes.models.Quiz;
import com.neteru.ankh.classes.models.Scores;

import java.util.List;

@SuppressWarnings("unused")
public class DbManager extends OrmLiteSqliteOpenHelper{

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "ankh.db";
    private final static String  TAG = "DB_MANAGER";
    private Dao<Scores, Integer> scoresDao;

    public DbManager(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Scores.class);
            TableUtils.createTable(connectionSource, Quiz.class);
        }catch (Exception e){
            Log.e(TAG, "Erreur lors de la création des Tables - "+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Scores.class, true);
            TableUtils.dropTable(connectionSource, Quiz.class, true);
        }catch (Exception e){
            Log.e(TAG, "Erreur lors de la mise à jour des Tables - "+e);
        }
    }

    public void db_insertScore(Scores score){
        try {

            getDao(Scores.class).create(score);

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de l'insertion de l'objet dans la Table - "+e);
        }
    }

    public void db_insertQuiz(Quiz quiz){
        try {

            getDao(Quiz.class).create(quiz);

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de l'insertion de l'objet dans la Table - "+e);
        }
    }

    public long db_removeScore(Scores scores){
        try {

            scoresDao = getDao(Scores.class);
            DeleteBuilder<Scores, Integer> ub = scoresDao.deleteBuilder();

            ub.where()
                    .eq("id", scores.getDate())
                    .and()
                    .eq("date", scores.getDate())
                    .and()
                    .eq("score", scores.getScore());
            ub.delete();

            return scores.getId();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la suppression dans la Table - "+e);
            return -1;
        }
    }

    public List<Scores> db_readAllScores(){
        try {

            scoresDao = getDao(Scores.class);
            QueryBuilder<Scores, Integer> qb = scoresDao.queryBuilder();
            qb.orderBy("id", true);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture de toute la Table - "+e);
            return null;
        }
    }

    public List<Quiz> db_readAllQuiz(){
        try {

            Dao<Quiz, Integer> quizDao = getDao(Quiz.class);
            QueryBuilder<Quiz, Integer> qb = quizDao.queryBuilder();
            qb.orderBy("id", false);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture de toute la Table - "+e);
            return null;
        }
    }

    public void clearAccountTable(){
        try {

            TableUtils.clearTable(connectionSource, Scores.class);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean clearQuizTable(){
        try {

            TableUtils.clearTable(connectionSource, Quiz.class);
            return true;

        }catch (Exception e){

            return false;

        }
    }
}
