package gomoku;

import static gomoku.BestMoveFinder.*;
import static gomoku.Main.*;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Evaluator {

    public static final double ALPHA = 100;

    public double evaluate(Board board, char whoEvaluates) {
        double r = 0;
        r += getDistToCenterFactor(board, whoEvaluates);
        r += smartFactor(board, whoEvaluates);
        //r += longestLineFactor(board, whoEvaluates);
        return r;
    }

    private double smartFactor(Board board, char whoEvaluates) {
        double r = 0;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                r += check5(board, i, j, 0, 1, whoEvaluates);
                r += check5(board, i, j, 1, 0, whoEvaluates);
                r += check5(board, i, j, 1, 1, whoEvaluates);
                r += check5(board, i, j, 1, -1, whoEvaluates);
            }
        }
        return r;
    }

    private double check5(Board board, int row, int col, int dRow, int dCol, char whoEvaluates) {
        int myColor = 0;
        int enemyColor = 0;
        for (int i = 0; i < 5; i++) {
            if (row >= 0 && col >= 0 && row < board.size() && col < board.size()) {
                if (board.get(row, col) == whoEvaluates) {
                    myColor++;
                } else if (board.get(row, col) != EMPTY) {
                    enemyColor++;
                }
                row += dRow;
                col += dCol;
            } else {
                return 0;
            }
        }
        if (myColor > 0 && enemyColor > 0) {
            return 0;
        }
        if (myColor > 0) {
            return powerFactor(myColor);
        }
        if (enemyColor > 0) {
            return -powerFactor(enemyColor) * sqrt(ALPHA);
        }
        return 0;
    }

    private double powerFactor(int cnt) {
        double r = 1;
        for (int i = 0; i < cnt; i++) {
            r *= ALPHA;
        }
        return r;
    }

    private double longestLineFactor(Board board, char whoEvaluates) {
        return getLongestLine(board, whoEvaluates);
    }

    private double getDistToCenterFactor(Board board, char whoEvaluates) {
        double sumDistToCenter = 0;
        int center = board.size() / 2;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                if (board.get(i, j) == whoEvaluates) {
                    sumDistToCenter += (abs(i - center) + abs(j - center)) / (double) board.size();
                }
            }
        }
        return -sumDistToCenter / (board.size() * board.size());
    }
}
