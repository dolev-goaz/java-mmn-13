package q2;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class GameController {
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 6;

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
        for (int i = 0; i < COLUMN_COUNT; i++) {
            gameGrid.addColumn(i);
        }
        // +1 for button row
        for (int i = 0; i < ROW_COUNT + 1; i++) {
            gameGrid.addRow(i);
        }
        gameGrid.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void initializeButtons() {
        for (int i = 0; i < COLUMN_COUNT; i++) {
            Button b = new Button((i+1) + "");
            gameGrid.add(b, i, ROW_COUNT - 1);
        }
    }

}
