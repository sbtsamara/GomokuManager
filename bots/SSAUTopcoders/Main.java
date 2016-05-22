package example;

import java.awt.*;
import java.io.*;
import java.util.HashSet;

public class Main {

    private final static char CROSS = 'x';
    private final static char ZERO = 'o';
    private final static char EMPTY = '-';
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String field;
    private static char[][] arr;
    private static char myChar = 0;
    private static char theirChar = 0;

    public static void main(String... args) throws IOException {
        while (true) {
            field = reader.readLine().replaceAll("[0-9]", "");
//            System.err.println(field);
            findMySymbol();
            getArr();
            if (noEmptyPlaces()) {
                break;
            }


//            Line[] lines = getLines();
//            System.err.println(Arrays.toString(lines));

            doStep(getPlaceToStep());
            field = getString();

            System.out.println(field);
        }
        System.out.println("Game over");
    }

    private static Line[] getLines() {
        HashSet<Line> res = new HashSet<>();

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                char type = 123;
                if (arr[i][j] == CROSS || arr[i][j] == ZERO) {
                    type = arr[i][j];
                }
                if (type != 123) {
                    Point start = new Point(j, i);
                    j++;
                    while (j < arr.length && arr[i][j] == type) {
                        j++;
                    }
                    j--;
                    Point end = new Point(j, i);
                    if (!start.equals(end)) {
                        res.add(new Line(start, end, type));
                    }
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                char type = 123;
                if (arr[i][j] == CROSS || arr[i][j] == ZERO) {
                    type = arr[i][j];
                }
                if (type != 123) {
                    Point start = new Point(j, i);
                    i++;
                    while (i < arr.length && arr[i][j] == type) {
                        i++;
                    }
                    i--;
                    Point end = new Point(j, i);
                    if (!start.equals(end)) {
                        res.add(new Line(start, end, type));
                    }
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int add = 0; add + i < arr.length; add++) {
                int xt = i + add;
                int yt = add;

                char type = 123;
                if (arr[yt][xt] == CROSS || arr[yt][xt] == ZERO) {
                    type = arr[yt][xt];
                }
                if (type != 123) {
                    Point start = new Point(xt, yt);
                    yt++;
                    xt++;
                    while (xt < arr.length && yt < arr.length && arr[yt][xt] == type) {
                        yt++;
                        xt++;
                        add++;
                    }
                    xt--;
                    yt--;
                    Point end = new Point(xt, yt);
                    if (!start.equals(end)) {
                        res.add(new Line(start, end, type));
                    }
                }
            }
            for (int sub = 0; i - sub >= 0; sub++) {
                int xt = i - sub;
                int yt = sub;

                char type = 123;
                if (arr[yt][xt] == CROSS || arr[yt][xt] == ZERO) {
                    type = arr[yt][xt];
                }
                if (type != 123) {
                    Point start = new Point(xt, yt);
                    yt++;
                    xt--;
                    while (xt >= 0 && yt >= 0 && arr[yt][xt] == type) {
                        yt++;
                        xt--;
                        sub++;
                    }
                    xt++;
                    yt--;
                    Point end = new Point(xt, yt);
                    if (!start.equals(end)) {
                        res.add(new Line(start, end, type));
                    }
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int add = 0; add + i < arr.length; add++) {
                int xt = 0;
                int yt = i;

                char type = 123;
                if (arr[yt][xt] == CROSS || arr[yt][xt] == ZERO) {
                    type = arr[yt][xt];
                }
                if (type != 123) {
                    Point start = new Point(xt, yt);
                    yt++;
                    xt++;
                    while (xt < arr.length && yt < arr.length && arr[yt][xt] == type) {
                        yt++;
                        xt++;
                        add++;
                    }
                    xt--;
                    yt--;
                    Point end = new Point(xt, yt);
                    if (!start.equals(end)) {
                        res.add(new Line(start, end, type));
                    }
                }
            }
            for (int sub = 0; arr.length - 1 - sub >= 0; sub++) {
                int xt = arr.length - 1;
                int yt = i;

                char type = 123;
                if (arr[yt][xt] == CROSS || arr[yt][xt] == ZERO) {
                    type = arr[yt][xt];
                }
                if (type != 123) {
                    Point start = new Point(xt, yt);
                    yt++;
                    xt--;
                    while (xt >= 0 && yt < arr.length && arr[yt][xt] == type) {
                        yt++;
                        xt--;
                        sub++;
                    }
                    xt++;
                    yt--;
                    Point end = new Point(xt, yt);
                    if (!start.equals(end)) {
                        res.add(new Line(start, end, type));
                    }
                }
            }
        }


        return res.toArray(new Line[0]);
    }

    private static String getString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("%02d", arr.length));
        sb.append("19");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sb.append(arr[i][j]);
            }
        }
        //field = sb.toString();
        return sb.toString();
    }

    private static void getArr() {
        int len = (int) Math.sqrt(field.length());
        char[] str = field.toCharArray();
        arr = new char[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                arr[i][j] = str[j + i * len];
            }
        }
        /*for (int i = 0; i < arr.length; i++) {
            System.err.println(new String(arr[i]));
        }*/
    }

    private static void doStep(int x, int y) {
        arr[y][x] = myChar;
    }

    private static void doStep(Point p) {
        doStep(p.x, p.y);
    }

    private static Point getPlaceToStep() {
        for (Line l :getLines()) { // 4 на тек ходе
            if(arr[l.start.y][l.start.x]==myChar&&l.len==4) {
                switch (l.dir) {
                    case HORIZ:
                        if (l.start.x - 1 >= 0 && arr[l.start.y][l.start.x - 1] == EMPTY) {
                            return new Point(l.start.x - 1, l.start.y);
                        }
                        if (l.end.x + 1 < arr.length && arr[l.end.y][l.end.x + 1] == EMPTY) {
                            return new Point(l.end.x + 1, l.end.y);
                        }
                        break;
                    case VERT:
                        if (l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x] == EMPTY) {
                            return new Point(l.start.x, l.start.y - 1);
                        }
                        if (l.end.y + 1 < arr.length && arr[l.end.y + 1][l.end.x] == EMPTY) {
                            return new Point(l.end.x, l.end.y + 1);
                        }
                        break;
                    case DIAGR:
                        if (l.start.x - 1 >= 0 && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x - 1] == EMPTY) {
                            return new Point(l.start.x - 1, l.start.y - 1);
                        }
                        if (l.end.y + 1 < arr.length && l.end.x + 1 < arr.length && arr[l.end.y + 1][l.end.x + 1] == EMPTY) {
                            return new Point(l.end.x + 1, l.end.y + 1);
                        }
                        break;
                    case DIAGL:
                        if (l.start.x + 1 < arr.length && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x + 1] == EMPTY) {
                            return new Point(l.start.x + 1, l.start.y - 1);
                        }
                        if (l.end.y + 1 < arr.length && l.end.x - 1 >= 0 && arr[l.end.y + 1][l.end.x - 1] == EMPTY) {
                            return new Point(l.end.x - 1, l.end.y + 1);
                        }
                        break;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {  //изи вин
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] == EMPTY) {
                    arr[i][j] = myChar;
                    Line[] lines = getLines();
                    for (Line l : lines) {
                        if (arr[l.start.y][l.start.x]==myChar&&l.len == 5) {
                            switch (l.dir) {
                                case HORIZ:
                                    if (l.start.x - 1 >= 0 && arr[l.start.y][l.start.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.x + 1 < arr.length && arr[l.end.y][l.end.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case VERT:
                                    if (l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && arr[l.end.y + 1][l.end.x] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case DIAGR:
                                    if (l.start.x - 1 >= 0 && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && l.end.x + 1 < arr.length && arr[l.end.y + 1][l.end.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case DIAGL:
                                    if (l.start.x + 1 < arr.length && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && l.end.x - 1 >= 0 && arr[l.end.y + 1][l.end.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                            }
                            arr[i][j] = EMPTY;
                            return new Point(j, i);
                        }
                    }
                    arr[i][j] = EMPTY;
                }
            }
        }

        for (Line l :getLines()) { // 4 на тек ходе
            if(l.len==4) {
                switch (l.dir) {
                    case HORIZ:
                        if (l.start.x - 1 >= 0 && arr[l.start.y][l.start.x - 1] == EMPTY) {
                            return new Point(l.start.x - 1, l.start.y);
                        }
                        if (l.end.x + 1 < arr.length && arr[l.end.y][l.end.x + 1] == EMPTY) {
                            return new Point(l.end.x + 1, l.end.y);
                        }
                        break;
                    case VERT:
                        if (l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x] == EMPTY) {
                            return new Point(l.start.x, l.start.y - 1);
                        }
                        if (l.end.y + 1 < arr.length && arr[l.end.y + 1][l.end.x] == EMPTY) {
                            return new Point(l.end.x, l.end.y + 1);
                        }
                        break;
                    case DIAGR:
                        if (l.start.x - 1 >= 0 && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x - 1] == EMPTY) {
                            return new Point(l.start.x - 1, l.start.y - 1);
                        }
                        if (l.end.y + 1 < arr.length && l.end.x + 1 < arr.length && arr[l.end.y + 1][l.end.x + 1] == EMPTY) {
                            return new Point(l.end.x + 1, l.end.y + 1);
                        }
                        break;
                    case DIAGL:
                        if (l.start.x + 1 < arr.length && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x + 1] == EMPTY) {
                            return new Point(l.start.x + 1, l.start.y - 1);
                        }
                        if (l.end.y + 1 < arr.length && l.end.x - 1 >= 0 && arr[l.end.y + 1][l.end.x - 1] == EMPTY) {
                            return new Point(l.end.x - 1, l.end.y + 1);
                        }
                        break;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {    //4 на след ходе
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] == EMPTY) {
                    arr[i][j] = theirChar;
                    Line[] lines = getLines();
                    for (Line l : lines) {
                        if (arr[l.start.y][l.start.x]==theirChar&&l.len >= 4) {
                            switch (l.dir) {
                                case HORIZ:
                                    if (l.start.x - 1 >= 0 && arr[l.start.y][l.start.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.x + 1 < arr.length && arr[l.end.y][l.end.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case VERT:
                                    if (l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && arr[l.end.y + 1][l.end.x] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case DIAGR:
                                    if (l.start.x - 1 >= 0 && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && l.end.x + 1 < arr.length && arr[l.end.y + 1][l.end.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case DIAGL:
                                    if (l.start.x + 1 < arr.length && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && l.end.x - 1 >= 0 && arr[l.end.y + 1][l.end.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                            }

                            arr[i][j] = EMPTY;
                            return new Point(j, i);
                        }
                    }
                    arr[i][j] = EMPTY;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {   //свой след ход 4
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] == EMPTY) {
                    arr[i][j] = myChar;
                    Line[] lines = getLines();
                    for (Line l : lines) {
                        if (arr[l.start.y][l.start.x]==myChar&&l.len == 4) {
                            switch (l.dir) {
                                case HORIZ:
                                    if (l.start.x - 1 >= 0 && arr[l.start.y][l.start.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.x + 1 < arr.length && arr[l.end.y][l.end.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case VERT:
                                    if (l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && arr[l.end.y + 1][l.end.x] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case DIAGR:
                                    if (l.start.x - 1 >= 0 && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && l.end.x + 1 < arr.length && arr[l.end.y + 1][l.end.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                                case DIAGL:
                                    if (l.start.x + 1 < arr.length && l.start.y - 1 >= 0 && arr[l.start.y - 1][l.start.x + 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    if (l.end.y + 1 < arr.length && l.end.x - 1 >= 0 && arr[l.end.y + 1][l.end.x - 1] == EMPTY) {
                                        arr[i][j] = EMPTY;
                                        return new Point(j, i);
                                    }
                                    break;
                            }
                            arr[i][j] = EMPTY;
                            return new Point(j, i);
                        }
                    }
                    arr[i][j] = EMPTY;
                }
            }
        }


        /*for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] == myChar) {

                }
            }
        }*/

        int x, y;
        while (true) {
            x = Math.max(0,(int) (Math.random() * (arr.length-1)));
            y = Math.max(0,(int) (Math.random() * (arr.length-1)));
            if (arr[y][x] == EMPTY) {
                break;
            }
        }
        return new Point(x, y);
    }

    private static boolean noEmptyPlaces() {
        for (int i = 0; i < field.length(); i++) {
            if (field.charAt(i) == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private static void findMySymbol() {
//        if (myChar == 0) {
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
            myChar = CROSS;
            theirChar = ZERO;
        } else {
            myChar = ZERO;
            theirChar = CROSS;
        }
//        }
    }
}

class Line {
    enum directional {
        VERT, HORIZ, DIAGR, DIAGL
    }

    public Point start, end;
    public char type;
    public int len;
    public directional dir;

    public Line(Point start, Point end, char type) {
        this.start = start;
        this.end = end;
        this.type = type;
        len = (int) start.distance(end) + 1;
        if (start.y == end.y) {
            dir = directional.HORIZ;
        } else if (start.x == end.x) {
            dir = directional.VERT;
        } else if (start.x < end.x) {
            dir = directional.DIAGR;
            len--;
        } else {
            dir = directional.DIAGL;
            len--;
        }


    }

    @Override
    public String toString() {
        return "Line{" +
                "start=" + start +
                ", end=" + end +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;

        Line line = (Line) o;

        if (type != line.type) return false;
        if (start != null ? !start.equals(line.start) : line.start != null) return false;
        return end != null ? end.equals(line.end) : line.end == null;

    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (int) type;
        return result;
    }
}