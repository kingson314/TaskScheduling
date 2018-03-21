package TestUnit.commonTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;


import common.util.jdbc.UtilJDBCManager;

public class Ordwth {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		createTable();
		createData();
	}

	public static void createData() throws SQLException {
		DbConnection dbconn = DbConnectionDao.getInstance().getMapDbConn("VS-DEV-09");
		Connection con = UtilJDBCManager.getConnection(dbconn);

		try {
			Statement sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = sm.executeQuery("select distinct acc from oiwhis..ashare_ordwth");
			rs.beforeFirst();
			int i = 1;
			while (rs.next()) {
				// sm.addBatch("delete from oiw" + String.valueOf(i)
				// + "..ashare_ordwth");
				sm.addBatch("insert into	 oiw" + String.valueOf(i) + "..ashare_ordwth select * from oiwhis..ashare_ordwth where acc='" + rs.getString(1) + "'");

				i += 1;
			}
			sm.executeBatch();

			rs = sm.executeQuery("select distinct acc from oiwhis..ashare_ordwth2");
			rs.beforeFirst();
			i = 1;
			while (rs.next()) {
				// sm.addBatch("delete from oiw2" + String.valueOf(i)
				// + "..ashare_ordwth");
				sm.addBatch("insert into	 oiw" + String.valueOf(i) + "..ashare_ordwth2 select *  from oiwhis..ashare_ordwth2 where acc='" + rs.getString(1) + "'");

				i += 1;
			}
			sm.executeBatch();
			rs.close();
			sm.close();
			System.out.println("success1");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createTable() throws SQLException {
		DbConnection dbconn = DbConnectionDao.getInstance().getMapDbConn("VS-DEV-09");
		Connection con = UtilJDBCManager.getConnection(dbconn);
		try {
			Statement sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for (int i = 1; i <= 90; i++) {
				String oiw = "oiw" + String.valueOf(i);
				sm.addBatch("USE " + oiw);
				sm.addBatch("drop table [dbo].[ashare_ordwth]");
				sm.addBatch("  CREATE TABLE [dbo].[ashare_ordwth](" + "   	[rec_num    ] [int] NULL," + "    	[date    ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[time    ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL," + "     	[reff      ] [nvarchar](10) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[acc       ] [nvarchar](10) COLLATE Chinese_PRC_CI_AS NULL," + "     	[stock ] [nvarchar](6) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[bs  ] [nvarchar](1) COLLATE Chinese_PRC_CI_AS NULL," + "     	[price   ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[qty     ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL," + "     	[status] [nvarchar](1) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[owflag] [nvarchar](3) COLLATE Chinese_PRC_CI_AS NULL," + "     	[ordrec  ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[firmid] [nvarchar](5) COLLATE Chinese_PRC_CI_AS NULL," + "     	[checkord                          ] [nvarchar](10) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "     	[branchid ] [nvarchar](50) COLLATE Chinese_PRC_CI_AS NULL)");
				sm.addBatch("drop table [dbo].[ashare_ordwth2]");

				sm.addBatch(" CREATE TABLE [dbo].[ashare_ordwth2](" + "[rec_num    ] [int] NULL," + "[date    ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[time    ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL," + "[reff      ] [nvarchar](10) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[acc       ] [nvarchar](10) COLLATE Chinese_PRC_CI_AS NULL," + "[stock ] [nvarchar](6) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[bs  ] [nvarchar](1) COLLATE Chinese_PRC_CI_AS NULL," + "[price   ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[qty     ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL," + "[status] [nvarchar](1) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[qty2    ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL," + "[remark ] [nvarchar](30) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[status1] [nvarchar](1) COLLATE Chinese_PRC_CI_AS NULL," + "[teordernum] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[owflag] [nvarchar](3) COLLATE Chinese_PRC_CI_AS NULL," + "[ordrec  ] [nvarchar](8) COLLATE Chinese_PRC_CI_AS NULL,"
						+ "[firmid] [nvarchar](5) COLLATE Chinese_PRC_CI_AS NULL," + "[checkord                          ] [varbinary](50) NULL,"
						+ "[branchid ] [nvarchar](50) COLLATE Chinese_PRC_CI_AS NULL)");

			}

			sm.executeBatch();
			sm.close();
			System.out.println("success");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
