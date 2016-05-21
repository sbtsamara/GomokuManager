package ru.botgame.botexecutor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class BotLogger {
    private String gameDir;
    private static String gameLog = "gameLog.txt";
    private String logFile;
    private BufferedWriter write;

    public BotLogger(String gameDir) {
        this.gameDir = gameDir;
        this.logFile = gameDir + File.separator + gameLog;
        try {
            write = new BufferedWriter(new FileWriter(logFile));
        } catch (IOException e) {

        }
    }

    // логирование хода
    public void writeTurnToLog(String turn) {
        if (write != null) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss.ms");
                String dt = dateTimeFormatter.print(DateTime.now());
                write.write("[" + dt + "]");
                write.write(turn);
                write.newLine();
                write.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        if (write != null) {
            try {
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // запись результата игры
    public void writeGameResult(GameResult result, String winnerBotLocation) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(gameDir + "\\results.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert writer != null;
            writer.write(result + ";" + winnerBotLocation);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
