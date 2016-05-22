program d32bot;

{$APPTYPE CONSOLE}

uses
  SysUtils;

{
   ������ ���� (� �����):
   1. ��������� �� �������� ������ (INPUT, STDIN, System.in) ������ � ������� ���������� �������� ����
   2. ����������� ���������� ����
   3. ������������ ������ ��������� ����
   4. �������� ���������� � �������� ����� (OUTPUT, STDOUT, System.out)

   ������ ������, ����������� ����:
   0..1 - 2 �����, ������������ �����, �������� �������� ����� ������ �������� ����. ��������� = 19
   2..362 - �������, ����������� 361 (19*19) ������ �������� ���� (����� �������, ������ ����):
     'x' - � ������ ������ �������
     'o' - � ������ ������ �����
     '-' - ������ ������
     ������ ������� �����������

   ������� ����:
   1. ������ ��� ��������� �������� ���������
   2. ������ ��������� ���� �� �������
   3. ��������� ���, ��� ������ �������� 5 ��� ����� ����� �������� � ��� - �� �����������, �� ��������� ��� �� ����� �� ���� ����������

   ��. ����� https://ru.wikipedia.org/wiki/������
}

const
  BOARD_SIZE = 19;                          // ������ �������� ���� - ������������ ������

var
  board: AnsiString;
  go, from, x, y: Integer;
  last: Integer = 0;
// ����������, ��� ������ ���
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
procedure stoa(a: Integer);
var
  t: Integer;
begin
  t := a - 2;
  y := t div 20;
  x := t - 19*y;
  y := y + 1;
end;

function atos(j: Integer; i: Integer): Integer;
begin
  atos := (j + ((i - 1) * 19)) + 2;
end;


// ���������� ����
procedure MakeMove(var board: AnsiString);
var
  i, j, k, a, b: Integer;
  p1, p2, p3, p4, p5, p6, p7, p8, p9: Integer;
  c, v: Char;
begin
  go := 0;
  c := GetCurrentFigure(board);
  if c = 'x' then v := 'o' else v := 'x';

  if last = 0 then                                  //
    begin
      repeat
        go := Random(BOARD_SIZE * BOARD_SIZE) + 3;
      until board[go] = '-';
    end
   else
    begin
       for a := 1 to 14 do                     // �� �������� ������ ����
         for b := 1 to 19 do
              if (board[atos(b, a)] = c) AND (board[atos(b, a + 1)] = c) AND (board[atos(b, a + 2)] = c) AND (board[atos(b, a + 3)] = '-') then go := atos(b, a + 3);
       if go = 0 then
         for a := 3 to 19 do                     // �� �������� ����� �����
           for b := 1 to 19 do
                if (board[atos(b, a)] = c) AND (board[atos(b, a + 1)] = c) AND (board[atos(b, a + 2)] = c) AND (board[atos(b, a - 1)] = '-') then go := atos(b, a - 1);

       if go = 0 then
       for a := 1 to 19 do                     // �� ������� ����� �������
         for b := 1 to 17 do
              if (board[atos(b, a)] = c) AND (board[atos(b + 1, a)] = c) AND (board[atos(b + 2, a)] = c) AND (board[atos(b + 3, a)] = '-') then go := atos(b + 3, a);

       if go = 0 then
       for a := 1 to 19 do                     // �� ������� ������ ������
         for b := 3 to 17 do
              if (board[atos(b, a)] = c) AND (board[atos(b + 1, a)] = c) AND (board[atos(b + 2, a)] = c) AND (board[atos(b - 1, a)] = '-') then go := atos(b - 1, a);

        p5 := last;
        p1 := p5 - 20;
        p2 := p5 - 19;
        p3 := p5 - 18;
        p4 := p5 - 1;
        p6 := p5 + 1;
        p7 := p5 + 18;
        p8 := p5 + 19;
        p9 := p5 + 20;

        if (go <= 0) AND (board[p1] = '-') then go := p1;
        if (go <= 0) AND (board[p3] = '-') then go := p3;
        if (go <= 0) AND (board[p9] = '-') then go := p9;
        if (go <= 0) AND (board[p7] = '-') then go := p7;

       if go <= 0 then                                  //
          begin
            repeat
              go := Random(BOARD_SIZE * BOARD_SIZE) + 3;
            until board[go] = '-';
          end;

    end;

  board[go] := c;
  last := go;
end;

// �������� ���� �����-������
begin
  Randomize;
  while true do
  begin
    ReadLn(board);
    //if Length(board) <> (2 + BOARD_SIZE * BOARD_SIZE) then   // ���� ������� ������ ������������ �����, �������, ��� ���� ���������
    //  Break;
    if Pos('-', board) <= 0 then                             // ���� ��� ������ �����, �������, ��� ���� ���������
      Break;
    MakeMove(board);
    Writeln(board);
  end;
  Readln();
end.
