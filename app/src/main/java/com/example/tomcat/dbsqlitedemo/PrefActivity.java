package com.example.tomcat.dbsqlitedemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by tomcat on 2016/12/16.
 */

public class PrefActivity extends PreferenceActivity
{
    private SharedPreferences   sharedPreferences;
    private Preference          defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.dbpreference);
        defaultColor = (Preference) findPreference("DEFAULT_COLOR");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        int color = sharedPreferences.getInt("DEFAULT_COLOR", -1);

        if (color != -1)
        {
            defaultColor.setSummary("add NEW item color" + ": " + ItemActivity.getColors(color));
        }
    }

}
