package com.neteru.ankh.classes;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.util.Log;

import com.neteru.ankh.R;

import java.io.File;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class AppUtilities {
    private Context context;
    private StringBuilder message;
    private MediaPlayer clickSong;
    public final static int DELAY = 4320;
    
    public AppUtilities(Context ctx){
        context = ctx;
        message = new StringBuilder();
        clickSong = MediaPlayer.create(ctx, R.raw.click);
    }
    
    public static AppUtilities getInstance(Context ctx){
        return new AppUtilities(ctx);
    }
    
    public StringBuilder getInformation(@Nullable Boolean getAdvancedInfos) {
        message.append("Locale: ").append(Locale.getDefault()).append('\n');
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(
                    context.getPackageName());
        }
        message.append("Phone Model: ").append(android.os.Build.MODEL)
                .append('\n');
        message.append("Android Version: ")
                .append(android.os.Build.VERSION.RELEASE).append('\n');

        if (getAdvancedInfos != null && getAdvancedInfos){

            message.append("Board: ").append(android.os.Build.BOARD).append('\n');
            message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
            message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
            message.append("Host: ").append(android.os.Build.HOST).append('\n');
            message.append("ID: ").append(android.os.Build.ID).append('\n');
            message.append("Model: ").append(android.os.Build.MODEL).append('\n');
            message.append("Product: ").append(android.os.Build.PRODUCT)
                    .append('\n');
            message.append("Type: ").append(android.os.Build.TYPE).append('\n');
            StatFs stat = getStatFs();
            message.append("Total Internal memory: ")
                    .append(getTotalInternalMemorySize(stat)).append('\n');
            message.append("Available Internal memory: ")
                    .append(getAvailableInternalMemorySize(stat)).append('\n');
        }

        return message;
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public void startClickSong(){
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switchSong", true)){

            if(clickSong == null) {
                Log.v(TAG, "Create() on MediaPlayer failed.");
            } else {
                clickSong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mediaplayer) {
                        mediaplayer.stop();
                        mediaplayer.release();
                    }
                });
                clickSong.start();
            }
        }
    }
}
