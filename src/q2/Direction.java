package q2;

// a direction class, used for checking win conditions in Four in a Row
public class Direction {
    private int x;
    private int y;

    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction reverse() {
        return new Direction(-x, -y);
    }
}
