package TestUnit.commonTest;

import java.util.HashMap;
import java.util.Map;

public class TestMap {
	public static void main(String[] args) throws Exception {
		Map<String, Long> fieldMap = new HashMap<String, Long>();
	//	System.out.println(fieldMap.get("a") == null ? 0l : fieldMap.get("a"));
		fieldMap.put("a", Long.valueOf(1l));
		fieldMap.put("b", 2l);
		Map<String ,Long > map = new HashMap<String, Long>();
		map.putAll(fieldMap);
		System.out.println(fieldMap.get("a"));
		System.out.println(map.get("a"));
//		 System.out.println(fieldMap);
//		Iterator it = fieldMap.entrySet().iterator();
//		while (it.hasNext()) {
//			Map.Entry<String, Long> entry = (Entry<String, Long>) it.next();
//			System.out.println(entry.getKey());
//			System.out.println(entry.getValue());
//		}
//		fieldMap.remove("b");
//		it = fieldMap.entrySet().iterator();
//		while (it.hasNext()) {
//			Map.Entry<String, Long> entry = (Entry<String, Long>) it.next();
//			System.out.println(entry.getKey());
//			System.out.println(entry.getValue());
//		}

//		for(Map.Entry<String,Long>entry:fieldMap.entrySet()){
//			System.out.println(entry.getKey()+"   "+entry.getValue());
//		}
	}

}
