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

public class GodsDocActivity extends AnkhBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ObservableWebView webView;
    private LoadingDialog loadingDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String title;

    @SuppressLint({"CommitPrefEdits", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gods_doc);
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

        loadingDialog = new LoadingDialog(GodsDocActivity.this);

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
        String[] fileNameList = {"intro","amemet","amon","amon_re","anubis","aton","bastet","bes","chou_tefnout","geb_nout","hapi","hathor","horus",
                                 "isis","khnoum","khonsou","maat","min","mout","neith","nekhbet","nephtys","osiris","ouadjet","ptah","re",
                                 "sekhmet","selkis","seth","sobek","thot","toueris"};

        for (int i = 0; i < fileNameList.length; i++){
            if (id == i){
                webView.loadUrl("file:///android_asset/documentation/"+getResources().getString(R.string.lang)+"/gods/gods.html#"+fileNameList[i]);
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
            editor.putBoolean("GodsDocOperator", false).apply();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gods_doc, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(GodsDocActivity.this, R.color.yellow), PorterDuff.Mode.SRC_ATOP);
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

            AppUtilities.getInstance(GodsDocActivity.this).startClickSong();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.gods_share, ":\n\n https://play.google.com/store/apps/details?id="+getPackageName()+"\n\n"));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_title)));

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        AppUtilities.getInstance(GodsDocActivity.this).startClickSong();

        switch (item.getItemId()){
            case R.id.nav_intro:
                loadDoc(getString(R.string.intro_title), 0);
                break;

            case R.id.nav_amemet:
                loadDoc(getString(R.string.amemet), 1);
                break;

            case R.id.nav_amon:
                loadDoc(getString(R.string.amon), 2);
                break;

            case R.id.nav_amon_re:
                loadDoc(getString(R.string.amon_re), 3);
                break;

            case R.id.nav_anubis:
                loadDoc(getString(R.string.anubis), 4);
                break;

            case R.id.nav_aton:
                loadDoc(getString(R.string.aton), 5);
                break;

            case R.id.nav_bastet:
                loadDoc(getString(R.string.bastet), 6);
                break;

            case R.id.nav_bes:
                loadDoc(getString(R.string.bes), 7);
                break;

            case R.id.nav_chou_tefnout:
                loadDoc(getString(R.string.chou_tefnout), 8);
                break;

            case R.id.nav_geb_nout:
                loadDoc(getString(R.string.geb_nout), 9);
                break;

            case R.id.nav_hapi:
                loadDoc(getString(R.string.hapi), 10);
                break;

            case R.id.nav_hathor:
                loadDoc(getString(R.string.hathor), 11);
                break;

            case R.id.nav_horus:
                loadDoc(getString(R.string.horus), 12);
                break;

            case R.id.nav_isis:
                loadDoc(getString(R.string.isis), 13);
                break;

            case R.id.nav_khnoum:
                loadDoc(getString(R.string.khnoum), 14);
                break;

            case R.id.nav_khonsou:
                loadDoc(getString(R.string.khonsou), 15);
                break;

            case R.id.nav_maat:
                loadDoc(getString(R.string.maat), 16);
                break;

            case R.id.nav_min:
                loadDoc(getString(R.string.min), 17);
                break;

            case R.id.nav_mout:
                loadDoc(getString(R.string.mout), 18);
                break;

            case R.id.nav_neith:
                loadDoc(getString(R.string.neith), 19);
                break;

            case R.id.nav_nekhbet:
                loadDoc(getString(R.string.nekhbet), 20);
                break;

            case R.id.nav_nephtys:
                loadDoc(getString(R.string.nephtys), 21);
                break;

            case R.id.nav_osiris:
                loadDoc(getString(R.string.osiris), 22);
                break;

            case R.id.nav_ouadjet:
                loadDoc(getString(R.string.ouadjet), 23);
                break;

            case R.id.nav_ptah:
                loadDoc(getString(R.string.ptah), 24);
                break;

            case R.id.nav_rê:
                loadDoc(getString(R.string.re), 25);
                break;

            case R.id.nav_sekhmet:
                loadDoc(getString(R.string.sekhmet), 26);
                break;

            case R.id.nav_selkis:
                loadDoc(getString(R.string.selkis), 27);
                break;

            case R.id.nav_seth:
                loadDoc(getString(R.string.seth), 28);
                break;

            case R.id.nav_sobek:
                loadDoc(getString(R.string.sobek), 29);
                break;

            case R.id.nav_thot:
                loadDoc(getString(R.string.thot), 30);
                break;

            case R.id.nav_touèris:
                loadDoc(getString(R.string.toueris), 31);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("GodsDocOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(GodsDocActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("GodsDocOperator", true)){

            stopService(new Intent(GodsDocActivity.this, MusicService.class));

        }else {
            editor.putBoolean("GodsDocOperator", true).apply();
        }
    }
}
