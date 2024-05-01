package q1;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

// Class that handles a paint GUI's logic.
public class PaintLogic {
    private static final int INITIAL_STROKE_WIDTH = 2;
    private static final Color INITIAL_COLOR = Color.BLACK;
    private static final PaneShape INITIAL_SHAPE = PaneShape.RECTANGLE;

    private final Pane drawingPane;

    private boolean isDragging;
    private boolean isFilled;
    private boolean beganDrawingShape;
    private PaneShape shape;
    private Color color;
    private int strokeWidth;

    private Point2D source, target; // mouse begin/end

    // constructor
    public PaintLogic(Pane drawingPane) {
        this.drawingPane = drawingPane;

        isDragging = false;
        isFilled = false;
        beganDrawingShape = false;
        strokeWidth = INITIAL_STROKE_WIDTH;
        shape = INITIAL_SHAPE;
        color = INITIAL_COLOR;
    }

    // Called at the start of a drawing event
    public void beginShapeDrag(Point2D mousePos) {
        isDragging = true;
        source = clampPoint(mousePos);
    }

    // Called during a drawing event
    public void onDrag(Point2D mousePos) {
        if (!this.isDragging) {
            return;
        }
        // we are relying on the fact that the shape was already created once, before deleting it.
        // that is why we have this beganDrawingShape variable. so if we haven't already created the shape, we won't
        // remove the last shape.
        if (beganDrawingShape) {
            removeLastShape();
        } else {
            beganDrawingShape = true;
        }
        target = clampPoint(mousePos);
        drawShape();
    }

    // Called at the end of a drawing event- cleanup
    public void onEndDrag() {
        isDragging = false;
        beganDrawingShape = false;
    }

    // Remove the last shape from the pane
    public void removeLastShape() {
        ObservableList<Node> shapes = drawingPane.getChildren();
        if (shapes.size() == 0) {
            return;
        }
        shapes.remove(shapes.size() - 1);
    }

    // Clear all shapes from the pane
    public void clear() {
        drawingPane.getChildren().clear();
    }

    // Draw the currently selected shape(using current arguments)
    private void drawShape() {
        Shape shape;
        try {
            shape = ShapeFactory.createShape(this.shape, source, target);
        } catch (ShapeDoesNotExistException e) {
            System.err.println(e.getMessage()); // print to the console if a shape was not implemented
            // in an actual application, would probably be better to have an assert statement inside 'createShape'
            return;
        }

        applyStyle(shape);
        drawingPane.getChildren().add(shape);
    }

    // sets color and stroke width
    private void applyStyle(Shape shape) {
        applyShapeColor(shape);
        shape.setStrokeWidth(strokeWidth);
    }

    // sets the color of the shape, taking fill into account
    private void applyShapeColor(Shape shape) {
        // line is not effected by fill so it's always stroke
        if (this.shape == PaneShape.LINE) {
            shape.setStroke(this.color);
            return;
        }

        if (isFilled) {
            shape.setFill(this.color);
            shape.setStroke(Color.TRANSPARENT);
        } else {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(this.color);
        }
    }

    // clamps point to be inside the limits of the drawing pane
    private Point2D clampPoint(Point2D point) {
        double drawOffset = this.strokeWidth / 2.0; // account for stroke width so it wont overflow
        double newX = clamp(point.getX(), 0 + drawOffset, drawingPane.getWidth() - drawOffset);
        double newY = clamp(point.getY(), 0 + drawOffset, drawingPane.getHeight() - drawOffset);
        return new Point2D(newX, newY);
    }

    // clamps value to be between min and max
    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    // ---------------- setters/getters ----------------

    // Set stroke width
    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
    }

    // Set shape fill
    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    // Set shape color
    public void setColor(Color color) {
        this.color = color;
    }

    // Set shape
    public void setShape(PaneShape shape) {
        this.shape = shape;
    }

    // Get shape
    public PaneShape getShape() {
        return shape;
    }

    // Get stroke width
    public int getStrokeWidth() {
        return strokeWidth;
    }

    // Get shape color
    public Color getColor() {
        return color;
    }
}
