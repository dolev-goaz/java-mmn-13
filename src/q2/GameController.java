package q2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.stream.Collectors;

public class GameController {
    private static final int GAME_COLUMN_COUNT = 7;
    private static final int GAME_ROW_COUNT = 6;
    // we separate grid/game row/col counts. makes the code easier to read logically
    private static final int GRID_COLUMN_COUNT = GAME_COLUMN_COUNT;
    private static final int GRID_ROW_COUNT = GAME_ROW_COUNT + 1; // extra row for buttons

    // ------------------------ CUSTOMIZATION ------------------------
    private static final int TILE_SIZE = 50;
    private static final double DISC_RADIUS = TILE_SIZE / 2.25;
    private static final double DISC_INNER_RADIUS = DISC_RADIUS - TILE_SIZE / 7.0;
    private static final double TILE_CENTER = TILE_SIZE / 2.0;
    // --------------------------- COLORS ---------------------------
    private static final Color PLAYER_ONE_COLOR = Color.CORNFLOWERBLUE;
    private static final Color PLAYER_TWO_COLOR = Color.MEDIUMVIOLETRED;

    private FourInARow game;

    @FXML
    private GridPane gameGrid;

    @FXML
    private VBox gameContainer;

    @FXML
    private Button clearButton;

    public void initialize() {
        game = new FourInARow(GAME_COLUMN_COUNT, GAME_ROW_COUNT);
        initializeContainer();
        initializeGrid();
        initializeButtons();
    }

    // sets the width/height of the window
    private void initializeContainer() {
        gameContainer.setPrefWidth(GRID_COLUMN_COUNT * TILE_SIZE);
        gameContainer.setPrefHeight(clearButton.getPrefHeight() + GRID_ROW_COUNT * TILE_SIZE);
    }

    // sets width/height of the grid, adds rows/columns to the grid.
    private void initializeGrid() {
        gameGrid.setGridLinesVisible(true);
        gameGrid.setMinHeight(GRID_ROW_COUNT * TILE_SIZE);
        gameGrid.setMinWidth(GRID_COLUMN_COUNT * TILE_SIZE);

        // Set columns layout
        for (int i = 0; i < GRID_COLUMN_COUNT; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0 / GRID_COLUMN_COUNT);
            constraints.setHalignment(HPos.CENTER);
            gameGrid.getColumnConstraints().add(constraints);
        }

        // Set rows layout
        for (int i = 0; i < GRID_ROW_COUNT; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / GRID_ROW_COUNT); // +1 for button row
            row.setValignment(VPos.CENTER);
            gameGrid.getRowConstraints().add(row);
        }
    }

    // creates and adds the buttons to the grid. adds event listeners to handle their press.
    private void initializeButtons() {
        for (int i = 0; i < GAME_COLUMN_COUNT; i++) {
            Button b = new Button(String.format("%d", i+1));
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            gameGrid.add(b, i, GAME_ROW_COUNT);
            b.setOnAction(event -> {
                Button target = (Button)event.getTarget();
                int buttonTextNumeric = Integer.parseInt(target.getText());
                onColumnPress(buttonTextNumeric - 1); // Columns are 0-indexed
            });
        }
    }

    // Handles turn play
    private void onColumnPress(int columnIndex) {
        int currentTurn = this.game.getCurrentTurn();
        TurnResults results;
        try {
            results = this.game.play(columnIndex);
        } catch (FilledColumnException e) {
            // column is already filled- shouldn't really happen since we disable the column
            return;
        }

        int rowIndex = GAME_ROW_COUNT - 1 - results.getRowIndex(); // convert from top-left origin to bottom-left coordinates
        placeDisc(currentTurn, columnIndex, rowIndex);

        // check game over
        if (results.getGameStatus() != GameStatus.InProgress) {
            handleGameOver(results.getGameStatus());
            return;
        }

        if (game.isColumnFilled(columnIndex)) {
            // if column is filled, disable the button(don't allow extra clicks)
            // not really necessary because it's checked in the game runner, but it's better UX
            this.getButton(columnIndex).setDisable(true);
        }
    }

    // draws the disc of the provided player in the provided coordinates
    private void placeDisc(int player, int colIndex, int rowIndex) {
        Color currentColor =
                player == FourInARow.PLAYER_ONE
                        ? PLAYER_ONE_COLOR
                        : PLAYER_TWO_COLOR;

        // outer circle(the border, basically)
        Circle circle = new Circle(TILE_CENTER, TILE_CENTER, DISC_RADIUS, currentColor);
        this.gameGrid.add(circle, colIndex, rowIndex);
        // inner circle
        Circle innerCircle = new Circle(TILE_CENTER, TILE_CENTER, DISC_INNER_RADIUS, currentColor.brighter());
        this.gameGrid.add(innerCircle, colIndex, rowIndex);
    }

    // handles the ending of the game
    private void handleGameOver(GameStatus gameStatus) {
        // disable all buttons
        setButtonsDisable(true);

        // pop up an alert with a message fitting the game status
        String message;
        switch (gameStatus) {
            case Draw:
                message = "Game ended in a draw!";
                break;
            case PlayerOneWin:
                message = "Player one won!";
                break;
            case PlayerTwoWin:
                message = "Player two won!";
                break;
            default:
                // should never happen
                return;
        }

        showAlert("Game Over", message);

    }

    // shows an alert with the provided title and content
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    // clear the game board, creating a new game
    void onClear(ActionEvent event) {
        this.gameGrid.getChildren().removeIf(node -> node instanceof Circle);
        game.reset();
        setButtonsDisable(false);
    }

    // set the disabled state of all buttons
    private void setButtonsDisable(boolean disabled) {
        for (Button btn : this.getButtons()) {
            btn.setDisable(disabled);
        }
    }

    // return all buttons
    private List<Button> getButtons() {
        List<? extends Node> out =
                gameGrid.getChildren()
                        .stream()
                        .filter(node -> node instanceof Button)
                        .collect(Collectors.toList());
        return (List<Button>)out;
    }

    // returns a specific button
    private Button getButton(int index) {
        return this.getButtons().get(index);
    }
}
