package com.neteru.ankh.activities.documentation.sections;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.neteru.ankh.R;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.LoadingDialog;
import com.neteru.ankh.classes.ObservableWebView;
import com.neteru.ankh.classes.services.MusicService;

public class MythsDocActivity extends AnkhBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ObservableWebView webView;
    private LoadingDialog loadingDialog;
    private String title;

    @SuppressLint({"CommitPrefEdits", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myths_doc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.bringToFront();
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadingDialog = new LoadingDialog(MythsDocActivity.this);

        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.setHorizontalScrollBarEnabled(false);
        webView.setBackgroundColor(Color.BLACK);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                loadingDialog.setCancelable();
                loadingDialog.show();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (getSupportActionBar() != null){
                        if (i1 < 100){
                            getSupportActionBar().setTitle(title);
                        }else {
                            getSupportActionBar().setTitle("");
                        }
                    }
                }
            });

        }else {

            webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
                @Override
                public void onScroll(int l, int t, int oldl, int oldt) {
                    if (getSupportActionBar() != null){
                        if (t < 100){
                            getSupportActionBar().setTitle(title);
                        }else {
                            getSupportActionBar().setTitle("");
                        }
                    }
                }
            });

        }

        loadDoc(getString(R.string.intro_title), 0);

    }

    private void loadDoc(String t, int id){
        title = t;
        String[] fileNameList = {"intro","circadien","osirien","mort"};

        for (int i = 0; i < fileNameList.length; i++){
            if (id == i){
                webView.loadUrl("file:///android_asset/documentation/"+getResources().getString(R.string.lang)+"/myths/myth_"+fileNameList[i]+".html");
            }
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            editor.putBoolean("MythsDocOperator", false).apply();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myths_doc, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(MythsDocActivity.this, R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            AppUtilities.getInstance(MythsDocActivity.this).startClickSong();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.myths_share, ":\n\n https://play.google.com/store/apps/details?id="+getPackageName()+"\n\n"));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_title)));

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        AppUtilities.getInstance(MythsDocActivity.this).startClickSong();

        switch (item.getItemId()){
            case R.id.nav_myth_intro:
                loadDoc(getString(R.string.intro_title), 0);
                break;

            case R.id.nav_myth_circadien:
                loadDoc(getString(R.string.myth_circadien_title), 1);
                break;

            case R.id.nav_myth_osirien:
                loadDoc(getString(R.string.myth_osiris_title), 2);
                break;

            case R.id.nav_myth_mort:
                loadDoc(getString(R.string.myth_mort_title), 3);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("MythsDocOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(MythsDocActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("MythsDocOperator", true)){

            stopService(new Intent(MythsDocActivity.this, MusicService.class));

        }else {
            editor.putBoolean("MythsDocOperator", true).apply();
        }
    }
}
