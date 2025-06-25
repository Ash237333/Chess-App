package com.example.chessapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class MainGameController {
    public AnchorPane dragPane;
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
                boardSquare.getProperties().put("row", row);
                boardSquare.getProperties().put("col", col);
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
                attachDragHandlers(piece);
                boardSquares[row][col].getChildren().add(piece);
            }
        }
    }

    /**
     * Move the specified piece onto the dragPane.
     * @param piece The piece to be moved.
     */
    public void moveToDragPane(ImageView piece){
        if (piece.getParent() != null){
            Pane parent = (Pane) piece.getParent();
            parent.getChildren().remove(piece);
        }
        dragPane.getChildren().add(piece);
    }

    /**
     * Moves a piece onto the gridPane at the specified location.
     * @param piece The piece to be moved {@link ImageView}.
     * @param row The row to move it to.
     * @param col The column to move it to.
     */
    public void moveToGridPane(ImageView piece, int row, int col){
        if (piece.getParent() != null){
            ((Pane) piece.getParent()).getChildren().remove(piece);
        }
        boardSquares[row][col].getChildren().clear();
        boardSquares[row][col].getChildren().add(piece);
    }

    /**
     * Moves the image to follow the mouse when on the dragPane.
     * @param event The mouse event to follow. {@link MouseEvent}
     * @param piece The piece to move to that location. {@link ImageView}
     */
    public void setNewImagePosition(MouseEvent event, ImageView piece){
        Point2D pointInPane = dragPane.sceneToLocal(event.getSceneX(), event.getSceneY());

        double w = piece.getLayoutBounds().getWidth();
        double h = piece.getLayoutBounds().getHeight();
        piece.setLayoutX(pointInPane.getX() - w / 2);
        piece.setLayoutY(pointInPane.getY() - h / 2);
    }

    /**
     * Converts from scene coordinates to a grid cell reference
     * @param scenePoint The scene coordinates to be converted. Passed in as a {@link Point2D}.
     * @return A two element int array that holds the grid cell reference (row, column)
     */
    public int[] scenePointToGridCell(Point2D scenePoint){
        Point2D gridpoint = chessBoard.sceneToLocal(scenePoint);
        int col =  (int) (gridpoint.getX() / (chessBoard.getWidth() / 8));
        int row = (int) (gridpoint.getY() / (chessBoard.getHeight() / 8));
        return new int[] {row, col};
    }

    /**
     * Adds drag handlers to allow users to drag and drop pieces.
     * @param piece The piece that the handlers will attach to. Represented as a {@link ImageView}.
     */
    public void attachDragHandlers(ImageView piece){
        piece.setOnMouseEntered(event -> piece.setCursor(Cursor.HAND));

        piece.setOnMousePressed(event -> {
           moveToDragPane(piece);
           setNewImagePosition(event, piece);
           event.consume();
        });

        piece.setOnMouseDragged(event -> {
            setNewImagePosition(event, piece);
            event.consume();
        });

        piece.setOnMouseReleased(event -> {
            Point2D scenePoint = new Point2D(event.getSceneX(), event.getSceneY());
            int[] gridCell = scenePointToGridCell(scenePoint);
            moveToGridPane(piece, gridCell[0], gridCell[1]);
            piece.setCursor(Cursor.HAND);
        });
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