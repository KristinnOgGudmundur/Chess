package com.example.Chess.objects;


import android.widget.TextView;
import com.example.Chess.R;
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
		instance.pieces.add(new Rook(Player.PLAYER1, new Coordinate(1,1),R.drawable.chessrlt60));
        instance.pieces.add(new Knight(Player.PLAYER1, new Coordinate(2,1), R.drawable.chessnlt60));
		instance.pieces.add(new Bishop(Player.PLAYER1, new Coordinate(3,1),R.drawable.chessblt60));
		instance.pieces.add(new Queen(Player.PLAYER1, new Coordinate(4,1), R.drawable.chessqlt60));
		instance.pieces.add(new King(Player.PLAYER1, new Coordinate(5,1), R.drawable.chessklt60));
		instance.pieces.add(new Bishop(Player.PLAYER1, new Coordinate(6,1), R.drawable.chessblt60));
		instance.pieces.add(new Knight(Player.PLAYER1, new Coordinate(7,1), R.drawable.chessnlt60));
		instance.pieces.add(new Rook(Player.PLAYER1, new Coordinate(8,1), R.drawable.chessrlt60));

		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(1,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(2,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(3,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(4,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(5,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(6,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(7,2), R.drawable.chessplt60));
		instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(8,2), R.drawable.chessplt60));


		//Add pieces for black
		instance.pieces.add(new Rook(Player.PLAYER2, new Coordinate(1,8), R.drawable.chessrdt60));
		instance.pieces.add(new Knight(Player.PLAYER2, new Coordinate(2,8), R.drawable.chessndt60));
		instance.pieces.add(new Bishop(Player.PLAYER2, new Coordinate(3,8), R.drawable.chessbdt60));
		instance.pieces.add(new Queen(Player.PLAYER2, new Coordinate(4,8), R.drawable.chessqdt60));
		instance.pieces.add(new King(Player.PLAYER2, new Coordinate(5,8), R.drawable.chesskdt60));
		instance.pieces.add(new Bishop(Player.PLAYER2, new Coordinate(6,8), R.drawable.chessbdt60));
		instance.pieces.add(new Knight(Player.PLAYER2, new Coordinate(7,8), R.drawable.chessndt60));
		instance.pieces.add(new Rook(Player.PLAYER2, new Coordinate(8,8), R.drawable.chessrdt60));

		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(1,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(2,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(3,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(4,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(5,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(6,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(7,7), R.drawable.chesspdt60));
		instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(8,7), R.drawable.chesspdt60));

		instance.gameStatus = GameStatus.TURN_PLAYER1;
	}

    private static void initializeInstance(String board){
        instance.pieces = new ArrayList<Piece>();

        int i = 0;
        for(int y = 1; y < 9; y++)
        {
            for(int x = 1; x < 9; x++)
            {
                System.out.println(i);
                switch(board.charAt(i))
                {
                    case '0':
                        break;
                    case '1': instance.pieces.add(new Pawn(Player.PLAYER1, new Coordinate(x,y), R.drawable.chessplt60));
                        break;
                    case '2':instance.pieces.add(new Pawn(Player.PLAYER2, new Coordinate(x,y), R.drawable.chesspdt60));
                        break;
                    case '3':instance.pieces.add(new Rook(Player.PLAYER1, new Coordinate(x,y),R.drawable.chessrlt60));
                        break;
                    case '4':instance.pieces.add(new Rook(Player.PLAYER2, new Coordinate(x,y), R.drawable.chessrdt60));
                        break;
                    case '5':instance.pieces.add(new Knight(Player.PLAYER1, new Coordinate(x,y), R.drawable.chessnlt60));
                        break;
                    case '6':instance.pieces.add(new Knight(Player.PLAYER2, new Coordinate(x,y), R.drawable.chessndt60));
                        break;
                    case '7':instance.pieces.add(new Bishop(Player.PLAYER1, new Coordinate(x,y), R.drawable.chessblt60));
                        break;
                    case '8':instance.pieces.add(new Bishop(Player.PLAYER2, new Coordinate(x,y), R.drawable.chessbdt60));
                        break;
                    case '9':instance.pieces.add(new Queen(Player.PLAYER1, new Coordinate(x,y), R.drawable.chessqlt60));
                        break;
                    case 'a':instance.pieces.add(new Queen(Player.PLAYER2, new Coordinate(x,y), R.drawable.chessqdt60));
                        break;
                    case 'b':instance.pieces.add(new King(Player.PLAYER1, new Coordinate(x,y), R.drawable.chessklt60));
                        break;
                    case 'c':instance.pieces.add(new King(Player.PLAYER2, new Coordinate(x,y), R.drawable.chesskdt60));
                        break;
                }
                i++;
            }
        }
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

    public static GameState getInstance(String board){
        instance = new GameState();
        initializeInstance(board);

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
						boolean killed = killPiece(end);
						if(killed == false){
							//The piece is a pawn and it is killing another pawn en passant
							System.out.println(String.format("Killing en passant at %s", new Coordinate(end.getCol(), start.getRow())));
							killed = killPiece(new Coordinate(end.getCol(), start.getRow()));
							System.out.println(String.format("Killed: %s", killed));
						}
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

	//Returns true if something was killed, false otherwise
	private boolean killPiece(Coordinate position){
		int index = -1;
		for(Piece p : pieces){
			if(p.getPosition().equals(position)){
				index = pieces.indexOf(p);
				break;
			}
		}
		if(index != -1){
			pieces.remove(index);
			return true;
		}
		return false;
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
		initializeInstance();
	}

    public String getGameState()
    {
        char[] state = new char[64];

        for(int i = 0; i < state.length; i++)
        {
            state[i] = '0';
        }

        for(Piece p : pieces)
        {
            Coordinate cor = p.getPosition();

            if(p.getPlayer() == Player.PLAYER1)
            {
                switch (p.getImage())
                {
                    case R.drawable.chessplt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '1';
                        break;
                    case R.drawable.chessrlt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '3';
                        break;
                    case R.drawable.chessnlt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '5';
                        break;
                    case R.drawable.chessblt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '7';
                        break;
                    case R.drawable.chessklt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '9';
                        break;
                    case R.drawable.chessqlt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = 'b';
                        break;
                }
            }
            else
            {
                switch (p.getImage())
                {
                    case R.drawable.chesspdt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '2';
                        break;
                    case R.drawable.chessrdt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '4';
                        break;
                    case R.drawable.chessndt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '6';
                        break;
                    case R.drawable.chessbdt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = '8';
                        break;
                    case R.drawable.chesskdt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = 'a';
                        break;
                    case R.drawable.chessqdt60: state[(cor.getCol() + ((cor.getRow() -1) * 8)) - 1] = 'c';
                        break;
                }
            }
        }
        return String.valueOf(state);
    }
}
