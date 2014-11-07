package com.example.Chess.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import com.example.Chess.R;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class OptionsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);

        EditTextPreference pref = (EditTextPreference) findPreference("tempTime");
        EditTextPreference pref2 = (EditTextPreference) findPreference("tempTime2");
        CheckBoxPreference pref3 = (CheckBoxPreference) findPreference("playerTurn");
        CheckBoxPreference pref4 = (CheckBoxPreference) findPreference("fen");
        PreferenceCategory cat = (PreferenceCategory) findPreference("preferenceCat");
        cat.removePreference(pref);
        cat.removePreference(pref2);
        cat.removePreference(pref3);
        cat.removePreference(pref4);
	}
}