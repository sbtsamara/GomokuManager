package ru.botgame.providers;

import ru.botgame.entities.Bot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 23.04.2016.
 */
public class BotsProvider {
    private String botsDirectory;

    public BotsProvider(String botsDirectory) {
        this.botsDirectory = botsDirectory;
    }

    public List<Bot> getBots() {
        File botsFolder = new File(botsDirectory);
        List<Bot> bots = new ArrayList<>();
        for (File botFolder : botsFolder.listFiles()) {
            if (botFolder.isDirectory()) {
                Bot bot = new Bot(botFolder.getName(), botFolder.getAbsolutePath());
                bots.add(bot);
            }
        }
        return bots;
    }
}
