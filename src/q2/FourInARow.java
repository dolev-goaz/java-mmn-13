package q2;

public class FourInARow {
    private static final int EMPTY = -1;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;

    private final int width;
    private final int height;

    private int currentTurn;

    private final int[][] plays;

    public FourInARow(int width, int height) {
        this.width = width;
        this.height = height;

        this.plays = new int[width][height];

        this.reset();
    }

    public int play(int columnIndex) {
        if (columnIndex < 0 || columnIndex > this.width) {
            throw new IllegalArgumentException("Invalid column " + columnIndex);
        }

        int[] column = this.plays[columnIndex];
        if (column[this.height - 1] != EMPTY) {
            return -1;
        }

        // an assignment for the compiler, no real need to assign here since the loop would always find the appropriate row
        int playedRow = -1;
        for (int i = 0; i < this.height; i++) {
            if (column[i] == EMPTY) {
                playedRow = i;
                break;
            }
        }

        column[playedRow] = this.getCurrentTurn();
        switchPlayer();
        return playedRow;
    }

    public void reset() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.plays[i][j] = EMPTY;
            }
        }
        this.currentTurn = PLAYER_ONE;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    private void switchPlayer() {
        this.currentTurn =
                this.currentTurn == PLAYER_ONE
                        ? PLAYER_TWO
                        : PLAYER_ONE;
    }
}
