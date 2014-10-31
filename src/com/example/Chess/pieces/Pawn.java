package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {

	public Pawn(Player player, Coordinate position, int image){
		super(player, position, image);
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

        if(this.hasMoved)
        {
             limit = 1;
        }
        else{limit = 2;}
        boolean blocked = false;
        for(int i = 1; i <= limit; i++)
        {
            Coordinate currentPosition = new Coordinate(pos.getCol(),( this.player == Player.PLAYER1 ? pos.getRow() + i : pos.getRow() - i));
            Piece otherPiece = gameState.getPiece(currentPosition);
            MoveStatus temp = getMoveStatusAt(currentPosition, this.player);

            if(otherPiece == null && temp != null)
            {
                if(!blocked)
                {
                    returnValue.add(new MoveOption(currentPosition, MoveStatus.CANMOVE));
                }
            }
            else{ blocked = true; }
        }
    //CheckAttackDiagonal
    //-----------------------------------------------------------------------------------------------------------------


        for(int i = -1; i < 2; i = i + 2)
        {
            Coordinate currentPosition;

            if(this.player == Player.PLAYER1)
            {
                currentPosition = new Coordinate((pos.getCol() + i), (pos.getRow() + 1));
            }
            else
            {
                currentPosition = new Coordinate((pos.getCol() - i), (pos.getRow() - 1));
            }

            Piece otherPiece = gameState.getPiece(currentPosition);
            MoveStatus temp = getMoveStatusAt(currentPosition, this.player);

            if(otherPiece != null && temp != null)
            {
                returnValue.add(new MoveOption(currentPosition, temp));
            }
        }

		//TODO: add en passant movement
		//------------------------------------------------------------------------------------------------------------------
		Piece otherPiece = gameState.getPiece(new Coordinate(this.position.getCol() - 1, this.position.getRow()));
		if(otherPiece != null) {
			System.out.println("OtherPiece not null");
			if (otherPiece instanceof Pawn) {
				System.out.println(String.format("%s. Moved2LastTurn: %s", otherPiece, ((Pawn) otherPiece).MovedTwoSpacesLastTurn));
				if (((Pawn) otherPiece).MovedTwoSpacesLastTurn && otherPiece.getPlayer() != this.getPlayer()) {
					Coordinate theCoordinate = new Coordinate(this.position.getCol() - 1, this.player == Player.PLAYER1 ? pos.getRow() + 1 : pos.getRow() - 1);
					returnValue.add(new MoveOption(theCoordinate, MoveStatus.CANKILL));
					System.out.println(String.format("En passant at %s", theCoordinate));
				}
			}
		}

		otherPiece = gameState.getPiece(new Coordinate(this.position.getCol() + 1, this.position.getRow()));
		if(otherPiece != null) {
			System.out.println("OtherPiece not null");
			if (otherPiece instanceof Pawn) {
				System.out.println(String.format("%s. Moved2LastTurn: %s", otherPiece, ((Pawn) otherPiece).MovedTwoSpacesLastTurn));
				if (((Pawn) otherPiece).MovedTwoSpacesLastTurn && otherPiece.getPlayer() != this.getPlayer()) {
					Coordinate theCoordinate = new Coordinate(this.position.getCol() + 1, this.player == Player.PLAYER1 ? pos.getRow() + 1 : pos.getRow() - 1);
					returnValue.add(new MoveOption(theCoordinate, MoveStatus.CANKILL));
					System.out.println(String.format("En passant at %s", theCoordinate));
				}
			}
		}

        return returnValue;
    }

    @Override
    public int getImage(){
        return image;
    }

    @Override
    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }

	public void setMovedTwoSpacesLastTurn(boolean moved){
		this.MovedTwoSpacesLastTurn = moved;
	}

	@Override
	public String getString() {
		return "P";
	}
}