package com.example.Chess.objects;

/**
 * Created by Gvendur Stefáns on 17.10.2014.
 */
public class CellBounds {
	private float Left;
	private float Right;
	private float Top;
	private float Bottom;

	public CellBounds(Coordinate c, float padding, float cellSize){
		Left = padding + (cellSize * (c.getCol() - 1));
		Right = padding + (cellSize * c.getCol());
		Top = padding + (cellSize * (8 - c.getRow()));
		Bottom = padding + (cellSize * (9 - c.getRow()));;
	}

	public float getLeft(){
		return Left;
	}

	public float getRight(){
		return Right;
	}

	public float getTop(){
		return Top;
	}

	public float getBottom(){
		return Bottom;
	}

	@Override
	public String toString(){
		return String.format("Left: %s. Right: %s. Bottom: %s. Top: %s", Left, Right, Bottom, Top);
	}
}
