package edu.augustana.quadsquad.householdmanager.model.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.augustana.quadsquad.householdmanager.data.preferences.SaveSharedPreference;
import edu.augustana.quadsquad.householdmanager.model.activity.GroupActivity;
import edu.augustana.quadsquad.householdmanager.model.activity.LoginActivity;
import edu.augustana.quadsquad.householdmanager.model.activity.MainActivity;

/**
 * Created by micha on 4/4/2016.
 */
public class SplashActivity extends AppCompatActivity {

    boolean isLoggedIn;
    boolean hasGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isLoggedIn = SaveSharedPreference.getIsLoggedIn(getApplicationContext());
        hasGroup = SaveSharedPreference.getHasGroup(getApplicationContext());

        if (isLoggedIn) {
            if (hasGroup){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(this, GroupActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
}