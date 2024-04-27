package q2;

// A class for the results of a turn in FourInARow
public class TurnResults {
    private GameStatus gameStatus;
    private int rowIndex;

    // constructor
    public TurnResults(GameStatus status, int rowIndex) {
        this.gameStatus = status;
        this.rowIndex = rowIndex;
    }

    // get game status
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    // set game status
    public int getRowIndex() {
        return rowIndex;
    }
}
