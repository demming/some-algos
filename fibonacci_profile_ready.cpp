/****************************************************************************
 *  Copyright (C) 2013 Demming (http://github.com/demming)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 ***************************************************************************/
 
#include <iostream>
#include <cstdlib>

unsigned long long fibr(unsigned long long n)
{
	return n <= 0 ? 0 : n == 1 ? 1 : fibr(n-1) + fibr(n-2);
}

unsigned long long fibi(int n)
{
	if(n == 0) return 0;
	
	unsigned long long fib[] = {0, 1, 2};

	int i;
	for(i = 2; i <= n; i++)
		fib[i%3] = fib[(2 + i%3)%3] + fib[(1 + i%3)%3];
	
	return fib[(i%3 + 2)%3];	// because we start at i==2
}

int main(int argc, char **argv)
{
	std::cout << fibr(atoll(argv[1])) << std::endl;

	std::cout << fibi(atoll(argv[1])) << std::endl;
		
	return 0;
}
