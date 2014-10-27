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

    public List<Coordinate>getHotZones(Player player) {

        List<Coordinate> hotZones = new ArrayList<Coordinate>();

        for (Piece p : pieces) {
            if (p.getPlayer() != player && p.getString() != "K") {
                for (MoveOption m : p.getMoveOptions()) {
                    hotZones.add(m.coordinate);
                }
            }
        }
        return hotZones;
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
		Piece thePiece = getPiece(start);
		System.out.println(String.format("Moving (%s) from %s to %s", thePiece, start, end));
		List<MoveOption> moves = thePiece.getMoveOptions();


		for(MoveOption m : moves){
			if(m.coordinate.equals(end)){
				switch(m.moveStatus){
					case CANKILL:
						killPiece(end);
					case CANMOVE:
						if(thePiece instanceof Pawn){
							if(!thePiece.getHasMoved()){
								if(end.getRow() == 4 || end.getRow() == 5){
									System.out.println("Moved two spaces");
									((Pawn)thePiece).setMovedTwoSpacesLastTurn(true);
								}
							}
						}
                        thePiece.setHasMoved(true);
						thePiece.setPosition(end);

						return updateGameStatus();
					case CANCASTLE:
                        if(start.getCol() < end.getCol())
                        {
                            //castle right
                            Coordinate rookPosition = new Coordinate((start.getCol() + 3), (start.getRow()));
                            Piece rook = getPiece(rookPosition);
                            rook.setHasMoved(true);
                            Coordinate rookEndPosition = new Coordinate((start.getCol() + 1), (start.getRow()));
                            rook.setPosition(rookEndPosition);

                        }else
                        {
                            //castle left
                            Coordinate rookPosition = new Coordinate((start.getCol() - 4), (start.getRow()));
                            Piece rook = getPiece(rookPosition);
                            rook.setHasMoved(true);
                            Coordinate rookEndPosition = new Coordinate((start.getCol() - 1), (start.getRow()));
                            rook.setPosition(rookEndPosition);
                        }
                        thePiece.setHasMoved(true);
                        thePiece.setPosition(end);
                        return updateGameStatus();
					case PROTECTS:
						break;
				}
			}
		}

		System.out.println("Nothing was moved");
		//Nothing was moved
		return null;
	}

	private void killPiece(Coordinate position){
		int index = -1;
		for(Piece p : pieces){
			if(p.getPosition().equals(position)){
				index = pieces.indexOf(p);
				break;
			}
		}
		if(index != -1){
			pieces.remove(index);
		}
	}

	private GameStatus updateGameStatus(){
		//TODO: Add all the missing GameStatuses
		if(gameStatus == GameStatus.TURN_PLAYER1){
			resetPawnStatus(Player.PLAYER2);
			gameStatus = GameStatus.TURN_PLAYER2;
		}
		else{
			resetPawnStatus(Player.PLAYER1);
			gameStatus = GameStatus.TURN_PLAYER1;
		}
		return gameStatus;
	}

	private void resetPawnStatus(Player player){
		for(Piece p : pieces){
			if(p.getPlayer() == player){
				if(p instanceof Pawn){
					((Pawn) p).setMovedTwoSpacesLastTurn(false);
				}
			}
		}
	}

	public void reset(){
		instance = new GameState();
	}
}
