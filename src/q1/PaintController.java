package q1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class PaintController {

    @FXML
    private Pane drawingPane;

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

    private static final PaneShape INITIAL_SHAPE = PaneShape.RECTANGLE;
    private static final Color INITIAL_COLOR = Color.BLACK;
    private PaneShape selectedShape;
    private Color color;

    private double x1, y1, x2, y2;
    private boolean isFilled;

    private boolean isDragging;
    private boolean beganDrawingShape;

    public void initialize() {
        isDragging = false;
        beganDrawingShape = false;
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
        circleRadioButton.setUserData(PaneShape.CIRCLE);
        lineRadioButton.setUserData(PaneShape.LINE);
        rectangleRadioButton.setUserData(PaneShape.RECTANGLE);

        shapeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            this.selectedShape = (PaneShape)newValue.getUserData();
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

    private void drawShape() {
        Shape shape;
        switch (this.selectedShape) {
            case RECTANGLE:
                shape = createRectangle();
                break;
            case CIRCLE:
                shape = createEllipse();
                break;
            case LINE:
                shape = createLine();
                break;
            default:
                return;
        }

        setColor(shape);

        drawingPane.getChildren().add(shape);
    }

    private Rectangle createRectangle() {
        double rectX = Math.min(x1, x2);
        double rectY = Math.min(y1, y2);
        double height = Math.abs(y2 - y1);
        double width = Math.abs(x2 - x1);
        return new Rectangle(rectX, rectY, width, height);
    }

    private Ellipse createEllipse() {
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;

        double rad1 = centerX - Math.min(x1, x2);
        double rad2 = centerY- Math.min(y1, y2);
        return new Ellipse(centerX, centerY, rad1, rad2);
    }

    private Line createLine() {
        return new Line(x1, y1, x2, y2);
    }

    private void setColor(Shape shape) {
        if (this.isFilled) {
            shape.setFill(this.color);
            shape.setStroke(Color.TRANSPARENT);
        } else {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(this.color);
        }
    }

    private void removeLastShape() {
        ObservableList<Node> shapes = drawingPane.getChildren();
        if (shapes.size() == 0) {
            return;
        }
        shapes.remove(shapes.size() - 1);
    }

    @FXML
    private void onBeginDrag(MouseEvent event) {
        isDragging = true;
        x1 = event.getX();
        y1 = event.getY();
    }

    @FXML
    void onDrag(MouseEvent event) {
        if (!this.isDragging) {
            return;
        }
        if (beganDrawingShape) {
            removeLastShape();
        } else {
            beganDrawingShape = true;
        }
        x2 = event.getX();
        y2 = event.getY();
        drawShape();
    }

    @FXML
    private void onEndDrag(MouseEvent event) {
        isDragging = false;
        beganDrawingShape = false;
    }

    @FXML
    private void onUndoShape(ActionEvent event) {
        removeLastShape();
    }

    @FXML
    void onClear(ActionEvent event) {
        drawingPane.getChildren().clear();
    }

    @FXML
    void onSetFilled(ActionEvent event) {
        CheckBox src = (CheckBox)event.getTarget();
        this.isFilled = src.isSelected();
    }
}
