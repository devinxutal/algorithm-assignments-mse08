package lmis.algorithm;

import java.util.Arrays;
import java.util.LinkedList;

public class KnapsackFinder {

	public static final int WEIGHT = 0;
	public static final int VALUE = 1;

	public int[][] find(int[][] items, int cap) {
		int[][] c = new int[items.length + 1][cap + 1];
		for (int w = 0; w < c[0].length; w++) {
			c[0][w] = 0;
		}
		for (int i = 1; i < c.length; i++) {
			for (int w = 0; w < c[i].length; w++) {
				if (w == 0) {
					c[i][w] = 0;
				} else {
					int weight = items[i - 1][WEIGHT];
					int value = items[i - 1][VALUE];
					if (weight <= w) {
						if (c[i - 1][w - weight] + value > c[i - 1][w]) {
							c[i][w] = c[i - 1][w - weight] + value;
						} else {
							c[i][w] = c[i - 1][w];
						}
					} else {
						c[i][w] = c[i - 1][w];
					}
				}
			}
		}
		return c;
	}

	public LinkedList<Integer> whatToTake(int[][] items, int cap) {
		int[][] result = find(items, cap);
		LinkedList<Integer> list = new LinkedList<Integer>();

		getResult(result, items, list);
		return list;
	}

	private void getResult(int[][] c, int[][] items, LinkedList<Integer> list) {
		int i = c.length - 1, w = c[i].length - 1;
		while (i > 0) {
			if (c[i][w] >= 0) {
				if (c[i][w] != c[i - 1][w]) {
					list.addFirst(i - 1);
					w = w - items[i - 1][WEIGHT];
				}
			}
			i--;
		}
	}

	public void printResult(int[][] c, int[][] items) {
		System.out.println(Arrays.toString(c[0]));
		System.out.println(Arrays.toString(c[1]));
		System.out.println(Arrays.toString(c[2]));
		System.out.println(Arrays.toString(c[3]));
		System.out.println("==========Result==========");
		int i = c.length - 1, w = c[i].length - 1;
		while (i > 0) {
			if (c[i][w] >= 0) {
				if (c[i][w] != c[i - 1][w]) {
					System.out.println("[ITEM " + (i - 1) + "] weight: "
							+ items[i - 1][WEIGHT] + ", value: "
							+ items[i - 1][VALUE]);
					w = w - items[i - 1][WEIGHT];
				}
			}
			i--;
		}
	}

	public static void main(String[] args) {
		int[][] items = new int[][] { { 10, 60 }, { 20, 100 }, { 30, 180 },
				{ 40, 500 } };
		KnapsackFinder finder = new KnapsackFinder();
		finder.printResult(finder.find(items, 60), items);
	}
}
