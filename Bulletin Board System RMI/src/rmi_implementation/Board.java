package rmi_implementation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Board extends Remote {
	String readValue(int id) throws RemoteException;;

	String updateValue(int value, int id) throws RemoteException;;
}
