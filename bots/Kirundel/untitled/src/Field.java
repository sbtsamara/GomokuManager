/**
 * Created by olymp22 on 14.05.2016.
 */
public class Field {
    static int EMPTY = 0;
    static int X = 1;
    static int O = 2;

    int size = 19;
    int[][] field;


    public Field() {
        field = new int[size][size];
    }

    public Field(String s) {
        field = new int[size][size];
        for(int i = 0; i < s.length(); i++) {
            int res;

            if(s.charAt(i) == '-') res = 0;
            else if(s.charAt(i) == 'o' || s.charAt(i) == 'O') res = 2;
            else res = 1;

            field[i / size][i % size] = res;
        }
    }

    int curStep() {
        int res = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(field[i][j] != EMPTY) res++;
            }
        }
        return res;
    }

    String res() {
        String res = "";
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                res += convert(field[i][j]);
            }
        }
        return res;
    }

    static int[] sx = {-1, 0, 1, -1, 1, -1, 0, 1};
    static int[] sy = {-1, -1, -1, 0, 0, 1, 1, 1};

    boolean isEmpty(int x, int y) {
        return field[x][y] == EMPTY;
    }

    void set(int x, int y, int player) {
        field[x][y] = player + 1;
    }

    void set(int z, int player) {
        set(z / field.length, z % field.length, player);
    }

    void clear(int x, int y) {
        field[x][y] = 0;
    }

    public int step() {
        int countZero = 0;
        int countCross = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(field[i][j] == O) countZero++;
                if(field[i][j] == X) countCross++;
            }
        }
        if(countCross == countZero) return X;
        else if(countCross == countZero + 1) return O;
        else throw new RuntimeException("Unreacheble state " + countCross + " " + countZero);
    }

    boolean isDraw() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(field[i][j] == EMPTY) return false;
            }
        }
        return true;
    }

    int findX(int opp, int width) {
        int res = -1;
        int max = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(!isEmpty(i, j)) continue;
                int count = 0;
                for(int s = 0; s < 8; s++) {
                    boolean check = true;
                    for(int k = 1; k <= width; k++) {
                        int nx = i + sx[s] * k;
                        int ny = j + sy[s] * k;
                        if(isOutside(nx, ny)) {
                            check = false;
                            break;
                        }
                        if(field[nx][ny] != opp) {
                            check = false;
                            break;
                        }
                    }
                    if(check) {
                        count++;
                    }
                }
                if(count == 0) continue;
                if(max < count) {
                    max = count;
                    res = i * size + j;
                }
            }
        }
        return res;
    }

    int findXWithSpace(int opp, int width) {
        int res = -1;
        int max = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(!isEmpty(i, j)) continue;
                int count = 0;
                for(int s = 0; s < 8; s++) {
                    int curRes = 0;
                    for(int k = 1; k <= width; k++) {
                        int nx = i + sx[s] * k;
                        int ny = j + sy[s] * k;
                        if(isOutside(nx, ny)) {
                            curRes = -10;
                            break;
                        }
                        if(field[nx][ny] != opp) {
                            if(field[nx][ny] == 0 && k != 1 && k != width) {
                                curRes--;
                            } else {
                                curRes -= 10;
                                break;
                            }
                        }
                    }
                    if(curRes >= -1) {
                        count++;
                    }
                }
                if(max < count) {
                    max = count;
                    res = i * size + j;
                }
            }
        }
        return res;
    }

    public boolean isEnd() {
        if(isDraw()) return true;

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                int cur = field[i][j];
                if(cur == 0) continue;
                for(int s = 0; s < sx.length; s++) {
                    boolean check = true;
                    for(int k = 0; k < 5; k++) {
                        int nx = i + sx[s] * k;
                        int ny = j + sy[s] * k;
                        if(isOutside(nx, ny)) {
                            check = false;
                            break;
                        }
                        if(cur != field[nx][ny]) {
                            check = false;
                            break;
                        }
                    }
                    if(check) return true;
                }
            }
        }
        return false;
    }

    boolean isOutside(int x, int y) {
        if(x < 0 || y < 0 || x >= size || y >= size) {
            return true;
        }
        return false;
    }

    char convert(int x) {
        if(x == EMPTY) return '-';
        if(x == X) return 'x';
        if(x == O) return 'o';
        return 0;
    }

    @Override
    public String toString() {
        String res = "Field: \n";

        res += "   ";
        for(int i = 0; i < size; i++) {
            String cur = Integer.toString(i+1);
            while (cur.length() < 3) {
                cur = " " + cur;
            }
            res += cur;
        }
        res += "\n";
        for(int i = 0; i < size; i++) {
            String cur = Integer.toString(i + 1);
            while (cur.length() < 3) {
                cur = " " + cur;
            }
            res += cur;
            for(int j = 0; j < size; j++) {
                String x = "  " + convert(field[i][j]);
                res += x;
            }
            res += "\n";
        }
        return res;
    }
}
