import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by olymp22 on 14.05.2016.
 */
public class Comparator {

    private static Comparator INSTANCE = new Comparator();

    public static Comparator getInstance() {
        return INSTANCE;
    }

    Templates templates;

    public Comparator() {
        this.templates = new Templates();
        try {
            templates.load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    int maxDeep = 1;

    public int score(Field field, int deep) {
        if (deep == maxDeep) {
            return score(field);
        }

        int curPlayer = field.curStep() % 2;
        int max = 10000000;
        if (field.isEnd()) return max;

        for (int i = 0; i < field.size; i++) {
            for (int j = 0; j < field.size; j++) {
                if (!field.isEmpty(i, j)) continue;
                if (distance(i, j, field) > 3) continue;
                field.set(i, j, curPlayer);

                int childScore = score(field, deep + 1);
                max = Math.min(childScore, max);

                field.clear(i, j);
            }
        }
        return max;
    }

    int getWinStep(Field field) {
        int curPlayer = field.curStep() % 2;
        int best = 10000000;
        int bestStep = -1;
        for (int i = 0; i < field.size; i++) {
            for (int j = 0; j < field.size; j++) {
                if (!field.isEmpty(i, j)) continue;
                if (distance(i, j, field) > 2) continue;
                int cur = getScoreForPoint(field, i, j, curPlayer + 1) * 100 + (4 - distance(i, j, field)) * 500;
                field.set(i, j, curPlayer);
                int childScore = score(field, 1) - cur;
                if (childScore < best) {
                    best = childScore;
                    bestStep = i * field.size + j;
                }
                field.clear(i, j);
            }
        }
        return bestStep;
    }

    int distance(int x, int y, Field field) {
        int res = 20;
        for (int i = 0; i < field.size; i++) {
            for (int j = 0; j < field.size; j++) {
                if (field.isEmpty(i, j)) continue;
                int z = Math.max(Math.abs(x - i), Math.abs(y - j));
                res = Math.min(res, z);
            }
        }
        return res;
    }

    public int score(Field field) {
        List<Template> all = templates.templates;
        int res = 0;
        int curStep = field.curStep();
        int our;
        int opp;

        if (curStep % 2 == 0) {
            our = 1;
            opp = 2;
        } else {
            opp = 1;
            our = 2;
        }
        for (int i = 0; i < field.size; i++) {
            for (int j = 0; j < field.size; j++) {
                for (Template template : all) {
                    int curRes = getRes(field, our, i, j, template);
                    int res1 = getRes(field, opp, i, j, template);
                    res1 = res1 + (res1 / 10);
                    curRes -= res1;
                    res += curRes;
                }
            }
        }
        return res;
    }

    private int getRes(Field field, int our, int i, int j, Template template) {
        boolean check = true;
        for (int dx = 0; dx < template.x; dx++) {
            for (int dy = 0; dy < template.y; dy++) {
                int curOur;
                if (field.isOutside(i + dx, j + dy)) {
                    curOur = 1;
                } else {
                    if (field.field[i + dx][j + dy] == 0) {
                        curOur = -1;
                    } else if (field.field[i + dx][j + dy] == our) {
                        curOur = 0;
                    } else {
                        curOur = 1;
                    }
                }
                int curTemp = template.template[dx][dy];
                if (curTemp == 2) continue;
                if (curTemp == 0 && curOur != 0) {
                    check = false;
                    break;
                }
                if (curTemp == -1 && curOur == 1) {
                    check = false;
                    break;
                }
            }
        }
        if (check) return template.score;
        else return 0;
    }

    static int[] sx = {-1, 0, 1, -1, 1, -1, 0, 1};
    static int[] sy = {-1, -1, -1, 0, 0, 1, 1, 1};

    int getScoreForPoint(Field field, int x, int y, int player) {
        if (field.isEmpty(x, y)) return 0;
        int res = 0;
        for (int i = 0; i < 8; i++) {
            int empties = 0;
            int outsides = 0;
            int ours = 0;
            int opps = 0;
            for (int j = -3; j <= 3; j++) {
                if (j == 0) continue;
                int nx = x + sx[i] * j;
                int ny = y + sy[i] * j;
                if (field.isOutside(nx, ny)) {
                    outsides++;
                } else if (field.isEmpty(nx, ny)) {
                    empties++;
                } else if (field.field[nx][ny] == player) {
                    ours++;
                } else {
                    opps++;
                }
            }

            res += ours * ours + opps * opps + empties;
        }
        return res;
    }
}
