package gomoku;

public class Move {
    public final int row, col;
    public final char ch;


    public Move(int row, int col, char ch) {
        this.row = row;
        this.col = col;
        this.ch = ch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (row != move.row) return false;
        if (col != move.col) return false;
        return ch == move.ch;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        result = 31 * result + (int) ch;
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "ch=" + ch +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
