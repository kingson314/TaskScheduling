package TestUnit.TestRmi.Demo1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiHelloRemoteIntfc extends Remote {
	String helloRemoteObj(String client) throws RemoteException;
}
