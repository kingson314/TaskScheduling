package TestUnit.TestRmi.Demo1;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class RmiHelloClient {

	public RmiHelloClient() {
	}

	public static void main(String[] args) {
		// 创建并安装安全管理器
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
