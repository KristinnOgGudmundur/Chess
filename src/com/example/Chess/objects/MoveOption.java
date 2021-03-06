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

	public MoveOption(ChessState state, ChessMove theMove){
		//TODO: Implement
		this.coordinate = new Coordinate(state.sqrToStr(theMove.getTo()));
		this.moveStatus = MoveStatus.CANMOVE;
	}

	@Override
	public boolean equals(Object other){
		if(other.getClass() == MoveOption.class){
			MoveOption temp = (MoveOption)other;
			return (temp.coordinate.equals(this.coordinate) && temp.moveStatus == this.moveStatus);
		}
		else{
			return false;
		}
	}
}
