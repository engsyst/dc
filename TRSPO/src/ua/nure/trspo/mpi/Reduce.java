package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.MPI;
import ua.nure.trspo.Util;

public class Reduce {
	public static final int ROOT = 0;
	public static final int BOUND = 50;
	public static final int DATA_SIZE_PER_PROCESS = 5;

	public static void main(String[] args) {
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();
		
		// Initialize data
		double[] values = new double[DATA_SIZE_PER_PROCESS];
		Util.init(values, BOUND);
		System.out.println("Process " + rank + " values -> " + Arrays.toString(values));
		
		// Each process calculate max value
		double max = max(values);
		System.out.println("Process " + rank + " Max    -> " + max);
		
		// All processes SHOULD create buffer for received data
		// But only ROOT process obtain filed data
		double[] buf = new double[] {max};
		
		// Gathering data. 
		// Each process sends own calculated value
		// ROOT calculate total value using given operation -> MPI.MAX
		MPI.COMM_WORLD.Reduce(buf, 0, buf, 0, 1, MPI.DOUBLE, MPI.MAX, ROOT); 
		
		// At this point ROOT have total value in buf
		if (rank == ROOT) {
			System.out.println("Process " + rank + " received -> " + Arrays.toString(buf));
		}
		
		// At this point all processes have total value in buf
		MPI.COMM_WORLD.Allreduce(buf, 0, buf, 0, 1, MPI.DOUBLE, MPI.MAX); 
		System.out.println("Process " + rank + " received -> " + Arrays.toString(buf));
		
		MPI.Finalize();
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
}
