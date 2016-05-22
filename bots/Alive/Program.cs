using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSBot
{
    class Program
    {
        private const char CROSS = 'x';
        private const char ZERO = 'o';
        private const char EMPTY = '-';

        private static HashSet<Vector> enemyHistory = new HashSet<Vector>();
        private static HashSet<Vector> currentHistory = new HashSet<Vector>();
        private static HashSet<Path> enemyPath = new HashSet<Path>();
        private static HashSet<Path> currentPath = new HashSet<Path>();

        private static char current = ' ';
        private static char enemy = ' ';

        private const int BOARD_SIZE = 19;
        private static Random random = new Random();

        static void Main(string[] args)
        {
            while (true)
            {
                string board = Console.ReadLine().ToLower();
                if (board.Length != (2 + BOARD_SIZE * BOARD_SIZE))
                    break;
                board = board.Substring(2);
                if (board.IndexOf('-') < 0)
                    break;

                if (current == ' ')
                {
                    current = GetCurrentFigure(board);
                    enemy = current == CROSS ? ZERO : CROSS;
                }

                Vector lastStep = GetLastStep(board, enemy);
                if (lastStep != null)
                    InsertStep(lastStep, true);
                List<Option> options = SearchOptions(enemyPath.ToList(), board);
                Union(options, true);

                char leader = GetLeader(board);
                List<Option> currentOptions = SearchOptions(currentPath.ToList(), board);
                Vector currentStep = null;
                try
                {
                    if (leader == current)
                    {
                        foreach (Path item in currentPath)
                        {
                            int sides;
                            if (item.length == 4 && GetMaxLength(item, enemy, board, BOARD_SIZE, out sides) >= 5)
                                currentStep = GetUnion(item, board);
                        }
                        if (currentStep == null)
                            foreach (Option item in currentOptions)
                            {
                                if (item.countStep == 1 && item.first.length + item.second.length + 1 >= 5)
                                    currentStep = GetIntersection(item.first, item.second);
                            }
                    }
                    if (currentStep == null)
                        foreach (Option option in options)
                            if (option.countStep == 1 && option.first.length + option.second.length + 1 >= 5)
                                currentStep = GetIntersection(option.first, option.second);
                    int tmp;
                    if (currentStep == null)
                        foreach (Path path in enemyPath)
                            if (path.length == 4 && GetMaxLength(path, current, board, BOARD_SIZE, out tmp) >= 5)
                                currentStep = GetUnion(path, board);
                    if (leader == enemy && currentStep == null)
                    {
                        foreach (Path path in enemyPath)
                            if (path.length == 3 && GetMaxLength(path, current, board, BOARD_SIZE, out tmp) >= 5 && tmp == 2)
                                currentStep = GetUnion(path, board);
                    }
                    Path target = null;
                    if (currentStep == null)
                    {
                        foreach (Path item in currentPath)
                            if ((target == null || target.length < item.length) && GetMaxLength(item, enemy, board, BOARD_SIZE, out tmp) >= 5)
                                target = item;
                        if (target != null)
                            currentStep = GetUnion(target, board);
                        else
                            currentStep = GetRandomPosition(board);
                    }
                }
                catch (Exception e)
                {
                    currentStep = GetRandomPosition(board);
                }

                InsertStep(currentStep, false);
                currentOptions = SearchOptions(currentPath.ToList(), board);
                Union(currentOptions, false);

                int position = GetPosition(currentStep);
                Console.WriteLine("19{0}{1}{2}", board.Substring(0, position), current, position == board.Length - 1 ? "" : board.Substring(position + 1));
            }
            Console.WriteLine("Game over");
        }

        private static Vector GetRandomPosition(string board)
        {
            Vector result = null;
            for (int size = 1; size <= BOARD_SIZE / 2; ++size)
                for (int i = 0; i < 10 * size && (result == null || board[GetPosition(result)] != EMPTY); ++i)
                    result = new Vector(random.Next(9 - size, 9 + size), random.Next(9 - size, 9 + size));
            return result;
        }

        private static Vector GetUnion(Path path, string board, int size = BOARD_SIZE)
        {
            switch (path.direct)
            {
                case Path.Direction.Horizontal:
                    if (path.start.x - 1 >= 0 && board[GetPosition(new Vector(path.start.x - 1, path.start.y))] == EMPTY)
                        return new Vector(path.start.x - 1, path.start.y);
                    else
                        return new Vector(path.start.x + path.length, path.start.y);
                    break;
                case Path.Direction.Vertical:
                    if (path.start.y - 1 >= 0 && board[GetPosition(new Vector(path.start.x, path.start.y - 1))] == EMPTY)
                        return new Vector(path.start.x, path.start.y - 1);
                    else
                        return new Vector(path.start.x, path.start.y + path.length);
                    break;
                case Path.Direction.DiagonalUp:
                    if (path.start.y + 1 < size && path.start.x - 1 >= 0 && board[GetPosition(new Vector(path.start.x - 1, path.start.y + 1))] == EMPTY)
                        return new Vector(path.start.x - 1, path.start.y + 1);
                    else
                        return new Vector(path.start.x + path.length, path.start.y - path.length);
                    break;
                default:
                    if (path.start.y - 1 >= 0 && path.start.x - 1 >= 0 && board[GetPosition(new Vector(path.start.x - 1, path.start.y - 1))] == EMPTY)
                        return new Vector(path.start.x - 1, path.start.y - 1);
                    else
                        return new Vector(path.start.x + path.length, path.start.y + path.length);
                    break;
            }
        }

        private static Vector GetIntersection(Path first, Path second)
        {
            if (first.direct == Path.Direction.Horizontal)
                return new Vector(second.start.x - 1, second.start.y);
            else if (first.direct == Path.Direction.Vertical)
                return new Vector(second.start.x, second.start.y - 1);
            else if (first.direct == Path.Direction.DiagonalUp)
                return new Vector(second.start.x - 1, second.start.y + 1);
            else
                return new Vector(second.start.x - 1, second.start.y - 1);
        }

        private static char GetLeader(string board, int size = BOARD_SIZE)
        {
            int maxEnemy = 0, maxCurrent = 0;
            int enemySides, currentSides;
            foreach (Path item in enemyPath)
                maxEnemy = Math.Max(maxEnemy, GetMaxLength(item, enemy, board, size, out enemySides));
            foreach (Path item in currentPath)
                maxCurrent = Math.Max(maxCurrent, GetMaxLength(item, current, board, size, out currentSides));
            return maxEnemy > maxCurrent && maxEnemy >= 3 ? enemy : current;
        }

        private static int GetMaxLength(Path path, char enemyPoint, string board, int size, out int countSides)
        {
            int maxLength = 0;
            int first = 0, second = 0;
            switch(path.direct)
            {
                case Path.Direction.Horizontal:
                    for (int i = path.start.x; i < size; ++i)
                        if (board[GetPosition(new Vector(i, path.start.y))] != enemyPoint)
                        {
                            if (i != path.start.x)
                                first = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    for (int i = path.start.x - 1; i >= 0; --i)
                        if (board[GetPosition(new Vector(i, path.start.y))] != enemyPoint)
                        {
                            second = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    break;
                case Path.Direction.Vertical:
                    for (int i = path.start.y; i < size; ++i)
                        if (board[GetPosition(new Vector(path.start.x, i))] != enemyPoint)
                        {
                            if (i != path.start.y)
                                first = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    for (int i = path.start.y - 1; i >= 0; --i)
                        if (board[GetPosition(new Vector(path.start.x, i))] != enemyPoint)
                        {
                            second = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    break;
                case Path.Direction.DiagonalUp:
                    for (int i = path.start.x, j = path.start.y; i < size && j >= 0; ++i, --j)
                        if (board[GetPosition(new Vector(i, j))] != enemyPoint)
                        {
                            if (i != path.start.x && j != path.start.y)
                                first = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    for (int i = path.start.x - 1, j = path.start.y + 1; i >= 0 && j < size; --i, ++j)
                        if (board[GetPosition(new Vector(i, j))] != enemyPoint)
                        {
                            second = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    break;
                case Path.Direction.DiagonalDown:
                    for (int i = path.start.x, j = path.start.y; i < size && j < size; ++i, ++j)
                        if (board[GetPosition(new Vector(i, j))] != enemyPoint)
                        {
                            if (i != path.start.x && j != path.start.y)
                                first = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    for (int i = path.start.x - 1, j = path.start.y - 1; i >= 0 && j >= 0; --i, --j)
                        if (board[GetPosition(new Vector(i, j))] != enemyPoint)
                        {
                            second = 1;
                            ++maxLength;
                        }
                        else
                            break;
                    break;
            }
            countSides = first + second;
            return maxLength >= 5 ? path.length : 0;
        }

        private static void InsertStep(Vector step, bool isEnemy)
        {
            // Добавление хода либо в начало существующего пути, либо создание нового пути
            if (isEnemy)
            {
                // Для горизонтального направления
                bool contains = false;
                foreach (Path item in enemyPath.ToList())
                    if (item.direct == Path.Direction.Horizontal && item.start.y == step.y && item.start.x - step.x == 1)
                    {
                        contains = true;
                        enemyPath.Remove(item);
                        enemyPath.Add(new Path(step, Path.Direction.Horizontal, item.length + 1));
                        break;
                    }
                if (!contains)
                    enemyPath.Add(new Path(step, Path.Direction.Horizontal, 1));

                // Для вертикального направления
                contains = false;
                foreach (Path item in enemyPath.ToList())
                    if (item.direct == Path.Direction.Vertical && item.start.x == step.x && item.start.y - step.y == 1)
                    {
                        contains = true;
                        enemyPath.Remove(item);
                        enemyPath.Add(new Path(step, Path.Direction.Vertical, item.length + 1));
                        break;
                    }
                if (!contains)
                    enemyPath.Add(new Path(step, Path.Direction.Vertical, 1));

                // Для направления по диагонали вниз
                contains = false;
                foreach (Path item in enemyPath.ToList())
                    if (item.direct == Path.Direction.DiagonalDown && item.start.x - step.x == 1 && item.start.y - step.y == 1)
                    {
                        contains = true;
                        enemyPath.Remove(item);
                        enemyPath.Add(new Path(step, Path.Direction.DiagonalDown, item.length + 1));
                        break;
                    }
                if (!contains)
                    enemyPath.Add(new Path(step, Path.Direction.DiagonalDown, 1));

                // Для направления по диагонали вверх
                contains = false;
                foreach (Path item in enemyPath.ToList())
                    if (item.direct == Path.Direction.DiagonalUp && item.start.x - step.x == 1 && step.y - item.start.y == 1)
                    {
                        contains = true;
                        enemyPath.Remove(item);
                        enemyPath.Add(new Path(step, Path.Direction.DiagonalUp, item.length + 1));
                        break;
                    }
                if (!contains)
                    enemyPath.Add(new Path(step, Path.Direction.DiagonalUp, 1));
            }
            else
            {
                // Для горизонтального направления
                bool contains = false;
                foreach (Path item in currentPath.ToList())
                    if (item.direct == Path.Direction.Horizontal && item.start.y == step.y && item.start.x - step.x == 1)
                    {
                        contains = true;
                        currentPath.Remove(item);
                        currentPath.Add(new Path(step, Path.Direction.Horizontal, item.length + 1));
                        break;
                    }
                if (!contains)
                    currentPath.Add(new Path(step, Path.Direction.Horizontal, 1));

                // Для вертикального направления
                contains = false;
                foreach (Path item in currentPath.ToList())
                    if (item.direct == Path.Direction.Vertical && item.start.x == step.x && item.start.y - step.y == 1)
                    {
                        contains = true;
                        currentPath.Remove(item);
                        currentPath.Add(new Path(step, Path.Direction.Vertical, item.length + 1));
                        break;
                    }
                if (!contains)
                    currentPath.Add(new Path(step, Path.Direction.Vertical, 1));

                // Для направления по диагонали вниз
                contains = false;
                foreach (Path item in currentPath.ToList())
                    if (item.direct == Path.Direction.DiagonalDown && item.start.x - step.x == 1 && item.start.y - step.y == 1)
                    {
                        contains = true;
                        currentPath.Remove(item);
                        currentPath.Add(new Path(step, Path.Direction.DiagonalDown, item.length + 1));
                        break;
                    }
                if (!contains)
                    currentPath.Add(new Path(step, Path.Direction.DiagonalDown, 1));

                // Для направления по диагонали вверх
                contains = false;
                foreach (Path item in currentPath.ToList())
                    if (item.direct == Path.Direction.DiagonalUp && item.start.x - step.x == 1 && step.y - item.start.y == 1)
                    {
                        contains = true;
                        currentPath.Remove(item);
                        currentPath.Add(new Path(step, Path.Direction.DiagonalUp, item.length + 1));
                        break;
                    }
                if (!contains)
                    currentPath.Add(new Path(step, Path.Direction.DiagonalUp, 1));
            }
        }

        private static void Union(List<Option> options, bool isEnemy)
        {
            foreach(Option item in options)
            {
                if (item.countStep == 0)
                {
                    if (isEnemy)
                    {
                        enemyPath.Remove(item.second);
                        enemyPath.Remove(item.first);
                        enemyPath.Add(new Path(item.first.start, item.first.direct, item.first.length + item.second.length));
                    }
                    else
                    {
                        currentPath.Remove(item.second);
                        currentPath.Remove(item.first);
                        currentPath.Add(new Path(item.first.start, item.first.direct, item.first.length + item.second.length));
                    }
                }
            }
        }

        private static List<Option> SearchOptions(List<Path> path, string board)
        {
            List<Option> result = new List<Option>();
            for (int i = 0; i < path.Count; ++i)
                for (int j = i + 1; j < path.Count; ++j)
                    if (path[i].direct == path[j].direct)
                    {
                        bool isClosed = false;
                        switch (path[i].direct)
                        {
                            case Path.Direction.Horizontal:
                                int left = path[i].start.x < path[j].start.x ? i : j;
                                int right = left == i ? j : i;
                                if (path[i].start.y == path[j].start.y)
                                {
                                    for (int k = path[left].start.x + path[left].length - 1; k < path[right].start.x; ++k)
                                        if (board[GetPosition(new Vector(k, path[i].start.y))] != EMPTY)
                                            isClosed = true;
                                    if (!isClosed)
                                        result.Add(new Option(
                                            path[left],
                                            path[right],
                                            path[right].start.x - (path[left].start.x + path[left].length)
                                        ));
                                }
                                break;
                            case Path.Direction.Vertical:
                                int up = path[i].start.y < path[j].start.y ? i : j;
                                int down = up == i ? j : i;
                                if (path[i].start.x == path[j].start.x)
                                {
                                    for (int k = path[up].start.y + path[up].length - 1; k < path[down].start.y; ++k)
                                        if (board[GetPosition(new Vector(path[up].start.x, k))] != EMPTY)
                                            isClosed = true;
                                    if (!isClosed)
                                        result.Add(new Option(
                                        path[up],
                                        path[down],
                                        path[down].start.y - (path[up].start.y + path[up].length)
                                    ));
                                }
                                break;
                            case Path.Direction.DiagonalDown:
                                int leftDown = path[i].start.x < path[j].start.x ? i : j;
                                int rightDown = leftDown == i ? j : i;
                                if (path[i].start.y - path[i].start.x == path[j].start.y - path[j].start.x)
                                {
                                    for (int x = path[leftDown].start.x, y = path[leftDown].start.y; x < path[rightDown].start.x && y < path[rightDown].start.y; ++x, ++y)
                                        if (board[GetPosition(new Vector(x, y))] != EMPTY)
                                            isClosed = true;
                                    if (!isClosed)
                                        result.Add(new Option(
                                            path[leftDown],
                                            path[rightDown],
                                            path[rightDown].start.x - (path[leftDown].start.x + path[leftDown].length)
                                        ));
                                }
                                break;
                            case Path.Direction.DiagonalUp:
                                int leftUp = path[i].start.x < path[j].start.x ? i : j;
                                int rightUp = leftUp == i ? j : i;
                                if (path[i].start.x + path[i].start.y == path[j].start.x + path[j].start.y)
                                {
                                    for (int x = path[leftUp].start.x, y = path[leftUp].start.y; x < path[rightUp].start.x && y < path[rightUp].start.y; ++x, ++y)
                                        if (board[GetPosition(new Vector(x, y))] != EMPTY)
                                            isClosed = true;
                                    if (isClosed)
                                        result.Add(new Option(
                                            path[leftUp],
                                            path[rightUp],
                                            path[rightUp].start.x - (path[leftUp].start.x + path[leftUp].length)
                                        ));
                                }
                                break;
                        }
                    }
            return result;
        }

        private static Vector GetLastStep(string board, char enemyPoint)
        {
            for (int i = 0; i < board.Length; ++i)
            {
                Vector current = GetPosition(i);
                if (board[i] == enemyPoint && !enemyHistory.Contains(current))
                {
                    enemyHistory.Add(current);
                    return current;
                }
            }
            return null;
        }

        private static Vector GetPosition(int position, int size = BOARD_SIZE)
        {
            return new Vector(position % size, position / size);
        }

        private static int GetPosition(Vector point, int size = BOARD_SIZE)
        {
            return point.y * size + point.x;
        }

        private static char GetCurrentFigure(string board)
        {
            int counter = 0;
            foreach (char c in board)
            {
                switch (c)
                {
                    case CROSS:
                        counter++;
                        break;
                    case ZERO:
                        counter--;
                        break;
                }
            }
            return (counter <= 0) ? CROSS : ZERO;
        }
    }
}