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

			theMenu.getMenu().add("New game");
			theMenu.getMenu().add("Load game");
			theMenu.setOnMenuItemClickListener(this);
			theMenu.show();
		}
		else{
			startActivity(new Intent(this, OptionsActivity.class));
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent theIntent = new Intent(this, PlayActivity.class);
		if(item.getTitle() == "New game"){
			theIntent.removeExtra("NewGame");
			theIntent.putExtra("NewGame", true);
		}
		if(item.getTitle() == "Load game"){
			theIntent.removeExtra("NewGame");
			theIntent.putExtra("NewGame", false);
		}
		startActivity(theIntent);
		return false;
	}
}
