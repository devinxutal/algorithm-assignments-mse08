#include "include/sorters.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>

typedef unsigned int *(* SORT_FUNCTION)(unsigned int*, int);

void print_result(unsigned int[], int);

unsigned int rand_int();

unsigned int * rand_array(int);

unsigned int * copy_array(unsigned int *, int size);

void test_sort(SORT_FUNCTION, unsigned int *, int size);

int main(int argc, char* argv[]) {
	int size = 200000000;
	unsigned
	int* a;
	srand(time(0));
	a = rand_array(size);
	test_sort(quicksort, a, size);
	free(a);
	return 0;
}

void print_result(unsigned int a[], int num) {
	int i;
	printf("===print array===\n");
	for (i = 0; i < num; i++) {
		printf("%u\n", a[i]);
	}
	printf("===end array===\n");
}

unsigned int* rand_array(int size) {
	unsigned int *a;
	int i;
	if (!(a = malloc(size * sizeof(unsigned int)))) {
		printf("error in rand array: couldn't malloc space");
	}
	for (i = 0; i < size; i++) {
		a[i] = rand_int();
	}
	return a;
}

unsigned int * copy_array(unsigned int *a, int size) {
	unsigned int *b;
	if (!(b = malloc(size * sizeof(unsigned int)))) {
		printf("error in rand array: couldn't malloc space");
	}
	memcpy(b, a, size * sizeof(unsigned int));
	return b;
}

unsigned int rand_int() {
	return ((rand() << 16) | rand());
}

unsigned long get_usec(struct timeval *s, struct timeval *e) {
	unsigned long time = (e->tv_sec - s->tv_sec) * 1000000 + (e->tv_usec
			- s->tv_usec);
	return time;
}

void test_sort(SORT_FUNCTION func, unsigned int * a, int size) {
	unsigned int * b = copy_array(a, size);
	struct timeval s, e;
	struct timezone tz;
	printf("start sorting %d digits...\n", size);
	gettimeofday(&s, &tz);
	func(b, size);
	gettimeofday(&e, &tz);
	printf("job completed.\n");
	//print_result(b, size);
	printf("consumed time: %f seconds\n", get_usec(&s, &e)/1000000.0);
	free(b);
	return;
}
