package com.example.Chess.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.Chess.R;
import com.example.Chess.objects.Board;

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
		System.out.println("Checking about new game");
		try {
			boolean newGame = extras.getBoolean("NewGame");
			if (newGame) {
				System.out.println("New game");
			} else {
				System.out.println("Load game");
			}
		}
		catch(Exception e){
			System.out.println("An exception was thrown");
		}
	}
}