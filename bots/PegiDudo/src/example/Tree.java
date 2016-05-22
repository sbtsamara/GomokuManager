package example;

import com.sun.org.apache.xpath.internal.SourceTree;
import jdk.internal.dynalink.support.BottomGuardingDynamicLinker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olymp15 on 14.05.2016.
 */
public class Tree {
    String[] gamefield;
    List<Tree> turns = new ArrayList<>();

    public Tree(String[] gamefield) {
        this.gamefield = gamefield;
    }
    
    public void fullTurns() {
        String[] gfield = this.gamefield;
        for (int i = 0; i < gfield.length; i++) {
            for (int j = 0; j < gfield[i].length(); j++) {
                if (gfield[i].charAt(j) == Bot.mySymbol) {
                    try {
                        replaceChar(gfield[i], i, j + 1);
                        replaceChar(gfield[i + 1], i + 1, j + 1);
                        replaceChar(gfield[i + 1], i + 1, j);
                        replaceChar(gfield[i + 1], i + 1, j - 1);
                        replaceChar(gfield[i], i, j - 1);
                        replaceChar(gfield[i - 1], i - 1, j - 1);
                        replaceChar(gfield[i - 1], i - 1, j);
                        replaceChar(gfield[i - 1], i - 1, j + 1);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
    }

    public void replaceChar (String s, int i, int j) {
        try {
            if (s.charAt(j) == Bot.EMPTY) {
                Tree newGamefield = new Tree(this.gamefield);
                if (j-1 < 0) j = 0;
                if (j+1 > s.length()) j = s.length();
                s = s.substring(0, j) + Bot.mySymbol + s.substring(j + 1);
                newGamefield.gamefield[i] = s;
                this.turns.add(newGamefield);
                Bot.printField(newGamefield.gamefield);
            }
        } catch (Exception e) {
            //continue;
        }
    }
}
