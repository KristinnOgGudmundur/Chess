package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {

	public Pawn(Player player, Coordinate position){
		super(player, position);
	}

    protected boolean MovedTwoSpacesLastTurn;
    @Override
    public List<MoveOption> getMoveOptions()
    {
        Coordinate pos = this.position;
        List<MoveOption> returnValue = new ArrayList<MoveOption>();

    //CheckMoveForward
    //------------------------------------------------------------------------------------------------------------------

        int limit;

        if(!this.hasMoved)
        {
             limit = 2;
        }
        else{limit = 3;}

        for(int i = 1; i <= limit; i++)
        {
            Coordinate currentPosition = new Coordinate(pos.getCol(), pos.getRow() + i);
            Piece otherPiece = gameState.getPiece(currentPosition);
            MoveStatus temp = getMoveStatusAt(currentPosition, this.player);

            if(otherPiece == null && temp != null)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.CANMOVE));
            }
            else{ break; }

    //CheckAttackDiagonal
    //-----------------------------------------------------------------------------------------------------------------

        }
        for(int i = -1; i < 2; i = i + 2)
        {
            Coordinate currentPosition = new Coordinate(pos.getCol() + i, pos.getRow() + 1);
            Piece otherPiece = gameState.getPiece(currentPosition);
            MoveStatus temp = getMoveStatusAt(currentPosition, this.player);

            if(otherPiece != null && temp != null)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.CANKILL));
            }
        }

    //TODO: add framhjáhlaup
    //------------------------------------------------------------------------------------------------------------------

        return returnValue;
    }

	@Override
	public String getString() {
		return "P";
	}
}