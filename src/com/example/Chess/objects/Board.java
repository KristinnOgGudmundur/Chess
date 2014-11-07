package com.example.Chess.objects;


import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.example.Chess.R;
import com.example.Chess.activities.PlayActivity;
import com.example.Chess.ch.ChessMove;
import com.example.Chess.ch.ChessState;
import com.example.Chess.pieces.Piece;
import game.Move;

import java.util.ArrayList;
import java.util.List;

public class Board extends View {
	//region Properties
	//region Preferences
	int soundVolume = 0;
	boolean useVibrations = false;
	LineNumberOption useLineNumbers = LineNumberOption.IF_BIG_ENOUGH;
	//endregion Preferences

	//region Drawing variables
    private boolean finished = false;
	private int NUM_CELLS = 8;
	private int m_cellSize;
	private int m_numberPadding;
	private int m_numberPaddingFactor = 20;
	private int m_highlightFactor = 8;
	private Paint m_paintGrid = new Paint();
	private Rect drawRect = new Rect();
	private Paint m_paintLineNumbers = new Paint();
	private Paint m_paintPieces = new Paint();
	private Paint m_paintHighlightCell = new Paint();
	private int minSizeForNumbers = 800;
    private Paint mypaint;
	//endregion Drawing variables

	//region Game logic variables
	private ChessState chessState;
	private Coordinate currentPieceCoordinate = null;
	//private List<MoveOption> currentMoveOptions = new ArrayList<MoveOption>();
	private Coordinate lastMoveStart = null;
	private Coordinate lastMoveEnd = null;
	//endregion Game logic variables


	//endregion Properties

	public Board(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(0);
		chessState = new ChessState();

		m_paintPieces.setStyle(Paint.Style.FILL);
		m_paintPieces.setStrokeWidth(4);
		m_paintPieces.setTextAlign(Paint.Align.CENTER);

		m_paintLineNumbers.setStyle(Paint.Style.FILL);
		m_paintLineNumbers.setStrokeWidth(4);
		m_paintPieces.setTextAlign(Paint.Align.CENTER);
		m_paintLineNumbers.setColor(Color.GRAY);
	}

	public void setPreferences(int soundVolume, boolean vibrations, LineNumberOption lineNumbers){
		this.soundVolume = soundVolume;
		this.useVibrations = vibrations;
		this.useLineNumbers = lineNumbers;
	}


	@Override
	protected void onDraw(Canvas canvas){
		CellBounds temp;

        this.mypaint=new Paint();
        mypaint.setColor(Color.RED);
        mypaint.setAntiAlias(true);
        mypaint.setFilterBitmap(true);
        mypaint.setDither(true);

        Bitmap backBitmap;
        backBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.plank);
        Bitmap backScaledBitmap = Bitmap.createScaledBitmap(backBitmap, m_cellSize * 8, m_cellSize * 8, true);
        canvas.drawBitmap(backScaledBitmap, m_numberPadding, m_numberPadding, mypaint);


		//Draw the squares
		for(int x = 1; x < NUM_CELLS + 1; x++){
			for(int y = 1; y < NUM_CELLS + 1; y++){
				temp = getCellBounds(new Coordinate(x, y));

				drawRect.set((int)temp.getLeft(),(int)temp.getTop(),(int)temp.getRight(),(int)temp.getBottom());
				if((((x & 1) ^ 1) ^ (y & 1)) != 1){
					m_paintGrid.setColor(Color.parseColor("#80efebe8"));
				}
				else{
					m_paintGrid.setColor(Color.parseColor("#80b8c1c0"));
				}
				canvas.drawRect(drawRect, m_paintGrid);
			}
		}

        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#423f3b"));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10);
        canvas.drawRect(-2 + m_numberPadding, -2 + m_numberPadding, m_cellSize * 8 + 2 + m_numberPadding, m_cellSize * 8 + 2 + m_numberPadding, borderPaint);

