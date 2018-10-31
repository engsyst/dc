package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.MPI;
import ua.nure.trspo.Util;

public class Scatterv {
	public static final int ROOT = 0;
	public static final int BOUND = 50;
	public static final int TOTAL_DATA_SIZE = 50;

	public static void main(String[] args) {
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		// ---- Distribute size of data ----
		int[] totalDataSize = new int[1];
		// Only root initialize data
		if (rank == ROOT) {
			totalDataSize[0] = TOTAL_DATA_SIZE;
		}
		MPI.COMM_WORLD.Bcast(totalDataSize, 0, 1, MPI.INT, ROOT);
		System.out.println("Process " + rank + " received dataSize -> " + totalDataSize[0]);
		
		// Each process calculate size of own portion of data
		int[] displs = new int[size];        // positions of data for each process
		int[] sendcount = new int[size];     // counts of data for each process
		int sliceSize = totalDataSize[0] / size;  
		if (sliceSize * size < totalDataSize[0]) {  // 
			sliceSize++;
		}
		for (int i = 0; i < size; i++) {
			sendcount[i] = sliceSize;
			displs[i] = i * sliceSize;
		}
		// The last process should only receive the remaining data.
		sendcount[size - 1] = totalDataSize[0] - (size - 1) * sliceSize;
		if (rank == ROOT) {
			System.out.println("Send count  -> " + Arrays.toString(sendcount));
			System.out.println("Displs      -> " + Arrays.toString(displs));
		}
		
		// ---- Distribute data ---- 
		// Each process receives own portion of data of given size
		// All processes SHOULD create buffer to send, 
		// ONLY for ScatterV in the receivers it can be zero size
		double[] sendbuf = new double[0];
		// But only root initialize it
		if (rank == ROOT) {
			sendbuf = Util.init(totalDataSize[0] * size, BOUND);
		}
		// All processes create a buffer for received data
		double[] recvbuf = new double[sendcount[rank]]; // Size of buffer is individual for each process
		MPI.COMM_WORLD.Scatterv(
				sendbuf, 0, sendcount, displs, MPI.DOUBLE, 
				recvbuf, 0, sendcount[rank], MPI.DOUBLE, ROOT);
		// At this point data in recvbuf
		System.out.println("Process " + rank + " received data -> " + Arrays.toString(recvbuf));

		MPI.Finalize();
	}
}
