package q2;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class GameController {
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 8;

    @FXML
    private GridPane gameGrid;


    @FXML
    private VBox gameContainer;

    public void initialize() {
        initializeGrid();
        initializeButtons();
    }

    private void initializeGrid() {
        gameGrid.setPrefWidth(gameContainer.getPrefWidth());
        gameGrid.setMaxWidth(gameContainer.getPrefWidth());
        gameGrid.setGridLinesVisible(true);
        gameGrid.setMaxWidth(Double.MAX_VALUE);
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
        }
    }

}
