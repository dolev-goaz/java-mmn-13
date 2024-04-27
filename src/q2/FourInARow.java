package q2;

public class FourInARow {
    private static final int EMPTY = -1;
    private static final int NO_WINNER = 0;
    private static final Direction[] winDirections = {
            new Direction(1, 0),
            new Direction(0, 1),
            new Direction(1, 1),
            new Direction(1, -1)
    };
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    public static final int CONNECT_COUNT_WIN = 4; // amount of discs in a row

    private final int width;
    private final int height;

    private int currentTurn;

    private final int[][] board;

    // constructor
    public FourInARow(int width, int height) {
        this.width = width;
        this.height = height;

        this.board = new int[width][height];

        this.reset();
    }

    // play a turn.
    public TurnResults play(int columnIndex) throws FilledColumnException {
        // check column is in bounds
        if ((columnIndex < 0) || (columnIndex > this.width)) {
            // shouldn't happen in our setup
            throw new IllegalArgumentException(String.format("Invalid column %d", columnIndex));
        }

        int[] column = this.board[columnIndex];
        // check column isn't filled
        if (this.isColumnFilled(columnIndex)) {
            // also shouldn't happen, since we disable the buttons of the filled columns. this is just for completeness'
            // sake.
            throw new FilledColumnException(String.format("Column %d is already filled!", columnIndex + 1));
        }

        // an assignment for the compiler, no real need to assign here since the loop would always find the appropriate row
        // get the row that the disc will land in.
        int playedRow = -1;
        for (int i = 0; i < this.height; i++) {
            if (column[i] == EMPTY) {
                playedRow = i;
                break;
            }
        }

        // play the disc
        column[playedRow] = this.getCurrentTurn();
        switchPlayer();
        return new TurnResults(getGameStatus(columnIndex, playedRow), playedRow);
    }

    // resets the game to its initial state
    public void reset() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[i][j] = EMPTY;
            }
        }
        this.currentTurn = PLAYER_ONE;
    }

    // return the current player
    public int getCurrentTurn() {
        return currentTurn;
    }

    // switches to the next player
    private void switchPlayer() {
        this.currentTurn =
                this.currentTurn == PLAYER_ONE
                        ? PLAYER_TWO
                        : PLAYER_ONE;
        // could also do 3 - currentTurn
        // which is basically PLAYER_ONE + PLAYER_TWO - currentTurn
        // but i think this way is more readable
    }

    // returns the game status(draw, player1 win, player2 win, in progress)
    private GameStatus getGameStatus(int playedColumn, int playedRow) {
        if (this.didPlayerWin(playedColumn, playedRow)) {
            return getWinnerGameStatus(board[playedColumn][playedRow]);
        }

        return this.isBoardFull()
                ? GameStatus.Draw
                : GameStatus.InProgress;

    }

    // For the given player, returns the corresponding winning game status
    private GameStatus getWinnerGameStatus(int player) {
        return player == PLAYER_ONE
                ? GameStatus.PlayerOneWin
                : GameStatus.PlayerTwoWin;
    }

    // checks whether a provided column is filled
    public boolean isColumnFilled(int columnIndex) {
        return this.board[columnIndex][height-1] != EMPTY;
    }

    // Checks if the entire board is full
    private boolean isBoardFull() {
        for (int i = 0; i < this.width; i++) {
            if (!this.isColumnFilled(i)) {
                // if the column isn't filled, then the game isn't over yet(there are empty slots)
                return false;
            }
        }
        return true;
    }

    // Checks if the current play won(a player that has CONNECT_COUNT_WIN discs in the same row/column/diagonal).
    private boolean didPlayerWin(int playedColumn, int playedRow) {
        for (Direction direction: winDirections) {
            int count = getConsecutiveCount(playedColumn, playedRow, direction)
                    + getConsecutiveCount(playedColumn, playedRow, direction.reverse())
                    - 1; // counting the initial point twice
            if (count >= CONNECT_COUNT_WIN) {
                return true;
            }
        }
        return false;
    }

    // Counts the amount of consecutive discs of the current player in the provided direction
    private int getConsecutiveCount(int playedCol, int playedRow, Direction dir) {
        int player = this.board[playedCol][playedRow];

        int count = 1; // current position included
        int currentCol = playedCol + dir.getX(); // next position
        int currentRow = playedRow + dir.getY(); // next position

        while (posInBounds(currentCol, currentRow) && (this.board[currentCol][currentRow] == player)) {
            count++;
            currentCol += dir.getX();
            currentRow += dir.getY();
        }

        return count;
    }

    // checks if the provided position is within the bounds of the game
    private boolean posInBounds(int col, int row) {
        return (col >= 0 && col < width) &&
                (row >= 0 && row < height);
    }

}
