package q2;

public class FourInARow {
    private static final int EMPTY = -1;
    private static final int NO_WINNER = 0;
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
        return new TurnResults(getGameStatus(), playedRow);
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
    }

    // returns the game status(draw, player1 win, player2 win, in progress)
    private GameStatus getGameStatus() {
        if (this.isGameOverDraw()) {
            return GameStatus.Draw;
        }
        int winner = this.getWinner();
        if (winner == NO_WINNER) {
            return GameStatus.InProgress;
        }

        return winner == PLAYER_ONE
                ? GameStatus.PlayerOneWin
                : GameStatus.PlayerTwoWin;
    }

    // checks whether a provided column is filled
    public boolean isColumnFilled(int columnIndex) {
        return this.board[columnIndex][height-1] != EMPTY;
    }

    // checks if the top row is filled(meaning the entire board is filled)
    private boolean isGameOverDraw() {
        for (int i = 0; i < this.width; i++) {
            if (!this.isColumnFilled(i)) {
                // if the column isn't filled, then the game isn't over yet(there are empty slots)
                return false;
            }
        }
        return true;
    }

    // returns the winner(a player that has 4 discs in the same row/column/diagonal).
    // if none is present, returns NO_WINNER.
    private int getWinner() {
        // check row
        for (int col = 0; col <= width - CONNECT_COUNT_WIN; col++) {
            for (int row = 0; row < height; row++) {
                int player = board[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (board[col + k][row] != player) {
                        win = false;
                        break;
                    }
                }
                if (win) return player;

            }
        }

        // check column
        for (int col = 0; col < width; col++) {
            for (int row = 0; row <= height - CONNECT_COUNT_WIN; row++) {
                int player = board[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (board[col][row + k] != player) {
                        win = false;
                        break;
                    }
                }
                if (win) return player;
            }
        }

        // check diagonal (top-left to bottom-right)
        for (int col = 0; col <= width - CONNECT_COUNT_WIN; col++) {
            for (int row = 0; row <= height - CONNECT_COUNT_WIN; row++) {
                int player = board[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (board[col + k][row + k] != player) {
                        win = false;
                        break;
                    }
                }
                if (win) return player;
            }
        }

        // check diagonal (top-right to bottom-left)
        for (int col = CONNECT_COUNT_WIN - 1; col < width; col++) {
            for (int row = 0; row <= height - CONNECT_COUNT_WIN; row++) {
                int player = board[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (board[col - k][row + k] != player) {
                        win = false;
                        break;
                    }
                }
                if (win) return player;
            }
        }

        // If none of the above conditions are met, it's a draw
        return 0;
    }

}
