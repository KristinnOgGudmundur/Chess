package com.example.Chess.objects;


import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.Chess.pieces.Piece;

public class Board extends View {
	//region Properties
	private int NUM_CELLS = 8;
	private int m_cellSize;
	private int m_numberPadding;
	private int m_numberPaddingFactor = 20;
	private int m_highlightFactor = 8;
	private Paint m_paintGrid = new Paint();
	private Rect drawRect = new Rect();
	private Paint m_paintPieces = new Paint();
	private Paint m_paintHighlightCell = new Paint();
	private int minSizeForNumbers = 800;
	private GameState gameState;


	//endregion Properties

	public Board(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(0);
		gameState = GameState.getInstance();

		m_paintPieces.setStyle(Paint.Style.FILL);
		m_paintPieces.setStrokeWidth(4);
		m_paintPieces.setTextAlign(Paint.Align.CENTER);
	}


	@Override
	protected void onDraw(Canvas canvas){
		System.out.println("onDraw");
		CellBounds temp;

		//Draw the squares
		for(int x = 1; x < NUM_CELLS + 1; x++){
			for(int y = 1; y < NUM_CELLS + 1; y++){
				temp = getCellBounds(new Coordinate(x, y));

				drawRect.set((int)temp.getLeft(),(int)temp.getTop(),(int)temp.getRight(),(int)temp.getBottom());
				if((((x & 1) ^ 1) ^ (y & 1)) != 1){
					m_paintGrid.setColor(Color.WHITE);
				}
				else{
					m_paintGrid.setColor(Color.BLACK);
				}
				canvas.drawRect(drawRect, m_paintGrid);
			}
		}


		//Draw the pieces
		for(Piece p : gameState.getPieces()){
			if(p.getPlayer() == Player.PLAYER1){
				m_paintPieces.setColor(Color.BLUE);
			}
			else{
				m_paintPieces.setColor(Color.RED);
			}

			Coordinate pos = p.getPosition();
			CellBounds bounds = getCellBounds(pos);
			canvas.drawText(p.getString(), bounds.getLeft() + m_cellSize * 0.5f, bounds.getBottom() - m_cellSize * 0.4f, m_paintPieces);
		}


		//Draw the edge of the board for clarity
		//If there is enough space
	}


	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );
		System.out.println("onMeasure");
		int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
		int size = Math.min(width, height);
		setMeasuredDimension( size + getPaddingLeft() + getPaddingRight(),
				size + getPaddingTop() + getPaddingBottom() );

		System.out.println("Size: " + size);
	}


	@Override
	protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
		System.out.println("onSizeChanged");
		int size = Math.min(xNew, yNew);
		int largestPadding = Math.max(Math.max(getPaddingBottom(), getPaddingTop()), Math.max(getPaddingLeft(), getPaddingRight()));
		if((size - 2 * largestPadding) >= minSizeForNumbers) {
			m_numberPadding = (size - 2 * largestPadding) / m_numberPaddingFactor;
			System.out.println("Large: " + (size - 2 * largestPadding));
		}
		else{
			m_numberPadding = 0;
			System.out.println("Small: " + (size - 2 * largestPadding));
		}

		m_cellSize = (size - 2 * largestPadding - 2 * m_numberPadding) / NUM_CELLS;
		m_paintPieces.setTextSize(m_cellSize * 0.5f);
		m_paintHighlightCell.setStrokeWidth(m_cellSize / m_highlightFactor);
		System.out.println("CellSize: " + m_cellSize + ", NumberPadding: " + m_numberPadding);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event){
		//System.out.println(event.getAction());
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			pressed(event);
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			heldDown(event);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			released(event);
		}
		return true;
	}


	/**
	 * Run when the finger touches the screen
	 * @param event
	 */
	private void pressed(MotionEvent event){
		int x = (int) event.getX();

		int y = (int) event.getY();

		System.out.println("x: " + x + ", y: " + y);
		Coordinate c = getCoordinate(x, y);
		if (c != null) {
			Piece thePiece = gameState.getPiece(c);
			if(thePiece != null) {
				System.out.println(thePiece);
			}
		}

		System.out.println(getCellBounds(c));
	}

	/**
	 * Run while the finger is held down
	 * @param event
	 */
	private void heldDown(MotionEvent event){

	}

	/**
	 * Run when the finger is released
	 * @param event
	 */
	private void released(MotionEvent event){

	}


	private Coordinate getCoordinate(int x, int y){
		int boardRange = getHeight() - 2 * m_numberPadding;

		int c = x - m_numberPadding;
		int r = y - m_numberPadding;

		if(c < 0 || c > boardRange || r < 0 || r > boardRange){
			return null;
		}

		c = c / m_cellSize;
		r = r / m_cellSize;

		return new Coordinate(c + 1, 8 - r);
	}


	private CellBounds getCellBounds(Coordinate c){
		return new CellBounds(c, m_numberPadding, m_cellSize);
	}

	private void highlightCell(Canvas canvas, Coordinate cell, int color){
		CellBounds theBounds = getCellBounds(cell);

		m_paintHighlightCell.setColor(color);
		m_paintHighlightCell.setStyle(Paint.Style.STROKE);
		Path thePath = new Path();
		float offset = m_paintHighlightCell.getStrokeWidth() / 2;
		thePath.moveTo(theBounds.getLeft() + offset, theBounds.getBottom() - offset);
		thePath.lineTo(theBounds.getLeft() + offset, theBounds.getTop() + offset);
		thePath.lineTo(theBounds.getRight() - offset, theBounds.getTop() + offset);
		thePath.lineTo(theBounds.getRight() - offset, theBounds.getBottom() - offset);
		thePath.lineTo(theBounds.getLeft(), theBounds.getBottom() - offset);
		canvas.drawPath(thePath, m_paintHighlightCell);
	}


	private void fillCell(Canvas canvas, Coordinate cell, int color){
		CellBounds theBounds = getCellBounds(cell);

		m_paintHighlightCell.setColor(color);
		m_paintHighlightCell.setStyle(Paint.Style.FILL);
		Path thePath = new Path();
		thePath.moveTo(theBounds.getLeft(), theBounds.getBottom());
		thePath.lineTo(theBounds.getLeft(), theBounds.getTop());
		thePath.lineTo(theBounds.getRight(), theBounds.getTop());
		thePath.lineTo(theBounds.getRight(), theBounds.getBottom());
		thePath.lineTo(theBounds.getLeft(), theBounds.getBottom());
		canvas.drawPath(thePath, m_paintHighlightCell);
	}
}
