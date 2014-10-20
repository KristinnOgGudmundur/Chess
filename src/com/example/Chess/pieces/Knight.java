package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Knight extends Piece {

	public Knight(Player player, Coordinate position){
		super(player, position);
	}

	@Override
	public List<MoveOption> getMoveOptions(){
		List<MoveOption> returnValue = new ArrayList<MoveOption>();
		return returnValue;
	}

	@Override
	public String getString() {
		return "C";
	}
}
