package com.example.Chess.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import com.example.Chess.R;
import com.example.Chess.objects.Board;
import com.example.Chess.objects.LineNumberOption;
import com.example.Chess.objects.Player;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class PlayActivity extends Activity{

    private long p1TimeLeft;
    private long p2TimeLeft;
    private boolean useTime;
    TimerTask timerTask;
    private int timerIter = 0;
    private boolean first = true;
    private Board theBoard;
    private boolean whitePlayerTurn = true;

    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);

		theBoard = (Board)findViewById(R.id.board);
		Bundle extras = getIntent().getExtras();
        boolean newGame = true;
		try {
			newGame = extras.getBoolean("NewGame");
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
        String time = settings.getString("gameTime", getString(R.string.timeValuesThirtyMin));
        String soundVolume = settings.getString("soundVolume", "0");
        boolean useVibrations = settings.getBoolean("useVibrations", false);

        LineNumberOption useLineNumbers = null;

        if(lineNumbers.equals(getString(R.string.optionLineNumberValues_Yes))){
            useLineNumbers = LineNumberOption.YES;
        }
        else if(lineNumbers.equals(getString(R.string.optionLineNumberValues_No))){
            useLineNumbers = LineNumberOption.NO;
        }
        else if(lineNumbers.equals(getString(R.string.optionLineNumberValues_OnlyIfBigEnough))) {
            useLineNumbers = LineNumberOption.IF_BIG_ENOUGH;
        }
        theBoard.setPreferences(Integer.parseInt(soundVolume), useVibrations, useLineNumbers);


        if(newGame)
        {
            whitePlayerTurn = true;
            if(time.equals(getString(R.string.timeValuesNoTime)))
            {
                useTime = false;
            }
            else if(time.equals(getString(R.string.timeValuesThreeMin)))
            {
                p1TimeLeft = 10;
                p2TimeLeft = 10;
                useTime = true;
            }
            else if(time.equals(getString(R.string.timeValuesTenMin)))
            {
                p1TimeLeft = 600;
                p2TimeLeft = 600;
                useTime = true;
            }
            else if(time.equals(getString(R.string.timeValuesThirtyMin)))
            {
                p1TimeLeft = 1800;
                p2TimeLeft = 1800;
                useTime = true;
            }
            if(useTime){
                TextView p1 =  (TextView)findViewById(R.id.player1);
                TextView p2 =  (TextView)findViewById(R.id.player2);
                p1.setText(parser(p1TimeLeft));
                p2.setText(parser(p2TimeLeft));
            }
        }
        else
        {
            p1TimeLeft = Long.valueOf(settings.getString("tempTime", getString(R.string.timeValuesThirtyMin))).longValue();
            p2TimeLeft = Long.valueOf(settings.getString("tempTime2", getString(R.string.timeValuesThirtyMin))).longValue();
            whitePlayerTurn = settings.getBoolean("playerTurn", true);

            System.out.println(p1TimeLeft);
            System.out.println(p2TimeLeft);

            TextView p1 =  (TextView)findViewById(R.id.player1);
            TextView p2 =  (TextView)findViewById(R.id.player2);

            if(p1TimeLeft != 1337 && p1TimeLeft != 1337)
            {
                useTime = true;
                p1.setText(parser(p1TimeLeft));
                p2.setText(parser(p2TimeLeft));
            }
            else
            {

                useTime = false;
            }

            if(!whitePlayerTurn)
            {
                p2.setBackgroundResource(R.drawable.back2);
                p1.setBackgroundResource(R.drawable.back);
            }
            else
            {
                p2.setBackgroundResource(R.drawable.back);
                p1.setBackgroundResource(R.drawable.back2);
            }

            if(p1TimeLeft == 0)
            {
                theBoard.finished();
                TextView timer = (TextView)findViewById(R.id.player1);
                timer.setText("Lost");
                timer.setBackgroundResource(R.drawable.back2);
                TextView timer2 = (TextView)findViewById(R.id.player2);
                timer2.setBackgroundResource(R.drawable.back2);
                timer2.setText("Won");
                useTime = false;

            }
            else if(p2TimeLeft == 0)
            {
                theBoard.finished();
                TextView timer = (TextView)findViewById(R.id.player1);
                timer.setText("Won");
                timer.setBackgroundResource(R.drawable.back2);
                TextView timer2 = (TextView)findViewById(R.id.player2);
                timer2.setBackgroundResource(R.drawable.back2);
                timer2.setText("Lost");
                useTime = false;
            }
        }


	}

    public void test()
    {

        TextView p1 =  (TextView)findViewById(R.id.player1);
        TextView p2 =  (TextView)findViewById(R.id.player2);
        whitePlayerTurn = !whitePlayerTurn;
        if(useTime)
        {
            if(!first)
            {
                timerTask.cancel();
                timerTask=null;
            }else
            {
                first = false;
            }

            startTimer(whitePlayerTurn);


        }

        if(!whitePlayerTurn)
        {
            p2.setBackgroundResource(R.drawable.back2);
            p1.setBackgroundResource(R.drawable.back);
        }
        else
        {
            p2.setBackgroundResource(R.drawable.back);
            p1.setBackgroundResource(R.drawable.back2);

        }
    }

    public void startTimer(final boolean player) {

        timerIter++;
        System.out.println(timerIter);
        if(player && timerIter > 2)
        {
            p1TimeLeft++;
        }
        else if(!player && timerIter > 2)
        {
            p2TimeLeft++;
        }
        final Handler handler = new Handler();
        Timer ourtimer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if(player)
                        {
                            TextView timer = (TextView)findViewById(R.id.player1);

                            if(p1TimeLeft <= 0)
                            {
                                theBoard.finished();
                                timer.setText("Lost");
                                TextView timer2 = (TextView)findViewById(R.id.player2);
                                timer2.setBackgroundResource(R.drawable.back2);
                                timer2.setText("Won");
                            }
                            else
                            {
                                timer.setText(parser(p1TimeLeft));
                                p1TimeLeft--;
                            }
                        }
                        else {
                            TextView timer = (TextView)findViewById(R.id.player2);

                            if(p2TimeLeft <= 0)
                            {
                                theBoard.finished();
                                timer.setText("Lost");
                                TextView timer2 = (TextView)findViewById(R.id.player1);
                                timer2.setBackgroundResource(R.drawable.back2);
                                timer2.setText("Won");
                            }
                            else
                            {
                                timer.setText(parser(p2TimeLeft));
                                p2TimeLeft--;
                            }
                        }
                    }
                });
            }};
        ourtimer.schedule(timerTask, 0, 1000);

    }

    private String parser(long time)
    {

        long minutes = time/60;
        long seconds = time % 60;

        if(minutes < 10)
        {
            if(seconds < 10)
            {
                return "0" + Long.toString(minutes) + " : 0" + Long.toString(seconds);
            }
            else
            {
                return "0" + Long.toString(minutes) + " : " + Long.toString(seconds);
            }

        }
        else
        {
            if(seconds < 10)
            {
                return Long.toString(minutes) + " : 0" + Long.toString(seconds);
            }
            else
            {
                return Long.toString(minutes) + " : " + Long.toString(seconds);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences tempSettings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = tempSettings.edit();

        if(useTime || theBoard.getFinished())
        {
            editor.putString("tempTime", Long.toString(p1TimeLeft));
            editor.putString("tempTime2", Long.toString(p2TimeLeft));
        }
        else
        {
            editor.putString("tempTime", "1337");
            editor.putString("tempTime2", "1337");
        }
        editor.putBoolean("playerTurn", whitePlayerTurn);

        // Commit the edits!
        editor.commit();
        System.out.println(p1TimeLeft);
        System.out.println(p2TimeLeft);
    }
}