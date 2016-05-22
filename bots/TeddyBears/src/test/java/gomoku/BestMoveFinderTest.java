package gomoku;

import org.testng.annotations.Test;

import static gomoku.Main.*;
import static org.testng.Assert.*;

@Test
public class BestMoveFinderTest {
    BestMoveFinder bestMoveFinder = new BestMoveFinder();

    @Test
    void test() {
        Board board = new Board("" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(2, 2, CROSS));
    }

    @Test
    void testLongestLine() {
        checkLongestLine("" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----",
                CROSS,
                0);
        checkLongestLine("" +
                        "-----\n" +
                        "-x-xo\n" +
                        "--xo-\n" +
                        "---x-\n" +
                        "-----",
                CROSS,
                3);
        checkLongestLine("" +
                        "-----\n" +
                        "-x-xo\n" +
                        "--xo-\n" +
                        "---x-\n" +
                        "-----",
                ZERO,
                2);
    }

    @Test
    void test3() {
        Board board = new Board("" +
                "xxxx-\n" +
                "----o\n" +
                "----o\n" +
                "----o\n" +
                "----o"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(0, 4, CROSS));
    }

    @Test
    void test4() {
        Board board = new Board("" +
                "xxxx-\n" +
                "----o\n" +
                "----o\n" +
                "----o\n" +
                "x---o"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(0, 4, ZERO));
    }

    @Test
    void banBadEnemyMove() {
        Board board = new Board("" +
                "x------\n" +
                "-------\n" +
                "-ooo---\n" +
                "-------\n" +
                "-------\n" +
                "-------\n" +
                "x---x--"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(2, 4, CROSS));
    }

    @Test
    void banBadEnemyMove2() {
        Board board = new Board("" +
                "-------\n" +
                "-------\n" +
                "---o---\n" +
                "----o--\n" +
                "xx-xxo-\n" +
                "-------\n" +
                "-------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 2, ZERO));
    }

    @Test
    void testFinish() {
        Board board = new Board("" +
                "-------\n" +
                "-------\n" +
                "---o---\n" +
                "----o--\n" +
                "xx-xxo-\n" +
                "-------\n" +
                "o------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 2, CROSS));
    }

    @Test
    void testFinish2() {
        Board board = new Board("" +
                "-------\n" +
                "--o----\n" +
                "---o---\n" +
                "----o--\n" +
                "xx-xxo-\n" +
                "-------\n" +
                "-------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 2, CROSS));
    }

    @Test
    void testPavel1() {
        Board board = new Board("" +
                "-o-o-----\n"+
                "oxxx-----\n"+
                "--ox-----\n"+
                "--xo-----\n"+
                "---------\n"+
                "---------\n"+
                "---------\n"+
                "---------\n"+
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(1, 4, CROSS));
    }

    @Test
    void testPavel2() {
        Board board = new Board("" +
                "-ooo-----\n"+
                "ox-x-----\n"+
                "o--------\n"+
                "ox-------\n"+
                "o--------\n"+
                "x--------\n"+
                "---------\n"+
                "---------\n"+
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(0, 0, CROSS));
    }

    @Test
    void testPavel3() {
        Board board = new Board("" +
                "-xxxxo---\n"+
                "-oooox---\n"+
                "---------\n"+
                "---------\n"+
                "o--------\n"+
                "x--------\n"+
                "---------\n"+
                "---------\n"+
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(0, 0, CROSS));
    }

    @Test
    void testPavel4() {
        Board board = new Board("" +
                "x-x-x-xo-\n"+
                "---------\n"+
                "---------\n"+
                "o--------\n"+
                "o--------\n"+
                "o--------\n"+
                "---------\n"+
                "---------\n"+
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(0, 3, CROSS));
    }

    @Test
    void testPavel5() {
        Board board = new Board("" +
                "--xxxo---\n"+
                "-oo---x--\n"+
                "x---o----\n"+
                "xo-x-----\n"+
                "x-----o--\n"+
                "o--o-o---\n"+
                "---------\n"+
                "---------\n"+
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(0, 0, CROSS));
    }

    @Test
    void testPavel6() {
        Board board = new Board("" +
                "---------\n"+
                "---------\n"+
                "----x----\n"+
                "---x-----\n"+
                "---oxo---\n"+
                "--ox--o--\n"+
                "---------\n"+
                "--------x\n"+
                "--------------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(3, 4, ZERO));
    }

    @Test
    void testPavel7() {
        Board board = new Board("" +
                "----ooo--\n" +
                "oooox----\n" +
                "xxxox----\n" +
                "xxx------\n" +
                "xxxox----\n" +
                "ooo------\n" +
                "---------\n" +
                "---------\n" +
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(3, 4, CROSS));
    }

    @Test
    void testPavel8() {
        Board board = new Board("" +
                "---------\n" +
                "---------\n" +
                "--oo-----\n" +
                "----xx---\n" +
                "---xox---\n" +
                "----o----\n" +
                "---------\n" +
                "---------\n" +
                "---------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(2, 5, CROSS));
    }

    @Test
    void testAlex1() {
        Board board = new Board("" +
                "-------------\n" +
                "-------------\n" +
                "----o--------\n" +
                "-----x-------\n" +
                "---oxxx------\n" +
                "---ooo-x-----\n" +
                "------x-o----\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 8, CROSS));
    }

    @Test
    void testAlex2() {
        Board board = new Board("" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "----o--------\n" +
                "-----x--x----\n" +
                "-----x-o-----\n" +
                "----xo-------\n" +
                "-----o-------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 6, CROSS));
    }

    @Test
    void testAlex3() {
        Board board = new Board("" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------o-----\n" +
                "------xx-----\n" +
                "-----oox-----\n" +
                "------xo-----\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(5, 8, CROSS));
    }

    @Test
    void testAlex4() {
        Board board = new Board("" +
                "-------------\n" +
                "-------------\n" +
                "-------x-----\n" +
                "------o------\n" +
                "--------o----\n" +
                "-----oxx-----\n" +
                "-----xoxo----\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 7, CROSS));
    }

    @Test()
    void testAlex5() {
        Board board = new Board("" +
                "-------------\n" +
                "-------------\n" +
                "---------o---\n" +
                "-------ox----\n" +
                "----o--x-----\n" +
                "----x-xxo----\n" +
                "-o-oxx-xo----\n" +
                "--xoo-ox-----\n" +
                "---x---o-----\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------"
        );
        Move move = bestMoveFinder.findMove(board);
//        assertEquals(move, new Move(5, 5, CROSS));
    }

    @Test
    void testAlex6() {
        Board board = new Board("" +
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-oox-x-------\n" +
                "----ooo------\n" +
                "--xxox-------\n" +
                "--xox--------\n" +
                "-----x-------\n" +
                "-------------\n" +
                "-------o-----\n" +
                "-------------\n" +
                "-------------\n" +
                "-------------"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(4, 2, CROSS));
    }

    @Test
    void testBigBoardFirstMove() {
        Board board = new Board("" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n" +
                "-------------------\n"
        );
        Move move = bestMoveFinder.findMove(board);
        assertEquals(move, new Move(9, 9, CROSS));
    }

    //utils

    private void checkLongestLine(String s, char who, int expected) {
        assertEquals(BestMoveFinder.getLongestLine(new Board(s), who), expected);
    }
}