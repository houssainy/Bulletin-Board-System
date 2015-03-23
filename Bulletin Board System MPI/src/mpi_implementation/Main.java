package mpi_implementation;

import ssh.User;
import mpi.MPI;
import mpi.MPIException;

public class Main {

	public static void main(String[] args) throws MPIException,
			InterruptedException {
		MPI.Init(null);
		int myrank = MPI.COMM_WORLD.getRank();
		if (myrank == 0)
			new MPIServer();
		else {
			if (myrank % 2 == 0)
				new MPIClient(myrank, User.CLIENT_READER_TYPE);
			else
				new MPIClient(myrank, User.CLIENT_WRITER_TYPE);
		}

		MPI.Finalize();
	}
}
