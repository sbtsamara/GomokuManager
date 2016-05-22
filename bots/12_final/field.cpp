#include "field.h"
#include "cell.h"
#include <ctime>
#include <iostream>
#include <vector>
#include <iomanip>
#include <math.h>
#include <limits>

template <typename T>
T max(T a,T b)
{
	return a > b ? a : b;
}
template <typename T>
T min(T a,T b)
{
	return a > b ? b : a;
}
template <typename T>
T fact(T l)
{
	if (l<=1) return 1;
	return l*fact(l-1);
}
double formula (bool alien, double length, int winLength)
{
	double mark = 0.;
	if (length < 1.1) mark = 1.;
	else 
		if ((!alien) && (length >= winLength)) mark = std::numeric_limits<double>::infinity();
		else 
			if (alien && length >= winLength - 0.1) mark = fact(double(length + 10)); 
			else mark = fact(double(length+3));
	return mark;
}

double field::calculate (std::vector<int> massive, int &place, int winLength, int status)
{
	double k0 = 0.,
	k1 = 0.;
	bool maybe = false;
	int counter;
	std::vector<int> ::iterator iter = massive.begin();
	if (massive.size() >= winLength) 
		while ((iter != massive.end()) && (!maybe))
		{
			bool findAlien = false;
			std::vector<int>::iterator iterInner = iter;
			counter = 0;
			while ((iterInner != massive.end()) && !findAlien && !maybe)
			{
				if (*iterInner == status % 2 +1) findAlien = true;
				else 
				{
					++counter; 
					if (counter >= winLength) maybe = true;
				}
				++iterInner;
			}
			++iter;
		}
		if (maybe)
		{
			int ind;
			int nvl;
			k0 = 1.;
			nvl = 0;
			ind = place;
			while ((--ind >= 0) && (massive[ind]==status)) ++k0;
			ind = place;
			while ((++ind < massive.size())&& massive[ind] == status) ++k0;
			
			k1 = 1.;
			nvl = 0;
			ind = place;
			while((--ind>=0)&& (massive[ind] != status % 2 + 1) && (nvl==0))
			{
				if (massive[ind]==0) nvl=1;
				else if (massive[ind]==status) ++k1;
			}
			ind = place;
			while ((++ind < massive.size()) && (massive[ind] != status % 2 + 1) && (nvl <=1))
			{
				if (massive[ind] == 0 && nvl == 0) nvl = 3;
				else if (massive[ind] == 0 && nvl == 1) nvl = 2;
				else if (massive[ind] == status) ++k1;
			}
			if ((nvl == 2)&& (k1>=winLength - 1)) k1 +=0.9;
			else if ((nvl == 2) && (k1 >= winLength - 2)) k1 += 0.8;
			
		}
		return max(max(k0,k1),0.);
}

std::vector<int> field::getArrayV(int rowIn, int colIn, int &place)
{
	std::vector <int> massive;
	massive.reserve(1);
	int min_row,max_row,min_col,max_col;
	min_row = max(0,rowIn - winLength);
	max_row = min(size - 1, rowIn + winLength);
	min_col = colIn;
	max_col = colIn;
	place = rowIn - min_row;
	for (int i = min_row, j = min_col; i<=max_row && j<=max_col; i++)
		massive.push_back(cells[i][j].getStatus());
	return massive;
}
std::vector<int> field::getArrayH(int rowIn, int colIn, int &place)
{
	std::vector <int> massive;
	massive.reserve(1);
	int min_row,max_row,min_col,max_col;
	min_row = rowIn; 
	max_row = rowIn; 
	min_col = max(0,colIn-winLength);
	max_col = min(size-1,colIn+winLength);
	place = colIn - min_col;
	for (int i = min_row, j = min_col; i<=max_row && j<=max_col; j++)
		massive.push_back(cells[i][j].getStatus());
	return massive;
}
std::vector<int> field::getArrayR(int rowIn, int colIn, int &place)
{
	std::vector <int> massive;
	massive.reserve(1);
	int min_row=rowIn,max_row=rowIn,min_col=colIn,max_col=colIn;
	int counter = 0;
	place = 0;
	
	while (checkValidRowCol(--min_row,++min_col)&&place<winLength) ++place;
	++min_row; --min_col;
	while (checkValidRowCol(++max_row,--max_col)&&counter<winLength) ++counter;
	--max_row; ++max_col;
	for (int i = min_row, j = min_col; i<=max_row && j>=max_col; ++i,--j)
		massive.push_back(cells[i][j].getStatus());
	return massive;
}
std::vector<int> field::getArrayL(int rowIn, int colIn, int &place)
{
	std::vector <int> massive;
	massive.reserve(1);
	int min_row=rowIn,max_row=rowIn,min_col=colIn,max_col=colIn;
	int counter = 0;
	place = 0;
	
	while (checkValidRowCol(--min_row,--min_col) && place<winLength) ++place;
	++min_row; ++min_col;
	while (checkValidRowCol(++max_row,++max_col)&&counter<winLength) ++counter;
	--max_row; --max_col;
	for (int i = min_row, j = min_col; i<=max_row && j>=max_col; ++i,++j)
		massive.push_back(cells[i][j].getStatus());
	return massive;
}

