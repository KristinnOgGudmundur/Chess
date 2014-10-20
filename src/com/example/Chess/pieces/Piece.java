package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.GameState;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.Player;

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
		//TODO: Implement
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
