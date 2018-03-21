package TestUnit.commonTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDBF_Db {

	public static void main(String[] args) {
		getDBF("D:/DBFFile","SJSHQ");
	}

	private static void getDBF(String DBFPath, String DBFFile) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
//		String url = "jdbc:odbc:Driver={Microsoft Visual FoxPro Driver};SourceType=DBF;"
//				+ "SourceDB='" + DBFPath + "';Exclusive=No;";
		String url="jdbc:odbc:Driver={Microsoft dBASE Driver(*.dbf)};DBQ="+DBFPath+";";
		System.out.println(url);
		try {
			con = DriverManager.getConnection(url,"","");
			String sql = "select *from " + DBFFile;
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.beforeFirst();
			System.out.println(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
