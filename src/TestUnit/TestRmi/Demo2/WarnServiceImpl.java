package TestUnit.TestRmi.Demo2;

/**
 * Description: 告警服务
 * 
 * @author Peter Wei
 * @version 1.0 2010-8-22
 */
public class WarnServiceImpl implements WarnService {
	public int dealWarn(String message) {
		// 告警处理方法
		System.out.println("receive");
		return 1;
	}
}