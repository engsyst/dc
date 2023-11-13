package ua.nure.trspo;

import java.util.Random;

public class Util {
	static Random r = new Random();
	
	private Util() {
		super();
	}

	public static double[][] init(int rows, int cols, int bound) {
		double[][] vals = new double[rows][cols];
		for (int i = 0; i < vals.length; i++) {
			for (int j = 0; j < vals[i].length; j++) {
				vals[i][j] = r.nextDouble() * bound;
			}
		}
		return vals;
	}
	
	public static double[] init(int cols, int bound) {
		double[] vals = new double[cols];
		for (int i = 0; i < vals.length; i++) {
				vals[i] = r.nextDouble() * bound;
		}
		return vals;
	}
	
	public static double[] init(double[] vals, int bound) {
		for (int i = 0; i < vals.length; i++) {
			vals[i] = r.nextDouble() * bound;
		}
		return vals;
	}

	public static double[] initSequental(double[] vals, int start) {
		for (int i = 0; i < vals.length; i++) {
			vals[i] = start++;
		}
		return vals;
	}
	
	public static double max(double[] values) {
		double max = values[0];
		for (int j = 1; j < values.length; j++) {
			if (max < values[j]) {
				max = values[j];
			}
		}
		return max;
	}
	
	public static void longWork(long timeout) {
//		if (timeout == 0) 
//			return;
//		try {
//			Thread.sleep(timeout);
//		} catch (InterruptedException e) {
//			Thread.currentThread().interrupt();
//		}
	}

	public static String toString(String format, String separator, double... vals) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < vals.length; i++) {
			sb.append(String.format(format, vals[i])).append(separator);
		}
		sb.delete(sb.length() - separator.length(), sb.length());
		return sb.toString();
	}
}
