package ua.nure.trspo;

import java.util.Arrays;
import java.util.Random;

import mpi.Comm;
import mpi.Intracomm;
import mpi.MPI;

public class Util {

	public static double[][] init(int rows, int cols, int bound) {
		double[][] vals = new double[rows][cols];
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			for (int j = 0; j < vals[i].length; j++) {
				vals[i][j] = r.nextDouble() * bound;
			}
		}
		return vals;
	}
	
	public static double[] init(int cols, int bound) {
		double[] vals = new double[cols];
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
				vals[i] = r.nextDouble() * bound;
		}
		return vals;
	}
	
	public static double[] init(double[] vals, int bound) {
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			vals[i] = r.nextDouble() * bound;
		}
		return vals;
	}

	public static double[] initSequental(double[] vals, int start) {
		for (int i = 0; i < vals.length; i++) {
			vals[i] = start++;
		}
		return vals;
	}
	
	public static void longWork(long timeout) {
		if (timeout == 0) 
			return;
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static Intracomm createStarComm(Intracomm comm) {
		int size = comm.Size();
		int[] index = new int[size]; 
		int[] edges = new int[size * 2]; 
		for (int i = 0; i < size; i++) {
			index[i] = i * 2;
		}
		int rank = comm.Rank();
		if (rank == 0) {
			System.out.println("Index -> " + Arrays.toString(index));
		}
		for (int i = 0, j = 0; i < size; i++, j++) {
			edges[i * 2]     = 0;
			edges[i * 2 + 1] = j;
		}
		if (rank == 0) { 
			System.out.println("Edges -> " + Arrays.toString(edges));
		}
		return comm.Create_graph(index, edges, false);
	}

	public static String toString(String format, String separator, double... vals) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vals.length; i++) {
			sb.append(String.format(format, vals[i])).append(separator);
		}
		sb.delete(sb.length() - separator.length(), sb.length());
		return sb.toString();
	}
}
