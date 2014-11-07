package com.example.Chess.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.*;
import com.example.Chess.R;
import com.example.Chess.database.BoardAdapter;
import com.example.Chess.objects.Board;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class OptionsActivity extends PreferenceActivity {

    private BoardAdapter mSA = new BoardAdapter( this );

    private AlertDialog.Builder confirmDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);

        EditTextPreference pref = (EditTextPreference) findPreference("tempTime");
        EditTextPreference pref2 = (EditTextPreference) findPreference("tempTime2");
        CheckBoxPreference pref3 = (CheckBoxPreference) findPreference("playerTurn");
        EditTextPreference pref4 = (EditTextPreference) findPreference("fen");
        PreferenceCategory cat = (PreferenceCategory) findPreference("preferenceCat");
        cat.removePreference(pref);
        cat.removePreference(pref2);
        cat.removePreference(pref3);
        cat.removePreference(pref4);

        confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setMessage(R.string.confirm);
        confirmDialog.setCancelable(true);
        confirmDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mSA.drop();
                        mSA.create();
                        dialog.cancel();
                    }
                });
        confirmDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        Preference myPref = (Preference) findPreference("reset");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                confirmDialog.show();
                return true;
            }
        });
	}
}