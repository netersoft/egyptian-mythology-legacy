package com.neteru.ankh.classes;

import android.app.Application;

import com.neteru.ankh.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class Ankh extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/PAPYRUS.TTF")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }
}
