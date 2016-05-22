import java.util.Random;

/**
 * Created by olymp22 on 14.05.2016.
 */
public class MyBot implements Botable {


    public MyBot() {
    }

    @Override
    public int step(Field field) {
        int step = field.curStep();
        if(step == 0) {
            return 9 * field.size + 9;
        }
        int cur = field.step();
        int four = field.findX(cur, 4);
        if(four != -1) return four;
        four = field.findX(cur == 1 ? 2 : 1, 4);
        if(four != -1) return four;
        int three = field.findX(cur, 3);
        if(three != -1) return three;
        three = field.findX(cur == 1 ? 2 : 1, 3);
        if(three != -1) return three;
        int space = field.findXWithSpace(cur == 1 ? 2 : 1, 4);
        if(space != -1) return space;
        space = field.findXWithSpace(cur == 1 ? 2 : 1, 3);
        if(space != -1) return space;
        int best = Comparator.getInstance().getWinStep(field);
        return best;
    }


}
