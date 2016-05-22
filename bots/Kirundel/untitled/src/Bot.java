import java.io.*;
import java.util.Scanner;

public class Bot {

    private final static Scanner reader = new Scanner(System.in);

    public static void main(String... args) throws IOException {
        Botable comp = new MyBot();
        while (true) {
            String field = reader.next();
            field = field.substring(2);
            if(noEmptyPlaces(field)){
                break;
            }
            Field curField = new Field(field);
            if(curField.isEnd()) {
                break;
            }
            int step = curField.curStep() % 2;

            curField.set(comp.step(curField), step);
            System.out.println("19" + curField.res());
        }
    }

    private static boolean noEmptyPlaces(String field) {
        for (int i = 0; i < field.length(); i++) {
            if(field.charAt(i) == '-') {
                return false;
            }
        }
        return true;
    }
}
