package gomoku;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Main {

    private static final int SIZE = 19;
    static BestMoveFinder bestMoveFinder = new BestMoveFinder();

    /**
     * @return 0-based index in the string
     * @param move
     */
    public int getPlaceToStep(Move move) {
        return move.row * SIZE + move.col;
    }

    private Move getMove(Board board) {
        return bestMoveFinder.findMove(board);
    }

    private Board getBoard(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i));
            if (i % SIZE == SIZE - 1 && i != s.length() - 1) {
                sb.append("\n");
            }
        }
        return new Board(sb.toString());
    }

//////////////////////////////////////////////////////////////////////////////////////////////

    public final static char CROSS = 'x';
    public final static char ZERO = 'o';
    public final static char EMPTY = '-';
    private final static boolean DEBUG_ENABLED = false;
    private final static UUID id = DEBUG_ENABLED ? UUID.randomUUID() : null;
    private final static Random rnd = DEBUG_ENABLED ? new Random(322) : null;


    private BufferedReader reader;
    private PrintWriter writer;
    private PrintWriter logger;

    public static void main(String... args) {
        new Main().run();
    }

    private void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(System.out, true); // auto flush
            if (DEBUG_ENABLED) {
                logger = new PrintWriter(new FileWriter(new File("our-log.txt"), true), true); // append, auto flush
            }
            work();
        } catch (Throwable e) {
            if (logger != null) e.printStackTrace(logger);
        } finally {
            try {
                reader.close();
                writer.close();
                logger.close();
            } catch (IOException e) {
                // nothing
            }
        }
    }

    private void log(Object... o) {
        if (DEBUG_ENABLED) {
            logger.println(id + ": " + Arrays.deepToString(o));
        }
    }

    private void work() throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line.isEmpty()) {
                continue;
            }
            log("Read line:  length = " + line.length() + ", string = " + line);
            if (!line.startsWith("19")) {
                log("Not starts with 19");
            } else {
                line = line.substring(2, line.length());
            }
            if (line.length() != SIZE * SIZE) {
                log("Wrong size = " + line.length());
            }

            if (noEmptyPlaces(line)) {
                break;
            }

            char mySymbol = findMySymbol(line);
            Board board = getBoard(line);
            //log("Board before move: " + board);
            if (bestMoveFinder.gameFinished(board)) {
                return;
            }
            Move move = getMove(board);
            log("Board after move: " + board.toString(move));
            int place = getPlaceToStep(move);
            String result = "19" + doStep(line, mySymbol, place);
            log("Write line: length = " + result.length() + ", string = " + result);
            writer.println(result);
        }
        log("Game over");
    }

    private static String doStep(String s, char mySymbol, int place) {
        return s.substring(0, place) + mySymbol + s.substring(place + 1);
    }

    private static boolean noEmptyPlaces(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private char findMySymbol(String s) {
        int zeroes = 0;
        int crosses = 0;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case ZERO:
                    zeroes++;
                    break;
                case CROSS:
                    crosses++;
                    break;
            }
        }
        if (zeroes == crosses) {
            return CROSS;
        }
        if (zeroes + 1 == crosses) {
            return ZERO;
        }
        log("Wrong balance: crosses = " + crosses + ", zeroes = " + zeroes);
        return zeroes < crosses ? ZERO : CROSS;
    }
}
