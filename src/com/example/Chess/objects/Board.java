package com.example.Chess.objects;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	private Paint m_paintGrid = new Paint();
	private Rect drawRect = new Rect();
	private Paint m_paintPieces = new Paint();
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
		int left;
		int right;
		int top;
		int bottom;
		//Draw the squares
		for(int x = 0; x < NUM_CELLS; x++){
			for(int y = 0; y < NUM_CELLS; y++){
				left = (int)getCellMin(x);
				top = (int)getCellMin(y);
				right = (int)getCellMax(x);
				bottom = (int)getCellMax(y);
				drawRect.set(left, top, right, bottom);
				if((((x & 1) ^ 1) ^ (y & 1)) == 1){
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
			canvas.drawText(p.getString(), getCellMin(pos.getCol()) - (m_cellSize * 0.5f), getCellMin(pos.getRow()) - (m_cellSize * 0.25f), m_paintPieces);
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

	private float getCellMin(int column){
		return m_numberPadding + m_cellSize * column;
	}

	private float getCellMax(int row){
		return m_numberPadding + m_cellSize * (row + 1);
	}
}
