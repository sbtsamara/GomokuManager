#include <iostream>
#include <string>
#include "field.h"
#include <fstream>

//using namespace std;

int main(int argc, char** argv)
{
	char x = 'x';
	char o = 'o';
		std::string s = "";
	/*	s = "19";
		for (int i=0;i<19*19;++i) s+='-';*/
	bool flag = true;
	do
	{
		std::cin >> s;
		/*
		if (flag) {
			std::ofstream fout;
			fout.open("test.txt",std::ios_base::out);
			fout << s;
			fout.close();
			flag = false;
		}*/
		int size = std::stoi(s.substr(0,2));
		field *f = new field(size,5);
		int countX = 0, countO = 0;
		char a;
		for(int i=0;i<size;++i)
		{
			for(int j=0;j<size;++j)
			{
				a = s.at(2+i*size+j);
				int b = 0;
				if (a=='-') b = 0;
				else if (a==x) 
				{
					++countX;
					b = 1;
				}
				else if (a==o)
				{
					++countO;
					b = 2;
				}
				f->cells[i][j].setStatus(b);
			}
		}
		if(countX<=countO)
		{
			f->curStatus = 1;
			a = x;
		}
		else if(countX>countO)
		{
			f->curStatus = 2;
			a = o;
		}
		f->setMarks();
		Point p = f->step();
		f->printField();
		s[2+p.row*size+p.col] = a;
		f->checkEndGame();
		std::cout << s << std::endl;
		//system("pause");
	}
	while(true);
	//system("pause");
	return 0;
}