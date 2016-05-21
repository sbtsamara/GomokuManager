package ru.botgame.providers;

import ru.botgame.entities.Bot;
import ru.botgame.entities.Meeting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created 21.04.16
 */
public class Standings {
    private String[][] standings;
    private String pathResult;

    List<Meeting> meetingList;
    List<Bot> bots;
    List<MyBot> myBots;

    public Standings(String resultsDirectory, List<Bot> bots, List<Meeting> meetingList) {
        this.meetingList = meetingList;
        this.bots = bots;

        myBots = new ArrayList<>();
        for (Bot bot:bots) {
            myBots.add(new MyBot(bot.getName()));
        }

        pathResult = "" + resultsDirectory + File.separator + "final_result";
        File fileDirectory = new File(pathResult);
        fileDirectory.mkdir();

        String[] botName = getBotsName();
        standings = new String[botName.length + 1][botName.length + 1];
        standings[0][0] = "-";
        for (int i = 0; i < standings.length - 1; i++) {
            standings[i + 1][0] = botName[i];
            standings[0][i + 1] = botName[i];
        }
        for (int i = 1; i < standings.length; i++) {
            standings[i][i] = "-";
        }
        for (int i = 1; i < standings.length; i++) {
            for (int j = 1; j < standings.length; j++) {
                if (i != j) standings[i][j] = "0";
            }
        }
    }

