package TestUnit.commonTest;

import java.sql.ResultSet;

//import jxl.Workbook;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableFont;

public class ExcelTest {

	/*
	 * public void GetExcel(Excel excel) { Connection con = null;
	 * PreparedStatement ps = null; ResultSet rs = null; DbConnection dbconn =
	 * new DbConnection(); try { con =
	 * DBConnectionManager.getAccessConnection(); String sql = "select * from
	 * dbconnection where dbName='" + excel.getDbName() + "'"; ps =
	 * con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
	 * ResultSet.CONCUR_READ_ONLY); rs = ps.executeQuery(); rs.last();
	 * 
	 * dbconn.setDbName(rs.getString("dbName"));
	 * dbconn.setDbType(rs.getString("dbType"));
	 * dbconn.setDbClassName(rs.getString("dbClassName"));
	 * dbconn.setDbCon(rs.getString("dbCon"));
	 * dbconn.setDbUser(rs.getString("dbUser"));
	 * dbconn.setDbPassword(rs.getString("dbPassword")); } catch (SQLException
	 * e) { this.setTaskStatus("执行失败"); this.setTaskFailMsg("获取Exce数据库连接错误: " +
	 * e.getMessage()); e.printStackTrace();
	 * StaticFun.WriteLog(StaticFun.LogSystem, "获取数据库连接配置错误:" + e.getMessage(),
	 * 0); } finally { if (rs != null) { try { rs.close(); } catch (SQLException
	 * e) { e.printStackTrace(); } rs = null; } if (ps != null) { try {
	 * ps.close(); } catch (SQLException e) { e.printStackTrace(); } ps = null; } }
	 * 
	 * try { con = DBConnectionManager.getOracleConnection(dbconn); String
	 * sql_sqlrule = StaticFun.readFile(excel.getSql()); String sql =
	 * StaticFun.getSql(sql_sqlrule); String sqlrule =
	 * StaticFun.getSqlrule(sql_sqlrule); if (sql.length() < 1) {
	 * this.setTaskStatus("执行失败"); this.setTaskFailMsg("Sql为空"); return; } ps =
	 * con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
	 * ResultSet.CONCUR_READ_ONLY); String[][] param = null; if
	 * (StaticFun.getSqlRuleParam(sqlrule) != null) { param =
	 * StaticFun.getSqlRuleParam(sqlrule); for (int i = 0; i < param.length;
	 * i++) { if (param[i][1].equalsIgnoreCase("varchar")) { ps.setString(i + 1,
	 * Parser.parse(param[i][0])); } if (param[i][1].equalsIgnoreCase("int")) {
	 * ps.setInt(i + 1, Integer.valueOf(Parser .parse(param[i][0]))); } if
	 * (param[i][1].equalsIgnoreCase("float")) { ps.setFloat(i + 1,
	 * Float.valueOf(Parser .parse(param[i][0]))); } if
	 * (param[i][1].equalsIgnoreCase("long")) { ps.setLong(i + 1,
	 * Long.valueOf(Parser .parse(param[i][0]))); } } } rs = ps.executeQuery();
	 * 
	 * Map<String, String> fieldMap = new HashMap<String, String>(); String
	 * title = excel.getTitle(); String[] titleItem = title.split(";");
	 * System.out.println(title); for (int i = 0; i < titleItem.length; i++) {
	 * fieldMap.put(titleItem[i], String.valueOf(i)); } List<Map<String,
	 * Object>> dataList = new ArrayList<Map<String, Object>>(); while
	 * (rs.next()) { { Map<String, Object> map = new HashMap<String,
	 * Object>(); for (int i = 0; i < titleItem.length; i++) {
	 * map.put(titleItem[i], rs.getString(i + 1)); } dataList.add(map); } }
	 * HSSFWorkbook hbook = produceExcel(Parser.parse(this.getDocName()),
	 * dataList, fieldMap); // 由于文件经常在锁定状态，先创建到临时文件夹中 hbook.write(new
	 * FileOutputStream(Constants.TEMP_DIR+ Parser.parse(excel.getDocName())));
	 * copyFile(Constants.TEMP_DIR + Parser.parse(excel.getDocName()),
	 * Parser.parse(excel .getDocPath() + excel.getDocName()));
	 * 
	 * 
	 * File file = new File(Constants.TEMP_DIR +
	 * Parser.parse(excel.getDocName())); delf(file); } catch (IOException e) {
	 * e.printStackTrace(); this.setTaskStatus("执行失败");
	 * this.setTaskFailMsg("创建Excel文件错误:" + e.getMessage());
	 * StaticFun.WriteLog(StaticFun.LogSystem, "创建Excel文件错误:" + e.getMessage(),
	 * 0); } catch (SQLException e) { e.printStackTrace();
	 * this.setTaskStatus("执行失败"); this.setTaskFailMsg("获取Excel数据集错误:" +
	 * e.getMessage()); StaticFun.WriteLog(StaticFun.LogSystem, "获取Excel数据集错误:" +
	 * e.getMessage(), 0); } finally { if (rs != null) { try { rs.close(); }
	 * catch (SQLException e) { e.printStackTrace(); } rs = null; } if (ps !=
	 * null) { try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	 * ps = null; } if (con != null) { try { con.close(); } catch (SQLException
	 * e) { e.printStackTrace(); } con = null; } } }
	 */

