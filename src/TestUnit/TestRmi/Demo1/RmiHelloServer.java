package TestUnit.TestRmi.Demo1;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

public class RmiHelloServer {

	public RmiHelloServer() {
	}

	public static void main(String[] args) {
		// 创建并安装安全管理器
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			// 创建远程对象
			RmiHelloRemoteObj ttt = new RmiHelloRemoteObj();
			// 启动注册表
			LocateRegistry.createRegistry(4588);
			// 奖名称绑定到对象
			// System.setProperty("java.rmi.server.localhost","211.81.207.109");
			Naming.rebind("//localhost/wx", ttt);

			System.out.println("RMI服务器正在运行。。。。。。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
