package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.MPI;
import ua.nure.trspo.Util;

public class Bcast {
	public static final int ROOT = 0;
	private static final int BOUND = 50;

	public static void main(String[] args) {
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();  // MPI_Comm_rank(MPI_COMM_WORLD, &rank);
		int size = MPI.COMM_WORLD.Size();  // MPI_Comm_size(MPI_COMM_WORLD, &size);
		
		double[] values = new double[size];
		if (rank == ROOT) {
			Util.init(values, BOUND);
		}
		MPI.COMM_WORLD.Bcast(values, 0, size, MPI.DOUBLE, ROOT);
		System.out.println("Process " + rank + " received -> " + Arrays.toString(values));
		
		MPI.Finalize();
	}
}
