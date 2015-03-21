package rmi_implementation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Board extends Remote{
	<T> T executeTask(Task<T> t) throws RemoteException;
}
