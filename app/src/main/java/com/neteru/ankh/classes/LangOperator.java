package com.neteru.ankh.classes;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import android.content.res.Configuration;

import java.util.Locale;

@SuppressWarnings("unused")
public class LangOperator {

    public LangOperator(){}

    public static ContextWrapper setLang(Context context, String code){

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();

        Configuration config = res.getConfiguration();

        Locale locale = new Locale(code);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setNewSystemLocale(config, code);
        } else {
            setOldSystemLocale(config, code);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = setNewSystemConfig(context, config);
        } else {
            setOldSystemConfig(config, res, dm);
        }

        return new ContextWrapper(context);
    }

    private static Locale getOldSystemLocale(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Locale getNewSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }

    private static void setOldSystemLocale(Configuration config, String code){
        config.locale = new Locale(code);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void setNewSystemLocale(Configuration config, String code){
        config.setLocale(new Locale(code));
    }

    private static void setOldSystemConfig(Configuration config, Resources res, DisplayMetrics dm){
        res.updateConfiguration(config, dm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Context setNewSystemConfig(Context ctx, Configuration config){
        return ctx.createConfigurationContext(config);
    }
}
