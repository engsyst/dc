package ua.nure.trspo.mpi;

import mpi.Comm;
import mpi.Intracomm;
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
			System.out.println("Initial data: " + Util.toString("%6.2f", ",", values));
		}
		MPI.COMM_WORLD.Bcast(values, 0, size, MPI.DOUBLE, ROOT);
		System.out.println("Process " + rank + " received -> " + Util.toString("%6.2f", ",", values));
		
		MPI.Finalize();
	}
}
