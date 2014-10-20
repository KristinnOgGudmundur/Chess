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

	@Override
	public String toString(){
		return String.format("Player: %s, position: %s", player, position);
	}
}
