package ru.botgame;

import java.io.IOException;

/**
 * Created on 13.05.2016.
 */
public class Run {

    public static void main(String[] args) throws IOException {
        GameManager gameManager = new GameManager(args[0]);
        gameManager.run();
    }
}
