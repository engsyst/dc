package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.Intracomm;
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
		
		Intracomm comm = MPI.COMM_WORLD;
		printMsg("\n---=== COMM_WORLD ===---", rank, comm);
		reduce(rank, max, comm);
		
		comm = Util.createStarComm(MPI.COMM_WORLD); 
		printMsg("\n---=== COMM_STAR ===---", rank, comm);
		reduce(rank, max, comm);
		
		MPI.Finalize();
	}

	public static void reduce(int rank, double max, Intracomm comm) {
		printMsg("---- Reduce ----", rank, comm);

		// All processes SHOULD create buffer for received data
		// But only ROOT process obtain filed data
		double[] buf = new double[] {max};
		
		// Gathering data. 
		// Each process sends own calculated value
		// ROOT calculate total value using given operation -> MPI.MAX
		comm.Reduce(buf, 0, buf, 0, 1, MPI.DOUBLE, MPI.MAX, ROOT); 
		
		// At this point ROOT have total value in buf
		// Other processes have undefined value in buf
		System.out.println("Process " + rank + " received -> " + Arrays.toString(buf));
		
		printMsg("---- Allreduce ----", rank, comm);
		// At this point all processes have total value in buf
		comm.Allreduce(buf, 0, buf, 0, 1, MPI.DOUBLE, MPI.MAX); 
		System.out.println("Process " + rank + " received -> " + Arrays.toString(buf));
	}

	public static void printMsg(String msg, int rank, Intracomm comm) {
		comm.Barrier(); // for better console output ONLY
		if (rank == ROOT) {
			System.out.println(msg);
		}
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
