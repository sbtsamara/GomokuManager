package gomoku;

import static gomoku.Main.*;
import static java.lang.Math.*;

public class BestMoveFinder {
    Evaluator evaluator = new Evaluator();
    int maxDepth = 1;

    public Move findMove(Board board) {
        return findMove0(board, detectWhoMoves(board), 1).move;
    }

    private MoveAndEvaluation findMove0(Board board, char whoMoves, int depth) {
        Move bestMove = null;
        double bestEvaluation = Double.NEGATIVE_INFINITY;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board.get(row, col) == EMPTY) {
//                    Board newBoard = new Board(board);
//                    newBoard.set(row, col, whoMoves);
                    board.set(row, col, whoMoves);
                    double evaluation;
                    if (depth == maxDepth) {
                        evaluation = evaluator.evaluate(board, whoMoves);
                    } else {
                        evaluation = -findMove0(board, enemy(whoMoves), depth + 1).evaluation;
                    }
                    if (evaluation > bestEvaluation) {
                        bestEvaluation = evaluation;
                        bestMove = new Move(row, col, whoMoves);
                    }
                    board.set(row, col, EMPTY);
                }
            }
        }
        return new MoveAndEvaluation(bestMove, bestEvaluation);
    }

    private char enemy(char who) {
        if (who == CROSS) {
            return ZERO;
        } else {
            return CROSS;
        }
    }

    private char detectWhoMoves(Board board) {
        int zeroes = 0;
        int crosses = 0;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                switch (board.get(i, j)) {
                    case ZERO:
                        zeroes++;
                        break;
                    case CROSS:
                        crosses++;
                        break;
                }
            }
        }
        if (zeroes == crosses) {
            return CROSS;
        }
        if (zeroes + 1 == crosses) {
            return ZERO;
        }
        return zeroes < crosses ? ZERO : CROSS;
    }

    public boolean gameFinished(Board board) {
        return getLongestLine(board, CROSS) == 5 || getLongestLine(board, ZERO) == 5;
    }

    public static int getLongestLine(Board board, char ch) {
        int ma = 0;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                ma = max(ma, longestLine(board, i, j, ch, 0, 1));
                ma = max(ma, longestLine(board, i, j, ch, 1, 0));
                ma = max(ma, longestLine(board, i, j, ch, 1, 1));
                ma = max(ma, longestLine(board, i, j, ch, 1, -1));
            }
        }
        return ma;
    }

    private static int longestLine(Board board, int row, int col, char ch, int dRow, int dCol) {
        int r = 0;
        while (row >= 0 && col >= 0 && row < board.size() && col < board.size() && board.get(row, col) == ch) {
            r++;
            row += dRow;
            col += dCol;
        }
        return r;
    }

    private class MoveAndEvaluation {
        final Move move;
        final double evaluation;

        private MoveAndEvaluation(Move move, double evaluation) {
            this.move = move;
            this.evaluation = evaluation;
        }
    }
}
