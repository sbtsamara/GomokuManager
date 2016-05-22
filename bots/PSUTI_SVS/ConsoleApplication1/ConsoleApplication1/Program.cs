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
                board = MakeAction(board);
                Console.WriteLine(board);
            }
            Console.WriteLine("Game over");
        }

        private static string MakeAction(string board)
        {
            //Создать двумерный массив
            char[,] arr = new char[BOARD_SIZE, BOARD_SIZE];
            int x = 2;
            for(int i = 0; i < BOARD_SIZE; i++)
            {
                for(int j=0;j< BOARD_SIZE; j++)
                {
          
                    arr[i, j] = board[x];
                    x++;
                }
            }

            //Определить фигуру противника
            char enemyFigure;
            if (GetCurrentFigure(board) == 'x')
            {
                enemyFigure = 'o';
            }
            else
            {
                enemyFigure = 'x';
            }

            //Создание массива вариантов хода
            int MovesLength = 0;
            int [,] Moves = new int[361, 3];
            for (int i = 0; i < BOARD_SIZE; i++)
            {
                for (int j = 0; j < BOARD_SIZE; j++)
                {

                    if (arr[i, j] != '-') // находим х или о
                    {
                        if (i + 1 < BOARD_SIZE && j + 1 < BOARD_SIZE) { // проверяем границы на возможность постановки фигуры
                            if (arr[i + 1, j + 1] == '-')
                            {
                                Moves[MovesLength, 0] = i+1;
                                Moves[MovesLength, 1] = j+1;
                                MovesLength++;
                            }
                        }
                        if (i - 1 >= 0 && j - 1 >= 0) {
                            if (arr[i - 1, j - 1] == '-')
                            {
                                Moves[MovesLength, 0] = i - 1;
                                Moves[MovesLength, 1] = j - 1;
                                MovesLength++;
                            }
                        }
                        if (i + 1 < BOARD_SIZE ) {
                            if (arr[i + 1, j] == '-')
                            {
                                Moves[MovesLength, 0] = i + 1;
                                Moves[MovesLength, 1] = j;
                                MovesLength++;
                            }
                        }
                        if (i - 1 >= 0 )
                        {
                            if (arr[i - 1, j] == '-')
                            {
                                Moves[MovesLength, 0] = i - 1;
                                Moves[MovesLength, 1] = j;
                                MovesLength++;
                            }
                        }
                        if (i - 1 >= 0 && j + 1 < BOARD_SIZE)
                        {
                            if (arr[i - 1, j+1] == '-')
                            {
                                Moves[MovesLength, 0] = i - 1;
                                Moves[MovesLength, 1] = j +1 ;
                                MovesLength++;
                            }
                        }
                        if (i + 1 < BOARD_SIZE && j - 1 >= 0)
                        {
                            if (arr[i + 1, j - 1] == '-')
                            {
                                Moves[MovesLength, 0] = i + 1;
                                Moves[MovesLength, 1] = j - 1;
                                MovesLength++;
                            }
                        }
                        if (j + 1 < BOARD_SIZE)
                        {
                            if (arr[i , j + 1] == '-')
                            {
                                Moves[MovesLength, 0] = i ;
                                Moves[MovesLength, 1] = j + 1;
                                MovesLength++;
                            }
                        }
                        if (j - 1 >= 0)
                        {
                            if (arr[i , j - 1] == '-')
                            {
                                Moves[MovesLength, 0] = i ;
                                Moves[MovesLength, 1] = j - 1;
                                MovesLength++;
                            }
                        }
                       
                    }
                }
            }

            int bufX = 0, bufY = 0;
            bool done = false;
            for (int i = 0; i < MovesLength; i++)
            {
                if (Moves[i, 0] == bufX && Moves[i, 1] == bufY)
                {
                    arr[bufX, bufY] = GetCurrentFigure(board);
                    done = true;
                }
            }

            if (!done)
            {
                arr[Moves[0, 0], Moves[0, 1]] = GetCurrentFigure(board);
            }

            string resultBoard = BOARD_SIZE.ToString();

            for (int i = 0; i < BOARD_SIZE; i++)
            {
                for (int j = 0; j < BOARD_SIZE; j++)
                {
                    resultBoard += arr[i, j];
                }
            }

            return resultBoard;
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
