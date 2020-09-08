package com.neteru.ankh.activities.documentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.neteru.ankh.R;
import com.neteru.ankh.activities.documentation.sections.CosmogoniesDocActivity;
import com.neteru.ankh.activities.documentation.sections.GodsDocActivity;
import com.neteru.ankh.activities.documentation.sections.MythsDocActivity;
import com.neteru.ankh.classes.AnkhBaseActivity;
import com.neteru.ankh.classes.AppUtilities;
import com.neteru.ankh.classes.Typewriter;
import com.neteru.ankh.classes.services.MusicService;

public class DocSectionsActivity extends AnkhBaseActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_sections);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        Typewriter title = findViewById(R.id.title);

        title.animateText(getString(R.string.doc_title));

        Button
                godsDoc = findViewById(R.id.godsDoc),
                cosmogoniesDoc = findViewById(R.id.creationTheoriesDoc),
                mythsDoc = findViewById(R.id.mythsDoc);

        godsDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("DocSectionsOperator", false).apply();
                AppUtilities.getInstance(DocSectionsActivity.this).startClickSong();

                startActivity(new Intent(DocSectionsActivity.this, GodsDocActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        cosmogoniesDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("DocSectionsOperator", false).apply();
                AppUtilities.getInstance(DocSectionsActivity.this).startClickSong();

                startActivity(new Intent(DocSectionsActivity.this, CosmogoniesDocActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mythsDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("DocSectionsOperator", false).apply();
                AppUtilities.getInstance(DocSectionsActivity.this).startClickSong();

                startActivity(new Intent(DocSectionsActivity.this, MythsDocActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("DocSectionsOperator", false).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        editor.putBoolean("DocSectionsOperator", false).apply();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(DocSectionsActivity.this, MusicService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preferences.getBoolean("DocSectionsOperator", true)){

            stopService(new Intent(DocSectionsActivity.this, MusicService.class));

        }else {
            editor.putBoolean("DocSectionsOperator", true).apply();
        }
    }
}
