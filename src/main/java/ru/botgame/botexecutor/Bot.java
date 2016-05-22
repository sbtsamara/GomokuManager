package ru.botgame.botexecutor;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 20.04.2016.
 */
class Bot {

    private String name;
    private Process process;

    private BufferedReader reader;
    private BufferedWriter writer;
    private final String botLocation;
    private long timer;

    Bot(String botLocation) {
        this.botLocation = botLocation;
        name = botLocation.substring(botLocation.lastIndexOf(File.separator)+1);
    }

    public void init() throws IOException, GameOverException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe",
                "/c",
                "cd " + botLocation + " && run.bat"
        );
        builder.redirectErrorStream(true);
        process = builder.start();
        if (process == null)
            throw new GameOverException(GameResult.WIN, null);
        if (!process.isAlive())
            throw new GameOverException(GameResult.WIN, null);

        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

   // public String getBotLocation() { return botLocation; }

    public void kill() {
        process.destroyForcibly();
    }

    //public Process getProcess() { return process; }

    public long getTimer() { return timer; }

    //public void increaseTimer(long timer) { this.timer += timer; }

    public String getName() { return name; }
}