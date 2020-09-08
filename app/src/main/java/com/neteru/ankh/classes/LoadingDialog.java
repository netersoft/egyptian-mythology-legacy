package com.neteru.ankh.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.neteru.ankh.R;

public class LoadingDialog {
    private AlertDialog LoadingBox;

    public LoadingDialog(Context context){

        LoadingBox = new AlertDialog.Builder(context, R.style.CustomDialogTheme)
                .setMessage(R.string.loading)
                .create();

        LayoutInflater factory = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View LoadingView = factory.inflate(R.layout.loading_model, null);

        ProgressBar progressBar = LoadingView.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.black), PorterDuff.Mode.SRC_IN);

        LoadingBox.setView(LoadingView);
        LoadingBox.setCancelable(false);
    }

    public void setCancelable(){
        LoadingBox.setCancelable(true);
    }

    public boolean isShowing(){
        return LoadingBox.isShowing();
    }

    public void show(){
        this.LoadingBox.show();
    }

    public void dismiss(){
        this.LoadingBox.dismiss();
    }

}
