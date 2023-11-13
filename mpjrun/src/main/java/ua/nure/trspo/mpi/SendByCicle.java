package ua.nure.trspo.mpi;

import mpi.MPI;

public class SendByCicle {
	public static final int ROOT = 0;

	public static void main(String[] args) {
		MPI.Init(args);
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();

//		int left = rank == 0 ? size - 1 : rank - 1; 
//		int right = rank == size - 1 ? 0 : rank + 1;
		
		// More effective then if else block
		int left = (rank + size - 1) % size;
		int right = (rank + 1) % size;
		int[] sendbuf = new int[] {rank};
		int[] recvbuf = new int[1];
		MPI.COMM_WORLD.Sendrecv(sendbuf, 0, 1, MPI.INT, right, 1, recvbuf, 0, 1, MPI.INT, left, 1);
		System.out.println("Process " + rank + ": Send -> " + sendbuf[0] + ", receive " + recvbuf[0]);
		
		MPI.Finalize();

	}
}
