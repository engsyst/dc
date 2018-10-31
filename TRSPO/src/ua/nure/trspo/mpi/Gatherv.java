package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.MPI;
import ua.nure.trspo.Util;

public class Gatherv {
	public static final int ROOT = 0;
	public static final int BOUND = 50;
	public static final int DATA_SIZE_PER_PROCESS = 5;

	public static void main(String[] args) {
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		// Distribute size of data
		int[] dataSize = new int[] {DATA_SIZE_PER_PROCESS * (rank + 1)};
		int[] recvcount = new int[size];
		MPI.COMM_WORLD.Gather(dataSize, 0, 1, MPI.INT, recvcount, 0, 1, MPI.INT, ROOT);
		if (rank == ROOT) {
			System.out.println("Process " + rank + " recvcount -> " + Arrays.toString(recvcount));
		}
		
		int[] displs = new int[size];
		if (rank == ROOT) {
			for (int i = 1; i < displs.length; i++) {
				displs[i] = displs[i - 1] + recvcount[i - 1];
			}
			System.out.println("Process " + rank + " displs -> " + Arrays.toString(displs));
		}
		
		// Gathering data. Each process sends own portion of data of given size
		// All processes SHOULD create buffer for received data
		int totalDataSize = 0;
		if (rank == ROOT) {
			for (int i = 0; i < size; i++) {
				totalDataSize += recvcount[i];
			}
		}
		double[] recvbuf = new double[totalDataSize];
		double[] sendbuf = new double[dataSize[0]];
		Util.init(sendbuf, BOUND);
		System.out.println("Process " + rank + " send values -> " + Arrays.toString(sendbuf));
		
		MPI.COMM_WORLD.Gatherv(sendbuf, 0, recvcount[rank], MPI.DOUBLE, recvbuf, 0, recvcount, displs, MPI.DOUBLE, ROOT);
		
		// At this point data in recvbuf
		if (rank == ROOT) {
		}
		System.out.println("Process " + rank + " received -> " + Arrays.toString(recvbuf));
		
		MPI.Finalize();
	}
}
