import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Kingprog {
    private static final char CROSS = 'o';
    private static final char ZERO = 'x';
    private static final char EMPTY = '-';
    private static final BufferedReader reader;
    private static String field;

    public static void main(String... args) throws IOException {
        field = reader.readLine();
        while(true)
        {
            if(noEmptyPlaces())
            {
                System.out.println("Game over");
                return;
            }
            char mySymbol = findMySymbol();
            int place = getPlaceToStep();
            String result = doStep(mySymbol, place);
            System.out.println(result);
            while (!reader.ready()) {}
            field = reader.readLine();
        }
    }

    private static String doStep(char mySymbol, int place)
    {
        return field.substring(0, place) + mySymbol + field.substring(place + 1);
    }

    private static int getPlaceToStep()
    {
        int place;
        do
        {
            place = (int)(Math.random() * (double)field.length());
        } while(field.charAt(place) != 45);

        return place;
    }

    private static boolean noEmptyPlaces()
    {
        for(int i = 0; i < field.length(); i++)
        {
            if(field.charAt(i) == 45)
            {
                return false;
            }
        }

        return true;
    }

    private static char findMySymbol()
    {
        int b = 0;

        for(int k = 0; k < field.length(); k++)
        {
            switch(field.charAt(k))
            {
                case 'x':
                    b++;
                    break;
                case 'o':
                    b--;
            }
        }

        if(b > 0)
        {
            return 'o';
        } else
        {
            return 'x';
        }
    }

    static
    {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
}
