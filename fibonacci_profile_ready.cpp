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
