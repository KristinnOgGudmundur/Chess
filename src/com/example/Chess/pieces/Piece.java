package com.example.Chess.pieces;

import com.example.Chess.objects.*;

import java.util.ArrayList;
import java.util.List;


public abstract class Piece {
	protected Player player;
	protected Coordinate position;
	protected boolean hasMoved = false;
	protected GameState gameState;


	public Piece(Player player, Coordinate position){
		this.player = player;
		this.position = position;
		this.gameState = GameState.getInstance();
	}

	public Coordinate getPosition(){
		return this.position;
	}

	public List<MoveOption> getMoveOptions(){
		return new ArrayList<MoveOption>();
	}

	/**
	 * Checks what if the given cell is empty, held by an enemy or held by a friend
	 * NOTE: Does not check if the piece can actually move to that cell
	 */
	protected MoveStatus getMoveStatusAt(Coordinate coordinate, Player player){
		if(coordinate.isInRange(1,8)){
			Piece otherPiece = gameState.getPiece(coordinate);
			if (otherPiece == null) {
				return MoveStatus.CANMOVE;
			}
			else if (otherPiece.getPlayer() == this.player) {
				return MoveStatus.PROTECTS;
			}
			else {
				return MoveStatus.CANKILL;
			}
		}
		return null;
	}


	public boolean isProtected(){
		MoveOption protectsSelf = new MoveOption(this.position, MoveStatus.PROTECTS);
		for(Piece p : gameState.getPieces()){
			if(p.getPlayer() == player){
				if(p.getMoveOptions().contains(protectsSelf)){
					return true;
				}
			}
		}
		return false;
	}

	public void setPosition(Coordinate position){
		this.position = position;
	}

	public Player getPlayer(){
		return this.player;
	}

	public abstract String getString();

    public abstract void setHasMoved(boolean hasMoved);

	public boolean getHasMoved(){
		return hasMoved;
	}

	@Override
	public String toString(){
		return String.format("Player: %s, position: %s", player, position);
	}
}
