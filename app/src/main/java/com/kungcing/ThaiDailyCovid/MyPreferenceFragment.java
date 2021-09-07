package com.kungcing.ThaiDailyCovid;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class MyPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
        Preference themePref = findPreference("theme");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sharedPref.contains("theme")) {
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("theme", false);
        }
        if (sharedPref.getBoolean("theme", false)) {
            assert themePref != null;
            themePref.setSummary("DARK MODE");
        } else {
            assert themePref != null;
            themePref.setSummary("LIGHT MODE");
        }
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if (sharedPreferences.getBoolean("theme", false)) {
                    assert themePref != null;
                    themePref.setSummary("DarK MODE".toUpperCase());
                } else {
                    assert themePref != null;
                    themePref.setSummary("LIGHT MODE");
                }
            }
        });
    }
}
