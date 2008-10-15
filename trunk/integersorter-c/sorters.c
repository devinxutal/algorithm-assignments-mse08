/*
 * sorters.c
 *
 *  Created on: Oct 11, 2008
 *      Author: devin
 */

#define R_BIT 4
#define INT_NUM 16

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

void countingsort(unsigned int *, unsigned int *, int, int);
void mergesort_r(unsigned int *, int, int, unsigned int*);
void quicksort_r(unsigned int *, int, int);
int partition(unsigned int *, int, int);
void swap(unsigned int*, unsigned int*);
void merge(unsigned int*, int, int, int, unsigned int *);
int get_radix(unsigned int, int);

unsigned int* sort(unsigned int* a, int num) {
	return 0;
}

unsigned int* mergesort(unsigned int* a, int num) {
	unsigned int* b;
	if (!(b = malloc(num * sizeof(unsigned int)))) {
		printf("merge sort err: cannot allocate memory");
		return 0;
	}
	mergesort_r(a, 0, num - 1, b);
	free(b);
	return a;
}

unsigned int* quicksort(unsigned int* a, int num) {
	quicksort_r(a, 0, num - 1);
	return a;
}

unsigned int* insertionsort(unsigned int* a, int num) {
	int i, j;
	unsigned int tmp;
	for (i = 1; i < num; i++) {
		j = i - 1;
		tmp = a[i];
		while (j >= 0 && a[j] > tmp) {
			a[j + 1] = a[j];
			j--;
		}
		a[j + 1] = tmp;
	}
	return a;
}

unsigned int* radixsort(unsigned int* a, int num) {
	unsigned int* b1, *b2;
	int i;
	if (!(b1 = malloc(num * sizeof(unsigned int)))) {
		printf("radix sort err: cannot allocate memory");
		exit(-1);
	}
	if (!(b2 = malloc(num * sizeof(unsigned int)))) {
		printf("radix sort err: cannot allocate memory");
		exit(-1);
	}
	memcpy(b1, a, num * sizeof(unsigned int));
	for (i = 0; i < (sizeof(unsigned int) * 8 / R_BIT); i++) {
		if (i % 2 == 0) {
			countingsort(b1, b2, num, i);
		} else {
			countingsort(b2, b1, num, i);
		}
	}
	if (i % 2 == 0) {
		memcpy(a, b1, num * sizeof(unsigned int));
	} else {
		memcpy(a, b2, num * sizeof(unsigned int));
	}
	free(b1);
	free(b2);
	return a;
}

void countingsort(unsigned int *a, unsigned int *b, int num, int index) {
	int c[INT_NUM];
	int i, j, radix;
	//initialize
	for (i = 0; i < INT_NUM; i++)
		c[i] = 0;
	// for j ← 1 to length[A]
	//    do C[A[j]] ← C[A[j]] + 1
	for (j = 0; j < num; j++) {
		radix = get_radix(a[j], index);
		c[radix] = c[radix] + 1;
	}
	// for i ← 1 to k
	//	  do C[i] ← C[i] + C[i − 1]
	for (i = 1; i < INT_NUM; i++) {
		c[i] = c[i] + c[i - 1];
	}
	// for j ← length[A] downto 1
	//	   do B[C[A[j]]] ← A[j]
	//	       C[A[j]] ← C[A[j]] − 1
	for (j = num - 1; j >= 0; j--) {
		radix = get_radix(a[j], index);
		b[c[radix] - 1] = a[j];
		c[radix] = c[radix] - 1;
	}
}

void mergesort_r(unsigned int *a, int p, int r, unsigned int *b) {
	if (p == r)
		return;
	int m = (p + r) / 2;
	mergesort_r(a, p, m, b);
	mergesort_r(a, m + 1, r, b);
	merge(a, p, m, r, b);
}

void merge(unsigned int *a, int p, int m, int r, unsigned int *b) {
	int i, j, index;
	for (i = p; i <= r; i++)
		b[i] = a[i];
	i = p;
	j = m + 1;
	index = p;
	while (i <= m && j <= r) {
		if (b[i] <= b[j]) {
			a[index++] = b[i++];
		} else {
			a[index++] = b[j++];
		}
	}
	if (i <= m) {
		for (i; i <= m; i++)
			a[index++] = b[i];
	} else if (j <= r) {
		for (j; j <= r; j++)
			a[index++] = b[j];
	}
}

void quicksort_r(unsigned int *a, int p, int r) {
	int pivot = partition(a, p, r);
	if(pivot - p >1)
		quicksort_r(a, p, pivot -1);
	if(r - pivot >1)
		quicksort_r(a, pivot+1, r);
}

int partition(unsigned int * a, int p, int r) {
	int pivot = r; //TODO replace the r with a random function;
	int i;
	unsigned int value = a[pivot];
	swap(a + r, a + pivot);
	pivot = p - 1;
	for (i = p; i < r; i++) {
		if (a[i] < value) {
			pivot++;
			swap(a + pivot, a + i);
		}
	}
	pivot++;
	swap(a + pivot, a + r);
	return pivot;
}
void swap(unsigned int *a, unsigned int *b) {
	unsigned int tmp = *a;
	*a = *b;
	*b = tmp;
}

int get_radix(unsigned int num, int i) {
	return (num >> (i * R_BIT)) & 0x0000000f;
}
