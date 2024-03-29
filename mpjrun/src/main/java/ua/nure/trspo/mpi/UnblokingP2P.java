package ua.nure.trspo.mpi;

import java.util.Arrays;
import java.util.Random;

import mpi.MPI;
import mpi.Request;
import mpi.Status;
import ua.nure.trspo.Util;

public class UnblokingP2P {
	private static final int ROOT = 0;
	private static final int BOUND = 50;

	public static void main(String[] args) {
		// initialize MPI
		MPI.Init(args);

		// Here your can use MPI
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();

		int left = rank == 0 ? size - 1 : rank - 1;
		int right = rank == size - 1 ? 0 : rank + 1;
		int[] sendbuf = new int[] { rank, rank };
		int[] recvbuf = new int[] {-1, -1};
		// Send lower part of sendbuf to the left process
		MPI.COMM_WORLD.Isend(sendbuf, 0, 1, MPI.INT, left, 1);
		// Send higher part of sendbuf to the right process
		MPI.COMM_WORLD.Isend(sendbuf, 1, 1, MPI.INT, right, 1);

		// Try receive higher part to the recvbuf from the right process
		Request rRequest = MPI.COMM_WORLD.Irecv(recvbuf, 1, 1, MPI.INT, right, MPI.ANY_TAG);
		// Try receive lower part to recvbuf from the left process
		Request lRequest = MPI.COMM_WORLD.Irecv(recvbuf, 0, 1, MPI.INT, left, MPI.ANY_TAG);
//		System.out.println("Process " + rank + " do something while message sends");

		Request[] requests = new Request[] { lRequest, rRequest };
		Status[] test;
		Status status;
//		do {
//			test = Request.Testsome(requests);
//			System.out.println("Process " + rank + " received -> " + Arrays.toString(recvbuf));
//			System.out.println(test != null);
//		} while (test == null);
		
//		do {
//			status = Request.Testany(requests);
//			System.out.println("Process " + rank + " received -> " + Arrays.toString(recvbuf));
//			System.out.println(status != null);
//		} while (status == null);

		// if Wait() was not called, recvbuf may be empty
		// try comment it
//		rRequest.Wait();
//		lRequest.Wait();
//		Request.Waitall(new Request[] {lRequest, rRequest});
//		Request.Waitany(new Request[] {lRequest, rRequest});
		Request.Waitsome(new Request[] {lRequest, rRequest});
		System.out.println("Process " + rank + " received -> " + Arrays.toString(recvbuf));

		// Gracefully shutdown MPI for each process
		MPI.Finalize();
	}
}
