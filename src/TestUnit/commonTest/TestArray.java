package TestUnit.commonTest;

import java.util.Arrays;


public class TestArray {
   public static void main(String[] arg){
	   String oiwStr="oiw70,oiw71,oiw72,oiw73,oiw74,oiw75,oiw76,oiw77,oiw78,oiw79";
	   String[] oiwArray=oiwStr.split(",");
	//   String[] oiwCur=Arrays.copyOfRange(oiwArray, 0,10);
	 /*  for(int i=0;i<oiwCur.length;i++)
	   System.out.println(oiwCur[i]);*/
	   int begin=0;
	   int oiwEachThread=5;
		while (begin < oiwArray.length) {
			int end=begin + oiwEachThread;
			if(end>oiwArray.length)end=oiwArray.length;
			String[] currentThreadOiw = Arrays.copyOfRange(oiwArray, begin,
					end );
			System.out.print(begin+":");
			for(int i=0;i<currentThreadOiw.length;i++){
			System.out.print(currentThreadOiw[i]+",");
			}
			System.out.print("\n");
			begin = end;
		}
   }
}
