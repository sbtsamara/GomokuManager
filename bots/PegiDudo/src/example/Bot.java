package example;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bot {

    public final static char CROSS = 'x';
    public final static char ZERO = 'o';
    public final static char EMPTY = '-';
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String field;
    public static char mySymbol;
    public static String[] basegamefield;

    public static void main(String... args) throws IOException {
        while (true) {
            field = reader.readLine();
            if(noEmptyPlaces()){
                break;
            }
            mySymbol = findMySymbol();

            char enemy = EMPTY;
            switch (mySymbol) {
                case ZERO:
                    enemy = CROSS;
                    break;
                case CROSS:
                    enemy = ZERO;
                    break;
            }
            String sizeS = field.substring(0,2);
            int size = Integer.parseInt(field.substring(0,2));
            basegamefield = new String[size];
            int position = 2;
            int row = 0;
            while (position <= field.length()-1) {
                basegamefield[row] = field.substring(position, position + size);
                position +=size;
                row++;
            }
            NewTree ourTree = new NewTree(basegamefield);
            ourTree.fillList(mySymbol, 'V');
            for (int i = 0; i < ourTree.list.size(); i++) {
                ourTree.list.get(i).fillList('V', mySymbol);
            }

            String sFind = String.valueOf(mySymbol+mySymbol+mySymbol+mySymbol+mySymbol);

            int[] victoryCounter = new int[ourTree.list.size()];
            int ourMax = 0;
            for (int i = 0; i < ourTree.list.size(); i++)
            {
                /*System.out.println("Ветка " + i);
                printField(ourTree.list.get(i).field);*/

                for (int j=0; j<ourTree.list.get(i).list.size(); j++) {
                    /*System.out.println("Ветка " +i +"Подветка"+ j);
                    printField(ourTree.list.get(i).list.get(j).field);*/
                    for (int k=0; k <ourTree.list.get(i).list.get(j).field.length; k++) {
                        if (ourTree.list.get(i).list.get(j).field[k].contains(sFind)) {
                            victoryCounter[i]++;
                        }
                    }
                    if (victoryCounter[i] > ourMax) ourMax = victoryCounter[i];
                }
            }

            //ветка противника
            NewTree enemyTree = new NewTree(basegamefield);
            enemyTree.fillList(enemy, 'V');
            for (int i = 0; i < enemyTree.list.size(); i++) {
                enemyTree.list.get(i).fillList('V', enemy);
            }

            sFind = String.valueOf(enemy+enemy+enemy+enemy+enemy);

            int[] enemyVictoryCounter = new int[enemyTree.list.size()];
            int enemyMax = 0;
            for (int i = 0; i < enemyTree.list.size(); i++)
            {
                for (int j=0; j<enemyTree.list.get(i).list.size(); j++) {
                    for (int k=0; k <enemyTree.list.get(i).list.get(j).field.length; k++) {
                        if (enemyTree.list.get(i).list.get(j).field[k].contains(sFind)) {
                            enemyVictoryCounter[i]++;
                        }
                    }
                    if (enemyVictoryCounter[i] > enemyMax ) enemyMax  = enemyVictoryCounter[i];
                }
            }
            String result = sizeS;
            if (ourMax >= enemyMax) {

                int index = 0;
                for (int i = 0; i < victoryCounter.length; i++) {
                    if (victoryCounter[i] == ourMax) index = i;
                }

                for (int i = 0; i < basegamefield.length; i++) {
                    result += ourTree.list.get(index).field[i];
                }
            } else {
                int index = 0;
                for (int i = 0; i < enemyVictoryCounter.length; i++) {
                    if (enemyVictoryCounter[i] == enemyMax) index = i;
                }
                for (int i = 0; i < basegamefield.length; i++) {
                    result += enemyTree.list.get(index).field[i];
                }}
                result = result.replace('V', mySymbol);
                System.out.println(result);
            }
        System.out.println("Game over");
    }

    //Тестовый вывод
    public static void printField(String[] gamefield) {
        for (int i = 0; i < gamefield.length; i++) {
            System.out.println(gamefield[i]);
        }
    }
//Формирует выходную строку
    private static String doStep(char mySymbol, int place) {
        return field.substring(0, place) + mySymbol + field.substring(place + 1);
    }
//Определяет куда сходить моему боту в этот шаг - случайно определяет позицию
    private static int getPlaceToStep() {
        int place;
        while (true) {
            place = (int) (Math.random() * field.length());
            if (field.charAt(place) == EMPTY) {
                break;
            }
        }
        return place;
    }
//Проверка на непустые поля
    private static boolean noEmptyPlaces() {
        for (int i = 0; i < field.length(); i++) {
            if(field.charAt(i) == EMPTY) {
                return false;
            }
        }
        return true;
    }
//Определение символа моего бота
    private static char findMySymbol() {
        int balance = 0;
        for (int i = 0; i < field.length(); i++) {
            switch (field.charAt(i)) {
                case ZERO:
                    balance++;
                    break;
                case CROSS:
                    balance--;
                    break;
            }
        }
        if (balance > 0) {
            return CROSS;
        }
        return ZERO;
    }
}
