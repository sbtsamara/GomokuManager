import java.util.Scanner;

/**
 * Created by olymp22 on 14.05.2016.
 */
public class Player implements Botable {

    private Scanner sc = new Scanner(System.in);


    @Override
    public int step(Field field) {
        while (true) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;

            if (field.isOutside(x, y) || !field.isEmpty(x, y)) {
                System.err.println("Invalid step");
            } else {
                return x * field.size + y;
            }
        }
    }
}
