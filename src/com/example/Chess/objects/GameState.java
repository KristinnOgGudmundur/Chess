package com.example.Chess.objects;


import java.util.List;

public class GameState {
	public static GameState instance;

	private GameStatus gameStatus;
	private List<Piece> pieces;


	private GameState(){

	}

	public static GameState getInstance(){
		if(instance == null){
			instance = new GameState();
		}
		return instance;
	}

	//region Getters
	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public List<Piece> getPieces() {
		return pieces;
	}
	//endregion Getters

	public Piece getPiece(Coordinate position){
		return null;
	}

	public GameStatus movePiece(Coordinate start, Coordinate end){
		return null;
	}

	private void killPiece(Coordinate position){

	}
}
