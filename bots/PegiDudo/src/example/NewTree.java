package example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olymp15 on 14.05.2016.
 */
public class NewTree {
    String[] field;
    List<NewTree> list;

    public NewTree(String[] field) {
        this.field = field;
        this.list = new ArrayList<>();
    }

    public void fillList(char charX, char charY) {
        String[] gfield = this.field.clone();
        for (int i = 0; i < gfield.length; i++) {
            for (int j = 0; j < gfield[i].length(); j++) {
                if (gfield[i].charAt(j) == charX) {
                    try {
                        replaceChar(gfield[i], i, j + 1, charY);
                        replaceChar(gfield[i + 1], i + 1, j + 1, charY);
                        replaceChar(gfield[i + 1], i + 1, j, charY);
                        replaceChar(gfield[i + 1], i + 1, j - 1, charY);
                        replaceChar(gfield[i], i, j - 1, charY);
                        replaceChar(gfield[i - 1], i - 1, j - 1, charY);
                        replaceChar(gfield[i - 1], i - 1, j, charY);
                        replaceChar(gfield[i - 1], i - 1, j + 1, charY);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
    }

    public void replaceChar (String s, int i, int j, char charY) {
        try {
            if (s.charAt(j) == Bot.EMPTY) {
                NewTree newGamefield = new NewTree(this.field.clone());
                if (j-1 < 0) j = 0;
                if (j+1 > s.length()) j = s.length();
                s = s.substring(0, j) + charY + s.substring(j + 1);
                newGamefield.field[i] = s;
                this.list.add(newGamefield);
            }
        } catch (Exception e) {
            //continue;
        }
    }
}
