package com.example.Chess.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class BoardAdapter {

    SQLiteDatabase db;
    DBHelper dbHelper;
    Context context;

    public BoardAdapter(Context c) {
        context = c;
    }

    public BoardAdapter openToRead() {
        dbHelper = new DBHelper( context );
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public BoardAdapter openToWrite() {
        dbHelper = new DBHelper( context );
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public long insertBoard( String name, String board, int p1time, int p2time, int playerturn, int finished) {
        String[] cols = DBHelper.TableBoardsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], name );
        contentValues.put( cols[2], board);
        contentValues.put( cols[3], ((Integer)p1time).toString() );
        contentValues.put( cols[4], ((Integer)p2time).toString());
        contentValues.put( cols[5], ((Integer)playerturn).toString());
        contentValues.put( cols[6], ((Integer)finished).toString());
        openToWrite();
        long value = db.insert(DBHelper.TableBoards, null, contentValues );
        close();
        return value;
    }

    public Cursor queryBoards() {
        openToRead();
        Cursor cursor = db.query( DBHelper.TableBoards,
                DBHelper.TableBoardsCols, null, null, null, null, null);
        return cursor;
    }

    public Cursor queryBoards( String name) {
        openToRead();
        String[] cols = DBHelper.TableBoardsCols;
        Cursor cursor = db.query( DBHelper.TableBoards,
                cols, cols[1] + " = \"" + name + "\"", null, null, null, null);
        return cursor;
    }

    public long count() {
        openToRead();
        long returnValue = DatabaseUtils.queryNumEntries(db, "boards");
        close();
        return returnValue;
    }
}