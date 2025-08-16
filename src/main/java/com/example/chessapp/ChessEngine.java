package com.example.chessapp;

import java.util.List;

public class ChessEngine {
    private final String[][] boardPos = {
            {"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"},
            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"}
    };
    private double turnCounter = 1.0;
    public final int NUM_OF_FILES = boardPos.length;
    public final int NUM_OF_RANKS = boardPos[0].length;

    private void incrementTurnCounter() {
        turnCounter += 0.5;
    }

    private void movePiece(int originRow, int originCol, int targetRow, int targetCol) {
        boardPos[targetRow][targetCol] = boardPos[originRow][originCol];
        boardPos[originRow][originCol] = "";
    }

    public String getPieceAtLocation(int row, int col) {
        return boardPos[row][col];
    }

    public boolean isWhitesTurn() {
        return turnCounter % 1 == 0;
    }

    public boolean isValidMove(int originRow, int originCol, int targetRow, int targetCol) {
        String piece = getPieceAtLocation(originRow, originCol);
        MoveValidator mv = new MoveValidator(boardPos, isWhitesTurn());
        List<int[]> legalMoves = mv.getLegalMoves(piece, originRow, originCol);

        for (int[] move : legalMoves) {
            if ((move[0] == targetRow) && (move[1] == targetCol)) {
                return true;
            }
        }
        return false;
    }

    public void makeMove(int originRow, int originCol, int targetRow, int targetCol) {
        movePiece(originRow, originCol, targetRow, targetCol);
        incrementTurnCounter();
    }

    public boolean isPlayersPiece(int row, int col) {
        String piece = getPieceAtLocation(row, col);
        if (piece.isEmpty()) return false; // no piece there

        boolean whitesTurn = isWhitesTurn();
        boolean isWhitePiece = piece.charAt(0) == 'w';

        return (whitesTurn && isWhitePiece) || (!whitesTurn && !isWhitePiece);
    }
}


