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
        boolean blocked = false;

        if(!this.hasMoved)
        {
             limit = 1;
        }
        else{limit = 2;}

        for(int i = 1; i <= limit; i++)
        {
            Coordinate currentPosition = new Coordinate(pos.getRow() + i, pos.getCol() + i);
            Piece otherPiece = gameState.getPiece(currentPosition);

            if(otherPiece == null && !blocked)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.CANMOVE));
            }
            else{ blocked = true; }

    //CheckAttackDiagonal
    //-----------------------------------------------------------------------------------------------------------------

        }
        for(int i = -1; i < 2; i = i + 2)
        {
            Coordinate currentPosition = new Coordinate(pos.getRow() + i, pos.getCol() + 1);
            Piece otherPiece = gameState.getPiece(currentPosition);

            if(otherPiece != null)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.CANKILL));
            }
        }
    }

	@Override
	public String getString() {
		return "P";
	}
}