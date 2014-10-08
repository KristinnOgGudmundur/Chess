package com.example.Chess.objects;

import java.util.List;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public class Piece {
	private Player player;
	private Coordinate position;
	private boolean hasMoved = false;
	private GameState gameState;

	public Piece(Player player, Coordinate position){
		this.player = player;
		this.position = position;
		this.gameState = GameState.getInstance();
	}

	public List<MoveOption> getMoveOptions(){
		return null;
	}

	public void setPosition(Coordinate position){
		this.position = position;
	}
}
