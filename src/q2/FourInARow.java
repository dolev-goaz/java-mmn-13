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

    private final int[][] plays;

    public FourInARow(int width, int height) {
        this.width = width;
        this.height = height;

        this.plays = new int[width][height];

        this.reset();
    }

    public int play(int columnIndex) throws FilledColumnException {
        if ((columnIndex < 0) || (columnIndex > this.width)) {
            // shouldn't happen in our setup
            throw new IllegalArgumentException(String.format("Invalid column %d", columnIndex));
        }

        int[] column = this.plays[columnIndex];
        if (column[this.height - 1] != EMPTY) {
            throw new FilledColumnException(String.format("Column %d is already filled!", columnIndex + 1));
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

    public GameStatus getGameStatus() {
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

    public boolean isColumnFilled(int columnIndex) {
        return this.plays[columnIndex][height-1] != EMPTY;
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

    private int getWinner() {
        // check row
        for (int col = 0; col <= width - CONNECT_COUNT_WIN; col++) {
            for (int row = 0; row < height; row++) {
                int player = plays[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (plays[col + k][row] != player) {
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
                int player = plays[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (plays[col][row + k] != player) {
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
                int player = plays[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (plays[col + k][row + k] != player) {
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
                int player = plays[col][row];
                if (player == EMPTY) {
                    continue;
                }
                boolean win = true;
                for (int k = 1; k < CONNECT_COUNT_WIN; k++) {
                    if (plays[col - k][row + k] != player) {
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
