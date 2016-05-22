package example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by olymp17 on 14.05.2016.
 */
@SuppressWarnings("Duplicates")
public class Main implements Runnable {

    public static void main(String[] args) {
        Main main = new Main();
        while (true) {
            main.run();
        }
    }

    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private PrintWriter out = new PrintWriter(System.out);

    String readState() {
        while (true) {
            try {
                String input = in.readLine();
                int begin = 0;
                if (input.startsWith("19")) {
                    begin = 2;
                }
                return input.substring(begin);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    void debug(String s) {
        System.err.println(s);
        System.err.flush();
    }

    final int BOARD_SIZE = 19;

    final char ZERO = 'o';
    final char CROSS = 'x';

    char whichMove(char[] boardState) {
        int zeroesCount = 0, crossesCount = 0;
        for (char c : boardState) {
            if (c == ZERO) {
                zeroesCount++;
            }
            if (c == CROSS) {
                crossesCount++;
            }
        }
        if (crossesCount == zeroesCount) {
            return CROSS;
        }
        return ZERO;
    }

    char[] align(String s) {
        char[] a = s.toLowerCase().toCharArray();
        for (int i = 0; i < a.length; i++) {
            if (a[i] == '0') a[i] = 'o';
        }
        return a;
    }

    @Override
    public void run() {
        char[] s = align(readState());
        boolean end = true;
        for (char x : s) {
            if (x == '-') end = false;
        }
        if (end) {
            //throw new RuntimeException("Game Over");
            System.exit(0);
        }

        char myMove = whichMove(s);
        int[] boardState = new int[BOARD_SIZE * BOARD_SIZE];
        for (int i = 0; i < boardState.length; i++) {
            if (s[i] == myMove) {
                boardState[i] = 1;
            } else if (s[i] != '-') {
                boardState[i] = 2;
            }
        }
        int move = createBestPossibleMove(boardState);
        s[move] = myMove;
        out.print(BOARD_SIZE);
        out.println(new String(s));
        out.flush();
    }

    boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }

    int getCoordinate(int x, int y) {
        return x * BOARD_SIZE + y;
    }

    public int createBestPossibleMove(int[] boardState) {
        int[] count = new int[3];
        for (int x : boardState) {
            count[x]++;
        }

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int stepX = -1; stepX <= 1; stepX++) {
                    for (int stepY = -1; stepY <= 1; stepY++) {
                        if (isValid(x + 4 * stepX, y + 4 * stepY)) {
                            int[] cnt = new int[3];
                            for (int i = 0; i <= 4; i++) {
                                cnt[boardState[(x + i * stepX) * BOARD_SIZE + (y + i * stepY)]]++;
                            }
                            if (cnt[1] == 4 && cnt[2] == 0) {
                                for (int i = 0; i <= 4; i++) {
                                    if (boardState[(x + i * stepX) * BOARD_SIZE + (y + i * stepY)] == 0) {
                                        return (x + i * stepX) * BOARD_SIZE + (y + i * stepY);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int stepX = -1; stepX <= 1; stepX++) {
                    for (int stepY = -1; stepY <= 1; stepY++) {
                        if (isValid(x + 4 * stepX, y + 4 * stepY)) {
                            int[] cnt = new int[3];
                            for (int i = 0; i <= 4; i++) {
                                cnt[boardState[(x + i * stepX) * BOARD_SIZE + (y + i * stepY)]]++;
                            }
                            if (cnt[2] == 4 && cnt[1] == 0) {
                                for (int i = 0; i <= 4; i++) {
                                    if (boardState[(x + i * stepX) * BOARD_SIZE + (y + i * stepY)] == 0) {
                                        return (x + i * stepX) * BOARD_SIZE + (y + i * stepY);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // FOR 4
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                for (int stepX = -1; stepX <= 1; stepX++) {
                    for (int stepY = -1; stepY <= 1; stepY++) {
                        if (isValid(x + 5 * stepX, y + 5 * stepY) && boardState[x * BOARD_SIZE + y] == 0 && boardState[(x + 5 * stepX) * BOARD_SIZE + (y + 5 * stepY)] == 0) {
                            int[] cnt = new int[3];
                            for (int i = 1; i <= 4; i++) {
                                cnt[boardState[(x + i * stepX) * BOARD_SIZE + (y + i * stepY)]]++;
                            }
                            if (cnt[1] == 3 && cnt[2] == 0) {
                                for (int i = 1; i <= 4; i++) {
                                    if (boardState[(x + i * stepX) * BOARD_SIZE + (y + i * stepY)] == 0) {
                                        return (x + i * stepX) * BOARD_SIZE + (y + i * stepY);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (boardState[3 * BOARD_SIZE + 4] == 0) {
            return 3 * BOARD_SIZE + 4;
        }
        if (boardState[4 * BOARD_SIZE + 3] == 0) {
            return 4 * BOARD_SIZE + 3;
        }

        if (boardState[2 * BOARD_SIZE + 5] == 0 && boardState[5 * BOARD_SIZE * 2] == 0) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                for (int y = 0; y < BOARD_SIZE; y++) {
                    for (int stepX = -1; stepX <= 1; stepX++) {
                        for (int stepY = -1; stepY <= 1; stepY++) {
                            int x1 = x + stepX;
                            int y1 = y + stepY;
                            int x2 = x1 + stepX;
                            int y2 = y1 + stepY;
                            if (isValid(x2, y2)) {
                                if (boardState[getCoordinate(x, y)] == 0 && boardState[getCoordinate(x1, y1)] == 2 && boardState[getCoordinate(x2, y2)] == 2) {
                                    return getCoordinate(x, y);
                                }
                            }
                        }
                    }
                }
            }
        }

        int move = getLeftCorner(boardState);
        if (move != -1) return move;
        move = getRightBottom(boardState);
        if (move != -1) return move;



        Random rnd = new Random();
        while (true) {
            int pos = rnd.nextInt(BOARD_SIZE * BOARD_SIZE);
            if (boardState[pos] == 0) return pos;
        }
    }

    int getLeftCorner(int[] boardState) {
        if (boardState[getCoordinate(2, 5)] == 0) {
            return getCoordinate(2, 5);
        }
        if (boardState[getCoordinate(5, 2)] == 0) {
            return getCoordinate(5, 2);
        }

        int cr, cc;
        if (boardState[getCoordinate(2, 5)] == 1) {
            cr = 3;
            cc = 4;
        } else {
            cr = 4;
            cc = 3;
        }

        if (boardState[getCoordinate(cr + 1, cc + 1)] == 0) {
            return getCoordinate(cr + 1, cc + 1);
        }
        if (boardState[getCoordinate(cr - 1, cc - 1)] == 0) {
            return getCoordinate(cr - 1, cc - 1);
        }

        if (boardState[getCoordinate(cr + 1, cc)] == 1 && boardState[getCoordinate(cr - 1, cc)] == 1) {
            if (boardState[getCoordinate(cr + 2, cc)] == 0) {
                return getCoordinate(cr + 2, cc);
            }
            if (boardState[getCoordinate(cr - 2, cc)] == 0) {
                return getCoordinate(cr - 2, cc);
            }
        }

        if (boardState[getCoordinate(cr + 1, cc)] == 0 && boardState[getCoordinate(cr - 1, cc)] == 0) {
            return getCoordinate(cr + 1, cc);
        }

        if (boardState[getCoordinate(cr + 1, cc)] == 1 && boardState[getCoordinate(cr - 1, cc)] == 0) {
            return getCoordinate(cr - 1, cc);
        }
        return -1;
    }

    int getRightBottom(int[] boardState) {
        try {
            if (boardState[getCoordinate(16, 13)] == 0) {
                return getCoordinate(16, 13);
            }
            if (boardState[getCoordinate(13, 16)] == 0) {
                return getCoordinate(13, 16);
            }

            int cr, cc;
            if (boardState[getCoordinate(16, 13)] == 1) {
                cr = 15;
                cc = 14;
            } else {
                cr = 14;
                cc = 15;
            }

            if (boardState[getCoordinate(cr - 1, cc - 1)] == 0) {
                return getCoordinate(cr - 1, cc - 1);
            }
            if (boardState[getCoordinate(cr + 1, cc + 1)] == 0) {
                return getCoordinate(cr + 1, cc + 1);
            }

            if (boardState[getCoordinate(cr - 1, cc)] == 1 && boardState[getCoordinate(cr + 1, cc)] == 1) {
                if (boardState[getCoordinate(cr - 2, cc)] == 0) {
                    return getCoordinate(cr - 2, cc);
                }
                if (boardState[getCoordinate(cr + 2, cc)] == 0) {
                    return getCoordinate(cr + 2, cc);
                }
            }

            if (boardState[getCoordinate(cr - 1, cc)] == 0 && boardState[getCoordinate(cr + 1, cc)] == 0) {
                return getCoordinate(cr - 1, cc);
            }

            if (boardState[getCoordinate(cr - 1, cc)] == 1 && boardState[getCoordinate(cr + 1, cc)] == 0) {
                return getCoordinate(cr + 1, cc);
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }
}
