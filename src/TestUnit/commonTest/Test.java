package TestUnit.commonTest;

import net.sf.json.JSONObject;

public class Test {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	private static String getStrackTrace(Exception e) {
		String stackTraceMsg = e.getMessage() + "\n";
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement element : stackTrace) {
			stackTraceMsg = stackTraceMsg + element.toString() + "\n";
		}
		return stackTraceMsg;
	}

	public static void main(String[] args) throws Exception {
		testBean b=new testBean();
		b.setId("1");
		b.setName("[开始日期,varchar,in,%date:yyyy-MM-dd%];" +
				"[结束日期,varchar,in,%date:yyyy-MM-dd%]; " +
				"[基金代码,varchar,in,''];[输出错误标志,int,out];" +
				"[输出错误信息,String,out]");
		testBean bb=(testBean)JSONObject.toBean(JSONObject.fromObject(b),testBean.class);
		
//		String str=JSONObject.fromObject(bb).toString();
		System.out.println(bb.getName());
//		System.out.println(str);
		
//		String jsonStr="{id:'1',name:'liu'}";
//		JSONObject jsonObj=JSONObject.fromObject(jsonStr);
//		testBean bean = (testBean)JSONObject.toBean(jsonObj,testBean.class);
//		System.out.println("name:"+bean.getName());
	}

	private static int CountString(String str, String match) {
		int result = 0;
		while (str.indexOf(match) >= 0) {
			result += 1;
			str = str.substring(str.indexOf(match) + 1);
		}
		return result;
	}

	@SuppressWarnings("unused")
	private static String match(String oldPath, String match) {
		if (oldPath.indexOf("{") >= 0) {
			if (oldPath.indexOf("}") < 0) {
				System.out.println("字符串匹配错误,应包含'}'"); 
				return null;
			}
		}
		if (oldPath.indexOf("}") >= 0) {
			if (oldPath.indexOf("{") < 0) {
				System.out.println( "字符串匹配错误,应包含'{'"); 
				return null;
			}
		}

		String realFilePath = oldPath.substring(0, oldPath.indexOf("{"));
		System.out.println(realFilePath);
		String matchString = oldPath.substring(oldPath.indexOf("{"));
		matchString = matchString.replace("{", "");
		matchString = matchString.replace("}", "");
		System.out.println(matchString);
		int matchCount = CountString(realFilePath, match);
		System.out.println(matchCount);
		String[] matchElement = matchString.split(",");
		if (matchCount != matchElement.length) {
			System.out.println("字符串匹配个数错误,*的个数应于{}内元素个数相等"); 
			return null;
		}

		String result = "";
		int i = 0;
		while (realFilePath.indexOf(match) >= 0) {
			result = result
					+ realFilePath.substring(0, realFilePath.indexOf(match))
					+ matchElement[i];

			realFilePath = realFilePath
					.substring(realFilePath.indexOf(match) + 1);
			i = i + 1;
		}
		System.out.println(result);
		return result;
	}

}