Point field::step()
{
	Point point = getMaxMark();
	cells[point.row][point.col].setStatus(getCurStatus());
	//std::cout<<point.row<<"-"<<point.col<<std::endl;
	return point;
}

Point field::getMaxMark()
{
	double maxMarkValue = -DBL_MAX;
	std::vector<Point> points,pointsNvl;
	points.reserve(0);
	pointsNvl.reserve(0);
	for (int i = 0; i <size; i++)
	{
		for (int j = 0; j<size;j++)
		{
			double mark = cells[i][j].getMark();
			if ((cells[i][j].getStatus()==0)&& (mark!=0))
			{
				if (maxMarkValue < mark)
				{
					maxMarkValue = mark;
					points.clear();
					points.push_back(Point(i,j));
				}
				else if ((maxMarkValue == mark)&&(!points.empty()))
					points.push_back(Point(i,j));
			}
			else if (cells[i][j].getStatus()==0)
				pointsNvl.push_back(Point(i,j));
		}
	}
	Point *result;
	if (points.empty() && !pointsNvl.empty())
		result = &pointsNvl.at(pointsNvl.size()/2);
	else 
		if (points.size() == 1) result = &points.at(0);
		else
		{
			result = &points.at(points.size()/2);
		}
	return *result;
}

field::field(int size, int winL)
{
	this->size = size;
	this->winLength = winL;
	cells = new cell*[size];
	for(int i=0;i<size;++i)	
	{
		cells[i] = new cell[size];
		for(int j=0;j<size;++j)
		{
			cells[i][j].setStatus(0);
		}
	}
}
void field::printField()
{
	int width = 6;
	for(int i=0;i<size;++i)	
	{
		for(int j=0;j<size;++j)
		{
			//std::cout.width(width);
			//std::cout << cells[i][j].getStatus();
		}
		//std::cout << std::endl;
	}	
}
void field::printMarks()
{
	int width = 6;
	for(int i=0;i<size;++i)	
	{
		for(int j=0;j<size;++j)
		{
			//std::cout.width(width);
			//std::cout << cells[i][j].getMark();
		}
		//std::cout << std::endl;
	}	
}

int field::getCurStatus()
{
	return curStatus;
}

double field::getMark(int row, int col)
{
	double F = 0,
		M = 0,
		Q = 0,
		K = 1;
	if (cells[row][col].getStatus()==0)
	{
		curStatus = getCurStatus();
		cells[row][col].setStatus(getCurStatus());
		M = calculateMark(row, col, true);
		curStatus = getCurStatus()%2+1;
		cells[row][col].setStatus(getCurStatus());
		Q = calculateMark(row, col, false);
		curStatus = getCurStatus()%2+1;
		cells[row][col].setStatus(0);
		F = M + Q*K;
	}
	else F = 0;
	return F;
}

bool checkSequences(std::vector<int> massive,int &win,int length)
{
	bool end = false;
	int counter = 1;
	int status = 0;
	std::vector<int>::iterator iter = massive.begin();
	while (iter != massive.end() && !end)
	{
		status = *iter;
		while (((++iter) != massive.end()) && (status !=0)&&(*iter == status)&&((++counter)<length)) {}
		if (counter >= length) end = true;
		else counter = 1;
	}
	if (end) win = status;
	return end;
}

