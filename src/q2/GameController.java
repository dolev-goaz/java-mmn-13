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
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 6;
    private static final int SQUARE_SIZE = 50;
    private static final double DISC_RADIUS = SQUARE_SIZE / 2.25;
    private static final double DISC_INNER_RADIUS = DISC_RADIUS - SQUARE_SIZE / 7.0;
    private static final double SQUARE_CENTER = SQUARE_SIZE / 2.0;
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
        game = new FourInARow(COLUMN_COUNT, ROW_COUNT);
        initializeContainer();
        initializeGrid();
        initializeButtons();
    }

    // sets the width/height of the window
    private void initializeContainer() {
        gameContainer.setPrefWidth(COLUMN_COUNT * SQUARE_SIZE);
        gameContainer.setPrefHeight(clearButton.getPrefHeight() + (ROW_COUNT + 1) * SQUARE_SIZE);
    }

    // sets width/height of the grid, adds rows/columns to the grid.
    private void initializeGrid() {
        gameGrid.setGridLinesVisible(true);
        gameGrid.setMinHeight((ROW_COUNT + 1) * SQUARE_SIZE);
        gameGrid.setMinWidth(COLUMN_COUNT * SQUARE_SIZE);

        for (int i = 0; i < COLUMN_COUNT; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0 / COLUMN_COUNT);
            constraints.setHalignment(HPos.CENTER);
            gameGrid.getColumnConstraints().add(constraints);
        }
        // +1 for button row
        for (int i = 0; i < ROW_COUNT; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / (ROW_COUNT + 1));
            row.setValignment(VPos.CENTER);
            gameGrid.getRowConstraints().add(row);
        }
    }

    // creates and adds the buttons to the grid. adds event listeners to handle their press.
    private void initializeButtons() {
        for (int i = 0; i < COLUMN_COUNT; i++) {
            Button b = new Button((i+1) + "");
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            gameGrid.add(b, i, ROW_COUNT);
            b.setOnAction(event ->
                    onButtonPress((Button)event.getTarget())
            );
        }
    }

    // handles button press(play a turn)
    private void onButtonPress(Button source) {
        int currentTurn = this.game.getCurrentTurn();

        int columnIndex = Integer.parseInt(source.getText()) - 1;
        int rowIndex;
        try {
            rowIndex = this.game.play(columnIndex);
        } catch (FilledColumnException e) {
            // column is already filled
            return;
        }

        rowIndex = ROW_COUNT - 1 - rowIndex; // convert from top-left origin to bottom-left
        placeDisc(currentTurn, columnIndex, rowIndex);

        // check game over
        GameStatus gameStatus = this.game.getGameStatus();
        if (gameStatus != GameStatus.InProgress) {
            handleGameOver(gameStatus);
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
        Circle circle = new Circle(SQUARE_CENTER, SQUARE_CENTER, DISC_RADIUS, currentColor);
        this.gameGrid.add(circle, colIndex, rowIndex);
        // inner circle
        Circle innerCircle = new Circle(SQUARE_CENTER, SQUARE_CENTER, DISC_INNER_RADIUS, currentColor.brighter());
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
