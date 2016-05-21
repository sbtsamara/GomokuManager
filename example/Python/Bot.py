import random

cross = 'x'
zero = 'o'
empty = '-'

def getMySymbol():
    if field.count(cross) > field.count(zero):
        return zero
    return cross

def getPlaceToStep():
    while True:
        place = random.randint(0, len(field) - 1)
        if field[place] == empty:
            return place
        
def doStep():
    return field[0:place] + mySymbol + field[place + 1:]

while True:
    field = input()
    if field.count(empty) == 0:
        break
    mySymbol = getMySymbol()
    place = getPlaceToStep()
    print(doStep())
print('Game over')
