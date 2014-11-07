package com.example.Chess.objects;

import game.Board;
import game.Move;
import game.Piece;
import game.State;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yngvi
 * Date: 12/25/12
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 *
 * TO DO:
 *   - 50 move rule.
 *   - 3-fold repetition rule.
 *   - terminal conditions.
 */
public class ChessState implements State {

    public enum Player { White, Black }
    public enum Piecetype { Pawn, Knight, Bishop, Rook, Queen, King }
    public enum Square {
        a1, b1, c1, d1, e1, f1, g1, h1,
        a2, b2, c2, d2, e2, f2, g2, h2,
        a3, b3, c3, d3, e3, f3, g3, h3,
        a4, b4, c4, d4, e4, f4, g4, h4,
        a5, b5, c5, d5, e5, f5, g5, h5,
        a6, b6, c6, d6, e6, f6, g6, h6,
        a7, b7, c7, d7, e7, f7, g7, h7,
        a8, b8, c8, d8, e8, f8, g8, h8
    }

    private enum Capturestyle { NoCapture, CanCapture, MustCapture }

    // Static
    private static final String startFEN = "rnbqkbnr/pppppppp/////PPPPPPPP/RNBQKBNR w";
    private static final Map<Character, Piece> chessPieces;
    private static final Map<Piece, Character> chessChFEN;
    static {
		//Maps characters to pieces(ex. P -> a white pawn, n -> a black knight)
        Map<Character, Piece> aMap = new HashMap<Character, Piece>();
        aMap.put( 'P', new Piece( Player.White.ordinal(), Piecetype.Pawn.ordinal() ) );
        aMap.put( 'N', new Piece( Player.White.ordinal(), Piecetype.Knight.ordinal() ) );
        aMap.put( 'B', new Piece( Player.White.ordinal(), Piecetype.Bishop.ordinal() ) );
        aMap.put( 'R', new Piece( Player.White.ordinal(), Piecetype.Rook.ordinal() ) );
        aMap.put( 'Q', new Piece( Player.White.ordinal(), Piecetype.Queen.ordinal() ) );
        aMap.put( 'K', new Piece( Player.White.ordinal(), Piecetype.King.ordinal() ) );
        aMap.put( 'p', new Piece( Player.Black.ordinal(), Piecetype.Pawn.ordinal() ) );
        aMap.put( 'n', new Piece( Player.Black.ordinal(), Piecetype.Knight.ordinal() ) );
        aMap.put( 'b', new Piece( Player.Black.ordinal(), Piecetype.Bishop.ordinal() ) );
        aMap.put( 'r', new Piece( Player.Black.ordinal(), Piecetype.Rook.ordinal() ) );
        aMap.put( 'q', new Piece( Player.Black.ordinal(), Piecetype.Queen.ordinal() ) );
        aMap.put( 'k', new Piece( Player.Black.ordinal(), Piecetype.King.ordinal() ) );
        chessPieces = Collections.unmodifiableMap(aMap);
        Map<Piece, Character> bMap = new HashMap<Piece, Character>();
        for ( Map.Entry<Character,Piece> e : aMap.entrySet() ) {
            bMap.put( e.getValue(), e.getKey() );
        }
        chessChFEN = Collections.unmodifiableMap(bMap);
    }

    // State
    private int       m_toMove;
    private Board     m_board;
    private boolean[] m_canCastleShort;
    private boolean[] m_canCastleLong;
    private ChessMove m_lastMove;
    private int       m_plyCounter50MoveRule;
    private Square    m_ep;

    //
    // Helper functions
    //

	//Checks whether or not given col and row are on the board
    private boolean onBoard( int col, int row ) {
        return ( col >= 0 && col < m_board.cols() && row >= 0 && row < m_board.rows() );
    }


    private boolean shouldAddMove( Capturestyle capture, int player, Piece piece ) {
        switch ( capture ) {
            case NoCapture:
                return piece == null;
            case CanCapture:
                return piece == null || piece.getPlayer() != player;
            case MustCapture:
                return piece != null && piece.getPlayer() != player;
        }
        return false;
    }


