//package TestUnit.TestRmi.Demo1;
//
//import java.rmi.Naming;
//import java.rmi.RMISecurityManager;
//import java.rmi.registry.LocateRegistry;
//
//public class RmiHelloServer {
//
//	public RmiHelloServer() {
//	}
//
//	public static void main(String[] args) {
//		// ��������װ��ȫ������
//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new RMISecurityManager());
//		}
//
//		try {
//			// ����Զ�̶���
//			RmiHelloRemoteObj ttt = new RmiHelloRemoteObj();
//			// ����ע���
//			LocateRegistry.createRegistry(4588);
//			// �����ư󶨵�����
//			// System.setProperty("java.rmi.server.localhost","211.81.207.109");
//			Naming.rebind("//localhost/wx", ttt);
//
//			System.out.println("RMI�������������С�����������");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
