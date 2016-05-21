package ru.botgame;

import ru.botgame.entities.Bot;
import ru.botgame.entities.Meeting;
import ru.botgame.providers.BotsProvider;
import ru.botgame.providers.MeetingProvider;
import ru.botgame.providers.Standings;
import ru.botgame.providers.ThreadPoolManager;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class GameManager {

    private String botsDirectory;
    private String resultDirectory;
    private int capacity;
    private long awaitTime;
    private long timeout;

    public GameManager(String pathToConfigFile) {
        Properties projectProperties = new Properties();
        InputStream in = null;
        try {
//            in = getClass().getClassLoader().getResourceAsStream("config.properties");
            in = new FileInputStream(pathToConfigFile);
            projectProperties.load(in);
            botsDirectory = projectProperties.getProperty("sbt.root.bots.directory");
            resultDirectory = projectProperties.getProperty("sbt.root.results.directory");
            capacity = Integer.parseInt(projectProperties.getProperty("sbt.thread.pool.capacity"));
            awaitTime = Integer.parseInt(projectProperties.getProperty("sbt.thread.termination.time.minutes"));
            timeout = Integer.parseInt(projectProperties.getProperty("sbt.bots.timeout"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void run() throws IOException {
        BotsProvider botsProvider = new BotsProvider(botsDirectory);
        List<Bot> bots = botsProvider.getBots();
        MeetingProvider meetingsProvider = new MeetingProvider(resultDirectory);
        List<Meeting> meetingList = meetingsProvider.getMeetingList(bots);

        ThreadPoolManager threadPoolManager = new ThreadPoolManager(capacity, awaitTime, timeout);
        threadPoolManager.startGames(meetingList);

        Standings standings = new Standings(resultDirectory, bots, meetingList);
        standings.calculateStandings();
    }


}
