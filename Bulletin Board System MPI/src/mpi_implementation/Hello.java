package mpi_implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import mpi.*;

class Hello {
	static public void main(String[] args) throws MPIException {
		CharBuffer ee = MPI.newCharBuffer(22);
		System.out.println(ee.length());
//		
//		MPI.Init(args);
//		int myrank = MPI.COMM_WORLD.getRank();
//		System.out.println("My rank is " + myrank);
//
//		try {
//			PrintWriter p = new PrintWriter(new File("mpilog.txt"));
//			p.write("My rank is " + myrank);
//			p.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (myrank == 0) {
//			int[] x = { 45 };
//			int[] y = new int[1];
//			IntBuffer out = MPI.newIntBuffer(1);
//			out.put(0, 2);
//			Scanner in = new Scanner(System.in);
//			while (true) {
//				int c = in.nextInt();
//
//				x[0] = c;
//				// MPI.COMM_WORLD.send(x, 1, MPI.INT, 1, 99);
//				MPI.COMM_WORLD.iSend(out, 1, MPI.INT, 1, 99);
//				// MPI.COMM_WORLD.sendRecv(x, /* length of message */1, MPI.INT,
//				// 1/* Destination */, 99/* TAG */, y, 1, MPI.INT,
//				// MPI.ANY_SOURCE, 99);
//				System.out.println("Received " + y[0]);
//				if (c == 2)
//					break;
//			}
//		} else {
//			int[] x = new int[1];
//			int[] y = { 6666 };
//			IntBuffer out = MPI.newIntBuffer(1);
//			while (true) {
//				// MPI.COMM_WORLD.recv(x, 1, MPI.INT, /* Source
//				// */MPI.ANY_SOURCE,99);
//				MPI.COMM_WORLD.recv(out, 1, MPI.INT, /* Source */MPI.ANY_SOURCE,
//						99);
//
//				System.out.println("received:" + out.get(0) + ":");
//				// if (x[0] == 2)
//				// break;
//
//				// MPI.COMM_WORLD.send(y, 1, MPI.INT, 0, 99);
//			}
//		}
//		MPI.Finalize();
	}
}