		//Draw the edge of the board for clarity
		//If there is enough space
		//Draw the left numbers
		for(int i = 0; i < 8; i++){
			canvas.drawText(String.format("%s", 8 - i), m_numberPadding * 0.25f, m_numberPadding + m_paintLineNumbers.getTextSize() * 0.25f + m_cellSize * (i + 0.5f), m_paintLineNumbers);
		}

		//Draw the right numbers
		for(int i = 0; i < 8; i++){
			canvas.drawText(String.format("%s", 8 - i), m_cellSize * 8 + m_numberPadding * 1.25f, m_numberPadding + m_paintLineNumbers.getTextSize() * 0.25f + m_cellSize * (i + 0.5f), m_paintLineNumbers);
		}

		//Draw the top letters
		for(int i = 0; i < 8; i++){
			canvas.drawText(numberToChar(i + 1), m_numberPadding + m_cellSize * (i + 0.5f) - m_paintLineNumbers.getTextSize() * 0.5f, m_numberPadding * 0.75f, m_paintLineNumbers);
		}


		//Draw the bottom letters
		for(int i = 0; i < 8; i++){
			canvas.drawText(numberToChar(i + 1), m_numberPadding + m_cellSize * (i + 0.5f) - m_paintLineNumbers.getTextSize() * 0.5f, canvas.getHeight() - m_paintLineNumbers.getTextSize() * 0.25f, m_paintLineNumbers);
		}

		if(this.lastMoveStart != null){
			highlightCell(canvas, lastMoveStart, Color.parseColor("#bdb29d"));
			highlightCell(canvas, lastMoveEnd, Color.parseColor("#bdb29d"));
		}

		//Draw highlights
		if(currentPieceCoordinate != null){
			for(Move aMove : chessState.getActions()){
			//for(MoveOption m : currentMoveOptions){
				ChessMove theMove = (ChessMove)aMove;
				MoveOption m = null;

				//TODO: Figure out how to get the start position from theMove
				if(chessState.sqrToStr(theMove.getFrom()).equals(currentPieceCoordinate)){
					m = new MoveOption(theMove);
				}

				if(m != null) {
					//Set the color
					switch (m.moveStatus) {
						case CANMOVE:
							fillCell(canvas, m.coordinate, Color.parseColor("#B31d1f63"));
							break;
						case CANCASTLE:
							fillCell(canvas, m.coordinate, Color.parseColor("#B3094809"));
							break;
						case CANKILL:
							highlightCell(canvas, m.coordinate, Color.parseColor("#B37e0404"));
							break;
						case PROTECTS:
							highlightCell(canvas, m.coordinate, Color.parseColor("#B3094809"));
							break;
					}

					//Highlight the cell
					switch (m.moveStatus) {
						case CANMOVE:
						case CANCASTLE:

							break;
						case CANKILL:
						case PROTECTS:

							break;
					}
				}
			}
		}



