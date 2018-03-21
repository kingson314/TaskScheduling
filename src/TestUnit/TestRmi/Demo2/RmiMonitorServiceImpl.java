package TestUnit.TestRmi.Demo2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Description: ʵʱ��ʾRMI�ӿ�ʵ��.
 * 
 * ʵ��RMI�ӿڼ�Զ�̷������̳�UnicastRemoteObject��
 * 
 * @author Peter Wei
 * @version 1.0 Feb 25, 2009
 */
public class RmiMonitorServiceImpl extends UnicastRemoteObject implements
		RmiMonitorService {
	private static final long serialVersionUID = -3771656108378649574L;
	public static final int SUCCSS = 1;
	public static final int FAIL = 0;
	public WarnService warnService;

	/**
	 * ���붨�幹�췽������ΪҪ�׳�RemoteException�쳣
	 * 
	 * @throws RemoteException
	 */
	public RmiMonitorServiceImpl() throws RemoteException {
		super();
	}

	public int interactive(int funindex, String param) throws RemoteException {
		int result = FAIL;
		System.out.println("funidex:" + funindex);
		switch (funindex) {

		// �澯
		case (1): {
			// warnService = (WarnService) AppContext.getAppContext().getBean(
			// "warn.warnService");
			// ʵ��Ӧ���Ǵ�SpringӦ���л�ȡ�澯Service,���ϴ���
			warnService = new WarnServiceImpl();
			// ����澯��ҵ�����
			warnService.dealWarn(param);
			result = SUCCSS;
		}
			break;
		case (2):
			// do other biz
			break;
		}
		// ......
		return result;
	}

	public WarnService getWarnService() {
		return warnService;
	}

	public void setWarnService(WarnService warnService) {
		this.warnService = warnService;
	}
}
