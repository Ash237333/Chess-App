package com.example.chessapp;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class MainGameController {
    @FXML private GridPane chessBoard;
    private final Map<String, Rectangle2D> textureViewpoints = new HashMap<>();
    private final Image pieceTextures = new Image(getClass().getResource("/Chess_Pieces.png").toExternalForm());
    private final StackPane[][] boardSquares = new StackPane[8][8];
    private final String[][] boardPos = {
            {"br","bn","bb","bq","bk","bb","bn","br"},
            {"bp","bp","bp","bp","bp","bp","bp","bp"},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"wp","wp","wp","wp","wp","wp","wp","wp"},
            {"wr","wn","wb","wq","wk","wb","wn","wr"}
    };
    private final int NUM_OF_FILES = boardPos.length;
    private final int NUM_OF_RANKS = boardPos[0].length;
    private static final int TEXTURE_SIZE = 53;


    /**
     * Populates the texture map dictionary.
     * <p>
     * The key is each piece as chess notation with its colour before it
     * (e.g. the white knight is "wn"). The corresponding value is a {@link Rectangle2D}
     * defining the viewport for each piece in the texture sheet.
     */
    private void initialiseTextureMap(){
        textureViewpoints.put("wk", new Rectangle2D(0,0, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("wq", new Rectangle2D(TEXTURE_SIZE, 0, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("wb", new Rectangle2D(TEXTURE_SIZE *2, 0, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("wn", new Rectangle2D(TEXTURE_SIZE *3,0, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("wr", new Rectangle2D(TEXTURE_SIZE *4,0, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("wp", new Rectangle2D(TEXTURE_SIZE *5,0, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("bk", new Rectangle2D(0,53, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("bq", new Rectangle2D(TEXTURE_SIZE, 53, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("bb", new Rectangle2D(TEXTURE_SIZE *2, 53, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("bn", new Rectangle2D(TEXTURE_SIZE *3,53, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("br", new Rectangle2D(TEXTURE_SIZE *4,53, TEXTURE_SIZE, TEXTURE_SIZE));
        textureViewpoints.put("bp", new Rectangle2D(TEXTURE_SIZE *5,53, TEXTURE_SIZE, TEXTURE_SIZE));
    }

    /**
     * Creates the checkerboard pattern of the chess board by adding a stack pane
     * to each grid cell and colouring its background.
     */
    public void boardSetup(){
        for (int row = 0; row < NUM_OF_FILES; row ++){
            for (int col = 0; col < NUM_OF_RANKS; col++){
                StackPane boardSquare = new StackPane();
                Color squareColor = Color.rgb(235, 136, 16);
                if ((col+row) % 2 == 0){
                    squareColor = Color.rgb(242, 226, 206);
                }

                BackgroundFill squareBackgroundFill = new BackgroundFill(squareColor, null, null);
                Background squareBackground = new Background(squareBackgroundFill);
                boardSquare.setBackground(squareBackground);
                chessBoard.add(boardSquare, col, row);
                boardSquares[row][col] = boardSquare;
            }
        }
    }

    /**
     * Sets up the board based upon the boardPos array.
     */
    public void pieceSetup(){
        for (int row = 0; row < NUM_OF_FILES; row++){
            for (int col = 0; col < NUM_OF_RANKS; col++){
                String currLocation = boardPos[row][col];
                boardSquares[row][col].getChildren().clear();
                if (currLocation.isEmpty()){
                    continue;
                }
                ImageView piece = new ImageView(pieceTextures);
                piece.setViewport(textureViewpoints.get(currLocation));
                boardSquares[row][col].getChildren().add(piece);
            }
        }
    }


    /**
     * Initialize method that is called automatically by JavaFX after
     * this controller is instantiated. This calls setup methods to set
     * the initial board state.
     */
    public void initialize(){
        initialiseTextureMap();
        boardSetup();
        pieceSetup();
    }
}