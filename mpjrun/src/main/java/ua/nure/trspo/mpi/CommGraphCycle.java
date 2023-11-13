package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.Comm;
import mpi.MPI;

public class CommGraphCycle {
	private static final int MILLIS = 1000;
	private static final int ITERATIONS = 500;
	private static final int ROOT = 0;

	public static void main(String[] args) {
		MPI.Init(args);
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();
		int[] index = new int[size];
		int[] edges = new int[size * 2];
		initGraph(index, edges, size);

		Comm cicle = MPI.COMM_WORLD.Create_graph(index, edges, false);
		System.out.println("Rank -> " + cicle.Rank() + " oldrank " + rank);
		
		// neighbors
		int left = (rank + size - 1) % size;
		int right = (rank + 1) % size;
		System.out.println("Left " + left + " Rank -> " + cicle.Rank() + " Right " + right);
		int[] sendbuf = new int[] { rank };
		int[] recvbuf = new int[1];
		cicle.Sendrecv(sendbuf, 0, 1, MPI.INT, right, 1, recvbuf, 0, 1, MPI.INT, left, 1);
		long st;
		long et;
		
		MPI.COMM_WORLD.Barrier();
		st = System.currentTimeMillis();
		for (int i = 0; i < ITERATIONS; i++) {
			cicle.Sendrecv(sendbuf, 0, 1, MPI.INT, right, 1, 
					recvbuf, 0, 1, MPI.INT, left, 1);
		}
		et = System.currentTimeMillis();
		System.out.println("Time " + ((double)(et - st)) / MILLIS + " s");
		System.out.println("Process " + rank + ": Send -> " + sendbuf[0] + ", receive " + recvbuf[0]);

		MPI.Finalize();
	}

	private static void initGraph(int[] index, int[] edges, int size) {
		for (int i = 0; i < size; i++) {
			index[i] = i * 2;
		}
		System.out.println("Index -> " + Arrays.toString(index));
		for (int i = 0, j = 0; i < size; i++, j++) {
			edges[i * 2]     = j == 0        ? size - 1 : j - 1;
			edges[i * 2 + 1] = j == size - 1 ? 0        : j + 1;
		}
		System.out.println("Edges -> " + Arrays.toString(edges));
	}
}
