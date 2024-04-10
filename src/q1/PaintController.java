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
    private static final PaneShape INITIAL_SHAPE = PaneShape.RECTANGLE;
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

        setStyle(shape);

        drawingPane.getChildren().add(shape);
    }

    private Rectangle createRectangle() {
        double rectX = Math.min(source.getX(), target.getX());
        double rectY = Math.min(source.getY(), target.getY());
        double height = Math.abs(source.getY() - target.getY());
        double width = Math.abs(source.getX() - target.getX());

        return new Rectangle(rectX, rectY, width, height);
    }

    private Ellipse createEllipse() {
        Point2D center = source.midpoint(target);
        double rad1 = center.getX() - Math.min(source.getX(), target.getX());
        double rad2 = center.getY() - Math.min(source.getY(), target.getY());

        return new Ellipse(center.getX(), center.getY(), rad1, rad2);
    }

    private Line createLine() {
        return new Line(source.getX(), source.getY(), target.getX(), target.getY());
    }

    private void setStyle(Shape shape) {
        if (this.isFilled) {
            shape.setFill(this.color);
            shape.setStroke(Color.TRANSPARENT);
        } else {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(this.color);
        }
        shape.setStrokeWidth(STROKE_WIDTH);
    }

    private void removeLastShape() {
        ObservableList<Node> shapes = drawingPane.getChildren();
        if (shapes.size() == 0) {
            return;
        }
        shapes.remove(shapes.size() - 1);
    }

    private Point2D clampPoint(double x, double y) {
        double newX = clamp(x, 0 + DRAW_OFFSET, drawingPane.getWidth() - DRAW_OFFSET);
        double newY = clamp(y, 0 + DRAW_OFFSET, drawingPane.getHeight() - DRAW_OFFSET);
        return new Point2D(newX, newY);
    }
    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    @FXML
    private void onBeginDrag(MouseEvent event) {
        isDragging = true;
        source = clampPoint(event.getX(), event.getY());
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
        target = clampPoint(event.getX(), event.getY());
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
