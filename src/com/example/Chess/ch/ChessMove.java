package com.example.Chess.ch;

import game.Move;
import game.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yngvi
 * Date: 12/12/12
 * Time: 10:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChessMove extends Move {

    enum Movetype { Normal, Castling, Promotion, enPassant }

    private Movetype m_type;
    private int      m_from;
    private int      m_to;
    private Piece    m_pieceCaptured;
    private int      m_promoteTo;

    public ChessMove( Movetype type, int from, int to, Piece pieceCaptured, int promoteTo ) {
        m_type = type;
        m_from = from;
        m_to = to;
        m_pieceCaptured = pieceCaptured;
        m_promoteTo = promoteTo;
    }

    public ChessMove( Movetype type, int from, int to, Piece pieceCaptured ) {
        this( type, from, to, pieceCaptured, -1 );
    }

    public ChessMove(  int from, int to, Piece pieceCaptured ) {
        this( Movetype.Normal, from, to, pieceCaptured, -1 );
    }

    public ChessMove( Movetype type, int from, int to ) {
        this( type, from, to, null, -1 );
    }

    public ChessMove( int from, int to ) {
        this( Movetype.Normal, from, to, null, -1 );
    }

    @Override
    public String toString() {
        String promote = "";
        if ( m_promoteTo != -1 ) {
            switch ( ChessState.Piecetype.values()[m_promoteTo] ) {
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
        }
        return String.valueOf( m_from ) + String.valueOf( m_to ) + promote;
    }

    public Movetype getType() {
        return m_type;
    }

    public int getFrom() {
        return m_from;
    }

    public int getTo() {
        return m_to;
    }

    public Piece getPieceCaptured() {
        return m_pieceCaptured;
    }

    public int getPromoteTo() {
        return m_promoteTo;
    }

    @Override
    public List<Integer> getSquares() {
        return new ArrayList<Integer>(Arrays.asList( m_from, m_to ) );
    }

}
