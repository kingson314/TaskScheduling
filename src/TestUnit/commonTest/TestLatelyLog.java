package TestUnit.commonTest;

import java.util.HashMap;
import java.util.Map;

import com.log.lately.AddLogLately;
import com.log.lately.LogLately;



public class TestLatelyLog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 @SuppressWarnings("unused")
		Map<Integer,AddLogLately> mapLatelyLog=new HashMap<Integer, AddLogLately>();
		 
//		 if(mapLatelyLog.get(1)==null){
//			 LatelyLog latelyLog=new LatelyLog(2);
//			 mapLatelyLog.put(1, latelyLog);
//		 }else 
//		 
		 {
			 AddLogLately latelyLog= new AddLogLately(4);
			 LogLately log0=new LogLately();
			 log0.setScheID("0");
			 latelyLog.addLog(log0);
			 System.out.println(latelyLog.getIndex());
			 LogLately log1=new LogLately();
			 log1.setScheID("1");
			 latelyLog.addLog(log1);
			 System.out.println(latelyLog.getIndex());
			 LogLately log2=new LogLately();
			 log2.setScheID("2");
			 latelyLog.addLog(log2);
			 System.out.println(latelyLog.getIndex());
			 LogLately[] loglate=latelyLog.getLogLately();
			 for(int i=0;i<loglate.length;i++){
				 if(loglate[i]!=null)
				 System.out.println("scheid"+loglate[i].getScheID());
			 }
		 }

	}

	
}
