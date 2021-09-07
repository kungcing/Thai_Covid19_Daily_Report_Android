package com.kungcing.ThaiDailyCovid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SettingActivity extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        layout = findViewById(R.id.settingBackGround);
        Fragment myFrag = new MyPreferenceFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contain, myFrag).commit();
        preferenceFromUser(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout = findViewById(R.id.settingBackGround);
        preferenceFromUser(layout);
    }

    private void preferenceFromUser(LinearLayout layout) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean("theme", false)) {
            layout.setBackgroundColor(getResources().getColor(R.color.purple_200));
        } else {
            layout.setBackgroundColor(getResources().getColor(R.color.white));
        }
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (sharedPreferences.getBoolean("theme", false)) {
                    layout.setBackgroundColor(getResources().getColor(R.color.purple_200));
                } else {
                    layout.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        });
    }
}