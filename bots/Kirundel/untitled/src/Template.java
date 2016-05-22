import java.util.Arrays;

/**
 * Created by olymp22 on 14.05.2016.
 */
public class Template {

    int x;
    int y;

    int[][] template;
    int score;

    public Template(int x, int y, String[] s, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
        template = new int[x][y];
        for(int i = 0; i < x; i++) {
            char[] c = s[i].toCharArray();
            for(int j = 0; j < y; j++) {
                template[i][j] = convert(c[j]);
            }
        }
    }

    public Template(int x, int y, int[][] template, int score) {
        this.x = x;
        this.y = y;
        this.template = template;
        this.score = score;
    }

    public Template rotate() {
        int[][] next = new int[y][x];
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                int ny = x - i  - 1;
                int nx = j;
                next[nx][ny] = template[i][j];
            }
        }
        return new Template(y, x, next, score);
    }

    private int convert(char c) {
        if(c == '.') return -1;
        if(c == 'o') return 1;
        if(c == 'x') return 0;
        if(c == '-') return 2;
        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template1 = (Template) o;

        if (x != template1.x) return false;
        if (y != template1.y) return false;
        if (score != template1.score) return false;
        return Arrays.deepEquals(template, template1.template);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + Arrays.deepHashCode(template);
        result = 31 * result + score;
        return result;
    }
}