    public void calculateStandings() throws IOException {
//        List<Meeting> meetingList = meetingsProvider.getMeetingList(botsProvider.getBots());

        for (int i = 0; i < meetingList.size(); i++) {
            Meeting meeting = meetingList.get(i);
            MyBot firstBot = null;


            for (MyBot bot:myBots) {
                if(bot.getName().equals(meeting.getFirstBot().getName())) firstBot = bot;
            }

            //Bot secondBot = meeting.getSecondBot();
            MyBot secondBot = null;

            for (MyBot bot:myBots) {
                if(bot.getName().equals(meeting.getSecondBot().getName())) secondBot = bot;
            }

            File file = new File(meeting.getResultLocation());
            String[] listNameFile = file.list();
            File[] listFile = file.listFiles();
            for (int j = 0; j < listNameFile.length; j++) {
                if (listNameFile[j].toString().split("\\.")[0].equals("results")) {

                    //читаем и  забираем победителя
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(listFile[j]));
                    String log = bufferedReader.readLine();
                    bufferedReader.close();
                    String cause = log.split(";")[0];
                    String nameWinner = null;
                    String nameDefeat = null;
                    if(cause.equals("WIN")){
                        nameWinner = log.split(";")[1];
                        if (nameWinner != null && nameWinner.equals(firstBot.getName())) {
                            setResultBattleInStandings(firstBot.getName(), secondBot.getName(), 1);
                            firstBot.addOneVictory();
                            secondBot.addOneDefeat();
                        }
                        if (nameWinner != null && nameWinner.equals(secondBot.getName())) {
                            setResultBattleInStandings(secondBot.getName(), firstBot.getName(), 1);
                            secondBot.addOneVictory();
                            firstBot.addOneDefeat();
                        }
                    }

                    if(cause.equals("LOOSE_BY_TIMEOUT")){
                        nameDefeat = log.split(";")[1];
                        if(nameDefeat != null && nameDefeat.equals(firstBot.getName())){
                            setResultBattleInStandings(secondBot.getName(), firstBot.getName(), 1);
                            secondBot.addOneVictory();
                            firstBot.addOneDefeat();
                        }
                        if (nameDefeat != null && nameDefeat.equals(secondBot.getName())) {
                            setResultBattleInStandings(firstBot.getName(), secondBot.getName(), 1);
                            firstBot.addOneVictory();
                            secondBot.addOneDefeat();
                        }
                    }

                    if(cause.equals("DRAW")){
                        secondBot.addOneDraw();
                        firstBot.addOneDraw();
                    }
                }
            }
        }
        //записать результат в файл
        saveStandings();
        saveWinners();
    }

    //прибавляет результат первому боту в игре со вторым в сетке
    private void setResultBattleInStandings(String nameBot1, String nameBot2, int resultBattle) {
        if (!nameBot1.equals(nameBot2)) {
            for (int i = 0; i < standings.length; i++) {
                for (int j = 0; j < standings.length; j++) {
                    if (standings[i][0].equals(nameBot1)
                            && standings[0][j].equals(nameBot2)) {
                        int result = Integer.valueOf(standings[i][j]);
                        standings[i][j] = String.valueOf(result + resultBattle);
                    }
                }
            }
        }
    }

    private void saveStandings() throws IOException {
        String pathStandings = pathResult + "\\standings.txt";
        File fileResult = new File(pathStandings);
        boolean newFile = fileResult.createNewFile();
        BufferedWriter writerStandings = new BufferedWriter(new FileWriter(pathStandings));
        for (int i = 0; i < standings.length; i++) {
            for (int j = 0; j < standings.length; j++) {
                writerStandings.write(standings[i][j]);
                writerStandings.write(";");
            }
            writerStandings.newLine();
        }
        writerStandings.flush();
        writerStandings.close();
    }

    private void saveWinners() throws IOException {
        String pathWinners = pathResult + "\\winners.txt";
        File f = new File(pathWinners);
        f.createNewFile();
        BufferedWriter writerWinners = new BufferedWriter(new FileWriter(pathWinners));
        writerWinners.write("Команды;Побед;Поражений;Ничьих");
        writerWinners.newLine();
        String[] winners = calculateWinners();
        for (int i = 0; i < winners.length; i++) {
            for (MyBot bot:myBots) {
                if(winners[i].equals(bot.getName())){
                    writerWinners.write("" + bot.getName() + ";" + bot.getVictoryCount() + ";" + bot.getDefeatCount() + ";" + bot.getDrawCount());
                }
            }
            writerWinners.newLine();
        }
        writerWinners.flush();
        writerWinners.close();
    }



    private String[] calculateWinners() {
        int[] s = new int[standings.length];
        for (int i = 1; i < standings.length; i++) {
            for (int j = 1; j < standings.length; j++) {
                if (!standings[i][j].equals("-")) {
                    Integer aa = Integer.valueOf(standings[i][j]);
                    s[i - 1] += Integer.valueOf(standings[i][j]);
                }
            }
        }

        return sortWinners(s);
    }

    private String[] sortWinners(int[] mass) {
        String[] s = new String[standings.length - 1];
        for (int i = 1; i < standings.length; i++) {
            s[i - 1] = standings[i][0];
        }

        for (int i = 0; i < mass.length; i++) {
            int max = mass[i];
            int max_i = i;
            for (int j = i + 1; j < mass.length; j++) {
                if (mass[j] > max) {
                    max = mass[j];
                    max_i = j;
                }
            }

            if (i != max_i) {
                int tmp = mass[i];
                mass[i] = mass[max_i];
                mass[max_i] = tmp;

                String tmpS = s[i];
                s[i] = s[max_i];
                s[max_i] = tmpS;
            }
        }
        return s;
    }

    private String[] getBotsName() {
//        List<Bot> bots = botsProvider.getBots();
        String[] list = new String[bots.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = bots.get(i).getName();
        }
        return list;
    }

    private class MyBot{

        private String Name;

        private int victoryCount;//количество побед
        private int defeatCount;//количество поражений
        private int drawCount;//Количество ничьей

        public MyBot (String nameBot){
            Name = nameBot;
            victoryCount = 0;
            defeatCount = 0;
            drawCount = 0;
        }

        public void addOneVictory(){victoryCount++;}

        public void addOneDefeat(){defeatCount++;}

        public void addOneDraw(){drawCount++;}

        public int getVictoryCount() {
            return victoryCount;
        }

        public int getDefeatCount() {
            return defeatCount;
        }

        public int getDrawCount() {
            return drawCount;
        }

        public String getName() {
            return Name;
        }
    }
}
