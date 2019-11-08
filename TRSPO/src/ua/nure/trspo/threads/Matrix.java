package ua.nure.trspo.threads;

import ua.nure.trspo.Util;

public class Matrix {
	
	double[][] values;
	private int maxThreads;
	
	public Matrix(double[][] values) {
		this.values = values;
		maxThreads = Runtime.getRuntime().availableProcessors();
	}

	public Matrix(double[][] values, int threads) {
		this.values = values;
		this.maxThreads = threads;
	}

	class MaxRowThread extends Thread {
		double[] v;
		double max;

		public double getMax() {
			return max;
		}

		public MaxRowThread(double[] v) {
			this.v = v;
			max = v[0];
		}

		@Override
		public void run() {
			max = max(v);
		}
	}
	
	public double parallelMax() {
		
		double max = values[0][0];
		MaxRowThread[] threads = new MaxRowThread[maxThreads];
		int s = 0;
		int f = 0;
		while(s < values.length) {
			for (int i = 0; i < threads.length && s < values.length; i++) {
				threads[i] = new MaxRowThread(values[s]);
				threads[i].start();
				s++;
			}
			
			for (int j = 0; j < threads.length && f < values.length; j++) {
				try {
					threads[j].join();
					double m = threads[j].getMax();
					Util.longWork(TIMEOUT);
					if (max < m) {
						max = m;
					}
					f++;
				} catch (InterruptedException e) {}
				
			}
		}
		return max;
	}
	
	public double max() {
		double max = values[0][0];
		for (int i = 0; i < values.length; i++) {
			Util.longWork(TIMEOUT);
			double m = max(values[i]);
			if (max < m) {
				max = m;
			}
		}
		return max;
	}

	public double max(double[] values) {
		double max = values[0];
		for (int j = 1; j < values.length; j++) {
			Util.longWork(TIMEOUT);
			if (max < values[j]) {
				max = values[j];
			}
		}
		return max;
	}
	
	private static final int BOUND = 50;
	private static final long TIMEOUT = 0;
	
	private static final int ROWS = 10;
	private static final int COLS = 1_000_000;

	public static void main(String[] args) {
		Matrix matrix = new Matrix(Util.init(ROWS, COLS, BOUND));
		System.out.println("Threads: " + matrix.maxThreads);
		System.out.println("Sequental ");
		long startTime = System.currentTimeMillis();
		double max = matrix.max();
		long endTime = System.currentTimeMillis();
		System.out.println("Max -> " + max + " Time -> " + (endTime - startTime));
		
		System.out.println("\nParallel ");
		startTime = System.currentTimeMillis();
		max = matrix.parallelMax();
		endTime = System.currentTimeMillis();
		System.out.println("Max -> " + max + " Time -> " + (endTime - startTime));
	}
	
}
