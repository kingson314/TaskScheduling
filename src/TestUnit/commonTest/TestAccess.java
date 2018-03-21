package TestUnit.commonTest;

import java.sql.SQLException;

public class TestAccess {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		// TaskExcel task =new TaskExcel();
		TestAccess test = new TestAccess();
		// test.addTask(task);
		System.out.println(test.getSqlrule());
	}

	// public void addTask(TaskExcel task) throws SQLException {
	// Connection con = null;
	// PreparedStatement ps = null;
	// // ResultSet rs = null;
	// try {
	// con = DBConnectionManager.getAccessConnection();
	// ps=con.prepareStatement("insert into
	// taskExcel(taskID,docPath,docName,title,"
	// + "dbName,sql,sqlrule" + ") values (?,?,?,?,?,?,?)");
	// ps.setString(1, String.valueOf(task.getTaskId()));
	// //System.out.println(sqlrule);
	// ps.addBatch();
	// ps.executeBatch();
	// con.commit();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// StaticFun.WriteLog(StaticFun.LogSystem, "新增任务错误:" + e.getMessage(),
	// 0);
	// } finally {
	// ps.clearBatch();
	// ps.close();
	// con.close();
	// }
	// }

	public String getSqlrule() {
		// Connection con = null;
		// Statement sm = null;
		// ResultSet rs = null;
		String sqlrule = null;
		// try {
		// con = ODBCManager.getAccessConnection();
		// sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
		// ResultSet.CONCUR_READ_ONLY);
		// rs = sm.executeQuery("select sqlrule from taskExcel ");
		// rs.last();
		//			  
		// //UtilTools ut = new UtilTools();
		// //sqlrule=ut.accessdbMemoToString(rs.getBinaryStream("sqlrule")).toString();
		// sqlrule=rs.getString("sqlrule");
		// /*try {
		// sqlrule=new String(sqlrule.getBytes("ISO-8859-1"),"GB2312");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }*/
		// }catch(SQLException e){
		// }
		return sqlrule;
	}

}
