package TestUnit.commonTest;


public class TestExcel { 
//	public static void GetExcel(Excel excel) {
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		jxl.write.WritableWorkbook wwb = null;
//		DbConnection dbconn = new DbConnection();
//		try {
//			con = DBConnectionManager.getAccessConnection();
//			String sql = "select * from dbconnection where dbName='"
//					+ excel.getDbName() + "'";
//			ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			rs = ps.executeQuery();
//			rs.last();
//
//			dbconn.setDbName(rs.getString("dbName"));
//			dbconn.setDbType(rs.getString("dbType"));
//			dbconn.setDbClassName(rs.getString("dbClassName"));
//			dbconn.setDbCon(rs.getString("dbCon"));
//			dbconn.setDbUser(rs.getString("dbUser"));
//			dbconn.setDbPassword(rs.getString("dbPassword"));
//
//		} catch (SQLException e) { 
//			e.printStackTrace();
//			StaticFun.WriteLog(StaticFun.LogSystem, "获取数据库连接配置错误:"
//					+ e.getMessage(), 0);
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				rs = null;
//			}
//			if (ps != null) {
//				try {
//					ps.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				ps = null;
//			}
//		}
//
//		try {
//			con = DBConnectionManager.getOracleCon(dbconn,"");
////			String sql_sqlrule = StaticFun.readFile(excel.getSql());
//			String sql = "";//StaticFun.getSql(sql_sqlrule);
//			String sqlrule ="";// StaticFun.getSqlrule(sql_sqlrule);
//            if(sql.length()<1){ 
//            	return;
//            }
//			ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			String[][] param = null;
//			if (StaticFun.getSqlRuleParam(sqlrule) != null) {
//				param = StaticFun.getSqlRuleParam(sqlrule);
//				for (int i = 0; i < param.length; i++) {
//					if (param[i][1].equalsIgnoreCase("varchar")) {
//						ps.setString(i + 1, Parser.parse(param[i][0]));
//					}
//					if (param[i][1].equalsIgnoreCase("int")) {
//						ps.setInt(i + 1, Integer.valueOf(Parser
//								.parse(param[i][0])));
//					}
//					if (param[i][1].equalsIgnoreCase("float")) {
//						ps.setFloat(i + 1, Float.valueOf(Parser
//								.parse(param[i][0])));
//					}
//					if (param[i][1].equalsIgnoreCase("long")) {
//						ps.setLong(i + 1, Long.valueOf(Parser
//								.parse(param[i][0])));
//					}
//				}
//			}
//			rs = ps.executeQuery();
//
//			File f = new File(Parser.parse(excel.getDocPath()
//					+ excel.getDocName()));
//			f.createNewFile();
//			wwb = Workbook.createWorkbook(new FileOutputStream(f));
//
//			// 设置文档名称
//			jxl.write.WritableSheet sheet = wwb.createSheet(Parser.parse(excel
//					.getDocName()), 0);
//
//			// 设置表题格式
//			WritableFont titleFont = new WritableFont(WritableFont.TIMES, 12,
//					WritableFont.NO_BOLD);
//			WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
//			titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
//			// 把垂直对齐方式指定为居中
//			titleFormat
//					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			// 设置格子格式 左对齐
//			WritableFont cellFont = new WritableFont(WritableFont.TIMES, 11,
//					WritableFont.NO_BOLD);
//			WritableCellFormat cellFormat_left = new WritableCellFormat(
//					cellFont);
//			cellFormat_left.setAlignment(jxl.format.Alignment.LEFT);
//			// 把垂直对齐方式指定为居中
//			cellFormat_left
//					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			// 设置格子格式 右对齐
//			WritableFont cellFont1 = new WritableFont(WritableFont.TIMES, 11,
//					WritableFont.NO_BOLD);
//			WritableCellFormat cellFormat_right = new WritableCellFormat(
//					cellFont1);
//			cellFormat_right.setAlignment(jxl.format.Alignment.RIGHT);
//			// 把垂直对齐方式指定为居中
//			cellFormat_right
//					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//
//			sheet.setRowView(0, 400);
//			String title = excel.getTitle();
//			String[] titleItem = title.split(";");
//			for (int i = 0; i < titleItem.length; i++) {
//
//				// 作用是指定第i+1列的宽度，比如: 将第一列的宽度设为20
//				sheet.setColumnView(i, 20);
//				jxl.write.Label ltitle = new jxl.write.Label(i, 0,
//						titleItem[i], titleFormat);
//				sheet.addCell(ltitle);
//			}
//			// 作用是指定第i+1行的高度，比如: 将第一行的高度设为200
//			WritableCellFormat format;
//			int row = 1;
//
//			while (rs.next()) {
//
//				for (int col = 0; col < titleItem.length; col++) {
//					format = cellFormat_left;
//					jxl.write.Label cell = new jxl.write.Label(col, row, rs
//							.getString(col + 1), format);
//					sheet.addCell(cell);
//				}
//				sheet.setRowView(row, 300);
//				row += 1;
//			}
//			wwb.write();
//			wwb.close();
//		 
//		} catch (SQLException e) {
//			e.printStackTrace(); 
//			StaticFun.WriteLog(StaticFun.LogSystem, "获取Excel数据集错误:"
//					+ e.getMessage(), 0);
//		} catch (IOException e) {
//			e.printStackTrace(); 
//			StaticFun.WriteLog(StaticFun.LogSystem, "创建Excel文件错误:"
//					+ e.getMessage(), 0);
//		} catch (WriteException e) {
//			e.printStackTrace(); 
//			StaticFun.WriteLog(StaticFun.LogSystem, "输入Excel数据集错误:"
//					+ e.getMessage(), 0);
//		} finally {
//			if (wwb != null) {
//				wwb = null;
//			}
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				rs = null;
//			}
//			if (ps != null) {
//				try {
//					ps.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				ps = null;
//			}
//			/*if (con != null) {
//				try {
//					con.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				con = null;
//			}*/
//		}
//
//	} 
//	
//	public static void main(String[] agrs){
//		Excel excel = new Excel();
//		excel.setDocPath("D:/t2/");
//		excel.setDocName("测试Float类型参数.xls");
//		excel.setTitle("s1;s2;s8;s11;d");
//		excel.setDbName("otc");
//		excel.setSql("11");
//		excel.setSqlRule("");
//		GetExcel(excel);
//
//	}
}