	public static void writeExcel(ResultSet rs, String fileName)
			throws Exception {
//		SimpleDateFormat fromattime = new SimpleDateFormat(// 日期格式
//				"yyyy-MM-dd");
//		Calendar now_HHMM = Calendar.getInstance();
//		String nowtime = fromattime.format(now_HHMM.getTime()); // 创建xls文档
//		File f = new File(fileName);
//		f.createNewFile();
//		jxl.write.WritableWorkbook wwb = Workbook
//				.createWorkbook(new FileOutputStream(f)); // 设置文档名称
//		String title = "业绩亏损下滑股票" + nowtime;
//		jxl.write.WritableSheet sheet = wwb.createSheet(title, 0);
//		// 设置表题格式
//		WritableFont titleFont = new WritableFont(WritableFont.TIMES, 12,
//				WritableFont.NO_BOLD);
//		WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
//		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
//		// 把垂直对齐方式指定为居中
//		titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//		// 设置格子格式
//		WritableFont cellFont = new WritableFont(WritableFont.TIMES, 11,
//				WritableFont.NO_BOLD);
//		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
//		cellFormat.setAlignment(jxl.format.Alignment.LEFT);
//		// 把垂直对齐方式指定为居中
//		cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//		// 设置格子格式1
//		WritableFont cellFont1 = new WritableFont(WritableFont.TIMES, 11,
//				WritableFont.NO_BOLD);
//		WritableCellFormat cellFormat1 = new WritableCellFormat(cellFont1);
//		cellFormat1.setAlignment(jxl.format.Alignment.RIGHT);
//		// 把垂直对齐方式指定为居中
//		cellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//
//		// 作用是指定第i+1列的宽度，比如: 将第一列的宽度设为30
//		sheet.setColumnView(0, 20);
//		sheet.setColumnView(1, 20);
//		sheet.setColumnView(2, 20);
//		sheet.setColumnView(3, 20);
//		sheet.setColumnView(4, 20);
//		// 作用是指定第i+1行的高度，比如: 将第一行的高度设为200
//		sheet.setRowView(0, 400);
//
//		jxl.write.Label ltitle = new jxl.write.Label(0, 0, "业绩亏损下滑股票",
//				titleFormat);
//		sheet.addCell(ltitle);
//		ltitle = new jxl.write.Label(1, 0, "交易市场", titleFormat);
//		sheet.addCell(ltitle);
//		ltitle = new jxl.write.Label(2, 0, "证券代码", titleFormat);
//		sheet.addCell(ltitle);
//		ltitle = new jxl.write.Label(3, 0, "0", titleFormat);
//		sheet.addCell(ltitle);
//		ltitle = new jxl.write.Label(4, 0, "0", titleFormat);
//		sheet.addCell(ltitle);
//		@SuppressWarnings("unused")
//		WritableCellFormat format;
//		@SuppressWarnings("unused")
//		int row = 1;
//
//	/*	while (rs.next()) {
//			for (int col = 0; col < 5; col++) {
//				if (col == 4 || col == 3)
//					format = cellFormat1;
//				else
//					format = cellFormat;
//				ltitle = new jxl.write.Label(col, row, rs.getString(col + 1),
//						format);
//				sheet.addCell(ltitle);
//			}
//			sheet.setRowView(row, 300);
//			row += 1;
//		}*/
//		wwb.write();
//		wwb.close();
	}

	public static void main(String[] agrs) throws Exception {
	}
}