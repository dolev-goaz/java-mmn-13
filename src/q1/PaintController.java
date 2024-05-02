package q1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.Optional;

// A controller the paint application
public class PaintController {

    @FXML
    private Pane drawingPane;

    @FXML
    private ColorPicker colorInput;

    @FXML
    private Slider strokeWidthSlider;

    @FXML
    GridPane leftContainer;

    private PaintLogic logic;
    private static final int CONTROLS_COUNT_LEFT = PaneShape.values().length + 3; // 3 for undo/clear buttons+stroke width

    // Paint setup
    public void initialize() {
        logic = new PaintLogic(this.drawingPane);
        initializeColorInput();
        initializeLeftPanel();
    }

    private void initializeLeftPanel() {
        // Set columns layout
        for (int i = 0; i < CONTROLS_COUNT_LEFT; i++) {
            RowConstraints constraints = new RowConstraints();
            constraints.setPercentHeight(100.0 / CONTROLS_COUNT_LEFT);
            constraints.setValignment(VPos.CENTER);
            leftContainer.getRowConstraints().add(constraints);
        }

        initializeShapes();
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
        ToggleGroup toggleGroup = new ToggleGroup();
        int i = 0;
        for (PaneShape shape : PaneShape.values()) {
            RadioButton button = new RadioButton();
            button.setText(shape.name());
            button.setUserData(shape);
            button.setToggleGroup(toggleGroup);

            leftContainer.add(button, 0, i);
            i++;
        }

        // event handler to store the selected shape
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            logic.setShape((PaneShape) newValue.getUserData());
        });

        // trigger the initial radio button
        Optional<Toggle> initialRadio = toggleGroup
                .getToggles()
                .stream()
                .filter(toggle -> toggle.getUserData() == logic.getShape())
                .findFirst();
        initialRadio.ifPresent(radio -> ((RadioButton) radio).fire());
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
