import java.io.IOException;

public class GameWithHuman {

    public static void main(String... args) throws IOException {
        Botable comp = new MyBot();
        Botable player = new Player();
        Field field = new Field();
        int step = 0;
        while (true) {
            if(step % 2 == 0) {
                field.set(comp.step(field), 0);
            } else {
                field.set(player.step(field), 1);
            }
            System.err.println(field.toString());

            if(field.isEnd()) {
                if(step % 2 == 0) {
                    System.out.println("You fail");
                } else {
                    System.out.println("You win");
                }
                break;
            }
            step++;
        }
    }


}
