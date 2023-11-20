package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.MPI;
import ua.nure.trspo.Util;

public class VectorSum {
	private static final int ROOT = 0;
	private static final int CAPACITY = 100;
	private static final int BOUND = 50;

	public static void main(String[] args) {

		MPI.Init(args);
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();

		int[] dataSize = new int[1];
		if (rank == ROOT) {
			dataSize[0] = CAPACITY;
		}
		// distribute size of data for each process
		MPI.COMM_WORLD.Bcast(dataSize, 0, 1, MPI.INT, ROOT);
		System.out.println("Process " + rank + " receive dataSize -> " + Arrays.toString(dataSize));

		// all processes should place sendbuf to the method call
		double[] vector = new double[dataSize[0] * size];
		// only root process initialize it
		if (rank == ROOT) {
			vector = Util.init(CAPACITY * size, BOUND);
		}
		double[] recvbuf = new double[dataSize[0]];
		
		// distribute data between processes
		MPI.COMM_WORLD.Scatter(vector, 0, dataSize[0], MPI.DOUBLE, recvbuf, 0, dataSize[0], MPI.DOUBLE, ROOT);
		
		// calculate SUM value for the given portion of data
		double[] sum = new double[] { Util.sum(recvbuf) };
		
		// gather calculated SUM value from all processes 
		// and perform MPI_SUM operation while gathering
		MPI.COMM_WORLD.Reduce(sum, 0, sum, 0, 1, MPI.DOUBLE, MPI.SUM, ROOT);
		
		// each process print local SUM value, but ROOT print total SUM value
		System.out.println("Sum " + rank +  " -> " + sum[0]);
		
		MPI.Finalize();
	}

}
