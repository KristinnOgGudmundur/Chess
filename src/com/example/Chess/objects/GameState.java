package com.example.Chess.objects;


import com.example.Chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	private static GameState instance;

	private GameStatus gameStatus;
	private List<Piece> pieces;


	private GameState(){

	}

	private static void initializeInstance(){
		instance.pieces = new ArrayList<Piece>();

		//Add pieces for white
		instance.pieces.add(new Rook(Player.PLAYER1, new Coordinate(1,1)));
		instance.pieces.add(new Knight(Player.PLAYER1, new Coordinate(2,1)));
		instance.pieces.add(new Bishop(Player.PLAYER1, new Coordinate(3,1)));
		instance.pieces.add(new Queen(Player.PLAYER1, new Coordinate(4,1)));
		instance.pieces.add(new King(Player.PLAYER1, new Coordinate(5,1)));
		instance.pieces.add(new Bishop(Player.PLAYER1, new Coordinate(6,1)));
		instance.pieces.add(new Knight(Player.PLAYER1, new Coordinate(7,1)));
		instance.pieces.add(new Rook(Player.PLAYER1, new Coordinate(8,1)));

		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(1,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(2,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(3,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(4,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(5,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(6,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(7,2)));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(8,2)));


		//Add pieces for black
		instance.pieces.add(new Rook(Player.PLAYER2, new Coordinate(1,8)));
		instance.pieces.add(new Knight(Player.PLAYER2, new Coordinate(2,8)));
		instance.pieces.add(new Bishop(Player.PLAYER2, new Coordinate(3,8)));
		instance.pieces.add(new Queen(Player.PLAYER2, new Coordinate(4,8)));
		instance.pieces.add(new King(Player.PLAYER2, new Coordinate(5,8)));
		instance.pieces.add(new Bishop(Player.PLAYER2, new Coordinate(6,8)));
		instance.pieces.add(new Knight(Player.PLAYER2, new Coordinate(7,8)));
		instance.pieces.add(new Rook(Player.PLAYER2, new Coordinate(8,8)));

		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(1,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(2,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(3,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(4,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(5,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(6,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(7,7)));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(8,7)));

		instance.gameStatus = GameStatus.TURN_PLAYER1;
	}

	public static GameState getInstance(){
		if(instance == null){
			System.out.println("Creating instance");
			instance = new GameState();
			initializeInstance();
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
		for(Piece p : pieces){
			if(p.getPosition().equals(position)){
				return p;
			}
		}
		return null;
	}

	public boolean playerTurn(Player p){
		boolean player1Turn;
		switch (gameStatus){
			case TURN_PLAYER1:
			case CHECK_PLAYER1:
				player1Turn = true;
				break;
			case TURN_PLAYER2:
			case CHECK_PLAYER2:
				player1Turn = false;
				break;
			default:
				return false;
		}

		if(p == Player.PLAYER1){
			return player1Turn;
		}
		else{
			return !player1Turn;
		}
	}

	public GameStatus movePiece(Coordinate start, Coordinate end){
		return null;
	}

	private void killPiece(Coordinate position){

	}

	public void reset(){
		instance = new GameState();
	}
}
