package TestUnit.commonTest;

public class TestSubString {
	public static void main(String[] args) {

		String str = "ftp://%ftpName:易方达FTP(hs2ta)%//清算/%yyyyMMdd%";
		String ftp = "ftp://%ftpName:易方达FTP(hs2ta)%";
		String out = str.substring(str.indexOf(ftp) + ftp.length());
		System.out.println(out);
//		System.out.println(str.substring(str.indexOf("中"), 3));
	}
}
