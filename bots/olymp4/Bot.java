package example;

import sun.font.FontRunIterator;

import java.io.*;
import java.util.Random;

public class Bot {

    private final static int mapSize = 19;

    private final static char CROSS = 'x';
    private final static char ZERO = 'o';
    private final static char EMPTY = '-';
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static char mySymbol;
    private static String field;
    private static PrintWriter out;

    public static void main(String... args) throws IOException {
        out = new PrintWriter(new FileWriter("out"+(new Random().nextInt(544))+".txt"));
        out.println("Start!!!");
        out.flush();
        try {
            field = reader.readLine().substring(2);
            mySymbol = findMySymbol();
            while (true) {
                int pos = needDefence();
                pos = (pos == -1) ? findAttack() : pos;
                pos = (pos == -1) ? getPlaceToStep() : pos;

                String result = doStep(pos);
                System.out.println("19" + result);

                field = reader.readLine().substring(2);
                if (noEmptyPlaces()) {
                    break;
                }
            }
            System.out.println("Game over");
        }
        catch (Exception e){
            for (StackTraceElement elem : e.getStackTrace())
                out.println(elem);
            out.flush();
        }
    }

    private static int needDefence(){
        for (int i = 0; i < mapSize-1; i++) {
            for (int j = 0; j < mapSize-1; j++) {
                String hor = "";
                String vert = "";
                String diag = "";
                String obrdiag = "";

                for (int k = 0; k < 6; k++) {
                    hor+=get(i,j+k);
                    vert+=get(i+k,j);
                    diag+=get(i+k,j+k);
                    obrdiag+=get(i+k,j-k);
                    //смотрим четырки
                    if (k == 4){
                        int defPos =getDefInd5(hor);
                        if (defPos != -1){
                            return i*mapSize+j+defPos;
                        }
                        defPos =getDefInd5(vert);
                        if (defPos != -1){
                            return (i+defPos)*mapSize+j;
                        }
                        defPos =getDefInd5(diag);
                        if (defPos != -1){
                            return (i+defPos)*mapSize+j+defPos;
                        }
                        defPos =getDefInd5(obrdiag);
                        if (defPos != -1){
                            return (i+defPos)*mapSize+j-defPos;
                        }
                    }
                }
                //смотрим тройки
                int defPos =getDefInd6(hor);
                if (defPos != -1){
                    return i*mapSize+j+defPos;
                }
                defPos =getDefInd6(vert);
                if (defPos != -1){
                    return (i+defPos)*mapSize+j;
                }
                defPos =getDefInd6(diag);
                if (defPos != -1){
                    return (i+defPos)*mapSize+j+defPos;
                }
                defPos =getDefInd6(obrdiag);
                if (defPos != -1){
                    return (i+defPos)*mapSize+j-defPos;
                }

            }
        }
        return -1;
    }

    private static int findAttack(){
        for (int i = 0; i < mapSize-1; i++) {
            for (int j = 0; j < mapSize-4; j++) {
                String hor = "";
                String vert = "";
                String diag = "";
                String obrdiag = "";
                for (int k = 0; k < 6; k++) {
                    hor+=get(i,j+k);
                    vert+=get(i+k,j);
                    diag+=get(i+k,j+k);
                    obrdiag+=get(i+k,j-k);
                    //смотрим четырки
                    if (k == 4){
                        int attackPos =getAttackInd5(hor);
                        if (attackPos != -1){
                            return i*mapSize+j+attackPos;
                        }
                        attackPos =getAttackInd5(vert);
                        if (attackPos != -1){
                            return (i+attackPos)*mapSize+j;
                        }
                        attackPos =getAttackInd5(diag);
                        if (attackPos != -1){
                            return (i+attackPos)*mapSize+j+attackPos;
                        }
                        attackPos =getAttackInd5(obrdiag);
                        if (attackPos != -1){
                            return (i+attackPos)*mapSize+j-attackPos;
                        }

                    }
                }
                //смотрим тройки
                int attackPos =getAttackInd6(hor);
                if (attackPos != -1){
                    return i*mapSize+j+attackPos;
                }
                attackPos =getAttackInd6(vert);
                if (attackPos != -1){
                    return (i+attackPos)*mapSize+j;
                }
                attackPos =getAttackInd6(diag);
                if (attackPos != -1){
                    return (i+attackPos)*mapSize+j+attackPos;
                }
                attackPos =getAttackInd6(obrdiag);
                if (attackPos != -1){
                    return (i+attackPos)*mapSize+j-attackPos;
                }
            }
        }
        return -1;
    }

