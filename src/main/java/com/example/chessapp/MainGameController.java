package com.example.chessapp;

import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MainGameController {
    @FXML private GridPane chessBoard;

    public void initialize(){
        for (int row = 0; row < 8; row ++){
            for (int col = 0; col < 8; col++){
                StackPane boardSquare = new StackPane();
                Color squareColor = Color.rgb(235, 136, 16);
                if ((col+row) % 2 == 0){
                    squareColor = Color.rgb(242, 226, 206);
                }

                BackgroundFill squareBackgroundFill = new BackgroundFill(squareColor, null, null);
                Background squareBackground = new Background(squareBackgroundFill);
                boardSquare.setBackground(squareBackground);
                chessBoard.add(boardSquare, col, row);
            }
        }
    }
}