package q2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameController {
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 6;
    private static final int SQUARE_SIZE = 50;
    private static final double DISC_RADIUS = SQUARE_SIZE / 2.25;
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

    private void initializeContainer() {
        gameContainer.setPrefWidth(COLUMN_COUNT * SQUARE_SIZE);
        gameContainer.setPrefHeight(clearButton.getPrefHeight() + (ROW_COUNT + 1) * SQUARE_SIZE);
    }

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

    private void onButtonPress(Button source) {
        int currentTurn = this.game.getCurrentTurn();

        int columnIndex = Integer.parseInt(source.getText()) - 1;
        int rowIndex = this.game.play(columnIndex);
        if (rowIndex == -1) return;

        rowIndex = ROW_COUNT - 1 - rowIndex; // convert from top-left origin to bottom-left
        placeDisc(currentTurn, columnIndex, rowIndex);
    }

    private void placeDisc(int player, int colIndex, int rowIndex) {
        Color currentColor =
                player == FourInARow.PLAYER_ONE
                        ? PLAYER_ONE_COLOR
                        : PLAYER_TWO_COLOR;

        Circle circle = new Circle(SQUARE_CENTER, SQUARE_CENTER, DISC_RADIUS, currentColor);
        this.gameGrid.add(circle, colIndex, rowIndex);
    }


    @FXML
    void onClear(ActionEvent event) {
        this.gameGrid.getChildren().removeIf(node -> node instanceof Circle);
        game.reset();
    }
}
