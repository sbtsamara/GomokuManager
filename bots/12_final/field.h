#pragma once
#include "cell.h"
#include <iostream>
#include <vector>

class Point
{
public:
	int row,col;
	Point(int row,int col)
	{
		this->row=row;
		this->col=col;
	}
};
class field{
public:
	int curStatus;
	int size;
	int winLength;
	int win;
	field(int,int);
	cell** cells;
	void setMarks();
	void setParam(int, int);
	bool checkValidRowCol(int,int);
	std::vector<int> getArrayH(int,int,int&);
	std::vector<int> getArrayV(int,int,int&);
	std::vector<int> getArrayR(int,int,int&);
	std::vector<int> getArrayL(int,int,int&);
	Point step();
	double getMark(int,int);
	void printMarks();
	void printField();
	double calculate(std::vector<int>, int &, int, int);
	double calculateMark(int, int, bool);
	Point getMaxMark();
	int getCurStatus();
	void checkEndGame();
};