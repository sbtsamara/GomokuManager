package ru.botgame.providers;

import java.util.concurrent.ThreadFactory;

/**
 * Created on 24.04.2016.
 */
public class DemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
