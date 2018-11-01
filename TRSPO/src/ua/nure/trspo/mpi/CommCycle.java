package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.Comm;
import mpi.MPI;

public class CommCycle {
	private static final int ITERATIONS = 500_500;
	private static final int ROOT = 0;

	public static void main(String[] args) {
		MPI.Init(args);
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();
		if (rank == ROOT) {
			System.out.println(MPI.Wtick());
		}
		int[] index = new int[size];
		int[] edges = new int[size * 2];
		for (int i = 0; i < size; i++) {
			index[i] = i * 2;
		}
		System.out.println("Index -> " + Arrays.toString(index));
		for (int i = 0, j = 0; i < size; i++, j++) {
			edges[i * 2]     = j == 0        ? size - 1 : j - 1;
			edges[i * 2 + 1] = j == size - 1 ? 0        : j + 1;
		}
		System.out.println("Edges -> " + Arrays.toString(edges));

		Comm cicle = MPI.COMM_WORLD.Create_graph(index, edges, false);
		int left = rank == 0 ? size - 1 : rank - 1;
		int right = rank == size - 1 ? 0 : rank + 1;
		int[] sendbuf = new int[] { rank };
		int[] recvbuf = new int[1];
		cicle.Sendrecv(sendbuf, 0, 1, MPI.INT, right, 1, recvbuf, 0, 1, MPI.INT, left, 1);
		long st;
		long et;
		st = System.currentTimeMillis();
		for (int i = 0; i < ITERATIONS; i++) {
			cicle.Sendrecv(sendbuf, 0, 1, MPI.INT, right, 1, recvbuf, 0, 1, MPI.INT, left, 1);
		}
		et = System.currentTimeMillis();
		System.out.println("Time " + (et - st));
		System.out.println("Process " + rank + ": Send -> " + sendbuf[0] + ", receive " + recvbuf[0]);
		System.out.println("Rank -> " + cicle.Rank() + " oldrank " + rank);

		MPI.Finalize();
	}
}
