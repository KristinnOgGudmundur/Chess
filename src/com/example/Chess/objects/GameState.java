package com.example.Chess.objects;


import com.example.Chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	public static GameState instance;

	private GameStatus gameStatus;
	private List<Piece> pieces;


	private GameState(){
		pieces = new ArrayList<Piece>();

		//Add pieces for white
		pieces.add(new Rook(Player.PLAYER1, new Coordinate(1,1)));
		pieces.add(new Knight(Player.PLAYER1, new Coordinate(2,1)));
		pieces.add(new Bishop(Player.PLAYER1, new Coordinate(3,1)));
		pieces.add(new Queen(Player.PLAYER1, new Coordinate(4,1)));
		pieces.add(new King(Player.PLAYER1, new Coordinate(5,1)));
		pieces.add(new Bishop(Player.PLAYER1, new Coordinate(6,1)));
		pieces.add(new Knight(Player.PLAYER1, new Coordinate(7,1)));
		pieces.add(new Rook(Player.PLAYER1, new Coordinate(8,1)));

		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(1,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(2,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(3,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(4,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(5,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(6,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(7,2)));
		pieces.add(new Pawn(Player.PLAYER1, new Coordinate(8,2)));


		//Add pieces for black
		pieces = new ArrayList<Piece>();
		pieces.add(new Rook(Player.PLAYER2, new Coordinate(1,7)));
		pieces.add(new Knight(Player.PLAYER2, new Coordinate(2,7)));
		pieces.add(new Bishop(Player.PLAYER2, new Coordinate(3,7)));
		pieces.add(new Queen(Player.PLAYER2, new Coordinate(4,7)));
		pieces.add(new King(Player.PLAYER2, new Coordinate(5,7)));
		pieces.add(new Bishop(Player.PLAYER2, new Coordinate(6,7)));
		pieces.add(new Knight(Player.PLAYER2, new Coordinate(7,7)));
		pieces.add(new Rook(Player.PLAYER2, new Coordinate(8,7)));

		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(1,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(2,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(3,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(4,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(5,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(6,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(7,8)));
		pieces.add(new Pawn(Player.PLAYER2, new Coordinate(8,8)));

		gameStatus = GameStatus.TURN_PLAYER1;
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
		for(Piece p : pieces){
			if(p.getPosition() == position){
				return p;
			}
		}
		return null;
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
