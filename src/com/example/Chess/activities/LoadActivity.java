package com.example.Chess.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.Chess.R;
import com.example.Chess.database.BoardAdapter;
import com.example.Chess.database.DBHelper;

/**
 * Created by Kristinn on 1.11.2014.
 */
public class LoadActivity extends ListActivity {

    private BoardAdapter mSA = new BoardAdapter( this );
    private SimpleCursorAdapter mCA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = mSA.queryBoards();
        String cols[] = DBHelper.TableBoardsCols;
        String from[] = { cols[1]};
        int to[] = { R.id.name};
        startManagingCursor( cursor );
        mCA = new SimpleCursorAdapter(this, R.layout.load, cursor, from, to );


        mCA.setViewBinder( new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                ((Button)view).setText((cursor.getString(cursor.getColumnIndex("name"))));

/*                if(view.getId() == R.id.s_fid)
                {
                    view.setTag(cursor.getInt(cursor.getColumnIndex("fid")));

                    if(cursor.getInt(cursor.getColumnIndex("finished")) == 1 )
                    {
                        ((Button)view).setText("x");
                        return true;
                    }
                }
                if(view.getId() == R.id.size)
                {
                    ((TextView)view).setText((cursor.getInt(cursor.getColumnIndex("size"))) + "x" + (cursor.getInt(cursor.getColumnIndex("size"))));
                    return true;
                }
                return false;*/
                return true;
            }
        });

        setListAdapter(mCA);

    }
    public void gameChosen(View view){

        Button text = (Button) view;
        Cursor cursor = mSA.queryBoards(text.getText().toString());
        cursor.moveToNext();
        System.out.println("bla :" + cursor.getString(2));

        Intent theIntent = new Intent(this, PlayActivity.class);
        theIntent.removeExtra("LoadGame");
        theIntent.putExtra("LoadGame", true);
        theIntent.putExtra("BoardState", cursor.getString(2));
        theIntent.putExtra("p1Time", cursor.getInt(3));
        theIntent.putExtra("p2Time", cursor.getInt(4));
        theIntent.putExtra("turn", cursor.getInt(5));

        startActivity(theIntent);
/*
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);*/
    }
}
