package com.example.Chess.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Chess.R;
import com.example.Chess.database.BoardAdapter;
import com.example.Chess.objects.Board;
import com.example.Chess.objects.LineNumberOption;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends Activity{

    private long p1TimeLeft;
    private long p2TimeLeft;
    private boolean useTime;
    TimerTask timerTask;
    private int timerIter = 0;
    private boolean first = true;
    private Board theBoard;
    private boolean whitePlayerTurn = true;
    private static AlertDialog.Builder finishedDialog;
    private static boolean finished;
	private Vibrator m_vibrator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.action_save)
        {
            final EditText input = new EditText(this);
            final BoardAdapter ba = new BoardAdapter( this );

            new AlertDialog.Builder(this)
                    .setTitle("Saving Game")
                    .setMessage("Name of game:")
                    .setView(input)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            System.out.println(input.getText());
                            int turn = (whitePlayerTurn) ? 1 : 0;
                            int gameFinished = (finished) ? 1 : 0;
                            int p1time = new BigDecimal(p1TimeLeft).intValueExact();
                            int p2time = new BigDecimal(p2TimeLeft).intValueExact();
                            String boardState = theBoard.getGameState();
                            System.out.println(boardState);
                            long success = -2;
                            //save game to database
                            if (!(input.getText().toString().equals(""))) {
                                success = ba.insertBoard(input.getText().toString(), boardState, p1time, p2time, turn, gameFinished);
                            }

                            CharSequence text;
                            if (success == -1) {
                                text = input.getText().toString() + " Already exists!!!";
                            } else if (success == -2) {
                                text = "You need to enter a name for the game!!!";
                            } else {
                                text = input.getText().toString() + " Saved";
                            }
                            Context context = getApplicationContext();

                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing.
                }
            }).show();
            return true;
        }
		else if(item.getItemId() == R.id.action_undo){
			theBoard.backTrack();
		}
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
        Bundle extras = getIntent().getExtras();

        SharedPreferences shareSettings = getSharedPreferences("MyPrefs", 0);
        SharedPreferences defaultSettings = PreferenceManager.getDefaultSharedPreferences(this);

        shareSettings.edit().putBoolean("is_first_time", false).commit();

        String lineNumbers = defaultSettings.getString("showLineNumbers", getString(R.string.optionLineNumberValues_OnlyIfBigEnough));
        String time = defaultSettings.getString("gameTime", getString(R.string.timeValuesThirtyMin));
        String soundVolume = defaultSettings.getString("soundVolume", "0");
        boolean useVibrations = defaultSettings.getBoolean("useVibrations", false);

        theBoard = (Board)findViewById(R.id.board);

		m_vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

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


        //Construct our finished dialog

        finishedDialog = new AlertDialog.Builder(this);
		finishedDialog.setCancelable(true);
		finishedDialog.setPositiveButton(R.string.mainMenu,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		finishedDialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});


        //Check if this is a new game or an existing game
        boolean newGame;
        boolean loadGame;
		try {
			newGame = extras.getBoolean("NewGame");
            loadGame = extras.getBoolean("LoadGame");
			if (newGame)
            {
                theBoard.reset();
                whitePlayerTurn = true;
                //fetch our clock preference
                if(time.equals(getString(R.string.timeValuesNoTime)))
                {
                    useTime = false;
                }
                else if(time.equals(getString(R.string.timeValuesThreeMin)))
                {
					//TODO: Revert
                    p1TimeLeft = 5;//180;
                    p2TimeLeft = 5;//180;
                    useTime = true;
                }
				else if(time.equals(getString(R.string.timeValuesFiveMin)))
				{
					p1TimeLeft = 300;
					p2TimeLeft = 300;
					useTime = true;
				}
				else if(time.equals(getString(R.string.timeValuesSevenMin)))
				{
					p1TimeLeft = 420;
					p2TimeLeft = 420;
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
                    TextView p1 = (TextView)findViewById(R.id.player1);
                    TextView p2 = (TextView)findViewById(R.id.player2);
                    p1.setText(parser(p1TimeLeft));
                    p2.setText(parser(p2TimeLeft));
                }
			}
            else
            {
                if(loadGame)
                {
                    String boardState = extras.getString("BoardState");
                    int p1t = extras.getInt("p1Time");
                    int p2t = extras.getInt("p2Time");
                    p1TimeLeft = Long.valueOf(p1t);
                    p2TimeLeft =Long.valueOf(p2t);
                    int turn = extras.getInt("turn");
                    whitePlayerTurn = (turn != 0);
                    theBoard.setGameState(boardState);
                }
                else
                {
                    //fetch preference from earlier game
                    p1TimeLeft = Long.valueOf(defaultSettings.getString("tempTime","1800")).longValue();
                    p2TimeLeft = Long.valueOf(defaultSettings.getString("tempTime2","1800")).longValue();
                    whitePlayerTurn = defaultSettings.getBoolean("playerTurn", true);
                    String fen = defaultSettings.getString("fen", "");
                    theBoard.setGameState(fen);
                }

                System.out.println(p1TimeLeft);
                System.out.println(p2TimeLeft);

                TextView p1 =  (TextView)findViewById(R.id.player1);
                TextView p2 =  (TextView)findViewById(R.id.player2);

                //if the time left was set to 13337 on both players, we know we don't want to use time
                if(p1TimeLeft != 13337 && p2TimeLeft != 13337)
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
                    //player one lost the current game
                    theBoard.finished();

                    TextView timer = (TextView)findViewById(R.id.player1);
                    timer.setText("Lost");
                    timer.setBackgroundResource(R.drawable.back2);
                    TextView timer2 = (TextView)findViewById(R.id.player2);
                    timer2.setBackgroundResource(R.drawable.back2);
                    timer2.setText("Won");
                    useTime = false;
                    finished = true;


                }
                else if(p2TimeLeft == 0)
                {
                    //player two lost the current game
                    theBoard.finished();

                    TextView timer = (TextView)findViewById(R.id.player1);
                    timer.setText("Won");
                    timer.setBackgroundResource(R.drawable.back2);
                    TextView timer2 = (TextView)findViewById(R.id.player2);
                    timer2.setBackgroundResource(R.drawable.back2);
                    timer2.setText("Lost");
                    useTime = false;
                    finished = true;
                }
			}
		}
		catch(Exception e){
			System.out.println("An exception was thrown");
		}
	}

    /**
     * This function is called when a player has finished his move
     */
    public void newTurn()
    {

        //System.out.println("------------->" + theBoard.gameTerminal());
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

    /**
     * Starts the timer for a specific player
     * @param player
     * the player clock that is to be started
     */
    public void startTimer(final boolean player) {


        timerIter++;
        final Handler handler = new Handler();
        Timer ourTimer = new Timer();
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
                                finishedDialog.setMessage("Black won on time \nTime left: " + parser(timerIter < 2 ? p2TimeLeft : p2TimeLeft + 1));
                                finishedDialog.show();
                                timerTask.cancel();
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
                                finishedDialog.setMessage("White won on time \nTime left: " + parser(timerIter < 2 ? p1TimeLeft : p1TimeLeft + 1));
                                finishedDialog.show();
                                timerTask.cancel();
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
        ourTimer.schedule(timerTask, 0, 1000);

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


    public void playerwon(int winner) {
        TextView timer1 = (TextView) findViewById(R.id.player1);
        TextView timer2 = (TextView) findViewById(R.id.player2);

		if(theBoard.useVibrations) {
			m_vibrator.vibrate(500);
		}
		if (winner != 0) {
            TextView timer = (TextView) findViewById(R.id.player1);
            theBoard.finished();
            finishedDialog.setMessage("White won the game \nTime left: " + parser(p2TimeLeft));
            finishedDialog.show();
            if(timerTask != null)
            {
                timerTask.cancel();
            }
            timer1.setText("Won");
            timer2.setBackgroundResource(R.drawable.back2);
            timer2.setText("Lost");

        } else {
            theBoard.finished();
            finishedDialog.setMessage("Black won the game \nTime left: " + parser(p2TimeLeft));
            finishedDialog.show();
            if(timerTask != null)
            {
                timerTask.cancel();
            }
            timer1.setText("Lost");
            timer2.setBackgroundResource(R.drawable.back2);
            timer2.setText("Won");
        }
    }
    @Override
    protected void onStop() {
        if(timerTask != null)
        {
            timerTask.cancel();
        }
        super.onStop();

        //save the current position in preference
        SharedPreferences tempSettings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = tempSettings.edit();

        if(useTime || theBoard.getFinished())
        {
            editor.putString("tempTime", Long.toString(p1TimeLeft));
            editor.putString("tempTime2", Long.toString(p2TimeLeft));
        }
        else
        {
            editor.putString("tempTime", "13337");
            editor.putString("tempTime2", "13337");
        }
        editor.putBoolean("playerTurn", whitePlayerTurn);
        editor.putString("fen", theBoard.getGameState());

        // Commit the edits!
        editor.commit();
        System.out.println(p1TimeLeft);
        System.out.println(p2TimeLeft);
    }
    public void displaytimers()
    {
        TextView timer1 = (TextView)findViewById(R.id.player1);
        timer1.setText(parser(p1TimeLeft));
        TextView timer2 = (TextView)findViewById(R.id.player2);
        timer2.setText(parser(p2TimeLeft));
    }

}