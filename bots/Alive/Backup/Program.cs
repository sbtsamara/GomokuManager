using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ru.sbt.codeit2016.CSBot
{

/*
   Работа бота (в цикле):
   1. получение из входного потока (INPUT, STDIN, System.in) строки с текущим состоянием игрового поля
   2. определение наилучшего хода
   3. формирование нового состояния поля
   4. отправка результата в выходной поток (OUTPUT, STDOUT, System.out)

   Формат строки, описывающей поле:
   0..1 - 2 цифры, составляющие число, значение которого равно ширине игрового поля. Константа = 19
   2..362 - символы, описывающие 361 (19*19) клетку игрового поля (слева направо, сверху вниз):
     'x' - в данной клетке крестик
     'o' - в данной клетке нолик
     '-' - пустая клетка
     прочие символы недопустимы

   Правила игры:
   1. первый ход совершают играющие крестиком
   2. игроки совершают ходы по очереди
   3. побеждает тот, кто первым выстроит 5 или более своих символов в ряд - по горизонтали, по вертикали или по одной из двух диагоналей

   см. также https://ru.wikipedia.org/wiki/Гомоку
*/

    class Program
    {
        private const int BOARD_SIZE = 19;                          // размер игрового поля
        private static Random random = new Random();

        // цикл ввода-вывода
        static void Main(string[] args)
        {
            while (true)
            {
                var board = Console.ReadLine();
                if (board.Length != (2 + BOARD_SIZE * BOARD_SIZE))  // если входные данные некорректной длины, считаем, что игра завершена
                    break;
                if (board.IndexOf('-') < 0)                         // если нет пустых ячеек, считаем, что игра завершена
                    break;
                board = MakeMove(board);
                Console.WriteLine(board);
            }
            Console.WriteLine("Game over");
        }

        // делает ход на случайное пустое поле
        private static string MakeMove(string board)
        {
            int i = 0;
            do
            {
                i = random.Next(BOARD_SIZE * BOARD_SIZE) + 2;
            } while (!board.Substring(i, 1).Equals("-"));
            return board.Substring(0, i - 1) + GetCurrentFigure(board) + board.Substring(i + 1);
        }

        // определяет, чей сейчас ход
        private static char GetCurrentFigure(string board)
        {
            int counter = 0;
            foreach (char c in board.ToLower().ToCharArray())
            {
                switch (c)
                {
                    case 'x':
                        counter++;
                        break;
                    case 'o':
                        counter--;
                        break;
                }
            }
            return (counter <= 0) ? 'x' : 'o';
        }
    }
}
