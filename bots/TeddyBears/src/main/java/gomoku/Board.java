package gomoku;

import java.util.Arrays;

public class Board {
    char[][] b;

    Board(String s) {
        String[] a = s.split("\n");
        if (a.length != a[0].length()) {
            throw new RuntimeException();
        }
        int size = a.length;
        b = new char[size][size];
        for (int i = 0; i < size; i++) {
            b[i] = a[i].toCharArray();
        }
    }

    public Board(Board board) {
        b = new char[board.size()][board.size()];
        for (int i = 0; i < size(); i++) {
            b[i] = board.b[i].clone();
        }
    }

    public char get(int row, int col) {
        return b[row][col];
    }

    public int size() {
        return b.length;
    }

    public void set(int row, int col, char ch) {
        b[row][col] = ch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                sb.append(get(i, j));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toString(Move move) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                char ch = get(i, j);
                if (move.row == i && move.col == j) {
                    ch = Character.toUpperCase(move.ch);
                }
                sb.append(ch);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
