package ua.nure.trspo.mpi;

import java.util.Arrays;

import mpi.Intracomm;
import mpi.MPI;

public class Hello {
	public static final int ROOT = 0;
	public static final String MSG = "Привет from ";
	public static final int MSG_LEN = MSG.getBytes().length + 4;
	
	public static void main(String[] args) {

		MPI.Init(args);
		Intracomm commWorld = MPI.COMM_WORLD;
		int size = commWorld.Size();
		int rank = commWorld.Rank();

		if (rank == ROOT) {
			byte[] rbuf = new byte[size * MSG_LEN];
			for (int i = 1; i < size; i++) {
				commWorld.Recv(rbuf, i * MSG_LEN, MSG_LEN, MPI.BYTE, MPI.ANY_SOURCE, MPI.ANY_TAG);
				System.out.println(new String(rbuf, i * MSG_LEN, MSG_LEN));
			}
		} else {
			byte[] sbuf = Arrays.copyOf((MSG + rank).getBytes(), MSG_LEN);
			commWorld.Send(sbuf, 0, MSG_LEN, MPI.BYTE, ROOT, 1);
		}
		
		MPI.Finalize();
	}
}
