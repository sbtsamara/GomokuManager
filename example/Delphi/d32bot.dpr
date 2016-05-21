program d32bot;

{$APPTYPE CONSOLE}

uses
  SysUtils;

{
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
}

const
  BOARD_SIZE = 19;                          // размер игрового поля - классический гомоку

var
  board: AnsiString;

// определяет, чей сейчас ход
function GetCurrentFigure(const board: AnsiString): Char;
var
  i: Integer;
  counter: Integer;
begin
  counter := 0;
  for i := 3 to Length(board) do
    if LowerCase(board[i]) = 'x' then
      Inc(counter)
    else
      if LowerCase(board[i]) = 'o' then
        Dec(counter);
  if counter <= 0 then
    Result := 'x'
  else
    Result := 'o';
end;

// выполнение хода
procedure MakeMove(var board: AnsiString);
var
  i: Integer;
begin
  repeat
    i := Random(BOARD_SIZE * BOARD_SIZE) + 3;
  until board[i] = '-';
  board[i] := GetCurrentFigure(board);
end;

// основной цикл ввода-вывода
begin
  Randomize;
  while true do
  begin
    ReadLn(board);
    if Length(board) <> (2 + BOARD_SIZE * BOARD_SIZE) then   // если входные данные некорректной длины, считаем, что игра завершена
      Break;
    if Pos('-', board) <= 0 then                             // если нет пустых ячеек, считаем, что игра завершена
      Break;
    MakeMove(board);
    Writeln(board);
  end;
  Writeln('Game over');
end.
