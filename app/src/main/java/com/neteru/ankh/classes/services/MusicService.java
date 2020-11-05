package com.neteru.ankh.classes.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.neteru.ankh.R;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class MusicService extends Service {
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {

        int song;
        if (new Random().nextInt(2) != 0){
            song = R.raw.egypt_1;
        }else {
            song = R.raw.egypt_2;
        }
        player = MediaPlayer.create(this, song); //select music file
        player.setLooping(true); //set looping
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("switchMusic", true)) {

            if(player == null) {
                Log.v(TAG, "Create() on MediaPlayer failed.");
            } else {
                try{

                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mediaplayer) {
                            mediaplayer.stop();
                            mediaplayer.release();
                        }
                    });
                    player.start();

                } catch (Exception ignored){ }
            }
        }

        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {
        try{
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                stopSelf();
            }
        } catch (Exception ignored){ }

        super.onDestroy();
    }
}
