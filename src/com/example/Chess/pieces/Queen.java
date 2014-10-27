package com.example.Chess.pieces;

import com.example.Chess.objects.Coordinate;
import com.example.Chess.objects.MoveOption;
import com.example.Chess.objects.MoveStatus;
import com.example.Chess.objects.Player;

import java.util.ArrayList;
import java.util.List;


public class Queen extends Piece {

	public Queen(Player player, Coordinate position){
		super(player, position);
	}
    protected static int whiteImage = 9;
    public static int blackImage = 10;

    public List<MoveOption> getMoveOptions()
    {
        List<MoveOption> returnValue = new ArrayList<MoveOption>();

        returnValue.addAll(checkStraight(1, 0));
        returnValue.addAll(checkStraight(0, 1));
        returnValue.addAll(checkStraight(-1, 0));
        returnValue.addAll(checkStraight(0, -1));
        returnValue.addAll(checkDiagonal(-1, -1));
        returnValue.addAll(checkDiagonal(-1, 1));
        returnValue.addAll(checkDiagonal(1, -1));
        returnValue.addAll(checkDiagonal(1, 1));

        return returnValue;
    }

    private List<MoveOption> checkStraight(int colAdd, int rowAdd)
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

    private List<MoveOption> checkDiagonal(int xDif, int yDif){
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
    public int getWhiteImage(){
        return whiteImage;
    }

    @Override
    public int getBlackImage(){
        return blackImage;
    }

	@Override
	public String getString() {
		return "Q";
	}

    @Override
    public void setHasMoved(boolean hasMoved)
    {

    }
}