package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class King extends Piece {

	public King(Player player, Coordinate position){
		super(player, position);
	}

	@Override
	public String getString() {
		return "K";
	}

	@Override
	public List<MoveOption> getMoveOptions(){
		Coordinate pos = this.position;
		List<MoveOption> returnValue = new ArrayList<MoveOption>();

		for(int x = pos.getCol() - 1; x < pos.getCol() + 1; x++){
			for(int y = pos.getCol() - 1; y < pos.getCol() + 1; y++){
				if(x != pos.getCol() || y != pos.getRow()){
					Coordinate currentPosition = new Coordinate(x, y);
					Piece otherPiece = gameState.getPiece(currentPosition);
					if(otherPiece == null){
						returnValue.add(new MoveOption(currentPosition, MoveStatus.CANMOVE));
					}
					else if(otherPiece.getPlayer() == this.player){
						returnValue.add(new MoveOption(currentPosition, MoveStatus.PROTECTS));
					}
					else{
						if(!otherPiece.isProtected()) {
							returnValue.add(new MoveOption(currentPosition, MoveStatus.CANKILL));
						}
					}
				}
			}
		}

		//TODO: Add castling

		return returnValue;
	}
}
