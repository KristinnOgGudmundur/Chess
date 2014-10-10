package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.GameState;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.Player;

import java.util.List;


public abstract class Piece {
	private Player player;
	private Coordinate position;
	private boolean hasMoved = false;
	private GameState gameState;


	public Piece(Player player, Coordinate position){
		this.player = player;
		this.position = position;
		this.gameState = GameState.getInstance();
	}

	public Coordinate getPosition(){
		return this.position;
	}

	public List<MoveOption> getMoveOptions(){
		return null;
	}

	public void setPosition(Coordinate position){
		this.position = position;
	}
}
