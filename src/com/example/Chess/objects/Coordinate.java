package com.example.Chess.objects;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class Coordinate {
	private int col;
	private int row;

	public Coordinate(int c, int r){
		col = c;
		row = r;
	}

	public int getCol(){
		return col;
	}

	public int getRow(){
		return row;
	}


	@Override
	public boolean equals(Object other){
		if(other.getClass() != Coordinate.class){
			return false;
		}
		else{
			return ((Coordinate)other).getCol() == this.col && ((Coordinate)other).getRow() == this.row;
		}
	}


	@Override
	public String toString(){
		return "(" + col + "," + row + ")";
	}
}
