package example;

import java.io.*;

public class Bot {

    private final static char CROSS = 'x';
    private final static char ZERO = 'o';
    private final static char EMPTY = '-';
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String field;

    enum dir{
        HORIZ, VERT, DIAG_LR, DIAG_RL;
    }

    public static void main(String... args) throws IOException {
        while (true) {
            field = reader.readLine();
            if(noEmptyPlaces()){
                break;
            }
            final char mySymbol = findMySymbol();
            int place = getPlaceToStep();
            String result = doStep(mySymbol, place);
            System.out.println(result);
        }
        System.out.println("Game over");
    }

    private static String doStep(char mySymbol, int place) {
        return field.substring(0, place) + mySymbol + field.substring(place + 1);
    }

    private static int getPlaceToStep() {
        int place = (int)(Math.random()*(field.length()-2));
        dir dir = Bot.dir.DIAG_LR;
        char smb = findMySymbol();
        int fs = (int)Math.sqrt(field.length()-2);
        while (true) {
            if (field.charAt(164) == EMPTY) {
                place = 164;
            } else {
                for (int i = 2; i < field.length(); i++) {
                    if (field.charAt(i) == EMPTY) {
                        place = i;
                    }
                }
                if (Math.abs(164 - place) == 1) {
                    dir = Bot.dir.HORIZ;
                } else if (Math.abs(164 - place) == fs) {
                    dir = Bot.dir.VERT;
                } else if (Math.abs(164 - place) == fs - 1) {
                    dir = Bot.dir.DIAG_RL;
                } else if (Math.abs(164 - place) == fs + 1) {
                    dir = Bot.dir.DIAG_LR;
                }

                if (place != 164 && place >= 164 - fs - 1 && place <= 14 + fs + 1) {
                    switch (dir) {
                        case DIAG_LR:
                            if (field.charAt(place - fs - 1) == EMPTY) place = place - fs - 1;
                            else if (field.charAt(place + fs + 1) == EMPTY) place = place + fs + 1;
                            break;
                        case DIAG_RL:
                            if (field.charAt(place - fs + 1) == EMPTY) place = place - fs + 1;
                            else if (field.charAt(place + fs - 1) == EMPTY) place = place + fs - 1;
                            break;
                        case HORIZ:
                            if (field.charAt(place - 1) == EMPTY) place = place - 1;
                            else if (field.charAt(place + 1) == EMPTY) place = place + 1;
                            break;
                        case VERT:
                            if (field.charAt(place - fs) == EMPTY) place = place - fs;
                            else if (field.charAt(place + fs) == EMPTY) place = place + fs;
                    }
                    break;
                }
            }
            if (field.charAt(place) == EMPTY) {
                break;
            }
        }
        return place;
    }

    private static boolean noEmptyPlaces() {
        for (int i = 0; i < field.length(); i++) {
            if(field.charAt(i) == EMPTY) {
                return false;
            }
        }
        return true;
    }

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
        if (balance >= 0) {
            return CROSS;
        }
        return ZERO;
    }
}
