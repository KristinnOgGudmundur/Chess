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
    protected static int whiteImage = 11;
    public static int blackImage = 12;

    @Override
    public List<MoveOption> getMoveOptions()
    {
        List<MoveOption> returnValue = new ArrayList<MoveOption>();

        returnValue.addAll(check(1, 0));
        returnValue.addAll(check(0, 1));
        returnValue.addAll(check(-1, 0));
        returnValue.addAll(check(0, -1));

        return returnValue;
    }

    private List<MoveOption> check(int colAdd, int rowAdd)
    {
        List<MoveOption> returnValue = new ArrayList<MoveOption>();

        boolean cont = true;
        int iterations = 1;
        do {
            Coordinate tempCoordinate = new Coordinate(this.position.getCol() - (colAdd * iterations), this.position.getRow() - (rowAdd * iterations));
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
            else
            {
                cont = false;
            }

            iterations++;

        }while (cont);

        return returnValue;
    }

    @Override
    public int getWhiteImage(){
        return whiteImage;
    }

    @Override
    public int getBlackImage(){
        return blackImage;
    }

	@Override
	public String getString() {
		return "R";
	}

    @Override
    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }
}