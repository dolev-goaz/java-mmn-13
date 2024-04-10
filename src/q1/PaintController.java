package q1;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class PaintController {

    @FXML
    private RadioButton circleRadioButton;

    @FXML
    private RadioButton lineRadioButton;

    @FXML
    private RadioButton rectangleRadioButton;

    @FXML
    private ToggleGroup shapeToggleGroup;

    @FXML
    private ColorPicker colorInput;

    private static final Shape INITIAL_SHAPE = Shape.RECTANGLE;
    private static final Color INITIAL_COLOR = Color.BLACK;
    private Shape selectedShape;
    private Color color;

    public void initialize() {
        initializeShapes();
        initializeColorInput();
    }

    private void initializeColorInput() {
        colorInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.color = newValue;
        });
        colorInput.valueProperty().setValue(INITIAL_COLOR);
    }

    private void initializeShapes() {
        // NOTE: ensure we set the user data of all radio buttons here
        circleRadioButton.setUserData(Shape.CIRCLE);
        lineRadioButton.setUserData(Shape.LINE);
        rectangleRadioButton.setUserData(Shape.RECTANGLE);

        shapeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            this.selectedShape = (Shape)newValue.getUserData();
        });

        switch (INITIAL_SHAPE){
            case LINE:
                lineRadioButton.fire();
                break;
            case CIRCLE:
                circleRadioButton.fire();
                break;
            case RECTANGLE:
                rectangleRadioButton.fire();
                break;
        }

    }
}
