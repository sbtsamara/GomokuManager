package example;

import java.io.*;

public class Bot {

    private final static int SIZE = 19;
    private final static char CROSS = 'x';
    private final static char ZERO = 'o';
    private final static char EMPTY = '-';
    private final static char ERR = '$';
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String field;
    private static String last_field;
    private static int step;

    public static void main(String... args) throws IOException {
        if (SIZE < 10) {
            last_field = "0" + SIZE;
        } else {
            last_field = "" + SIZE;
        }

        for (int i = 0; i < SIZE * SIZE; ++i) {
            last_field += "-";
        }

        while (true) {
            field = reader.readLine();
            if (noEmptyPlaces()) {
                break;
            }
            char mySymbol = findMySymbol();
            if (step == 0 && mySymbol == CROSS) {
                int place = (SIZE / 2) * SIZE + (SIZE / 2) + 2;
                String result = doStep(CROSS, place);
                System.out.println(result);
                last_field = result;
                step++;
                continue;
            }
            char enemy;
            if (mySymbol == CROSS) {
                enemy = ZERO;
            } else {
                enemy = CROSS;
            }

            int place = getPlaceToStep(enemy);
            String result = doStep(mySymbol, place);
            System.out.println(result);
            last_field = result;
            step++;
        }
        System.out.println("Game over");
    }

    private static int lastStep() {
        for (int i = 0; i < field.length(); ++i) {
            if (field.charAt(i) != last_field.charAt(i)) {
                return i;
            }
        }
        return -1;
    }

    private static char getChar(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return ERR;
        }
        return field.charAt(x * SIZE + y + 2);
    }

    private static char[][] area(int x, int y, int n) {
        char res[][] = new char[2 * n + 2][2 * n + 2];

        for (int i = 0; i <= 2 * n + 1; ++i) {
            for (int j = 0; j <= 2 * n + 1; ++j) {
                res[i][j] = getChar(i + x - n, j + y - n);
            }
        }

        return res;
    }

    private static String doStep(char mySymbol, int place) {
        return field.substring(0, place) + mySymbol + field.substring(place + 1);
    }

    private static int getPlaceToStep(char enemy) {
        int place;
        int last = lastStep() - 2;
        int x = last % SIZE;
        int y = last / SIZE;

        int n = 15;

        char[][] area = area(x, y, n);

        int val[][] = new int[2 * n + 1][2 * n + 1];

        for (int i = 0; i < 2 * n + 1; ++i) {
            for (int j = 0; j < 2 * n + 1; ++j) {
                if (area[i][j] != EMPTY) {
                    val[i][j] = -1;
                    continue;
                }
                for (int a = Math.max(0, i - n); a < Math.min(2 * n + 1, i + n); ++a) {
                    for (int b = Math.max(0, j - n); b < Math.min(2 * n + 1, j + n); ++b) {
                        if (area[a][b] == enemy) {
                            val[i][j]++;
                        }
                    }
                }
            }
        }

        int max_i = 0;
        int max_j = 0;

        for (int i = 0; i < 2 * n + 1; ++i) {
            for (int j = 0; j < 2 * n + 1; ++j) {
                if (val[i][j] > val[max_i][max_j]) {
                    max_i = i;
                    max_j = j;
                }
            }
        }

        int new_x = x + (max_i - n);
        int new_y = y + (max_j - n);
        place = new_x * SIZE + new_y + 2;

        return place;
    }

    private static boolean noEmptyPlaces() {
        for (int i = 0; i < field.length(); i++) {
            if (field.charAt(i) == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private static char findMySymbol() {
        int balance = 0;
        for (int i = 0; i < field.length(); i++) {
            switch (field.charAt(i)) {
                case ZERO:
                    balance++;
                    break;
                case CROSS:
                    balance--;
                    break;
            }
        }
        if (balance >= 0) {
            return CROSS;
        }
        return ZERO;
    }
}
