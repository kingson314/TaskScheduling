package TestUnit.TestRmi.Demo2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Description: RMI�����.
 * 
 * @author Peter Wei
 * @version 1.0 Feb 25, 2009
 */
public class RmiServer {
	public String ip = "localhost";
	public int port = 8889;

	/**
	 * ����RMIע����񣬲�ע��Զ�̶���.ʵ��Ӧ��������Spring��ʼ��������
	 */
	public void init() {
		try {
			LocateRegistry.createRegistry(port);
			// ����һ��Զ�̶���
			RmiMonitorService comm = new RmiMonitorServiceImpl();
			Naming.bind("//" + ip + ":" + port + "/comm", comm);
		} catch (RemoteException e) {
			System.out.println("����Զ�̶������쳣��" + e.toString());
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.out.println("�����ظ��󶨶����쳣��" + e.toString());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("����URL�����쳣��" + e.toString());
			e.printStackTrace();
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void main(String[] args) {
		// ʵ��Ӧ��������Spring��ʼ��������
		RmiServer rmiServer = new RmiServer();
		System.out.println("RMI�����ʼ��:");
		rmiServer.init();
	}
}
