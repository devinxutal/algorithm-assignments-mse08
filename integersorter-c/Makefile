CFLAGS = -Wall -O3
INCLUDES = -Iinclude

all: integersorter

integersorter: sorters.o sort.o
	gcc  -o"integersorter" sorters.o sort.o
	
sorters.o: sorters.c include/sorters.h
	gcc $(CFLAGS) $(INCLUDES) -c sorters.c

sort.o: sort.c
	gcc $(CFLAGS) $(INCLUDES) -c sort.c
	