		// Draw the pieces
		//for(Piece p : gameState.getPieces()){
		game.Board theBoard = chessState.getBoard();
		for(int i1 = 0; i1 < 8; i1++) {
			for(int i2 = 0; i2 < 8; i2++){
				game.Piece p = theBoard.get(i1, i2);
				if(p != null) {
					System.out.println("Player: " + p.getPlayer());
					if (p.getPlayer() == 0) {
						m_paintPieces.setColor(Color.BLUE);
					} else {
						m_paintPieces.setColor(Color.RED);
					}

					Coordinate pos = new Coordinate(i1 + 1, i2 + 1);
					CellBounds bounds = getCellBounds(pos);

					this.mypaint = new Paint();
					mypaint.setColor(Color.RED);
					mypaint.setAntiAlias(true);
					mypaint.setFilterBitmap(true);
					mypaint.setDither(true);

					Bitmap bitmap;
					bitmap = BitmapFactory.decodeResource(getResources(), getImage(p));
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, m_cellSize, m_cellSize, true);

					canvas.drawBitmap(scaledBitmap, bounds.getLeft() - m_cellSize * 0.01f, bounds.getBottom() - m_cellSize, mypaint);

					//canvas.drawText(p.getString(), bounds.getLeft() + m_cellSize * 0.5f, bounds.getBottom() - m_cellSize * 0.4f, m_paintPieces);
				}
			}
		}
	}


	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );
		int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
		int size = Math.min(width, height);
		setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());

	}


	@Override
	protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
		int size = Math.min(xNew, yNew);
		int largestPadding = Math.max(Math.max(getPaddingBottom(), getPaddingTop()), Math.max(getPaddingLeft(), getPaddingRight()));
		if(this.useLineNumbers == LineNumberOption.YES ||
			(this.useLineNumbers == LineNumberOption.IF_BIG_ENOUGH && (size - 2 * largestPadding) >= minSizeForNumbers)) {
			m_numberPadding = (size - 2 * largestPadding) / m_numberPaddingFactor;
		}
		else{
			m_numberPadding = 0;
		}

		m_cellSize = (size - 2 * largestPadding - 2 * m_numberPadding) / NUM_CELLS;
		m_paintPieces.setTextSize(m_cellSize * 0.5f);
		m_paintHighlightCell.setStrokeWidth(m_cellSize / m_highlightFactor);
		m_paintLineNumbers.setTextSize(m_numberPadding * 0.75f);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event){
        if(!finished)
        {
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
        return true;
	}


	/**
	 * Run when the finger touches the screen
	 * @param event
	 */
	private void pressed(MotionEvent event){
		int x = (int) event.getX();

		int y = (int) event.getY();

		Coordinate c = getCoordinate(x, y);
		System.out.println("Pressed: " + c);
		if (c != null) {
			Coordinate oldPosition = this.currentPieceCoordinate;
			int oldPlayerToMove = chessState.getPlayerToMove();

			//TODO: Figure out how moves are created
			if(oldPosition != null){
				String moveString = oldPosition.toString() + c.toString();
				//d1d2
				game.Move theMove = chessState.strToMove(moveString);
				if(theMove != null) {
					chessState.make(theMove, null);
				}
				if(oldPlayerToMove != chessState.getPlayerToMove()){
					//A move was made
					this.lastMoveStart = oldPosition;
					this.lastMoveEnd = c;
					PlayActivity activity = (PlayActivity)getContext();
					activity.newTurn();

					currentPieceCoordinate = null;
				}
				else{
					//No move was made
					currentPieceCoordinate = c;
				}
			}
			else{
				currentPieceCoordinate = c;
			}
		}
		invalidate();
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

	private String numberToChar(int number){
		switch(number){
			case(1):
				return "A";
			case(2):
				return "B";
			case(3):
				return "C";
			case(4):
				return "D";
			case(5):
				return "E";
			case(6):
				return "F";
			case(7):
				return "G";
			case(8):
				return "H";
			default:
				throw new RuntimeException("Illegal coordinate");
		}
	}

	public void reset(){
		this.currentPieceCoordinate = null;
		this.chessState.reset();
		invalidate();
	}

    public void finished()
    {
        finished = true;
    }

    public boolean getFinished(){ return this.finished;}

	public int getImage(game.Piece thePiece){
		//TODO: Implement
		//White
		if(thePiece.getPlayer() == 0){
			switch(ChessState.Piecetype.values()[thePiece.getType()]){
				case King:
					return R.drawable.chessklt60;
				case Queen:
					return R.drawable.chessqlt60;
				case Rook:
					return R.drawable.chessrlt60;
				case Knight:
					return R.drawable.chessnlt60;
				case Bishop:
					return R.drawable.chessblt60;
				case Pawn:
					return R.drawable.chessplt60;
			}
		}
		//Black
		else{
			switch(ChessState.Piecetype.values()[thePiece.getType()]){
				case King:
					return R.drawable.chesskdt60;
				case Queen:
					return R.drawable.chessqdt60;
				case Rook:
					return R.drawable.chessrdt60;
				case Knight:
					return R.drawable.chessndt60;
				case Bishop:
					return R.drawable.chessbdt60;
				case Pawn:
					return R.drawable.chesspdt60;
			}
		}
		return R.drawable.ic_launcher;
	}

}
