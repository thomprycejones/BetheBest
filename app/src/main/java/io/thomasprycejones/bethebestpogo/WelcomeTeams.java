package io.thomasprycejones.bethebestpogo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

public class WelcomeTeams extends AppCompatActivity {


    private Button btnValor, btnMystic, btnInstinct;
    private PrefManager prefManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_teams);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //Get Preferences Manager
        prefManager = new PrefManager(this);

        //Get and Set teams buttons
        btnValor = (Button) findViewById(R.id.btn_valor);
        btnValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Analytics para ver de que team son
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.GROUP_ID, "Valor");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.JOIN_GROUP, bundle);
                prefManager.setTeamSelected("Valor");
                launchHomeScreen();
            }
        });

        btnMystic = (Button) findViewById(R.id.btn_mystic);
        btnMystic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Analytics para ver de que team son
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.GROUP_ID, "MYstic");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.JOIN_GROUP, bundle);
                prefManager.setTeamSelected("Mystic");
                launchHomeScreen();
            }
        });

        btnInstinct = (Button) findViewById(R.id.btn_instinct);
        btnInstinct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Analytics para ver de que team son
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.GROUP_ID, "Instinct");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.JOIN_GROUP, bundle);
                prefManager.setTeamSelected("Instinct");
                launchHomeScreen();
            }
        });

        // Making notification bar transparent
        changeStatusBarColor();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }


    /**
     * Launching Main
     */
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeTeams.this, MainActivity.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
