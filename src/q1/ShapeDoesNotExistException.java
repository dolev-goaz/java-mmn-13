package q1;

// Custom exception class for when the provided shape does not exist/not supported
public class ShapeDoesNotExistException extends Exception {
    public ShapeDoesNotExistException(PaneShape shape) {
        super(String.format("Shape %s does not exist!", shape.name()));
    }
}
