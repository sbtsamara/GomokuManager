package example;

import java.io.*;

public class Bot {

    private final static char CROSS = 'x';
    private final static char ZERO = 'o';
    private final static char EMPTY = '-';
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String field;

    public static void main(String... args) throws IOException {
        while (true) {
            field = reader.readLine();
            if(noEmptyPlaces()){
                break;
            }
            char mySymbol = findMySymbol();
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
        int place;
        while (true) {
            place = (int) (Math.random() * field.length());
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
                    balance--;
                    break;
                case CROSS:
                    balance++;
                    break;
            }
        }
        if (balance <= 0) {
            return CROSS;
        }
        return ZERO;
    }
}
