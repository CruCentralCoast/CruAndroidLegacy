package org.androidcru.crucentralcoast.presentation.views.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.androidcru.crucentralcoast.R;

/**
 * Created by main on 5/4/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // sets up the SettingS Activity title and view
        getSupportActionBar().setTitle("Settings");
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }
}