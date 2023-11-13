package ua.nure.trspo.mpi;

import java.util.Arrays;
import java.util.Random;

import mpi.MPI;

public class MpiDemo {
	private static final int ROOT = 0;

	public static void main(String[] args) {
		// Works without MPI
		// ...
		System.out.println("Start works without MPI");
		
		// initialize MPI 
		MPI.Init(args);
		
		// Here your can use MPI
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();
		System.out.println("Process " + rank + " Start works with MPI");
		
		if (rank == ROOT) {
			System.out.println("---=== Point to point ===---");
			double[] rBuf = new double[size];
			rBuf[0] = rank; 
			for (int i = 1; i < size; i++) {
				MPI.COMM_WORLD.Recv(rBuf, i, 1, MPI.DOUBLE, i, MPI.ANY_TAG);
			}
			System.out.println("Received -> " + Arrays.toString(rBuf));
		} else {
			double[] sBuf = new double[] {rank};
			MPI.COMM_WORLD.Send(sBuf, 0, 1, MPI.DOUBLE, ROOT, 1);
		}
		
		MPI.COMM_WORLD.Barrier();
		double[] sBuf = new double[size];
		if (rank == ROOT) {
			System.out.println("---=== Broadcast ===---");
			Random r = new Random();
			for (int i = 0; i < sBuf.length; i++) {
				sBuf[i] = r.nextDouble();
			}
		}
		MPI.COMM_WORLD.Bcast(sBuf, 0, size, MPI.DOUBLE, ROOT);
		System.out.println("Process " + rank + " Receive " + Arrays.toString(sBuf));
		
		MPI.COMM_WORLD.Barrier();
		double[] sendbuf = new double[size * 2];
		if (rank == ROOT) {
			System.out.println("---=== Scatter ===---");
			Random r = new Random();
			for (int i = 0; i < sendbuf.length; i++) {
				sendbuf[i] = r.nextDouble();
			}
		}
		double[] recvbuf = new double[2];
		MPI.COMM_WORLD.Scatter(sendbuf, 0, 2, MPI.DOUBLE, recvbuf, 0, 2, MPI.DOUBLE, ROOT);
		System.out.println("Process " + rank + " Receive " + Arrays.toString(recvbuf));
		
		System.out.println("Process " + rank + " End works with MPI");
		
		// Gracefully shutdown MPI for each process
		MPI.Finalize();
		
		// Works without MPI
		// ...
		System.out.println("End works without MPI");
	}
}
