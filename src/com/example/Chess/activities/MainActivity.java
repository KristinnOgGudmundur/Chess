package com.example.Chess.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import com.example.Chess.R;

public class MainActivity extends Activity implements PopupMenu.OnMenuItemClickListener{
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void buttonClick(View view){
		Button button = (Button) view;

		int id = button.getId();
		if(id == R.id.button_play){
			PopupMenu theMenu = new PopupMenu(this, button);

			theMenu.getMenu().add("New Game");
			theMenu.getMenu().add("Continue Game");
            theMenu.getMenu().add("Load Game");
			theMenu.setOnMenuItemClickListener(this);
			theMenu.show();
		}
		else{
			startActivity(new Intent(this, OptionsActivity.class));
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent theIntent = null;
		if(item.getTitle() == "New Game"){
            theIntent = new Intent(this, PlayActivity.class);
			theIntent.removeExtra("NewGame");
			theIntent.putExtra("NewGame", true);
		}
		if(item.getTitle() == "Continue Game"){
            theIntent = new Intent(this, PlayActivity.class);
			theIntent.removeExtra("NewGame");
			theIntent.putExtra("NewGame", false);
		}
        if(item.getTitle() == "Load Game"){
            theIntent = new Intent(this, LoadActivity.class);
        }
		startActivity(theIntent);
		return false;
	}
}
