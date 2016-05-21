package ru.botgame.entities;

import java.io.File;

public class Meeting {

    private Bot firstBot;
    private Bot secondBot;
    private String resultLocation;
    private String meetingLocation;

    public Meeting(String resultLocation) {
        this.resultLocation = resultLocation;
    }

    public Meeting(Bot first, Bot second, String resultDirectory) {
        this.firstBot = first;
        this.secondBot = second;
        this.resultLocation = resultDirectory;
    }

    public Bot getFirstBot() {
        return firstBot;
    }

    public void setFirstBot(Bot firstBot) {
        this.firstBot = firstBot;
    }

    public Bot getSecondBot() {
        return secondBot;
    }

    public void setSecondBot(Bot secondBot) {
        this.secondBot = secondBot;
    }

    public String getResultLocation() {
        return resultLocation;
    }

    public void setResultLocation(String resultLocation) {
        this.resultLocation = resultLocation;
    }

    public String getMeetingLocation() {
        meetingLocation = resultLocation + File.separator + firstBot.getName() + "_" + secondBot.getName();
        return meetingLocation;
    }

}
