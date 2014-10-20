package com.example.Chess.objects;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class MoveOption {
	public Coordinate coordinate;
	public MoveStatus moveStatus;

	public MoveOption(Coordinate coordinate, MoveStatus status){
		this.coordinate = coordinate;
		this.moveStatus = status;
	}
}
