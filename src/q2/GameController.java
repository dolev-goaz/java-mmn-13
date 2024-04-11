package q2;

import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.control.Button;

public class GameController {
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 8;
    private static final int SQUARE_SIZE = 50;

    @FXML
    private GridPane gameGrid;

    @FXML
    private VBox gameContainer;

    @FXML
    private Button clearButton;

    public void initialize() {
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
            gameGrid.getColumnConstraints().add(constraints);
        }
        // +1 for button row
        for (int i = 0; i < ROW_COUNT; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / (ROW_COUNT + 1));
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
        System.out.println(source.getText());
    }

}
