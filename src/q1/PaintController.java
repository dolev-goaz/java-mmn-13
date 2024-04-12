package q1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
    private static final PaneShape INITIAL_SHAPE = PaneShape.RECTANGLE; // initial selected shape
    private static final Color INITIAL_COLOR = Color.BLACK;
    private static final double STROKE_WIDTH = 2;
    private static final double DRAW_OFFSET = STROKE_WIDTH / 2;

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

    private PaneShape selectedShape;
    private Color color;

    private Point2D source, target;
    private boolean isFilled;

    private boolean isDragging;
    private boolean beganDrawingShape;

    public void initialize() {
        isDragging = false;
        beganDrawingShape = false;
        initializeShapes();
        initializeColorInput();
    }

    // initializes the color input component, sets default color and listens to changes
    private void initializeColorInput() {
        // event handler to store the selected color
        colorInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.color = newValue;
        });
        colorInput.valueProperty().setValue(INITIAL_COLOR);
    }

    // initializes the radio group. listens to changes, and sets initial shape
    private void initializeShapes() {
        // NOTE: ensure we set the user data of all radio buttons here
        // this is so we won't rely on their text values
        circleRadioButton.setUserData(PaneShape.CIRCLE);
        lineRadioButton.setUserData(PaneShape.LINE);
        rectangleRadioButton.setUserData(PaneShape.RECTANGLE);

        // event handler to store the selected shape
        shapeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            this.selectedShape = (PaneShape)newValue.getUserData();
        });

        // trigger the initial radio button
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

    // draws a shape
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

        setStyle(shape);
        drawingPane.getChildren().add(shape);
    }

    // creates a rectangle using the stored data
    private Rectangle createRectangle() {
        double rectX = Math.min(source.getX(), target.getX());
        double rectY = Math.min(source.getY(), target.getY());
        double height = Math.abs(source.getY() - target.getY());
        double width = Math.abs(source.getX() - target.getX());

        return new Rectangle(rectX, rectY, width, height);
    }

    // creates an ellipse using the stored data
    private Ellipse createEllipse() {
        Point2D center = source.midpoint(target);
        double rad1 = center.getX() - Math.min(source.getX(), target.getX());
        double rad2 = center.getY() - Math.min(source.getY(), target.getY());

        return new Ellipse(center.getX(), center.getY(), rad1, rad2);
    }

    // creates a line using the stored data
    private Line createLine() {
        return new Line(source.getX(), source.getY(), target.getX(), target.getY());
    }

    // sets color and stroke width
    private void setStyle(Shape shape) {
        setColor(shape);
        shape.setStrokeWidth(STROKE_WIDTH);
    }

    // sets the color of the shape, taking fill into account
    private void setColor(Shape shape) {
        // line is not effected by fill so it's always stroke
        if (this.selectedShape == PaneShape.LINE) {
            shape.setStroke(this.color);
            return;
        }

        if (this.isFilled) {
            shape.setFill(this.color);
            shape.setStroke(Color.TRANSPARENT);
        } else {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(this.color);
        }
    }

    // clears the last shape from the pane
    private void removeLastShape() {
        ObservableList<Node> shapes = drawingPane.getChildren();
        if (shapes.size() == 0) {
            return;
        }
        shapes.remove(shapes.size() - 1);
    }

    // clamps point to be inside the limits of the drawing pane
    private Point2D clampPoint(double x, double y) {
        double newX = clamp(x, 0 + DRAW_OFFSET, drawingPane.getWidth() - DRAW_OFFSET);
        double newY = clamp(y, 0 + DRAW_OFFSET, drawingPane.getHeight() - DRAW_OFFSET);
        return new Point2D(newX, newY);
    }

    // clamps value to be between min and max
    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    @FXML
    // event handler for the user starts dragging
    private void onBeginDrag(MouseEvent event) {
        isDragging = true;
        source = clampPoint(event.getX(), event.getY());
    }

    @FXML
    // event handler for the user is currently dragging
    void onDrag(MouseEvent event) {
        if (!this.isDragging) {
            return;
        }
        // we are counting on the fact that the shape was already created once, before deleting it.
        // that is why we have this beganDrawingShape variable. so if we haven't already created the shape, we won't
        // remove the last shape.
        if (beganDrawingShape) {
            removeLastShape();
        } else {
            beganDrawingShape = true;
        }
        target = clampPoint(event.getX(), event.getY());
        drawShape();
    }

    @FXML
    // event handler for when the user stopped dragging
    private void onEndDrag(MouseEvent event) {
        isDragging = false;
        beganDrawingShape = false;
    }

    @FXML
    // event handler for the 'undo' button
    private void onUndoShape(ActionEvent event) {
        removeLastShape();
    }

    @FXML
    // event handler for the 'clear' button
    void onClear(ActionEvent event) {
        drawingPane.getChildren().clear();
    }

    @FXML
    // event handler for the 'filled' checkbox
    void onSetFilled(ActionEvent event) {
        CheckBox src = (CheckBox)event.getTarget();
        this.isFilled = src.isSelected();
    }
}
