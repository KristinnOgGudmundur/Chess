package com.example.Chess.objects;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public enum GameStatus {
	TURN_PLAYER1,		//It's player 1's turn
	TURN_PLAYER2,		//It's player 2's turn
	VICTORY_PLAYER1,	//Player 1 has won
	VICTORY_PLAYER2,	//Player 2 has won
	STALEMATE,			//The game has ended because the active player can't move
	CHECK_PLAYER1,		//It's player 1's turn and his/her king is checked
	CHECK_PLAYER2		//It's player 2's turn and his/her king is checked
}