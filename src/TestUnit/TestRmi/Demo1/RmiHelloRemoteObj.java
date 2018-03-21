package TestUnit.TestRmi.Demo1;

import java.rmi.server.*;
import java.rmi.*;

public class RmiHelloRemoteObj extends UnicastRemoteObject implements RmiHelloRemoteIntfc {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RmiHelloRemoteObj() throws RemoteException {
		super();
	}

	public String helloRemoteObj(String client) throws RemoteException {
		return "Hello World" + client;
	}
}