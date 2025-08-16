package com.example.chessapp;

public enum Direction {
    UP(0, 1, DirectionType.ORTHOGONAL),
    DOWN(0, -1, DirectionType.ORTHOGONAL),
    RIGHT(1, 0, DirectionType.ORTHOGONAL),
    LEFT(-1, 0, DirectionType.ORTHOGONAL),
    UP_RIGHT(1, 1, DirectionType.DIAGONAL),
    UP_LEFT(1, -1, DirectionType.DIAGONAL),
    DOWN_RIGHT(-1, 1, DirectionType.DIAGONAL),
    DOWN_LEFT(-1, -1, DirectionType.DIAGONAL);

    public final int rowStep;
    public final int colStep;
    public final DirectionType type;

    Direction(int rowStep, int colStep, DirectionType type) {
        this.rowStep = rowStep;
        this.colStep = colStep;
        this.type = type;
    }
}

