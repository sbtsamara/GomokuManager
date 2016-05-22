#include "cell.h"

double cell::getMark()
{
	return this->mark;
}

int cell::getStatus()
{
	return this->status;
}

void cell::setMark(double m)
{
	this->mark = m;
}

void cell::setStatus(int s)
{
	this->status = s;
}