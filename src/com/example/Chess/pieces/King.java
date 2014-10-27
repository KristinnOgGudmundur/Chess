package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class King extends Piece {

	public King(Player player, Coordinate position){
		super(player, position);
	}

	@Override
	public String getString() {
		return "K";
	}

	@Override
	public List<MoveOption> getMoveOptions(){
		Coordinate pos = this.position;
		List<MoveOption> returnValue = new ArrayList<MoveOption>();

		for(int x = pos.getCol() - 1; x <= pos.getCol() + 1; x++){
			for(int y = pos.getRow() - 1; y <= pos.getRow() + 1; y++){
				if(x != pos.getCol() || y != pos.getRow()){
					Coordinate currentPosition = new Coordinate(x, y);
					MoveStatus temp = getMoveStatusAt(currentPosition, this.player);
					if(temp != null){
						if(temp == MoveStatus.CANKILL){
							Piece otherPiece = gameState.getPiece(currentPosition);
							if(!otherPiece.isProtected()){
								returnValue.add(new MoveOption(currentPosition, temp));
							}
						}
						else {
							returnValue.add(new MoveOption(currentPosition, temp));
						}
					}
				}
			}
		}

		if(!this.hasMoved)
        {
            boolean blocked = false;

            for(int i = 1; i < 4; i++)
            {
                Coordinate currentPosition = new Coordinate(pos.getCol() + i, pos.getRow());
                Piece otherPiece = gameState.getPiece(currentPosition);

                if(otherPiece != null)
                {

                    if (otherPiece.getString() != "R") {
                        blocked = true;
                    }

                    if (otherPiece.getString() == "R" && !(otherPiece.hasMoved) && (!blocked)) {
                        Coordinate castlePosition = new Coordinate(currentPosition.getCol() - 1, currentPosition.getRow());
                        returnValue.add(new MoveOption(castlePosition, MoveStatus.CANCASTLE));
                    }
                }
                else if(gameState.getHotZones(this.player).contains(currentPosition))
                {
                    blocked = true;
                }

            }

            blocked = false;

            for(int i = 1; i < 5; i++)
            {
                Coordinate currentPosition = new Coordinate(pos.getCol() - i, pos.getRow());
                Piece otherPiece = gameState.getPiece(currentPosition);

                if(otherPiece != null)
                {
                    if (otherPiece.getString() != "R") {
                        blocked = true;
                    }

                    if (otherPiece.getString() == "R" && !(otherPiece.hasMoved) && (!blocked)) {
                        Coordinate castlePosition = new Coordinate(currentPosition.getCol() + 2, currentPosition.getRow());
                        returnValue.add(new MoveOption(castlePosition, MoveStatus.CANCASTLE));
                    }
                }
                else if(gameState.getHotZones(this.player).contains(currentPosition))
                {
                    blocked = true;
                }
            }

        }

		return returnValue;
	}

    @Override
    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }
}
