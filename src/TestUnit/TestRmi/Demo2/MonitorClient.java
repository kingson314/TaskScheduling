package TestUnit.TestRmi.Demo2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Description: RMI�ͻ���.
 * 
 * @author Peter Wei
 * @version 1.0 Feb 25, 2009
 */
public class MonitorClient {
	public RmiMonitorService monitorService;
	public String ip = "localhost";
	public int port = 8889;

	public int interactive(int funindex, String param) {
		int result = 0;
		try {
			getMonitorService().interactive(funindex, param);
			result = 1;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}

	public RmiMonitorService getMonitorService() {
		try {
			// ��RMI����ע����в�������ΪRmiMonitorService�Ķ��󣬲��������ϵķ���
			monitorService = (RmiMonitorService) Naming.lookup("rmi://" + ip
					+ ":" + port + "/comm");
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return monitorService;
	}

	public String getValue(String content, String key) {
		String value = "";
		int begin = 0, end = 0;
		begin = content.indexOf(key + "=");
		end = content.indexOf("&", begin);
		if (end == -1)
			end = content.length();
		value = content.substring(begin + key.length() + 1, end);
		return value;
	}

	public static void main(String args[]) throws RemoteException {
		MonitorClient client = new MonitorClient();
		System.out.println("���͸澯��Ϣ:");
		String msg = "tsid=1022&devid=10001027&warnid=102&warntype=01&warnlevel=1&warnmsg=�豸����,����.";
		System.out.println(client.getValue(msg, "warnmsg"));
		client.interactive(1, msg);
	}

}
