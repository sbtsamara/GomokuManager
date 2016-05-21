package lite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created on 13.05.2016.
 */
public class LiteExecutor {

    private static String board;
    private static final long TIMEOUT = 1000L;

    public static void main(String[] args) throws IOException {
        Bot bot1 = new Bot("Semen1");
        Bot bot2 = new Bot("Semen2");
        bot1.init();
        bot2.init();
        board = new String(new char[361]).replace("\0", "-");
        try {
            while (true) {
                doStep(bot1);
                doStep(bot2);
            }
        } catch (IllegalStateException ex) {
            writeTurnToLog(ex.getMessage());
        } catch (Exception ignored){}
        finally {
            bot1.kill();
            bot2.kill();
        }
        System.out.println("Ходы записаны в gameLog.txt");
    }

    private static void doStep(Bot bot) throws IOException {
        BufferedWriter writer = bot.getWriter();
        writer.write(board);
        writer.newLine();
        writer.flush();
        wait(bot);
        board = bot.getReader().readLine();
        writeTurnToLog(bot.getName() + " " + board);
    }

    private static void wait(Bot bot) throws IOException {
        long start = System.currentTimeMillis();
        while (!bot.getReader().ready()) {
            long current = System.currentTimeMillis();
            if (current - start > TIMEOUT) {
                throw new IllegalStateException("TIMEOUT");
            }
        }
    }

    private static void writeTurnToLog(String turn) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("gameLog.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert writer != null;
            writer.write(turn + "\r\n");
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
