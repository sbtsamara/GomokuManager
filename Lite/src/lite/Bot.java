package lite;

import java.io.*;

/**
 * Created by SBT-Galimov-RR on 13.05.2016.
 */
class Bot {
    private Process process;

    private BufferedReader reader;
    private BufferedWriter writer;
    private final String name;

    public Bot(String name) {
        this.name = name;
    }

    public void init() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe",
                "/c",
                "cd .\\botHere && run.bat"
        );
        builder.redirectErrorStream(true);
        process = builder.start();
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() { return writer; }

    public String getName() {
        return name;
    }

    public void kill() {
        process.destroyForcibly();
    }

    public Process getProcess() {
        return process;
    }
}
