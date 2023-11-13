package ua.nure.trspo.mpi;

import mpi.MPI;
import ua.nure.trspo.Util;

public class AllToAll {
	public static final int ROOT = 0;
	public static final int BOUND = 50;
	public static final int DATA_SIZE_PER_PROCESS = 2;

	public static void main(String[] args) {
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		// Initialize data
		double[] sendbuf = new double[DATA_SIZE_PER_PROCESS * size];
		Util.initSequental(sendbuf, rank * DATA_SIZE_PER_PROCESS * size);
		System.out.println("Send values -> " + Util.toString("%6.2f", ", ", sendbuf));
		
		// Gathering data. Each process sends own portion of data of given size
		// All processes SHOULD create buffer for received data
		// But only ROOT process obtain filed data
		double[] recvbuf = new double[DATA_SIZE_PER_PROCESS * size];
		
		MPI.COMM_WORLD.Alltoall(sendbuf, 0, DATA_SIZE_PER_PROCESS, MPI.DOUBLE, recvbuf, 0, DATA_SIZE_PER_PROCESS, MPI.DOUBLE); 
//		Gather(sendbuf, 0, DATA_SIZE_PER_PROCESS, MPI.DOUBLE, recvbuf, 0, DATA_SIZE_PER_PROCESS, MPI.DOUBLE, ROOT);
		
		// At this point data in recvbuf
		System.out.println("Process " + rank + " received -> " + Util.toString("%6.2f", ", ", recvbuf));
		
		MPI.Finalize();
	}
}
