package ua.nure.trspo.threads.callable;

import java.io.FileNotFoundException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ua.nure.trspo.Util;

public class Matrix {
	private double[][] values;
	private double max;

	public Matrix(double[][] values) {
		this.values = values;
	}

	public double getMax() {
		return max;
	}

	public double max() {
		return max(0, values.length);
	}

	public double max(int start, int end) {
		double max = values[start][0];
		for (int i = start; i < end; i++) {
			double m = maxRow(i);
			Util.longWork(TIMEOUT);
			if (max < m) {
				max = m;
			}
		}
		return max;
	}

	private double maxRow(int row) {
		double max = values[row][0];
		for (int i = 0; i < values[row].length; i++) {
			Util.longWork(TIMEOUT);
			double m = values[row][i];
			if (max < m) {
				max = m;
			}
		}
		return max;
	}

	private ExecutorService pool;

	public void shutdown() {
		if (pool == null)
			return;
		try {
			while (pool.awaitTermination(1, TimeUnit.SECONDS)) {
			}
		} catch (InterruptedException e) {
		}
		pool.shutdown();
	}
	@SuppressWarnings("unchecked")
	public double parallelMax(int rowsPerThread) {
		if (rowsPerThread < 1 || rowsPerThread >= values.length) {
			rowsPerThread = 1;
		}
		if (pool == null) {
			pool = Executors.newFixedThreadPool(CORES);
		}
		// map
		int totalTasks = values.length / rowsPerThread;
		if (values.length % rowsPerThread > 0) {
			totalTasks++;
		}
		Future<Worker>[] tasks = new Future[totalTasks];
		for (int i = 0; i < totalTasks; i++) {
			int start = i * rowsPerThread;
			int end = start + rowsPerThread;
			if (end >= values.length) {
				end = values.length;
			}
			tasks[i] = (Future<Worker>) pool.submit(new Worker(start, end, values));
		}
		
		// reduce
		try {
			double max = tasks[0].get().getMax();
			for (int i = 0; i < tasks.length; i++) {
				double m = tasks[i].get().getMax();
				Util.longWork(TIMEOUT);
				if (max < m) {
					max = m;
				}
			}
			this.max = max;
		} catch (Exception e) {
			// do nothing
		}
		
		return max;
	}

	private static final int CORES = Runtime.getRuntime().availableProcessors();
	private static final int BOUND = 50;
	private static final long TIMEOUT = 0;
	
	private static final int ROWS = 100;
	private static final int COLS = 1_000_000;

	public static void main(String[] args) throws FileNotFoundException {
		Matrix matrix = new Matrix(Util.init(ROWS, COLS, BOUND));
		
		System.out.println("Available cores -> " + CORES);

		long startTime = System.currentTimeMillis();
		double max = matrix.max();
		long endTime = System.currentTimeMillis();
		System.out.println("Sequental -> " + max + " time -> " + (endTime - startTime));
		
		startTime = System.currentTimeMillis();
		max = matrix.parallelMax(1/*ROWS / CORES / 8*/);
		endTime = System.currentTimeMillis();
		System.out.println("Parallel  -> " + max + " time -> " + (endTime - startTime));
		matrix.shutdown();
		
	}

	class Worker implements Callable<Worker> {

		int start;
		int end;
		double[][] values;

		double max;

		public Worker() {
		}

		public Worker(double[][] values) {
			this.values = values;
		}

		public Worker(int start, int end, double[][] values) {
			this(values);
			this.start = start;
			this.end = end;
		}

		public void setRange(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public void setRange(int start, int end, double[][] values) {
			setRange(start, end);
			this.values = values;
		}

		public double getMax() {
			return max;
		}

		@Override
		public Worker call() throws Exception {
			max = max(start, end);
			return this;
		}

	}
}
