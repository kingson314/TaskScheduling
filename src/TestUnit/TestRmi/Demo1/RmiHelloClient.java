package TestUnit.TestRmi.Demo1;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

@SuppressWarnings("deprecation")
public class RmiHelloClient {

	public RmiHelloClient() {
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			RmiHelloRemoteIntfc c1 = (RmiHelloRemoteIntfc) Naming
					.lookup("rmi://localhost/wx");
			System.out.println(c1.helloRemoteObj("Everyone"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