	//Checks a move from one square to another, ignoring obstacles
    private void step( int player, int colFrom, int rowFrom, int colDelta, int rowDelta,
                       Capturestyle cap, ChessMove.Movetype movetype, List<Move> moves ) {
        int col = colFrom + colDelta;
        int row = rowFrom + rowDelta;
        if ( onBoard( col, row )  ) {
            Piece pce = m_board.get( col, row );
            if ( shouldAddMove( cap, player, pce ) ) {
                if ( movetype == ChessMove.Movetype.Promotion ) {
                    int sqFrom = m_board.toSqr(colFrom, rowFrom);
                    int sqTo   = m_board.toSqr(col, row);
                    moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Queen.ordinal() ) );
                    moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Rook.ordinal() ) );
                    moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Bishop.ordinal() ) );
                    moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Knight.ordinal() ) );
                }
                else {
                    moves.add( new ChessMove( movetype, m_board.toSqr(colFrom, rowFrom), m_board.toSqr(col, row), pce ) );
                }
            }
        }
    }


	//Checks a move from one square to another, considering obstacles
    private void slide( int player, int colFrom, int rowFrom, int colDelta, int rowDelta, int maxSlide,
                        Capturestyle cap, ChessMove.Movetype movetype, List<Move> moves ) {
        int n = maxSlide;
        for ( int col = colFrom + colDelta, row = rowFrom + rowDelta;
              onBoard( col, row ) && ((maxSlide<=0)||(n>0)); col += colDelta, row += rowDelta, --n ) {
            if ( onBoard( col, row ) ) {
                Piece pce = m_board.get( col, row );
                if ( shouldAddMove( cap, player, pce ) ) {
                    if ( movetype == ChessMove.Movetype.Promotion ) {
                        int sqFrom = m_board.toSqr(colFrom, rowFrom);
                        int sqTo   = m_board.toSqr(col, row);
                        moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Queen.ordinal() ) );
                        moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Rook.ordinal() ) );
                        moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Bishop.ordinal() ) );
                        moves.add( new ChessMove( movetype, sqFrom, sqTo, pce, Piecetype.Knight.ordinal() ) );
                    }
                    else {
                        moves.add( new ChessMove( movetype, m_board.toSqr(colFrom, rowFrom), m_board.toSqr(col, row), pce ) );
                    }
                }
                if ( pce != null ) { break; }
            }
            else { break; }
        }
    }


    private boolean isOnSquare( Player player, Piecetype type, Square sq ) {
        Piece piece = m_board.get( sq.ordinal() );
        return ( piece != null ) && ( piece.getPlayer() == player.ordinal() ) && ( piece.getType() == type.ordinal() );
    }


    //
    // Special types of moves.
    //

    private void enPassantMove( List<Move> moves ) {

        int colEP = m_board.toCol( m_ep.ordinal() );
        int rowEP = m_board.toRow( m_ep.ordinal() );
        Square sq;
        if ( m_toMove == Player.White.ordinal() ) {
            if ( onBoard(colEP-1, rowEP-1) ) {
                sq = Square.values()[m_board.toSqr( colEP-1, rowEP-1 )];
                if ( isOnSquare( Player.White, Piecetype.Pawn, sq ) ) {
                    moves.add( new ChessMove( ChessMove.Movetype.enPassant, sq.ordinal(), m_ep.ordinal()) );
                }
            }
            if ( onBoard(colEP+1, rowEP-1) ) {
                sq = Square.values()[m_board.toSqr( colEP+1, rowEP-1 )];
                if ( isOnSquare( Player.White, Piecetype.Pawn, sq ) ) {
                    moves.add( new ChessMove( ChessMove.Movetype.enPassant, sq.ordinal(), m_ep.ordinal()) );
                }
            }
        }
        else {
            if ( onBoard(colEP-1, rowEP+1) ) {
                sq = Square.values()[m_board.toSqr( colEP-1, rowEP+1 )];
                if ( isOnSquare( Player.Black, Piecetype.Pawn, sq ) ) {
                    moves.add( new ChessMove( ChessMove.Movetype.enPassant, sq.ordinal(), m_ep.ordinal()) );
                }
            }
            if ( onBoard(colEP+1, rowEP+1) ) {
                sq = Square.values()[m_board.toSqr( colEP+1, rowEP+1 )];
                if ( isOnSquare( Player.Black, Piecetype.Pawn, sq ) ) {
                    moves.add( new ChessMove( ChessMove.Movetype.enPassant, sq.ordinal(), m_ep.ordinal()) );
                }
            }
        }
    }


    private void castleMoves( int player, int colOn, int rowOn, List<Move> moves ) {
        if ( m_canCastleShort[ player ] ) {
            if ( m_board.get(colOn+1, rowOn) == null &&
                    m_board.get(colOn+2, rowOn) == null &&
                    !sqrIsAttacked( m_board.toSqr(colOn+1,rowOn), m_toMove ) &&
                    !sqrIsAttacked( m_board.toSqr(colOn+2,rowOn), m_toMove ) ) {
                moves.add( new ChessMove( ChessMove.Movetype.Castling,
                        m_board.toSqr(colOn,rowOn), m_board.toSqr(colOn+2,rowOn)) );
            }
        }
        if ( m_canCastleLong[ player ] ) {
            if ( m_board.get(colOn-1, rowOn) == null &&
                    m_board.get(colOn-2, rowOn) == null &&
                    m_board.get(colOn-3, rowOn) == null &&
                    !sqrIsAttacked( m_board.toSqr(colOn-1,rowOn), m_toMove ) &&
                    !sqrIsAttacked( m_board.toSqr(colOn-2,rowOn), m_toMove ) ) {
                moves.add( new ChessMove( ChessMove.Movetype.Castling,
                        m_board.toSqr(colOn,rowOn), m_board.toSqr(colOn-2,rowOn)) );
            }
        }
    }

    //
    //  Helper functions for moves and attached squares.
    //

    private boolean sqrIsAttacked( int sqr, int player ) {

        ArrayList<Move> moves = new ArrayList<Move>();
        int colOn = m_board.toCol( sqr );
        int rowOn = m_board.toRow( sqr );

        slide(player, colOn, rowOn, +1,  0, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        slide(player, colOn, rowOn, -1,  0, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        slide(player, colOn, rowOn,  0, +1, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        slide(player, colOn, rowOn,  0, -1, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        for ( Move move : moves ) {
            ChessMove cmove = (ChessMove) move;
            Piece pce = m_board.get( cmove.getTo() );
            if ( pce.getType() == Piecetype.Rook.ordinal() ||
                    pce.getType() == Piecetype.Queen.ordinal() ) {
                return true;
            }
        }
        moves.clear();

        slide(player, colOn, rowOn, +1, +1, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        slide(player, colOn, rowOn, +1, -1, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        slide(player, colOn, rowOn, -1, +1, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        slide(player, colOn, rowOn, -1, -1, 0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        for ( Move move : moves ) {
            ChessMove cmove = (ChessMove) move;
            Piece pce = m_board.get( cmove.getTo() );
            if ( pce.getType() == Piecetype.Bishop.ordinal() ||
                    pce.getType() == Piecetype.Queen.ordinal() ) {
                return true;
            }
        }
        moves.clear();

        step(player, colOn, rowOn, +1, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, +1, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -1, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -1, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, +1,  0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -1,  0, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn,  0, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn,  0, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        for ( Move move : moves ) {
            ChessMove cmove = (ChessMove) move;
            Piece pce = m_board.get( cmove.getTo() );
            if ( pce.getType() == Piecetype.King.ordinal() ) {
                return true;
            }
        }
        moves.clear();

        step(player, colOn, rowOn, +1, +2, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, +2, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, +1, -2, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, +2, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -1, +2, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -2, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -1, -2, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        step(player, colOn, rowOn, -2, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves );
        for ( Move move : moves ) {
            ChessMove cmove = (ChessMove) move;
            Piece pce = m_board.get( cmove.getTo() );
            if ( pce.getType() == Piecetype.Knight.ordinal() ) {
                return true;
            }
        }
        moves.clear();

        if ( player == Player.White.ordinal() ) {
            step(player, colOn, rowOn, -1, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves);
            step(player, colOn, rowOn, +1, +1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves);
        }
        else {
            step(player, colOn, rowOn, -1, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves);
            step(player, colOn, rowOn, +1, -1, Capturestyle.MustCapture, ChessMove.Movetype.Normal, moves);
        }
        for ( Move move : moves ) {
            ChessMove cmove = (ChessMove) move;
            Piece pce = m_board.get( cmove.getTo() );
            if ( pce.getType() == Piecetype.Pawn.ordinal() ) {
                return true;
            }
        }
        moves.clear();

        return false;
    }

	//Checks all valid moves for the piece on a coordinate and adds them to moves
    private void moves( int colOn, int rowOn, List<Move> moves ) {
        Piece piece = m_board.get( colOn, rowOn );

        if ( (piece == null) || (piece.getPlayer() != m_toMove) ) { return; }

        int pl = m_toMove;
        switch ( Piecetype.values()[piece.getType()] ) {
            case Pawn:
                if ( Player.values()[piece.getPlayer()] == Player.White ) {
                    ChessMove.Movetype mtype = (rowOn == 6) ? ChessMove.Movetype.Promotion : ChessMove.Movetype.Normal;
                    step( pl, colOn, rowOn, -1, +1, Capturestyle.MustCapture, mtype, moves);
                    slide( pl, colOn, rowOn, 0, +1, (rowOn == 1) ? 2 : 1, Capturestyle.NoCapture, mtype, moves);
                    step( pl, colOn, rowOn, +1, +1, Capturestyle.MustCapture, mtype, moves);
                }
                else {
                    ChessMove.Movetype mtype = (rowOn == 1) ? ChessMove.Movetype.Promotion : ChessMove.Movetype.Normal;
                    step( pl, colOn, rowOn, -1, -1, Capturestyle.MustCapture, mtype, moves );
                    slide( pl, colOn, rowOn, 0, -1, (rowOn==6) ? 2 : 1, Capturestyle.NoCapture, mtype, moves);
                    step( pl, colOn, rowOn, +1, -1, Capturestyle.MustCapture, mtype, moves );
                }
                break;
            case Knight:
                step( pl, colOn, rowOn, +1, +2, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, +2, +1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, +1, -2, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, +2, -1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -1, +2, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -2, +1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -1, -2, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -2, -1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                break;
            case Bishop:
                slide( pl, colOn, rowOn, +1, +1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, +1, -1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, -1, +1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, -1, -1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                break;
            case Rook:
                slide( pl, colOn, rowOn, +1,  0, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, -1,  0, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn,  0, +1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn,  0, -1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                break;
            case Queen:
                slide( pl, colOn, rowOn, +1, +1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, +1, -1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, -1, +1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, -1, -1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, +1,  0, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn, -1,  0, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn,  0, +1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                slide( pl, colOn, rowOn,  0, -1, 0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                break;
            case King:
                step( pl, colOn, rowOn, +1, +1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, +1, -1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -1, +1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -1, -1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, +1,  0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn, -1,  0, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn,  0, +1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                step( pl, colOn, rowOn,  0, -1, Capturestyle.CanCapture, ChessMove.Movetype.Normal, moves );
                castleMoves( pl, colOn,  rowOn, moves );
                break;
        }
    }

    private List<Move> getPseudoLegalMoves() {
        List<Move> moves = new ArrayList<Move>();
        for ( int row = 0; row < m_board.rows(); ++row ) {
            for ( int col = 0; col < m_board.cols(); ++col ) {
                Piece piece = m_board.get( col, row );
                if ( (piece != null) && (piece.getPlayer() == m_toMove) ) {
                    moves(col, row, moves);
                }
            }
        }
        if ( m_ep != null ) {
            enPassantMove( moves );
        }
        return moves;
    }

    //
    //  Public interface.
    //

    public class RetractInfo {
        public boolean[] canCastleShort = { false, false };
        public boolean[] canCastleLong  = { false, false };
        public Square    ep = null;
    }


    public ChessState() {

        List<String> C = new ArrayList<String>();
        C.add( "a" ); C.add( "b" ); C.add( "c" );  C.add( "d" ); C.add( "e" ); C.add( "f" ); C.add( "g" ); C.add( "h" );
        List<String> R = new ArrayList<String>();
        R.add("1"); R.add("2"); R.add("3");  R.add("4"); R.add("5"); R.add("6"); R.add("7"); R.add("8");

        m_toMove = Player.White.ordinal();
        m_board  = new Board( C, R );
        m_canCastleShort = new boolean[] { false, false };
        m_canCastleLong = new boolean[]  { false, false };
        m_lastMove = null;
        m_plyCounter50MoveRule = 0;

        setup( startFEN );
    }

    @Override
    public int getNumberOfPlayers() {
        return 2;
    }

    @Override
    public int getPlayerToMove() {
        return m_toMove;
    }

    @Override
    public Board getBoard() {
        return m_board;
    }

    @Override
    public List<Move> getActions() {
        List<Move> movesPseudo = getPseudoLegalMoves();
        List<Move> moves = new ArrayList<Move>();

        RetractInfo ri = retractInfo();
        int player = getPlayerToMove();
        for ( Move move : movesPseudo ) {
            make( move, ri );
            // Note: find a better way to find king position, maybe in state.
            int sqKing = -1;
            for ( int sq = 0; sq < m_board.size(); ++sq ) {
                Piece piece = m_board.get( sq );
                if ( piece != null && piece.getPlayer() == player && piece.getType() == Piecetype.King.ordinal() ) {
                    sqKing = sq;
                    break;
                }
            }
            if ( !sqrIsAttacked( sqKing, player ) ) {
                moves.add(move);
            }
            retract( move, ri );
        }
        return moves;
    }

    @Override
    public RetractInfo retractInfo( ) {
        RetractInfo ri = new RetractInfo();
        ri.canCastleShort[ Player.White.ordinal() ] = m_canCastleShort[ Player.White.ordinal() ];
        ri.canCastleLong [ Player.White.ordinal() ] = m_canCastleLong [ Player.White.ordinal() ];
        ri.canCastleShort[ Player.Black.ordinal() ] = m_canCastleShort[ Player.Black.ordinal() ];
        ri.canCastleLong [ Player.Black.ordinal() ] = m_canCastleLong [ Player.Black.ordinal() ];
        ri.ep = m_ep;
        return ri;
    }

    @Override
    public void make( Move move, Object ri ) {
        ChessMove cmove = (ChessMove) move;
        Piece piece = m_board.get( cmove.getFrom() );

        if ( m_board.get( cmove.getTo() ) != null ) {
            if ( cmove.getTo() == Square.h1.ordinal() ) {
                m_canCastleShort[ Player.White.ordinal() ] = false;
            }
            else if ( cmove.getTo() == Square.a1.ordinal() ) {
                m_canCastleLong[ Player.White.ordinal() ] = false;
            }
            if ( cmove.getTo() == Square.h8.ordinal() ) {
                m_canCastleShort[ Player.Black.ordinal() ] = false;
            }
            else if ( cmove.getTo() == Square.a8.ordinal() ) {
                m_canCastleLong[ Player.Black.ordinal() ] = false;
            }
        }

        m_board.set( cmove.getTo(), piece );
        m_board.set( cmove.getFrom(), null );
        m_ep = null;

        if ( piece.getType() == Piecetype.King.ordinal() ) {
            m_canCastleShort[ m_toMove ] = false;
            m_canCastleLong[ m_toMove ] = false;
            if ( cmove.getType() == ChessMove.Movetype.Castling ) {
                Square sq = Square.values()[ cmove.getTo() ];
                switch (  sq ) {
                    case g1:
                        m_board.set( Square.f1.ordinal() , m_board.get( Square.h1.ordinal() ) );
                        m_board.set( Square.h1.ordinal(), null );
                        break;
                    case c1:
                        m_board.set( Square.d1.ordinal() , m_board.get( Square.a1.ordinal() ) );
                        m_board.set( Square.a1.ordinal(), null );
                        break;
                    case g8:
                        m_board.set( Square.f8.ordinal() , m_board.get( Square.h8.ordinal() ) );
                        m_board.set( Square.h8.ordinal(), null );
                        break;
                    case c8:
                        m_board.set( Square.d8.ordinal() , m_board.get( Square.a8.ordinal() ) );
                        m_board.set( Square.a8.ordinal(), null );
                        break;
                }
            }
        }
        else if ( piece.getType() == Piecetype.Rook.ordinal() ) {
            if ( cmove.getFrom() == Square.h1.ordinal() || cmove.getFrom() == Square.h8.ordinal() ) {
                m_canCastleShort[ m_toMove ] = false;
            }
            else if ( cmove.getFrom() == Square.a1.ordinal() || cmove.getFrom() == Square.a8.ordinal() ) {
                m_canCastleLong[ m_toMove ] = false;
            }
        }
        else if ( piece.getType() == Piecetype.Pawn.ordinal() ) {
            if ( cmove.getType() == ChessMove.Movetype.Promotion ) {
                Piece piecePromoteTo = null;
                if ( m_toMove == Player.White.ordinal() ) {
                    switch ( Piecetype.values()[cmove.getPromoteTo()] ) {
                        case Queen:
                            piecePromoteTo = chessPieces.get( 'Q' );
                            break;
                        case Rook:
                            piecePromoteTo = chessPieces.get( 'R' );
                            break;
                        case Bishop:
                            piecePromoteTo = chessPieces.get( 'B' );
                            break;
                        case Knight:
                            piecePromoteTo = chessPieces.get( 'N' );
                            break;
                    }
                }
                else {
                    switch ( Piecetype.values()[cmove.getPromoteTo()] ) {
                        case Queen:
                            piecePromoteTo = chessPieces.get( 'q' );
                            break;
                        case Rook:
                            piecePromoteTo = chessPieces.get( 'r' );
                            break;
                        case Bishop:
                            piecePromoteTo = chessPieces.get( 'b' );
                            break;
                        case Knight:
                            piecePromoteTo = chessPieces.get( 'n' );
                            break;
                    }
                }
                m_board.set( cmove.getTo(),  piecePromoteTo );
            }
            else if ( cmove.getType() == ChessMove.Movetype.enPassant ) {
                int sqEP = m_board.toSqr( m_board.toCol(cmove.getTo()), m_board.toRow(cmove.getFrom()) );
                m_board.set( sqEP, null );
            }
            else {
                int colFrom = m_board.toCol( cmove.getFrom() );
                int rowFrom = m_board.toRow(cmove.getFrom());
                int rowTo   = m_board.toRow(cmove.getTo());
                if ( m_toMove == Player.White.ordinal() && (rowTo - rowFrom) == 2 ) {
                    m_ep = Square.values()[ m_board.toSqr(colFrom,rowFrom+1)];
                }
                else if ( m_toMove == Player.Black.ordinal() && (rowFrom - rowTo) == 2 ) {
                    m_ep = Square.values()[ m_board.toSqr(colFrom,rowFrom-1)];
                }
            }
        }
        m_toMove = (m_toMove == Player.White.ordinal()) ?  Player.Black.ordinal() : Player.White.ordinal();

    }

    @Override
    public void retract( Move move, Object ori ) {
        ChessMove cmove = (ChessMove) move;
        RetractInfo ri = (RetractInfo) ori;

        if ( ri != null ) {
            m_canCastleShort[ Player.White.ordinal() ] = ri.canCastleShort[ Player.White.ordinal() ];
            m_canCastleLong[ Player.White.ordinal() ] = ri.canCastleLong[ Player.White.ordinal() ];
            m_canCastleShort[ Player.Black.ordinal() ] = ri.canCastleShort[ Player.Black.ordinal() ];
            m_canCastleLong[ Player.Black.ordinal() ] = ri.canCastleLong[ Player.Black.ordinal() ];
            m_ep = ri.ep;
        }

        m_toMove = (m_toMove == Player.White.ordinal()) ?  Player.Black.ordinal() : Player.White.ordinal();

        if ( cmove.getType() == ChessMove.Movetype.Promotion ) {
            if ( m_toMove == Player.White.ordinal() ) {
                m_board.set( cmove.getTo(), chessPieces.get( 'P' ) );
            }
            else {
                m_board.set( cmove.getTo(), chessPieces.get( 'p' ) );
            }
        }
        else if ( cmove.getType() == ChessMove.Movetype.enPassant ) {
            int sqEP = m_board.toSqr( m_board.toCol(cmove.getTo()), m_board.toRow(cmove.getFrom()) );
            if ( m_toMove == Player.White.ordinal() ) {
                m_board.set( sqEP, chessPieces.get( 'p' ) );
            }
            else {
                m_board.set( sqEP, chessPieces.get( 'P' ) );
            }
        }
        else if ( cmove.getType() == ChessMove.Movetype.Castling ) {
            Square sq = Square.values()[ cmove.getTo() ];
            switch (  sq ) {
                case g1:
                    m_board.set( Square.h1.ordinal() , m_board.get( Square.f1.ordinal() ) );
                    m_board.set( Square.f1.ordinal(), null );
                    break;
                case c1:
                    m_board.set( Square.a1.ordinal() , m_board.get( Square.d1.ordinal() ) );
                    m_board.set( Square.d1.ordinal(), null );
                    break;
                case g8:
                    m_board.set( Square.h8.ordinal() , m_board.get( Square.f8.ordinal() ) );
                    m_board.set( Square.f8.ordinal(), null );
                    break;
                case c8:
                    m_board.set( Square.a8.ordinal() , m_board.get( Square.d8.ordinal() ) );
                    m_board.set( Square.d8.ordinal(), null );
                    break;
            }
        }

        m_board.set( cmove.getFrom(), m_board.get( cmove.getTo() )  );
        m_board.set( cmove.getTo(), cmove.getPieceCaptured() );
    }

    @Override
    public boolean isTerminal( ) {
        int count = 0;
        for ( int row = 0; row < m_board.rows(); ++row ) {
            for ( int col = 0; col < m_board.cols(); ++col ) {
                Piece piece = m_board.get( col, row );
                if ( (piece != null) && (piece.getType() == Piecetype.King.ordinal()) ) {
                    ++count;
                }
            }
        }

        return count < 2;
    }

    @Override
    public int getResult( ) {
        return 0;
    }

    @Override
    public void reset( ) {
        setup( startFEN );
    }

    public String sqrToStr( int sqr ) {
        return m_board.getColCoordinates().get( m_board.toCol(sqr) ) +
                m_board.getRowCoordinates().get( m_board.toRow(sqr) );
    }

    public Integer strToSqr( String strSqr ) {
        Integer sqr = null;
        if ( strSqr.length() == 2 ) {
            List<String> cols = m_board.getColCoordinates();
            List<String> rows = m_board.getRowCoordinates();
            int col, row;
            boolean colFound = false;
            for ( col=0; col<cols.size(); ++col ) {
                if ( strSqr.charAt(0) == cols.get(col).charAt(0) ) {
                    colFound = true;
                    break;
                }
            }
            boolean rowFound = false;
            for ( row=0; row<rows.size(); ++row ) {
                if ( strSqr.charAt(1) == rows.get(row).charAt(0) ) {
                    rowFound = true;
                    break;
                }
            }
            if ( colFound && rowFound ) {
                sqr = m_board.toSqr( col, row );
            }
        }
        return sqr;
    }

    @Override
    public String moveToStr( Move move ) {
        ChessMove cmove = (ChessMove) move;
        String str = sqrToStr( cmove.getFrom() ) + sqrToStr( cmove.getTo() );
        if ( cmove.getPromoteTo() != -1 ) {
            String promote = "";
            switch ( ChessState.Piecetype.values()[cmove.getPromoteTo()] ) {
                case Queen:
                    promote = "q";
                    break;
                case Rook:
                    promote = "r";
                    break;
                case Bishop:
                    promote = "b";
                    break;
                case Knight:
                    promote = "n";
                    break;
            }
            str = str + promote;
        }
        return str;
    }

    @Override
    public Move strToMove( String strMove ) {
        List<Move> moves = getActions();
        /*
        for ( Move move : moves ) {
            if ( move.toString().equals( strMove ) ) {
                return move;
            }
        }
        */
        for ( Move move : moves ) {
            if ( moveToStr( move ).equals( strMove ) ) {
                return move;
            }
        }
        return null;
    }


    @Override
    public String getFEN(  ) {
        StringBuilder sb;
        sb = new StringBuilder();
        for ( int row=m_board.rows()-1; row>=0; --row ) {
            for ( int col=0; col<m_board.cols(); ++col ) {
                Character ch = chessChFEN.get( m_board.get( col, row ) );
                if ( ch != null ) {
                    sb.append( ch );
                }
                else {
                    sb.append( '.' );
                }
            }
        }
        sb.append( ' ' );
        sb.append( (m_toMove == Player.White.ordinal()) ? 'w' : 'b' );

        sb.append( ' ' );
        int len = sb.length();
        if ( m_canCastleShort[Player.White.ordinal()] ) {
            sb.append( 'K' );
        }
        if ( m_canCastleLong[Player.White.ordinal()] ) {
            sb.append( 'Q' );
        }
        if ( m_canCastleShort[Player.Black.ordinal()] ) {
            sb.append( 'k' );
        }
        if ( m_canCastleLong[Player.Black.ordinal()] ) {
            sb.append( 'q' );
        }
        if ( sb.length() == len ) { // no castling rights
            sb.append( '-' );
        }

        sb.append( ' ' );
        if ( m_ep != null ) {
            sb.append( sqrToStr( m_ep.ordinal() ) );
        }
        else {
            sb.append( '-' );
        }

        // en passant square.
        //sb.append( ' ' );
        //sb.append( m_plyCounter50MoveRule );

        // move number.
        return sb.toString();
    }


    @Override
    public boolean setup( String strFEN ) {

        // Example: rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2

        int       toMove = Player.White.ordinal();
        Board     board  = new Board( m_board.getColCoordinates(), m_board.getRowCoordinates() );
        boolean[] canCastleShort = { false, false };
        boolean[] canCastleLong = { false, false };
        ChessMove lastMove = null;
        int       plyCounter50MoveRule = 0;
        Square    ep = null;

        boolean ok = true;
        String[] strs = strFEN.split( " " );

        if ( strs.length >= 1 ) {
            int row = board.rows() - 1;
            int col = 0;
            for ( int i=0; i < strs[0].length() && ok; ++i ) {
                char ch = strs[0].charAt(i);
                switch ( ch ) {
                    case 'P':
                    case 'N':
                    case 'B':
                    case 'R':
                    case 'Q':
                    case 'K':
                    case 'p':
                    case 'n':
                    case 'b':
                    case 'r':
                    case 'q':
                    case 'k':
                        if ( col >= board.cols() ) {
                            ok = false;
                        }
                        else {
                            board.set( col, row, chessPieces.get( ch ) );
                            ++col;
                        }
                        break;
                    case '.':
                        if ( col >= board.cols() ) {
                            ok = false;
                        }
                        else {
                            ++col;
                        }
                        break;
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        col += Integer.valueOf( Character.toString(ch) );
                        if ( col > board.cols() ) {
                            ok = false;
                        }
                        break;
                    case '/':
                        row = row - 1;
                        col = 0;
                        if ( row < 0 ) {
                            ok = false;
                        }
                        break;
                    default:
                        ok = false;
                }
            }
        }

        if ( ok && strs.length >= 2 ) {
            char ch = strs[1].charAt(0);
            switch ( ch ) {
                case 'w':
                    toMove = Player.White.ordinal();
                    break;
                case 'b':
                    toMove = Player.Black.ordinal();
                    break;
                default:
                    ok = false;
                    break;
            }
        }

        if ( ok && strs.length >= 3 ) { // castling rights
            for ( int i=0; ok && i < strs[2].length(); ++i ) {
                char ch = strs[2].charAt(i);
                switch ( ch ) {
                    case 'K':
                        canCastleShort[Player.White.ordinal()] = true;
                        break;
                    case 'Q':
                        canCastleLong[Player.White.ordinal()] = true;
                        break;
                    case 'k':
                        canCastleShort[Player.Black.ordinal()] = true;
                        break;
                    case 'q':
                        canCastleLong[Player.Black.ordinal()] = true;
                        break;
                    case '-':
                        break;
                    default:
                        ok = false;
                        break;
                }
            }
        }
        else {
            // Assume castling rights.
            if ( isOnSquare( Player.White, Piecetype.King, Square.e1 ) ) {
                if ( isOnSquare( Player.White, Piecetype.Rook, Square.h1 ) ) {
                    canCastleShort[ Player.White.ordinal() ] = true;
                }
                if ( isOnSquare( Player.White, Piecetype.Rook, Square.a1 ) ) {
                    canCastleLong[ Player.White.ordinal() ] = true;
                }
            }
            if ( isOnSquare( Player.Black, Piecetype.King, Square.e8 ) ) {
                if ( isOnSquare( Player.Black, Piecetype.Rook, Square.h8 ) ) {
                    canCastleShort[ Player.Black.ordinal() ] = true;
                }
                if ( isOnSquare( Player.Black, Piecetype.Rook, Square.a8 ) ) {
                    canCastleLong[ Player.Black.ordinal() ] = true;
                }
            }
        }

        if ( ok && strs.length >= 4 ) {
            if ( !strs[3].equals( "-" ) ) {
                Integer sqr = strToSqr( strs[3] );
                if ( sqr != null ) {
                    int row = m_board.toRow( sqr );
                    int col = m_board.toCol( sqr );
                    ep = Square.values()[sqr];
                    if ( row == 2 ) {
                        lastMove = new ChessMove( m_board.toSqr(col,row-1), m_board.toSqr(col,row+1) );
                    }
                    else if ( row == 5 ) {
                        lastMove = new ChessMove( m_board.toSqr(col,row+1), m_board.toSqr(col,row-1) );
                    }
                    else { ok = false; }
                }
                else { ok = false; }
            }
        }

        if ( ok && strs.length >= 5 ) {  // ply since pawn move / capture?
            Integer no = Integer.parseInt( strs[4] );
            if ( no != null && no >= 0 ) {
                plyCounter50MoveRule = no;
            }
            else { ok = false; }
        }

        if ( ok && strs.length >= 6 ) {
            // Move number, ignore.
        }

        if ( ok ) {
            // Only modify state if parsing was successful.
            m_board = board;
            m_toMove = toMove;
            m_lastMove = lastMove;
            m_canCastleShort = canCastleShort;
            m_canCastleLong = canCastleLong;
            m_plyCounter50MoveRule = plyCounter50MoveRule;
            m_ep = ep;
        }
        return ok;
    }
}
