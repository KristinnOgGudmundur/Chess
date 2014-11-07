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

	public Coordinate(Coordinate other){
		col = other.getCol();
		row = other.getRow();
	}

	public Coordinate(String str){
		System.out.println("The string is: " + str + ": " + Character.toString(str.charAt(0)) + "," + Character.toString(str.charAt(1)));
		row = Integer.parseInt(Character.toString(str.charAt(1)));
		col = charToNumber(Character.toString(str.charAt(0)));
	}

	public int getCol(){
		return col;
	}

	public int getRow(){
		return row;
	}


	public boolean isInRange(int lower, int upper){
		if(lower > upper){
			return this.isInRange(upper, lower);
		}
		return (col >= lower) && (row >= lower) && (col <= upper) && (row <= upper);
	}


	@Override
	public boolean equals(Object other){
		if(other.getClass() == Coordinate.class){
			return ((Coordinate)other).getCol() == this.col && ((Coordinate)other).getRow() == this.row;
		}
		else if(other instanceof String){
			return ((String)other).equals(this.toString());
		}
		else{
			return false;
		}
	}


	private String numberToChar(int number){
		switch(number){
			case(1):
				return "a";
			case(2):
				return "b";
			case(3):
				return "c";
			case(4):
				return "d";
			case(5):
				return "e";
			case(6):
				return "f";
			case(7):
				return "g";
			case(8):
				return "h";
			default:
				throw new RuntimeException("Illegal coordinate");
		}

	}

	private int charToNumber(String character){
		if(character.equals("a")){
			return 1;
		}
		else if(character.equals("b")){
			return 2;
		}
		else if(character.equals("c")){
			return 3;
		}
		else if(character.equals("d")){
			return 4;
		}
		else if(character.equals("e")){
			return 5;
		}
		else if(character.equals("f")){
			return 6;
		}
		else if(character.equals("g")){
			return 7;
		}
		else if(character.equals("h")){
			return 8;
		}
		else{
			throw new RuntimeException("Illegal coordinate: \"" + character + "\"");
		}
	}

	@Override
	public String toString(){
		return String.format("%s%s", numberToChar(col), row);
	}
}
