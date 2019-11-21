package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.MPI;
import ua.nure.trspo.Util;

public class Scatter {
	public static final int ROOT = 0;
	public static final int BOUND = 50;
	public static final int DATA_SIZE_PER_PROCESS = 5;

	public static void main(String[] args) {
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		// ---- Distribute size of data ----
		int[] dataSize = new int[1];
		// Only root initialize data
		if (rank == ROOT) {
			dataSize[0] = DATA_SIZE_PER_PROCESS;
		}
		MPI.COMM_WORLD.Bcast(dataSize, 0, 1, MPI.INT, ROOT);
		System.out.println("Process " + rank + " received dataSize -> " + dataSize[0]);
		
		// ---- Distribute data ---- 
		// Each process receives own portion of data of given size
		// All processes SHOULD create buffer to send 
		double[] sendbuf = new double[dataSize[0] * size];
		// But only ROOT initialize them
		if (rank == ROOT) {
			Util.init(sendbuf, BOUND);
			System.out.println("Send values -> " + Arrays.toString(sendbuf));
		}
		// All processes create buffer for received data
		double[] recvbuf = new double[dataSize[0]];
		MPI.COMM_WORLD.Scatter(
				sendbuf, 0, dataSize[0], MPI.DOUBLE, 
				recvbuf, 0, dataSize[0], MPI.DOUBLE, ROOT);
		// At this point data in recvbuf
		System.out.println("Process " + rank + " received -> " + Arrays.toString(recvbuf));

		MPI.COMM_WORLD.Barrier();
		System.out.println("Process " + rank + " sendbuf -> " + Arrays.toString(sendbuf));
		
		MPI.Finalize();
	}
}
