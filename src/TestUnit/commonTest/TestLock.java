package TestUnit.commonTest;
 
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class TestLock {
	public static void main(String[] args) throws Exception {
		Runnable r = new Runnable() {
			public void run() {

				try {
					FileChannel channel = new RandomAccessFile("D:/目的文件夹/复制/settings.ini", "rw")
							.getChannel();
					FileLock lock = channel.tryLock();
					if (lock != null) {
						System.out.println(Thread.currentThread().getName()
								+ ":Open the File");

						Thread.sleep(10000);
						lock.release();
						System.out.println("文件释放可用");
					} else
						System.out.println(Thread.currentThread().getName()
								+ "文件在使用中");

				} catch (Exception e) {

				} finally {

				}
			}

		};
		new Thread(r).start();
		new Thread(r).start();
	}
}
