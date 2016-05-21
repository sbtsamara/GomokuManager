package ru.botgame.botexecutor;

/**
 * Created 11.05.16
 */
public class GameOverException extends Exception {
    private GameResult result;
    private String winnerName;

    public GameOverException(GameResult result, String name) {
        this.result = result;
        this.winnerName = name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}
