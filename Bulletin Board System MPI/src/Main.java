import java.io.IOException;

import mpi.MPI;
import mpi.MPIException;

public class Main {

	public static void main(String[] args) throws MPIException,
			InterruptedException, NumberFormatException, IOException {
		MPI.Init(args);
		int myrank = MPI.COMM_WORLD.getRank();
		System.out.println("My rank is " + myrank);
		if (myrank == 0)
			new MPIServer(Integer.parseInt(args[0]));
		else {
			if (myrank % 2 == 0)
				new MPIClient(myrank, User.CLIENT_READER_TYPE);
			else
				new MPIClient(myrank, User.CLIENT_WRITER_TYPE);
		}
	}
}
