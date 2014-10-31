package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Knight extends Piece {

	public Knight(Player player, Coordinate position, int image){
		super(player, position, image);
	}
	@Override
	public List<MoveOption> getMoveOptions(){
		List<MoveOption> returnValue = new ArrayList<MoveOption>();
		int x = this.position.getCol();
		int y = this.position.getRow();

		Coordinate tempCoord1 = new Coordinate(x + 2, y + 1);
		MoveStatus tempStatus1 = getMoveStatusAt(tempCoord1, this.player);
		if(tempStatus1!= null){
			returnValue.add(new MoveOption(tempCoord1, tempStatus1));
		}
		Coordinate tempCoord2 = new Coordinate(x + 2, y - 1);
		MoveStatus tempStatus2 = getMoveStatusAt(tempCoord2, this.player);
		if(tempStatus2!= null){
			returnValue.add(new MoveOption(tempCoord2, tempStatus2));
		}
		Coordinate tempCoord3 = new Coordinate(x - 2, y + 1);
		MoveStatus tempStatus3 = getMoveStatusAt(tempCoord3, this.player);
		if(tempStatus3!= null){
			returnValue.add(new MoveOption(tempCoord3, tempStatus3));
		}
		Coordinate tempCoord4 = new Coordinate(x - 2, y - 1);
		MoveStatus tempStatus4 = getMoveStatusAt(tempCoord4, this.player);
		if(tempStatus4!= null){
			returnValue.add(new MoveOption(tempCoord4, tempStatus4));
		}


		Coordinate tempCoord5 = new Coordinate(x + 1, y + 2);
		MoveStatus tempStatus5 = getMoveStatusAt(tempCoord5, this.player);
		if(tempStatus5!= null){
			returnValue.add(new MoveOption(tempCoord5, tempStatus5));
		}
		Coordinate tempCoord6 = new Coordinate(x + 1, y - 2);
		MoveStatus tempStatus6 = getMoveStatusAt(tempCoord6, this.player);
		if(tempStatus6!= null){
			returnValue.add(new MoveOption(tempCoord6, tempStatus6));
		}
		Coordinate tempCoord7 = new Coordinate(x - 1, y + 2);
		MoveStatus tempStatus7 = getMoveStatusAt(tempCoord7, this.player);
		if(tempStatus7!= null){
			returnValue.add(new MoveOption(tempCoord7, tempStatus7));
		}
		Coordinate tempCoord8 = new Coordinate(x - 1, y - 2);
		MoveStatus tempStatus8 = getMoveStatusAt(tempCoord8, this.player);
		if(tempStatus8!= null){
			returnValue.add(new MoveOption(tempCoord8, tempStatus8));
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
    }

	@Override
	public String getString() {
		return "C";
	}
}
