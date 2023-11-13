package ua.nure.trspo.mpi;

import mpi.Cartcomm;
import mpi.MPI;
import mpi.ShiftParms;

public class CommGridCycle {

	public static void main(String[] args) {
		MPI.Init(args);
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();
		int[] dims = new int[] { size };
		boolean[] periods = new boolean[] { true };
//		initGraph(dims, periods, size);

		Cartcomm cicle = MPI.COMM_WORLD.Create_cart(dims, periods, true);
		System.out.println("Rank -> " + cicle.Rank() + " oldrank " + rank);
		
		// for better output only
		cicle.Barrier();
		
		// neighbors
		ShiftParms coords = cicle.Shift(0, 2);
		int left = coords.rank_source;
		int right = coords.rank_dest;
		System.out.println("Left " + left + " Rank -> " + cicle.Rank() + " Right " + right);
		
		// for better output only
		cicle.Barrier();

		int[] sendbuf = new int[] { rank };
		int[] recvbuf = new int[1];
		cicle.Sendrecv(sendbuf, 0, 1, MPI.INT, right, 1, recvbuf, 0, 1, MPI.INT, left, 1);
		System.out.println("Process " + rank + ": Send -> " + sendbuf[0] + ", receive " + recvbuf[0]);

		MPI.Finalize();
	}
}
