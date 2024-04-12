package q2;

// an exception class to be thrown when a player tries to place a disc in a column that's already filled
public class FilledColumnException extends RuntimeException {
    public FilledColumnException(String message) {
        super(message);
    }
}
