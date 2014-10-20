package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Rook extends Piece {

	public Rook(Player player, Coordinate position){
		super(player, position);
	}
/*
    @Override
    public List<MoveOption> getMoveOptions()
    {
        Coordinate pos = this.position;
        List<MoveOption> returnValue = new ArrayList<MoveOption>();

        returnValue.addAll(check(1, 0, pos));
        returnValue.addAll(check(0, 1, pos));
        returnValue.addAll(check(-1, 0, pos));
        returnValue.addAll(check(0, -1, pos));

        return returnValue;
    }

    private List<MoveOption> check(int colAdd, int rowAdd, Coordinate pos)
    {
        List<MoveOption> returnValue = new ArrayList<MoveOption>();
        boolean blocked = false;
        int myColAdd = colAdd;
        int myRowAdd = rowAdd;

        for(int i = 0; i < 8; i++) {
            myColAdd += colAdd;
            myRowAdd += rowAdd;

            Coordinate currentPosition = new Coordinate(pos.getCol() + myColAdd, pos.getRow() + myRowAdd);
            Piece otherPiece = gameState.getPiece(currentPosition);
            MoveStatus temp = getMoveStatusAt(currentPosition, this.player);

            if (otherPiece == null && temp != null)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.CANMOVE));
            }
            else if (otherPiece.getPlayer() == this.player && temp != null)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.PROTECTS));
                break;
            }
            else if (otherPiece.getPlayer() != this.player && temp != null)
            {
                returnValue.add(new MoveOption(currentPosition, MoveStatus.CANKILL));
                break;
            }
        }

        return returnValue;
    }
*/
	@Override
	public String getString() {
		return "R";
	}
}