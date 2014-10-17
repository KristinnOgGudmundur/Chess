package com.example.Chess.objects;

/**
 * Created by Gvendur Stefáns on 8.10.2014.
 */
public enum MoveStatus {
	CANMOVE,	//The piece can move to the position
	CANKILL,	//The piece can kill an enemy piece at the position
	PROTECTS,	//The piece protects the friendly piece at the position
	CANCASTLE	//The piece is a King and can castle to the position
}
