package ru.botgame.providers;

import ru.botgame.entities.Bot;
import ru.botgame.entities.Meeting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 23.04.2016.
 */
public class MeetingProvider {

    private String resultDirectory;

    public MeetingProvider(String resultDirectory) {
        this.resultDirectory = resultDirectory;
    }

    public List<Meeting> getMeetingList(List<Bot> bots) throws IOException {
        List<Meeting> resultDirectories = new ArrayList<>(bots.size() * (bots.size() - 1));
        File f = new File(resultDirectory);
        f.mkdir();
        for (int i = 0; i < bots.size(); i++) {
            for (int j = i + 1; j < bots.size(); j++) {
                Bot first = bots.get(i);
                Bot second = bots.get(j);

                Meeting meeting = getMeeting(first, second);
                resultDirectories.add(meeting);

                meeting = getMeeting(second, first);
                resultDirectories.add(meeting);
            }
        }
        return resultDirectories;
    }

    private Meeting getMeeting(Bot first, Bot second) throws IOException {
        // create directory for meeting
        String meetingDirectory = resultDirectory + File.separator + first.getName() + "_" + second.getName();
        Files.createDirectories(new File(meetingDirectory).toPath());
        // copy bots into meeting directory
        Bot newBotFirst = getBot(first, meetingDirectory);
        Bot newBotSecond = getBot(second, meetingDirectory);

        return new Meeting(newBotFirst, newBotSecond, meetingDirectory);
    }

    private Bot getBot(Bot bot, String meetingDirectory) throws IOException {
        String path = meetingDirectory + File.separator + bot.getName();
        copyFolder(new File(bot.getLocation()), new File(path));
        return new Bot(bot.getName(), path);
    }

    private static void copyFolder(File sourceFolder, File destinationFolder) throws IOException
    {
        //Check if sourceFolder is a directory or file
        //If sourceFolder is file; then copy the file directly to new location
        if (sourceFolder.isDirectory())
        {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists())
            {
                destinationFolder.mkdir();
//                System.out.println("Directory created :: " + destinationFolder);
            }

            //Get all files from source directory
            String files[] = sourceFolder.list();

            //Iterate over all files and copy them to destinationFolder one by one
            for (String file : files)
            {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);
                copyFolder(srcFile, destFile);
            }
        }
        else
        {
            //Copy the file content from one place to another
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
