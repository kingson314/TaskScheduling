package TestUnit.commonTest;

public class ThreadExample extends Thread {
 

	public ThreadExample(String string) {
	}

	public void run() {
		System.out.println("trhead is running!");
		for(int i =0;i<100;i++){
			System.out.print(i+" ");
			
		}
		System.out.println("trhead is stop!");
	}
}
