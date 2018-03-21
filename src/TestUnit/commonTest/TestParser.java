package TestUnit.commonTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "D:/dyj/backup/Simulate%date:yyyy-MM-dd%/%date:yyyyMMdd%";
	 

		String newPath = path;
		String[] dateFormat = new String[] { "%date:yyyyMMdd%",
				"%date:yyyy-MM-dd%" };
		if (path.toLowerCase().indexOf(dateFormat[0].toLowerCase()) >= 0) {
			newPath = path.toLowerCase().replace(dateFormat[0].toLowerCase(),
					"2012-03-14".replace("-", ""));
		}
		if (newPath.toLowerCase().indexOf(dateFormat[1].toLowerCase()) >= 0) {
	 
			newPath = newPath.toLowerCase().replace(
					dateFormat[1].toLowerCase(), "2012-03-14");
		}
		//System.out.println(newPath);
		
		
		String sd1="2011-12-28";
		String sd2="2012-01-05";
		SimpleDateFormat df = new SimpleDateFormat(// 日期格式
		"yyyy-MM-dd");
		try {
			Date d1= df.parse(sd1);
			Date d2=df.parse(sd2);
			Date d3=d1;
			Calendar c= Calendar.getInstance();
			
			while(d2.compareTo(d3)>=0){
				c.setTime(d3);
				System.out.println(df.format(c.getTime()));
				c.add(Calendar.DAY_OF_WEEK, 1);
				d3=c.getTime(); 
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
	}

}
