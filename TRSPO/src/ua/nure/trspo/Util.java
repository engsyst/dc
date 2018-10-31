package ua.nure.trspo;

import java.util.Random;

public class Util {

	public static double[][] init(int rows, int cols, int bound) {
		double[][] vals = new double[rows][cols];
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			for (int j = 0; j < vals[i].length; j++) {
				vals[i][j] = r.nextDouble() * bound;
			}
		}
		return vals;
	}
	
	public static double[] init(int cols, int bound) {
		double[] vals = new double[cols];
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
				vals[i] = r.nextDouble() * bound;
		}
		return vals;
	}
	
	public static double[] init(double[] vals, int bound) {
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			vals[i] = r.nextDouble() * bound;
		}
		return vals;
	}

	public static void longWork(long timeout) {
		if (timeout == 0) 
			return;
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
