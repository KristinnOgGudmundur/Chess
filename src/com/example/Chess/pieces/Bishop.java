package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.Player;


public class Bishop extends Piece {

	public Bishop(Player player, Coordinate position){
		super(player, position);
	}

	@Override
	public String getString() {
		return "B";
	}
}