void field::checkEndGame()
{
	bool end = false;
	std::vector<int> massive;
	for (int j = 0; j<size && !end;j++)
	{
		massive.clear();
		for (int i = 0; i<size;i++)
			massive.push_back(cells[i][j].getStatus());
		end = checkSequences(massive,win,winLength);
	}
	for (int i=0;i<size && !end;i++)
	{
		massive.clear();
		for (int j=0;j<size;j++)
			massive.push_back(cells[i][j].getStatus());
		end = checkSequences(massive,win,winLength);
	}
	for (int i=0, j=0;i<size && !end;++i)
	{
		massive.clear();
		int min_row=i,max_row=i,min_col=j,max_col=j;
		int counter = 1;
		while (checkValidRowCol(++min_row,--min_col)) ++counter;
		--min_row; ++min_col;
		while (checkValidRowCol(--max_row,++max_col)) ++counter;
		++max_row; --max_col;
		if (counter>=winLength)
		{
			for (int k =min_row,l=min_col;k>=max_row &&l<=max_col; --k,++l)
			massive.push_back(cells[k][l].getStatus());
			end = checkSequences(massive,win,winLength);
		}
		else end = false;	
	}
	for (int i=size-1,j=0;j<size && !end;++j)
	{
		massive.clear();
		int min_row=i,max_row=i,min_col=j,max_col=j;
		int counter = 1;
		while (checkValidRowCol(++min_row,--min_col)) ++counter;
		--min_row; ++min_col;
		while (checkValidRowCol(--max_row,++max_col)) ++counter;
		++max_row; --max_col;
		if (counter>=winLength)
		{
			for (int k=min_row,l = min_col;k>=max_row&&l<=max_col;--k,++l)
			massive.push_back(cells[k][l].getStatus());
			end = checkSequences(massive,win,winLength);
		}
		else end = false;	
	}
	for (int i=size-1,j=0;i>=0&& !end;--i)
	{
		massive.clear();
		int min_row=i,max_row=i,min_col=j,max_col=j;
		int counter = 0;
		while (checkValidRowCol(--min_row,--min_col)) ++counter;
		++min_row; ++min_col;
		while (checkValidRowCol(++max_row,++max_col)) ++counter;
		--max_row; --max_col;
		if (counter>=winLength)
		{
			for (int k=min_row,l = min_col;k<=max_row&&l<=max_col;++k,++l)
			massive.push_back(cells[k][l].getStatus());
			end = checkSequences(massive,win,winLength);
		}
		else end = false;	
	}
	for (int i=0,j=size-1;j<size-1 && !end;++j)
	{
		massive.clear();
		int min_row=i,max_row=i,min_col=j,max_col=j;
		int counter = 0;
		while (checkValidRowCol(--min_row,--min_col)) ++counter;
		++min_row; ++min_col;
		while (checkValidRowCol(++max_row,++max_col)) ++counter;
		--max_row; --max_col;
		if (counter>=winLength)
		{
			for (int k=min_row,l = min_col;k<=max_row&&l<=max_col;++k,++l)
			massive.push_back(cells[k][l].getStatus());
			end = checkSequences(massive,win,winLength);
		}
		else end = false;	
	}
}

void field::setMarks()
{
	for(int i=0;i<size;++i)	
	{
		for(int j=0;j<size;++j)
		{
			cells[i][j].setMark(getMark(i,j));
		}
		//std::cout << std::endl;
	}	
}

double field::calculateMark (int row, int col, bool alien)
{
	int place = 0;
	double result;
	double v,h,r,l;
	int threeThrees = 0;
	v =calculate(getArrayV(row,col,place),place,winLength,getCurStatus());
	if (int(v*10)%10==8) ++threeThrees;
	if (threeThrees>=2) v +=2;
	h = calculate(getArrayH(row,col,place),place,winLength,getCurStatus());
	if (int(h*10)%10==8) ++threeThrees;
	if (threeThrees>=2) h +=2;
	r = calculate(getArrayR(row,col,place),place,winLength,getCurStatus());
	if (int(r*10)%10==8) ++threeThrees;
	if (threeThrees>=2) r +=2;
	l = calculate(getArrayL(row,col,place),place,winLength,getCurStatus());
	if (int(l*10)%10==8) ++threeThrees;
	if (threeThrees>=2) l +=2;
	result = formula(alien,v,winLength);
	result *= formula(alien,h,winLength);
	result *= formula(alien,r,winLength);
	result *= formula(alien,l,winLength);
	result = sqrt(sqrt(result));
	return result;
}

bool field::checkValidRowCol(int row,int col)
{
	return row>=0 && row<size && col>=0 && col<size;
}
