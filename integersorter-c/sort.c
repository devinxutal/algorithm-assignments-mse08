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

char* name = "unknown";

int check_arg(char* arg) {
	if (strlen(arg) != 2)
		return 0;
	if (arg[0] != '-')
		return 0;
	if (arg[1] != 'i' && arg[1] != 'r' && arg[1] != 'm' && arg[1] != 'q')
		return 0;
	return 1;
}

int main(int argc, char* argv[]) {
	int size = 0;
	unsigned
	int* a;
	SORT_FUNCTION func = 0;
	if (argc != 3) {
		printf("usage: integersort -[i|q|r|m] [size]\n");
		exit(0);
	}
	if (!check_arg(argv[1])) {
		printf("invalid argument %s, must be -[i|q|r|m]\n", argv[1]);
		exit(0);
	}

	if(argv[1][1] == 'i'){
		name = "insertion sort";
		func = insertionsort;
	}else if(argv[1][1] == 'q'){
		name = "quick sort";
		func = quicksort;
	}else if(argv[1][1] == 'm'){
		name = "merge sort";
		func = mergesort;
	}else if(argv[1][1] == 'r'){
		name = "radix sort";
		func = radixsort;
	}
	size = atoi(argv[2]);

	srand(time(0));
	a = rand_array(size);
	test_sort(func, a, size);
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
	//unsigned int * b = copy_array(a, size);
	unsigned int *b = a;
	struct timeval s, e;
	struct timezone tz;
	printf("start sorting %d digits...\n", size);
	gettimeofday(&s, &tz);
	func(b, size);
	gettimeofday(&e, &tz);
	printf("job completed.\n");
	//print_result(b, size);
	printf("consumed time: %f seconds\n", get_usec(&s, &e) / 1000000.0);
	return;
}
