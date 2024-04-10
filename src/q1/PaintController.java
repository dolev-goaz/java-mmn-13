package q1;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

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

    private double x1, y1;
    private boolean isDragging;

    public void initialize() {
        isDragging = false;
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

    private void drawShape(double x2, double y2) {
        System.out.println(String.format("Drawing a %s %s from %f, %f to %f, %f", this.color, this.selectedShape.name(), this.x1, this.y1,
                x2, y2));
    }

    @FXML
    void onBeginDrag(MouseEvent event) {
        isDragging = true;
        x1 = event.getX();
        y1 = event.getY();
    }

    @FXML
    void onEndDrag(MouseEvent event) {
        isDragging = false;
        drawShape(event.getX(), event.getY());
    }
}
