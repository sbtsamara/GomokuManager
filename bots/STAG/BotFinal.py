from random import randint as rand
from time import sleep
from itertools import chain


class Game:
    EMPTY = '-'
    CROSS = 'x'
    ZERO = 'o'
    WIN_SCORE = 5

    def __init__(self, instr):
        self.n = int(instr[:2])
        self.updatemap(instr)
        self.sign = Game.ZERO
        self.enemy = Game.CROSS
        if self.is_empty():
            self.sign, self.enemy = self.enemy, self.sign

    def updatemap(self, newin):
        if int(newin[:2]) != self.n:
            raise Exception("dimension")
        tmp = newin[2:]
        if len(tmp) != self.n * self.n:
            raise Exception("cells count")
        self.map = []
        for i in range(self.n):
            self.map.append(list(tmp[:self.n]))
            tmp = tmp[self.n:]

    def is_empty(self):
        return all(all(c == Game.EMPTY for c in row) for row in self.map)

    def cells(self):
        for row in self.map:
            for c in row:
                yield c

    def rightup(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i - n, j + n) else min(i + 1, self.n - j)
        for k in range(1, limit):
            yield self.map[i - k][j + k]

    def right(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i, j + n) else self.n - j
        for k in range(1, limit):
            yield self.map[i][j + k]

    def rightdown(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i + n, j + n) else min(self.n - i, self.n - j)
        for k in range(1, limit):
            yield self.map[i + k][j + k]

    def down(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i + n, j) else self.n - i
        for k in range(1, limit):
            yield self.map[i + k][j]

    def leftdown(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i + n, j - n) else min(self.n - i, j + 1)
        for k in range(1, limit):
            yield self.map[i + k][j - k]

    def left(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i, j - n) else j + 1
        for k in range(1, limit):
            yield self.map[i][j - k]

    def leftup(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i - n, j - n) else min(i + 1, j + 1)
        for k in range(1, limit):
            yield self.map[i - k][j - k]

    def up(self, i, j, n=WIN_SCORE // 2):
        limit = n + 1 if self.is_valid(i - n, j) else i + 1
        for k in range(1, limit):
            yield self.map[i - k][j]

    def neighbours(self, i, j):
        return chain(*self.directions(i, j))

    def directions(self, i, j):
        return [
            self.up(i, j),
            self.rightup(i, j),
            self.right(i, j),
            self.rightdown(i, j),
            self.down(i, j),
            self.leftdown(i, j),
            self.left(i, j),
            self.leftup(i, j),
        ]

    def is_valid(self, i, j):
        return i < self.n and j < self.n and j > 0 and i > 0

    def __str__(self):
        return str(self.n) + ''.join(str(cell) for cell in self.cells())

    def mapstr(self):
        return '\n'.join(''.join(row) for row in self.map)

    def enumerated_cells(self):
        yield from self.trim(0)

    def leftright(self, i, j):
        return chain(self.left(i, j), self.right(i, j))

    def leftdownrightup(self, i, j):
        return chain(self.leftdown(i, j), self.rightup(i, j))

    def downup(self, i, j):
        return chain(self.down(i, j), self.down(i, j))

    def leftuprightdown(self, i, j):
        return chain(self.leftup(i, j), self.rightdown(i, j))

    def crossdirections(self, i, j):
        return [
            self.downup(i, j),
            self.leftdownrightup(i, j),
            self.leftright(i, j),
            self.leftup(i, j)
        ]

    def trim(self, n=WIN_SCORE // 2):
        for i in range(n, self.n - n):
            for j in range(n, self.n - n):
                yield self.map[i][j], i, j

    def make_random_turn(self):
        from random import randint
        while True:
            i, j = randint(0, self.n), randint(0, self.n)
            if self.is_valid(i,j) and self.map[i][j] == Game.EMPTY:
                self.map[i][j] = self.sign
                return

    def make_turn(self):
        turns = []
        for cell, i, j in self.enumerated_cells():
            if cell == Game.EMPTY:
                score = 0
                d = self.crossdirections(i, j)
                for direction in d:
                    direction_score = sum(1 for c in direction if c == self.sign)
                    if direction_score >= Game.WIN_SCORE - 1:
                        self.map[i][j] = self.sign
                        return None
                    score += direction_score
                turns.append((i, j, score))
        """
        if len(turns) == 0:
            self.make_random_turn()
            return
        """

        #i, j, score = max(turns, key=lambda turn: turn[2])
        #self.map[i][j] = self.sign
        return turns

    def is_end(self):
        return all(cell != Game.EMPTY for cell in self.cells())





def isEmpty(lst):
    for i in lst:
        if i != "-":
            return False
    return True

def printAsFuckingHuman(field):
    for row in field:
        for cell in row:
            print(cell, end="")
        print()

def ranger(field):
    up = 0
    dn = 0
    lt = 0
    rg = 0
    for i in range(len(field)):
        if not isEmpty(field[i]):
            up = i
            break
    for i in reversed(range(len(field))):
        if not isEmpty(field[i]):
            dn = i
            break
    for i in range(len(field[0])):
        if not isEmpty([j[i] for j in field]):
            lt = i
            break
    for i in reversed(range(len(field[0]))):
        if not isEmpty([j[i] for j in field]):
            rg = i
            break

    return up, dn, lt, rg

def getNeigh(field, coords, enemy):
    x, y = coords
    res = []
    res.append(sum([2**(4-t) if field[x - t][y] == enemy else 0 for t in range(1,5)])) #up
    res.append(sum([2**(4-t) if field[x + t][y] == enemy else 0 for t in range(1,5)])) #down
    res.append(sum( [2**(4-t) if field[x][y - t] == enemy else 0 for t in range(1,5)])) #left
    res.append(sum( [2**(4-t) if field[x][y + t] == enemy else 0 for t in range(1,5)])) #right
    res.append(sum([2**(4-t) if field[x-t][y-t] == enemy else 0 for t in range(1,5)]))
    res.append(sum([2**(4-t) if field[x-t][y+t] == enemy else 0 for t in range(1,5)]))
    res.append(sum([2**(4-t) if field[x+t][y-t] == enemy else 0 for t in range(1,5)]))
    res.append(sum([2**(4-t) if field[x+t][y+t] == enemy else 0 for t in range(1,5)]))
    #print(x,y)
    #print([res[0]+res[1], res[2]+res[3], res[4]+ res[7], res[5]+res[6]])
    return max([res[0]+res[1], res[2]+res[3], res[4]+ res[7], res[5]+res[6]])


def findHoles(field, rans, enemy):
    fld = []
    for i in range(27):
        fld.append(["-"]*27)
    for i in range(19):
        for j in range(19):
            fld[i+4][j+4] = field[i][j]
    up, dn, lt, rg = rans
    #print(rans)
    values = []
    coord = None
    for row in range(up+4, dn+5):
        for col in range(lt+4, rg+5):
            #print(row, col)
            if fld[row][col] == "-":
                mx = getNeigh(fld, (row, col), enemy)
                values.append([(row-4, col-4), mx])
    return values


def find4(field, rans, enemy):
    up, dn, lt, rg = rans
    if up ==0:
        up =1
    if lt == 0:
        lt = 1
    if rg>14:
        rg = 14
    if dn>14:
        dn = 14
    if dn - up < 4 and rg - lt < 4:
        return None
    for col in range(lt, rg+1):
        for cell in range(up, dn-2):
            four = "".join([field[cell + i][col] for i in range(4)])
            if four == enemy*4:
                if field[cell-1][col] == "-" and field[cell+4][col] == "-":
                    return None
                elif field[cell-1][col] == "-":
                    return cell-1, col
                elif field[cell+4][col] == "-":
                    return cell+4, col
    for row in range(up, dn + 1):
        for cell in range(lt, rg - 2):
            four = "".join([field[row][cell + i] for i in range(4)])
            if four == enemy * 4:
                if field[row][cell - 1] == "-" and field[row][cell + 4] == "-":
                    return None
                elif field[row][cell - 1] == "-":
                    return row, cell - 1
                elif field[row][cell + 4] == "-":
                    return row, cell + 4
    for row in range(up, dn - 2):
        for cell in range(lt, rg - 2):
            four = "".join([field[row + i][cell + i] for i in range(4)])
            if four == enemy * 4:
                if field[row- 1][cell - 1] == "-" and field[row + 4][cell + 4] == "-":
                    return None
                elif field[row - 1][cell - 1] == "-":
                    return row- 1, cell - 1
                elif field[row + 4][cell + 4] == "-":
                    return row+4, cell + 4
    return None

def find3(field, rans, enemy):
    up, dn, lt, rg = rans
    if up == 0:
        up = 1
    if lt == 0:
        lt = 1
    if rg > 14:
        rg = 14
    if dn > 14:
        dn = 14
    if dn - up < 3 and rg - lt < 3:
        return None
    for col in range(lt, rg+1):
        for cell in range(up, dn-1):
            four = "".join([field[cell + i][col] for i in range(3)])
            if four == enemy*3:
                if field[cell-1][col] == "-" and field[cell+3][col] == "-":
                    return cell-1, col
                elif field[cell-1][col] == "-":
                    return cell-1, col
                elif field[cell+3][col] == "-":
                    return cell+3, col
    for row in range(up, dn + 1):
        for cell in range(lt, rg - 1):
            four = "".join([field[row][cell + i] for i in range(3)])
            if four == enemy * 3:
                if field[row][cell - 1] == "-" and field[row][cell + 3] == "-":
                    return row, cell-1
                elif field[row][cell - 1] == "-":
                    return row, cell - 1
                elif field[row][cell + 3] == "-":
                    return row, cell + 3
    for row in range(up, dn - 1):
        for cell in range(lt, rg - 1):
            four = "".join([field[row + i][cell + i] for i in range(3)])
            if four == enemy * 3:
                if field[row- 1][cell - 1] == "-" and field[row + 3][cell + 3] == "-":
                    return row-1, cell-1
                elif field[row - 1][cell - 1] == "-":
                    return row- 1, cell - 1
                elif field[row + 3][cell + 3] == "-":
                    return row+3, cell + 3
    return None




def retList(st):
    st = list(st[2:])
    res = []
    for i in range(19):
        tmp = []
        for j in range(19):
            tmp.append(st[i*19 + j])
        res.append(tmp)
    return res

def getEnemy(we):
    return "x" if we == "o" else "o"

def whoseTurn(field):
    x = 0
    o = 0
    for row in field:
        for cell in row:
            if cell == "x":
                x += 1
            elif cell == "o":
                o += 1
    return "o" if o<x else "x"



def randTurn(field):
    first = True
    x,y = 0, 0
    while field[x][y] != "-" or first:
        first = False
        x, y = rand(0,18), rand(0,18)
    return x, y

def updated(field, coord, sign):
    res = field
    res[coord[0]][coord[1]] = sign
    return res

def toString(field):
    return "19" + "".join("".join(c for c in row) for row in field)

def isOver(field):
    for row in field:
        if row.count("-") != 0:
            return False
    return True

inp = input()
game = Game(inp)

while True:

    field = retList(inp)

    if isOver(field):
        break
    we = whoseTurn(field)
    defence = findHoles(field, ranger(field), getEnemy(we))

    attack = game.make_turn()
    if attack is None:
        print(str(game))
    else:

        mx = 0
        coord = None
        for i in defence:
            if i[1] > mx:
                mx = i[1]
                coord = i[0]


        if coord is None:
            coord = randTurn(field)
        field = updated(field, coord, we)
        print(toString(field))
    inp = input()
    game.updatemap(inp)
print('Game over')