    private static int getDefInd5(String line){
        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == notMy()&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == notMy())
            return 0;
        if (line.charAt(0) == notMy()&&
                line.charAt(1) == notMy()&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == EMPTY)
            return 4;
        if (line.charAt(0) == notMy()&&
                line.charAt(1) == notMy()&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == EMPTY&&
                line.charAt(4) == notMy())
            return 3;
        if (line.charAt(0) == notMy()&&
                line.charAt(1) == EMPTY&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == notMy())
            return 1;
        if (line.charAt(0) == notMy()&&
                line.charAt(1) == notMy()&&
                line.charAt(2) == EMPTY&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == notMy())
            return 2;
        return -1;
    }

    private static int getDefInd6(String line){
        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == notMy()&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == EMPTY&&
                line.charAt(5) == EMPTY)
            return 4;

        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == EMPTY&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == notMy()&&
                line.charAt(5) == EMPTY)
            return 1;

        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == notMy()&&
                line.charAt(2) == EMPTY&&
                line.charAt(3) == notMy()&&
                line.charAt(4) == notMy()&&
                line.charAt(5) == EMPTY)
            return 2;
        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == notMy()&&
                line.charAt(2) == notMy()&&
                line.charAt(3) == EMPTY&&
                line.charAt(4) == notMy()&&
                line.charAt(5) == EMPTY)
            return 4;
        return -1;
    }

    private static int getAttackInd5(String line){
        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == mySymbol)
            return 0;
        if (line.charAt(0) == mySymbol&&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == EMPTY)
            return 4;
        if (line.charAt(0) == mySymbol&&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == EMPTY&&
                line.charAt(4) == mySymbol)
            return 3;
        if (line.charAt(0) == mySymbol&&
                line.charAt(1) == EMPTY&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == mySymbol)
            return 1;
        if (line.charAt(0) == mySymbol&&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == EMPTY&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == mySymbol)
            return 2;
        return -1;
    }

    private static int getAttackInd6(String line){
        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == EMPTY&&
                line.charAt(5) == EMPTY)
            return 4;

        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == EMPTY&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == mySymbol&&
                line.charAt(5) == EMPTY)
            return 1;

        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == EMPTY&&
                line.charAt(3) == mySymbol&&
                line.charAt(4) == mySymbol&&
                line.charAt(5) == EMPTY)
            return 2;
        if (line.charAt(0) == EMPTY &&
                line.charAt(1) == mySymbol&&
                line.charAt(2) == mySymbol&&
                line.charAt(3) == EMPTY&&
                line.charAt(4) == mySymbol&&
                line.charAt(5) == EMPTY)
            return 4;
        return -1;
    }

    private static char notMy(){
        return (mySymbol == CROSS)?ZERO:CROSS;
    }

    private static String doStep(int place) {
        return field.substring(0, place) + mySymbol + field.substring(place + 1);
    }

    private static char get(int x,int y){
        if (x*mapSize+y < field.length() && x*mapSize+y >=0 )
            return field.charAt(x*mapSize+y);
        else
            return '#';
    }

    private static int getPlaceToStep() {
        int place;
//        for (int i = 0; i < mapSize-2; i++) {
//            for (int j = 0; j < mapSize - 2; j++) {
//                if (get(i,j) == mySymbol && get(i+1,j) == mySymbol &&
//                        get(i-1,j) == EMPTY &&
//                        get(i-1,j-1) == EMPTY &&
//                        get(i-1,j-2) == mySymbol &&
//                        get(i-1,j+1) == mySymbol)
//                    return (i-1)*mapSize+j;
//                if (get(i,j) == EMPTY && get(i+1,j) == mySymbol &&
//                        get(i-1,j) == mySymbol &&
//                        get(i,j+1) == mySymbol &&
//                        get(i,j-1) == mySymbol)
//                    return (i)*mapSize+j;
//                if (get(i,j) == mySymbol &&
//                        get(i+1,j) == EMPTY &&
//                        get(i+2,j) == mySymbol &&
//                        get(i,j+1) == EMPTY &&
//                        get(i-1,j+2) == mySymbol &&
//                        get(i-2,j+3) == mySymbol)
//                    return (i+1)*mapSize+j;
//
//                if (get(i,j) == EMPTY &&
//                        get(i,j-1) == mySymbol &&
//                        get(i,j+1) == mySymbol &&
//                        get(i-1,j+2) == mySymbol &&
//                        get(i-1,j-1) == mySymbol &&
//                        get(i+2,j-1) == EMPTY)
//                    return (i+2)*mapSize+j-1;
//            }
//        }
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
            if (field.charAt(i) == CROSS) {
                return ZERO;
            }
        }
        return CROSS;
    }
}
