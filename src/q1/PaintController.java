package q1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

// A controller the paint application
public class PaintController {

    @FXML
    private Pane drawingPane;

    @FXML
    private RadioButton ellipseRadioButton;

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

    @FXML
    private Slider strokeWidthSlider;

    private PaintLogic logic;

    // Paint setup
    public void initialize() {
        logic = new PaintLogic(this.drawingPane);
        initializeShapes();
        initializeColorInput();
        initializeStrokeWidthInput();
    }

    // initializes the stroke width input component, sets default width and listens to changes
    private void initializeStrokeWidthInput() {
        strokeWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            logic.setStrokeWidth(newValue.intValue());
        });
        // set the initial value
        strokeWidthSlider.valueProperty().setValue(logic.getStrokeWidth());
    }

    // initializes the color input component, sets default color and listens to changes
    private void initializeColorInput() {
        // event handler to store the selected color
        colorInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            logic.setColor(newValue);
        });
        // set the initial value
        colorInput.valueProperty().setValue(logic.getColor());
    }

    // initializes the radio group. listens to changes, and sets initial shape
    private void initializeShapes() {
        // NOTE: ensure we set the user data of all radio buttons here.
        // This is so we won't rely on their text values
        ellipseRadioButton.setUserData(PaneShape.ELLIPSE);
        circleRadioButton.setUserData(PaneShape.CIRCLE);
        lineRadioButton.setUserData(PaneShape.LINE);
        rectangleRadioButton.setUserData(PaneShape.RECTANGLE);

        // event handler to store the selected shape
        shapeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            logic.setShape((PaneShape)newValue.getUserData());
        });

        // trigger the initial radio button
        switch (logic.getShape()){
            case LINE:
                lineRadioButton.fire();
                break;
            case CIRCLE:
                ellipseRadioButton.fire();
                break;
            case RECTANGLE:
                rectangleRadioButton.fire();
                break;
        }

    }

    // ---------------- events ----------------

    @FXML
    // event handler for the user starts dragging
    private void onBeginDrag(MouseEvent event) {
        logic.beginShapeDrag(mouseEventToPoint(event));
    }

    @FXML
    // event handler for the user is currently dragging
    void onDrag(MouseEvent event) {
        logic.onDrag(mouseEventToPoint(event));
    }

    @FXML
    // event handler for when the user stopped dragging
    private void onEndDrag(MouseEvent event) {
        logic.onEndDrag();
    }

    @FXML
    // event handler for the 'undo' button
    private void onUndoShape(ActionEvent event) {
        logic.removeLastShape();
    }

    @FXML
    // event handler for the 'clear' button
    void onClear(ActionEvent event) {
        logic.clear();
    }

    @FXML
    // event handler for the 'filled' checkbox
    void onSetFilled(ActionEvent event) {
        boolean isSelected = ((CheckBox)event.getTarget()).isSelected();
        logic.setFilled(isSelected);
    }

    // Extracts a point out of a mouse event
    private Point2D mouseEventToPoint(MouseEvent event) {
        return new Point2D(event.getX(), event.getY());
    }

}
