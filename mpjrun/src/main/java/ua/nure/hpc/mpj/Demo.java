package ua.nure.hpc.mpj;

import mpi.MPI;

public class Demo {
    public static void main(String[] args) {
        MPI.Init(args);
        double time = MPI.Wtime();
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        time -= MPI.Wtime();
        System.out.printf("Rank %s of %s with time%s%n", rank, size, time);
        MPI.Finalize();
    }
}
