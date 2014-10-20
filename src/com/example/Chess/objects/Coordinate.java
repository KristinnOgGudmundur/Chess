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


	public boolean isInRange(int lower, int upper){
		return (col >= lower) && (row >= lower) && (col <= upper) && (col <= upper);
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


	private String numberToChar(int number){
		switch(number){
			case(1):
				return "A";
			case(2):
				return "B";
			case(3):
				return "C";
			case(4):
				return "D";
			case(5):
				return "E";
			case(6):
				return "F";
			case(7):
				return "G";
			case(8):
				return "H";
			default:
				throw new RuntimeException("Illegal coordinate");
		}

	}

	@Override
	public String toString(){
		return String.format("%s%s", numberToChar(col), row);
	}
}
