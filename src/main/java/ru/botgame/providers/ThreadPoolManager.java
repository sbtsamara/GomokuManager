package ru.botgame.providers;

import ru.botgame.entities.Meeting;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created on 23.04.2016.
 */
public class ThreadPoolManager {

    private int threadPoolCapacity = 1;
    private long awaitTimeMinutes = 1;
    private long timeout;

    public ThreadPoolManager(int threadPoolCapacity, long awaitTimeMinutes, long timeout) {
        this.threadPoolCapacity = threadPoolCapacity;
        this.awaitTimeMinutes = awaitTimeMinutes;
        this.timeout = timeout;
    }

    public void startGames(List<Meeting> meetingList) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolCapacity, new DemonThreadFactory());
        for (final Meeting meeting : meetingList) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    ru.botgame.botexecutor.Executor m = new ru.botgame.botexecutor.Executor();
                    m.run(meeting.getFirstBot().getLocation(), meeting.getSecondBot().getLocation(), meeting.getResultLocation(), timeout);

                 }
            });
        }
        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(awaitTimeMinutes, TimeUnit.MINUTES)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            executorService.shutdownNow();
        }

        //games finished and we have some results
        System.out.println("The End.\n" /*+ results*/);
    }
}
