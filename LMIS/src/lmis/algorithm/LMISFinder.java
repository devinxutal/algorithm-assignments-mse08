package lmis.algorithm;

import java.util.Arrays;

public class LMISFinder {

	public int[] find(int[] seqs) {
		int[] m = new int[seqs.length + 1];
		int[] p = new int[seqs.length];
		for (int i = 0; i < m.length; i++) {
			m[i] = Integer.MAX_VALUE;
		}
		int len = 0;
		m[0] = -1;
		for (int i = 0; i < seqs.length; i++) {
			int j = bsearch(seqs, m, 1, len, seqs[i]);
			p[i] = m[j];
			if (j == len || seqs[i] < seqs[m[j + 1]]) {
				m[j + 1] = i;
				len = len > j + 1 ? len : j + 1;
			}
		}
		return getLMIS(seqs, m, p, len);
	}

	private int bsearch(int[] x, int[] m, int p, int r, int value) {
		if (r < p)
			return 0;
		else if (r == p) {
			if (x[m[p]] < value)
				return p;
			else
				return 0;
		}
		int middle = (r + p - 1) / 2;
		if (x[m[middle]] < value && x[m[middle + 1]] >= value) {
			return middle;
		} else if (x[m[middle]] < value) {
			return bsearch(x, m, middle + 1, r, value);
		} else if (middle == p) {
			if (x[m[p]] < value)
				return p;
			else
				return 0;
		} else {
			return bsearch(x, m, p, middle - 1, value);
		}
	}

	private int[] getLMIS(int[] x, int[] m, int[] p, int len) {
		int[] result = new int[len];
		int index = len - 1;
		for (int i = m[len]; i >= 0; i = p[i]) {
			result[index--] = x[i];
		}
		return result;
	}

	// Just for test
	public static void main(String[] args) {
		int[] seqs = new int[] { 5, 4, 2, 3, 45, 5 };
		LMISFinder finder = new LMISFinder();
		int[] result = finder.find(seqs);
		System.out.println(Arrays.toString(seqs));
		System.out.println(Arrays.toString(result));
	}
}
