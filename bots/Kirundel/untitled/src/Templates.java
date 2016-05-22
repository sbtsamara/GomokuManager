import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by olymp22 on 14.05.2016.
 */
public class Templates {

    List<Template> templates = new ArrayList<>();

    String s = "25\n" +
            "\n" +
            "1 6 7000\n" +
            ".x.xx.\n" +
            "\n" +
            "1 6 4000\n" +
            "ox.xx.\n" +
            "\n" +
            "5 5 9000\n" +
            "----x\n" +
            "---.-\n" +
            "--x--\n" +
            "-x---\n" +
            "x----\n" +
            "\n" +
            "5 5 9000\n" +
            "----x\n" +
            "---x-\n" +
            "--x--\n" +
            "-.---\n" +
            "x----\n" +
            "\n" +
            "5 5 9000\n" +
            "----x\n" +
            "---x-\n" +
            "--.--\n" +
            "-x---\n" +
            "x----\n" +
            "\n" +
            "5 5 4000\n" +
            "----x\n" +
            "---x-\n" +
            "--.--\n" +
            "-x---\n" +
            "o----\n" +
            "\n" +
            "6 6 7000\n" +
            "-----.\n" +
            "----x-\n" +
            "---x--\n" +
            "--.---\n" +
            "-x----\n" +
            ".-----\n" +
            "\n" +
            "1 5 100000\n" +
            "xxxxx\n" +
            "\n" +
            "1 6 9000\n" +
            ".xxxx.\n" +
            "\n" +
            "1 6 5000\n" +
            ".xxxxo\n" +
            "\n" +
            "1 5 6000\n" +
            ".xxx.\n" +
            "\n" +
            "1 4 3000\n" +
            "xxxo\n" +
            "\n" +
            "1 4 4000\n" +
            ".xx.\n" +
            "\n" +
            "4 4 4000\n" +
            ".-.-\n" +
            ".xx.\n" +
            "--x-\n" +
            "--..\n" +
            "\n" +
            "5 5 100000\n" +
            "----x\n" +
            "---x-\n" +
            "--x--\n" +
            "-x---\n" +
            "x----\n" +
            "\n" +
            "6 6 9000\n" +
            "-----.\n" +
            "----x-\n" +
            "---x--\n" +
            "--x---\n" +
            "-x----\n" +
            ".-----\n" +
            "\n" +
            "6 6 8000\n" +
            "-----o\n" +
            "----x-\n" +
            "---x--\n" +
            "--x---\n" +
            "-x----\n" +
            ".-----\n" +
            "\n" +
            "5 5 6000\n" +
            "----.\n" +
            "---x-\n" +
            "--x--\n" +
            "-x---\n" +
            ".----\n" +
            "\n" +
            "5 5 4000\n" +
            "----o\n" +
            "---x-\n" +
            "--x--\n" +
            "-x---\n" +
            ".----\n" +
            "\n" +
            "4 4 3000\n" +
            "---.\n" +
            "--x-\n" +
            "-x--\n" +
            ".---\n" +
            "\n" +
            "3 4 3000\n" +
            "x.x-\n" +
            "-xx-\n" +
            "----\n" +
            "\n" +
            "3 4 4000\n" +
            "x.x-\n" +
            "-xx-\n" +
            "--.-\n" +
            "\n" +
            "4 5 3000\n" +
            "-----\n" +
            "-x.x-\n" +
            "--x--\n" +
            "-----\n" +
            "\n" +
            "4 5 2000\n" +
            "--.--\n" +
            "-xox-\n" +
            "--x--\n" +
            "-----\n" +
            "\n" +
            "4 5 4000\n" +
            "-----\n" +
            "-xxx-\n" +
            "--x--\n" +
            "-----";
    public void load() throws FileNotFoundException {
        Scanner scanner = new Scanner(s);
        int count = scanner.nextInt();
        for(int i = 0; i < count; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int score = scanner.nextInt();
            String[] templateString = new String[x];
            for(int j = 0; j < x; j++) {
                templateString[j] = scanner.next();
            }
            Template template = new Template(x, y, templateString, score);
            templates.add(template);
            for(int j = 0; j < 3; j++) {
                Template next = template.rotate();
                boolean exist = false;
                for(Template t: templates) {
                    if(t.equals(next)) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) templates.add(next);
            }
        }
    }
}
