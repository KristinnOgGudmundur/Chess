package com.example.Chess.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.example.Chess.R;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class OptionsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}
}