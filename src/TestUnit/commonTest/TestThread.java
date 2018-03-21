package TestUnit.commonTest;

public class TestThread {

	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Thread thread = new ThreadExample("1");
		thread.start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("1: " + thread.getState());
		thread.yield();

		System.out.println("2: " + thread.getState());
		thread.notify();
		System.out.println("3: " + thread.getState());

	}

}
