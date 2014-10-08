package com.example.Chess.objects;


import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Board extends View {
	//region Properties
	private int NUM_CELLS = 8;
	private int m_cellSize;
	private int m_numberPadding;
	private int m_numberPaddingFactor = 20;
	private Paint m_paintGrid = new Paint();

	//endregion Properties

	public Board(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(0);
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
		m_numberPadding = (size - 2 * largestPadding) / m_numberPaddingFactor;
		m_cellSize = (size - 2 * largestPadding - 2 * m_numberPadding) / NUM_CELLS;
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
			System.out.println(c);
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

		return new Coordinate(c, r);
	}
}
