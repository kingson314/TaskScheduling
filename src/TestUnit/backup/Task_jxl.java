package TestUnit.backup;


public class Task_jxl /*extends TaskAbstract*/ {

	public Task_jxl() {
//		this.setTaskStatus("等待执行");
//		this.setTaskFailMsg("");
	}

	// 执行
	public void fireTask() {
		GetExcel();
	}

	public void GetExcel() {
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		jxl.write.WritableWorkbook wwb = null;
//		try {
//			Bean bean = (Bean) Fun.getJsonBean(this.getParserJsonStr(),
//					Bean.class);
//			DbConnection dbconn = bean.getDbConDest();
//			if (dbconn == null) {
//				this.setTaskStatus("执行失败");
//				this.setTaskFailMsg("获取Excel数据库连接错误");
//				return;
//			}
//			con = JDBCManager.getConnection(dbconn);
//
//			String sql = bean.getSql();
//			String sqlrule = bean.getSqlRule();
//			if (sql.length() < 1) {
//				this.setTaskStatus("执行失败");
//				this.setTaskFailMsg("Sql为空");
//				return;
//			}
//			ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//			String[][] param = null;
//			if (Fun.getSqlRuleParam(sqlrule) != null) {
////				param = Fun.getSqlRuleParam(sqlrule);
//				for (int i = 0; i < param.length; i++) {
//					if (param[i][1].equalsIgnoreCase("varchar")) {
//						ps.setString(i + 1, param[i][0]);
//					}
//					if (param[i][1].equalsIgnoreCase("int")) {
//						ps.setInt(i + 1, Integer.valueOf(param[i][0]));
//					}
//					if (param[i][1].equalsIgnoreCase("float")) {
//						ps.setFloat(i + 1, Float.valueOf(param[i][0]));
//					}
//					if (param[i][1].equalsIgnoreCase("long")) {
//						ps.setLong(i + 1, Long.valueOf(param[i][0]));
//					}
//				}
//			}
//			rs = ps.executeQuery();
//			File f1 = new File(bean.getDocPath());
//			if (!f1.exists()) {
//				f1.mkdirs();
//			}
//			// 设置文档名称
//			File f = new File(bean.getDocPath() + bean.getDocName());
//			f.createNewFile();
//			FileOutputStream fileoutputStream = new FileOutputStream(f);
//			wwb = Workbook.createWorkbook(fileoutputStream);
//			// 
//			WritableSheet sheet = wwb.createSheet(bean.getDocName(), 0); // 设置表题格式
//			WritableFont titleFont = new WritableFont(WritableFont.TIMES, 12,
//					WritableFont.NO_BOLD);
//			WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
//			titleFormat.setAlignment(jxl.format.Alignment.CENTRE); // 把垂直对齐方式指定为居中
//			titleFormat
//					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//			// 设置格子格式 左对齐
//			WritableFont cellFont = new WritableFont(WritableFont.TIMES, 11,
//					WritableFont.NO_BOLD);
//			WritableCellFormat cellFormat_left = new WritableCellFormat(
//					cellFont);
//			cellFormat_left.setAlignment(jxl.format.Alignment.LEFT); // 把垂直对齐方式指定为居中
//			cellFormat_left
//					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 设置格子格式
//			// 右对齐
//			WritableFont cellFont1 = new WritableFont(WritableFont.TIMES, 11,
//					WritableFont.NO_BOLD);
//			WritableCellFormat cellFormat_right = new WritableCellFormat(
//					cellFont1);
//			cellFormat_right.setAlignment(jxl.format.Alignment.RIGHT); // 把垂直对齐方式指定为居中
//			cellFormat_right
//					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//
//			sheet.setRowView(0, 400);
//			String title = bean.getTitle();
//			String[] titleItem = title.split(";");
//			for (int i = 0; i < titleItem.length; i++) {
//				// 作用是指定第i+1列的宽度，比如: 将第一列的宽度设为20
//				sheet.setColumnView(i, 20);
//				jxl.write.Label ltitle = new jxl.write.Label(i, 0,
//						titleItem[i], titleFormat);
//				sheet.addCell(ltitle);
//			} // 作用是指定第i+1行的高度，比如: 将第一行的高度设为200
//			int row = 1;
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int len = rsmd.getColumnCount();
//			while (rs.next()) {
//				for (int col = 0; col < len; col++) {
//					jxl.write.Label cell = new jxl.write.Label(col, row, rs
//							.getString(col + 1), cellFormat_left);
//					sheet.addCell(cell);
//				}
//				sheet.setRowView(row, 300);
//				row += 1;
//			}
//			wwb.write();
//			wwb.close();
//			fileoutputStream.close();
//			this.setTaskStatus("执行成功");
//			String execResult = Parser.removeSlash(bean.getDocPath())
//					+ Parser.removeSlash(bean.getDocName()) + "\n";
//			this.setTaskFailMsg(execResult);
//		} catch (SQLException e) {
//			this.setTaskStatus("执行失败");
//			this.setTaskFailMsg("获取Excel数据集错误:", e);
//		} catch (IOException e) {
//			this.setTaskStatus("执行失败");
//			this.setTaskFailMsg("创建Excel文件错误:", e);
//		} catch (WriteException e) {
//			this.setTaskStatus("执行失败");
//			this.setTaskFailMsg("输入Excel数据集错误:", e);
//		} catch (Exception e) {
//			this.setTaskStatus("执行失败");
//			this.setTaskFailMsg("执行错误:", e);
//		} finally {
//			if (wwb != null) {
//				wwb = null;
//			}
//			Sql.close(rs, ps, con);
//		}
	}
}