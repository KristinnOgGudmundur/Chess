package com.example.Chess.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.example.Chess.R;
import com.example.Chess.objects.Board;
import com.example.Chess.objects.LineNumberOption;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class PlayActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);

		Board theBoard = (Board)findViewById(R.id.board);
		Bundle extras = getIntent().getExtras();
		try {
			boolean newGame = extras.getBoolean("NewGame");
			if (newGame) {
				theBoard.reset();
			} else {
			}
		}
		catch(Exception e){
			System.out.println("An exception was thrown");
		}

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String lineNumbers = settings.getString("showLineNumbers", getString(R.string.optionLineNumberValues_OnlyIfBigEnough));
		String soundVolume = settings.getString("soundVolume", "0");
		boolean useVibrations = settings.getBoolean("useVibrations", false);

		LineNumberOption useLineNumbers = null;

		if(lineNumbers.equals(getString(R.string.optionLineNumberValues_Yes))){
			useLineNumbers = LineNumberOption.YES;
		}
		else if(lineNumbers.equals(getString(R.string.optionLineNumberValues_No))){
			useLineNumbers = LineNumberOption.NO;
		}
		else if(lineNumbers.equals(getString(R.string.optionLineNumberValues_OnlyIfBigEnough))){
			useLineNumbers = LineNumberOption.IF_BIG_ENOUGH;
		}

		theBoard.setPreferences(Integer.parseInt(soundVolume), useVibrations, useLineNumbers);
	}
}