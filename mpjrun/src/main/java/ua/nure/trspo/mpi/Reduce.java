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
		double max = Util.max(values);
		System.out.println("Process " + rank + " Max    -> " + max);
		
		Intracomm comm = MPI.COMM_WORLD;
		printMsg("\n---=== COMM_WORLD ===---",comm);
		double rMax = reduce(max, comm);
		System.out.println("Process " + rank + " received -> " + rMax);
		rMax = allreduce(max, comm);
		System.out.println("Process " + rank + " received -> " + rMax);
		
//		printMsg("\n---=== COMM_STAR ===---", comm);
//		comm = Util.createStarComm(MPI.COMM_WORLD); 
//		rMax = reduce(max, comm);
//		System.out.println("Process " + rank + " received -> " + rMax);
//		rMax = allreduce(max, comm);
//		System.out.println("Process " + rank + " received -> " + rMax);
		
		MPI.Finalize();
	}

	public static double reduce(double max, Intracomm comm) {
		printMsg("---- Reduce ----", comm);

		// All processes SHOULD create buffer for received data
		// But only ROOT process obtain filed data
		double[] buf = new double[] {max};
		
		// Gathering data. 
		// Each process sends own calculated value
		// ROOT calculate total value using given operation -> MPI.MAX
		comm.Reduce(buf, 0, buf, 0, 1, MPI.DOUBLE, MPI.MAX, ROOT); 
		return buf[0]; 
	}

	public static double allreduce(double max, Intracomm comm) {
		printMsg("---- Allreduce ----", comm);
		
		// All processes SHOULD create buffer for received data
		double[] buf = new double[] {max};

		// At this point all processes have total value in buf
		comm.Allreduce(buf, 0, buf, 0, 1, MPI.DOUBLE, MPI.MAX); 
		return buf[0]; 
	}
	
	public static void printMsg(String msg, Intracomm comm) {
		comm.Barrier(); // for better console output ONLY
		if (comm.Rank() == ROOT) {
			System.out.println(msg);
		}
	}
}
