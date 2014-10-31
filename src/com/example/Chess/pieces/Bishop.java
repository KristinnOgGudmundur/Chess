package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Bishop extends Piece {

	public Bishop(Player player, Coordinate position, int image){
		super(player, position, image);
	}

	@Override
	public List<MoveOption> getMoveOptions(){
		List<MoveOption> returnValue = new ArrayList<MoveOption>();

		returnValue.addAll(checkLine(-1, -1));
		returnValue.addAll(checkLine(-1, 1));
		returnValue.addAll(checkLine(1, -1));
		returnValue.addAll(checkLine(1, 1));

		return returnValue;
	}

	private List<MoveOption> checkLine(int xDif, int yDif){
		List<MoveOption> returnValue = new ArrayList<MoveOption>();

		boolean cont = true;
		int iterations = 1;
		do{
			Coordinate tempCoordinate = new Coordinate(this.position.getCol() - (xDif * iterations), this.position.getRow() - (yDif * iterations));
			MoveStatus temp = getMoveStatusAt(tempCoordinate, this.player);
			if(temp != null){
				switch(temp){
					case CANMOVE:
						returnValue.add(new MoveOption(tempCoordinate, temp));
						break;
					case CANKILL:
						returnValue.add(new MoveOption(tempCoordinate, temp));
						cont = false;
						break;
					case PROTECTS:
						returnValue.add(new MoveOption(tempCoordinate, temp));
						cont = false;
						break;
				}
			}
			else{
				cont = false;
			}
			iterations++;
		} while(cont);


		return returnValue;
	}

	@Override
	public String getString() {
		return "B";
	}

    @Override
    public int getImage(){
        return image;
    }

    @Override
    public void setHasMoved(boolean hasMoved)
    {
    }
}