package TestUnit.commonTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {
	public static Dao dao = null;
	private Connection con;

	public static Dao getInstance() {
		if (dao == null)
			dao = new Dao();
		return dao;
	}

	public Dao() {
		// this.setCon(ODBCManager.getAccessConnection());
	}

	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			rs = null;
		}
	}

	public void close(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			ps = null;
		}
	}

	public void close(Statement sm) {
		if (sm != null) {
			try {
				sm.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			sm = null;
		}
	}

	public void close(Connection con) {
		// Connection先不关, 降低开销.
		// 考虑使用连接池
		// 真正由DBConnectionManager.closeConnection()函数关闭
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}
