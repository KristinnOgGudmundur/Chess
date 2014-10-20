package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.Player;


public class Queen extends Piece {

	public Queen(Player player, Coordinate position){
		super(player, position);
	}

	@Override
	public String getString() {
		return "Q";
	}

    @Override
    public void setHasMoved(boolean hasMoved)
    {

    }
}