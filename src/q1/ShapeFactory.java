package q1;

import javafx.geometry.Point2D;
import javafx.scene.shape.*;

// A factory class that creates shapes
public class ShapeFactory {

    // Creates a shape using the provided shape type, source and target.
    public static Shape createShape(PaneShape shape, Point2D source, Point2D target) throws ShapeDoesNotExistException {
        switch (shape) {
            case RECTANGLE:
                return createRectangle(source, target);
            case ELLIPSE:
                return createEllipse(source, target);
            case CIRCLE:
                return createCircle(source, target);
            case LINE:
                return createLine(source, target);
        }
        throw new ShapeDoesNotExistException(shape);
    }

    // Creates a rectangle using source and target points
    private static Rectangle createRectangle(Point2D source, Point2D target) {
        double rectX = Math.min(source.getX(), target.getX());
        double rectY = Math.min(source.getY(), target.getY());
        double height = Math.abs(source.getY() - target.getY());
        double width = Math.abs(source.getX() - target.getX());

        return new Rectangle(rectX, rectY, width, height);
    }

    // creates an ellipse using source and target points
    private static Ellipse createEllipse(Point2D source, Point2D target) {
        Point2D center = source.midpoint(target);
        double rad1 = center.getX() - Math.min(source.getX(), target.getX());
        double rad2 = center.getY() - Math.min(source.getY(), target.getY());

        return new Ellipse(center.getX(), center.getY(), rad1, rad2);
    }

    // creates a circle using source and target points
    private static Circle createCircle(Point2D source, Point2D target) {
        // calculate min distance(vertical/horizontal) between points
        double dy = target.getY() - source.getY();
        double dx = target.getX() - source.getX();

        // since we want a perfect circle, we make it so the diameter of the drawn circle will be the minimum of the
        // distances- constant radius for both width and height
        double diameter = Math.min(Math.abs(dx), Math.abs(dy));

        Point2D newTarget = source.add(diameter * Math.signum(dx), diameter * Math.signum(dy));
        // using sign method to maintain the direction of the circle
        Point2D center = source.midpoint(newTarget);

        return new Circle(center.getX(), center.getY(), diameter / 2);
    }


    // Creates a line using source and target points
    private static Line createLine(Point2D source, Point2D target) {
        return new Line(source.getX(), source.getY(), target.getX(), target.getY());
    }

}
