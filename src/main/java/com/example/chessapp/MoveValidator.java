package com.example.chessapp;

import java.util.ArrayList;
import java.util.List;

public class MoveValidator {
    private final String[][] board;
    private final boolean whiteFlag;
    private final int numFiles;
    private final int numRanks;

    public MoveValidator(String[][] board, boolean whiteFlag) {
        this.board = board;
        this.whiteFlag = whiteFlag;
        this.numFiles = board.length;
        this.numRanks = board[0].length;
    }


    public List<int[]> getLegalMoves(String piece, int originRow, int originCol) {
        return switch (piece.charAt(1)) {
            case 'p' -> pawnLegalMoves(originRow, originCol);
            case 'r' -> rookLegalMoves(originRow, originCol);
            case 'b' -> bishopLegalMoves(originRow, originCol);
            case 'q' -> queenLegalMoves(originRow, originCol);
            case 'k' -> kingLegalMoves(originRow, originCol);
            case 'n' -> knightLegalMoves(originRow, originCol);
            default -> new ArrayList<>();
        };
    }

    private void addToList(int row, int col, List<int[]> legalMoves) {
        int[] legalMove = new int[]{row, col};
        legalMoves.add(legalMove);
    }

    private boolean checkIfEmpty(int row, int col) {
        return board[row][col].isEmpty();
    }

    private boolean checkIfCapturable(int row, int col) {
        if (board[row][col].isEmpty()) {
            return false;
        }
        char oppositeColor = whiteFlag ? 'b' : 'w';
        return board[row][col].charAt(0) == oppositeColor;
    }

    private boolean inBounds(int row, int col) {
        return (row >= 0 && row < this.numFiles && col >= 0 && col < this.numRanks);
    }

    private List<int[]> checkSingleDirection(int originRow, int originCol, int range, Direction directionToCheck) {
        List<int[]> legalMoves = new ArrayList<>();
        int currRow = originRow;
        int currCol = originCol;

        for (int i = 0; i < range; i++) {
            currRow += directionToCheck.rowStep;
            currCol += directionToCheck.colStep;

            if (!inBounds(currRow, currCol)) break;

            if (checkIfEmpty(currRow, currCol)) {
                addToList(currRow, currCol, legalMoves);
            } else {
                if (checkIfCapturable(currRow, currCol)) {
                    addToList(currRow, currCol, legalMoves);
                }
                break;

            }
        }
        return legalMoves;
    }

    private List<int[]> rookLegalMoves(int originRow, int originCol) {
        List<int[]> legalMoves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (direction.type == DirectionType.ORTHOGONAL) {
                legalMoves.addAll(
                        checkSingleDirection(originRow, originCol, numFiles, direction)
                );
            }
        }
        return legalMoves;
    }

    private List<int[]> bishopLegalMoves(int originRow, int originCol) {
        List<int[]> legalMoves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (direction.type == DirectionType.DIAGONAL) {
                legalMoves.addAll(
                        checkSingleDirection(originRow, originCol, numFiles, direction)
                );
            }
        }
        return legalMoves;
    }

    private List<int[]> queenLegalMoves(int originRow, int originCol) {
        List<int[]> legalMoves = new ArrayList<>();
        legalMoves.addAll(
                rookLegalMoves(originRow, originCol));
        legalMoves.addAll(
                bishopLegalMoves(originRow, originCol));
        return legalMoves;
    }

    private List<int[]> kingLegalMoves(int originRow, int originCol) {
        List<int[]> legalMoves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            legalMoves.addAll(
                    checkSingleDirection(originRow, originCol, 1, direction)
            );
        }
        return legalMoves;
    }

    private List<int[]> pawnLegalMoves(int originRow, int originCol) {
        List<int[]> legalMoves = new ArrayList<>();
        int step = whiteFlag ? -1 : 1;
        int startingRow = whiteFlag ? 6 : 1;
        int currRow = originRow + step;
        int currCol = originCol;

        if (checkIfEmpty(currRow, originCol)) {
            addToList(currRow, originCol, legalMoves);
            if (startingRow == originRow) {
                currRow += step;
                if (checkIfEmpty(currRow, originCol)) {
                    addToList(currRow, originCol, legalMoves);
                }
            }
        }


        currCol += 1;
        currRow = originRow + step;
        if (inBounds(currRow, currCol)) {
            if (checkIfCapturable(currRow, currCol)) {
                addToList(currRow, currCol, legalMoves);
            }
        }

        currCol -= 2;
        if (inBounds(currRow, currCol)) {
            if (checkIfCapturable(currRow, currCol)) {
                addToList(currRow, currCol, legalMoves);
            }
        }

        return legalMoves;
    }

    private List<int[]> knightLegalMoves(int originRow, int originCol) {
        List<int[]> legalMoves = new ArrayList<>();
        int[][] posOffsets = {{1, 2}, {-1, -2}, {-1, 2}, {1, -2}, {2, 1}, {-2, -1}, {-2, 1}, {2, -1}};
        for (int[] move : posOffsets) {
            int currRow = originRow + move[0];
            int currCol = originCol + move[1];
            if (!inBounds(currRow, currCol)) continue;

            if (checkIfEmpty(currRow, currCol) || checkIfCapturable(currRow, currCol)) {
                addToList(currRow, currCol, legalMoves);
            }
        }

        return legalMoves;
    }
}
