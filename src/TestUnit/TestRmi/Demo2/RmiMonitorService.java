package TestUnit.TestRmi.Demo2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Description: ʵʱ��ʾRMI����ӿ�.
 * 
 * RMI�ӿڱ�����չ�ӿ�java.rmi.Remote
 * 
 * @author Peter Wei
 * @version 1.0 Feb 25, 2009
 */
public interface RmiMonitorService extends Remote {
	/**
	 * ʵʱ��ʾ����ӿ�
	 * 
	 * @param funindex
	 *            ���ܺ�
	 * @param param
	 *            �����б�Ҳ����ʵ�ʴ��������
	 * @return
	 * @throws RemoteException
	 *             Զ�̽ӿڷ��������׳�java.rmi.RemoteException
	 */
	public int interactive(int funindex, String param) throws RemoteException;
}
