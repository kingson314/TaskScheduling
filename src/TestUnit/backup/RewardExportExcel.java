package TestUnit.backup;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

public class RewardExportExcel {

	private static int COLUMNS = 6; // 定义列数

	// 定义样式格式
	static Map<String, HSSFCellStyle> styles = new HashMap<String, HSSFCellStyle>();

	public static HSSFWorkbook produceExcel(
			List<Map<String, String>> dataList1,
			List<Map<String, String>> dataList2,
			List<Map<String, String>> dataList3, String beginDate,
			String endDate) {

		HSSFWorkbook hb = new HSSFWorkbook();
		@SuppressWarnings("unused")
		Workbook c = new Workbook();
		HSSFCellStyle cellStyle = hb.createCellStyle();
		HSSFSheet sheet = hb.createSheet("sheet1");
		// 设置sheet的列宽
		for (int i = 0; i < COLUMNS; i++) {
			if (i == 1 || i == 2) {
				sheet.setColumnWidth((short) i, (short) 8250);
			} else {
				sheet.setColumnWidth((short) i, (short) 5500);
			}
		}
		// 左右居中
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 上下居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 字体格式
		HSSFFont titlef = hb.createFont();
		titlef.setFontName("宋体");
		// 加粗
		titlef.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titlef.setColor(HSSFColor.GREEN.index);
		titlef.setFontHeight((short) 360);
		// 设置样式的字体
		cellStyle.setFont(titlef);
		styles.put("titleStyle", cellStyle);
		sheet
				.addMergedRegion(new Region(0, (short) 0, 0,
						(short) (COLUMNS - 1)));

		// 第二行 某公司
		HSSFCellStyle cellStyle2 = hb.createCellStyle();
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont twof = hb.createFont();
		twof.setFontName("宋体");
		twof.setColor(HSSFColor.GREEN.index);
		twof.setFontHeight((short) 240);
		// 设置样式的字体
		cellStyle2.setFont(twof);
		styles.put("TwoRowStyle", cellStyle2);
		sheet
				.addMergedRegion(new Region(1, (short) 0, 1,
						(short) (COLUMNS - 1)));

		// 第三行 日期时间
		HSSFCellStyle cellStyle3 = hb.createCellStyle();
		HSSFFont thirdf = hb.createFont();
		thirdf.setFontName("宋体");
		thirdf.setFontHeight((short) 220);
		// 设置样式的字体
		cellStyle3.setFont(thirdf);
		styles.put("ThirdRowStyle", cellStyle3);
		sheet
				.addMergedRegion(new Region(2, (short) 0, 2,
						(short) (COLUMNS - 1)));

		// 第一行标题
		HSSFRichTextString titleValue = new HSSFRichTextString("业绩报酬计算过程中间报表");
		HSSFRow titleRow = sheet.createRow(0);
		HSSFCell titleCell = titleRow.createCell((short) 0);
		titleCell.setCellStyle(styles.get("titleStyle"));
		titleCell.setCellValue(titleValue);

		// 第二行公司
		HSSFRichTextString companyValue = new HSSFRichTextString(
				"易方达基金管理有限公司__" + dataList1.get(0).get("STBYNAME") + "__专用表");
		HSSFRow companyRow = sheet.createRow(1);
		HSSFCell companyCell = companyRow.createCell((short) 0);
		companyCell.setCellStyle(styles.get("TwoRowStyle"));
		companyCell.setCellValue(companyValue);

		// 第三行日期
		HSSFRichTextString dateValue = new HSSFRichTextString("");
		HSSFRow dateRow = sheet.createRow(2);
		HSSFCell dateCell = dateRow.createCell((short) 0);
		dateCell.setCellStyle(styles.get("ThirdRowStyle"));
		dateCell.setCellValue(dateValue);

		Region region1 = new Region(0, (short) 0, 0, (short) (COLUMNS - 1));
		sheet.addMergedRegion(region1);

		// 第一个模块 基本参数
		HSSFCellStyle cellStyle4 = hb.createCellStyle();
		// 设置边框
		borderSetting(cellStyle4, 1, 0, 1, 1);
		HSSFFont fourf = hb.createFont();
		fourf.setFontName("宋体");
		fourf.setFontHeight((short) 220);
		// 设置样式的字体
		cellStyle4.setFont(thirdf);
		styles.put("FourRowStyle", cellStyle4);
		Region region4 = new Region(3, (short) 0, 3, (short) (COLUMNS - 1));
		for (int i = region4.getRowFrom(); i <= region4.getRowTo(); i++) {
			HSSFRow row4 = sheet.createRow(i);
			HSSFRichTextString rtsValue4 = new HSSFRichTextString("一、基本参数");
			if (region4.getColumnFrom() != region4.getArea()) {
				for (int j = region4.getColumnFrom(); j <= region4
						.getColumnTo(); j++) {
					HSSFCell cell = row4.createCell((short) j);
					if (j == 0) {
						cell.setCellValue(rtsValue4);
					}
					cell.setCellStyle(cellStyle4);
				}
			}
		}
		sheet.addMergedRegion(region4);

		// 单元格基本格式（除每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyCellStyle = hb.createCellStyle();
		borderSetting(everyCellStyle, 0, 0, 0, 0);
		HSSFFont f = hb.createFont();
		f.setFontName("宋体");
		f.setFontHeight((short) 220);
		// 设置样式的字体
		everyCellStyle.setFont(f);
		styles.put("everyCellStyle", everyCellStyle);

		// 单元格基本格式（序号 除每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyCellStyleXu = hb.createCellStyle();
		everyCellStyleXu.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		everyCellStyleXu.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		everyCellStyleXu.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		borderSetting(everyCellStyleXu, 0, 0, 0, 0);
		HSSFFont f11 = hb.createFont();
		f11.setFontName("宋体");
		f11.setFontHeight((short) 220);
		// 设置样式的字体
		everyCellStyleXu.setFont(f11);
		styles.put("everyCellStyleXu", everyCellStyleXu);

		// 单元格基本格式（千分格式 除每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyCellStyleQ = hb.createCellStyle();
		everyCellStyleQ.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("#,##0.00"));
		borderSetting(everyCellStyleQ, 0, 0, 0, 0);
		HSSFFont f13 = hb.createFont();
		f13.setFontName("宋体");
		f13.setFontHeight((short) 220);
		// 设置样式的字体
		everyCellStyleQ.setFont(f13);
		styles.put("everyCellStyleQ", everyCellStyleQ);

		// 单元格基本格式（千分格式 每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyleQ2 = hb.createCellStyle();
		borderSetting(everyFinalCellStyleQ2, 0, 0, 0, 1);
		everyFinalCellStyleQ2.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("#,##0.00"));
		HSSFFont f23 = hb.createFont();
		f23.setFontName("宋体");
		f23.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyleQ2.setFont(f23);
		styles.put("everyFinalCellStyleQ2", everyFinalCellStyleQ2);

		// 单元格基本格式（千分格式 每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyleQ21 = hb.createCellStyle();
		borderSetting(everyFinalCellStyleQ21, 0, 0, 0, 1);
		HSSFDataFormat format = hb.createDataFormat();
		everyFinalCellStyleQ21
				.setDataFormat(format.getFormat("#,##0.00000000"));
		HSSFFont f26 = hb.createFont();
		f26.setFontName("宋体");
		f26.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyleQ21.setFont(f26);
		styles.put("everyFinalCellStyleQ21", everyFinalCellStyleQ21);

		// 单元格基本格式（每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle = hb.createCellStyle();
		borderSetting(everyFinalCellStyle, 0, 0, 0, 1);
		HSSFFont f2 = hb.createFont();
		f2.setFontName("宋体");
		f2.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle.setFont(f2);
		styles.put("everyFinalCellStyle", everyFinalCellStyle);

		// 单元格基本格式2（除每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyCellStyle2 = hb.createCellStyle();
		everyCellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		everyCellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		everyCellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		borderSetting(everyCellStyle2, 0, 0, 0, 0);
		HSSFFont f12 = hb.createFont();
		f12.setFontName("宋体");
		f12.setFontHeight((short) 220);
		// 设置样式的字体
		everyCellStyle2.setFont(f12);
		styles.put("everyCellStyle2", everyCellStyle2);

		// 单元格基本格式2（每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle2 = hb.createCellStyle();
		everyFinalCellStyle2
				.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		everyFinalCellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		everyFinalCellStyle2
				.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		borderSetting(everyFinalCellStyle2, 0, 0, 0, 1);
		HSSFFont f22 = hb.createFont();
		f22.setFontName("宋体");
		f22.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle2.setFont(f22);
		styles.put("everyFinalCellStyle2", everyFinalCellStyle2);

		// 单元格基本格式2（每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle4 = hb.createCellStyle();
		everyFinalCellStyle4
				.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		everyFinalCellStyle4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		everyFinalCellStyle4
				.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		borderSetting(everyFinalCellStyle4, 0, 0, 0, 1);
		HSSFFont f32 = hb.createFont();
		f32.setFontName("宋体");
		f32.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle4.setFont(f32);
		styles.put("everyFinalCellStyle4", everyFinalCellStyle4);

		// 单元格基本格式2（每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle51 = hb.createCellStyle();
		everyFinalCellStyle51.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("0"));
		everyFinalCellStyle51.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		everyFinalCellStyle51
				.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		borderSetting(everyFinalCellStyle51, 0, 0, 0, 0);
		HSSFFont f51 = hb.createFont();
		f51.setFontName("宋体");
		f51.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle51.setFont(f51);
		styles.put("everyFinalCellStyle51", everyFinalCellStyle51);

		// 单元格基本格式2（每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle52 = hb.createCellStyle();
		everyFinalCellStyle52.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("0"));
		everyFinalCellStyle52.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		everyFinalCellStyle52
				.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		borderSetting(everyFinalCellStyle52, 0, 0, 0, 1);
		HSSFFont f52 = hb.createFont();
		f52.setFontName("宋体");
		f52.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle52.setFont(f52);
		styles.put("everyFinalCellStyle52", everyFinalCellStyle52);

		// 单元格基本格式3（百分号格式 除每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle3 = hb.createCellStyle();
		everyFinalCellStyle3.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("0.00%"));
		borderSetting(everyFinalCellStyle3, 0, 0, 0, 0);
		HSSFFont f24 = hb.createFont();
		f24.setFontName("宋体");
		f24.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle3.setFont(f24);
		styles.put("everyFinalCellStyle3", everyFinalCellStyle3);

		// 单元格基本格式3（保留6位小数的百分号格式 除每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle31 = hb.createCellStyle();

		everyFinalCellStyle31.setDataFormat(format.getFormat("0.000000%"));
		borderSetting(everyFinalCellStyle31, 0, 0, 0, 0);
		HSSFFont f25 = hb.createFont();
		f25.setFontName("宋体");
		f25.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle31.setFont(f25);
		styles.put("everyFinalCellStyle31", everyFinalCellStyle31);

		// 单元格基本格式3（百分号格式 每行的最后一个格式，因为外边框是双线）
		HSSFCellStyle everyFinalCellStyle32 = hb.createCellStyle();
		everyFinalCellStyle32.setDataFormat(HSSFDataFormat
				.getBuiltinFormat("0.00%"));
		borderSetting(everyFinalCellStyle32, 0, 0, 0, 1);
		HSSFFont f312 = hb.createFont();
		f312.setFontName("宋体");
		f312.setFontHeight((short) 220);
		// 设置样式的字体
		everyFinalCellStyle32.setFont(f312);
		styles.put("everyFinalCellStyle32", everyFinalCellStyle32);

		// 基本管理费率（%）
		HSSFRichTextString rtsValue5 = new HSSFRichTextString("基本管理费率");
		HSSFRow row5 = sheet.createRow(4);
		batchInsertBai(row5, rtsValue5, Double.parseDouble(dataList1.get(0)
				.get("MANAGEFEE")));
		System.out.println("MANAGEFEE" + dataList1.get(0).get("MANAGEFEE"));

		// 计提方式:
		String jtfs = null;
		HSSFRichTextString rtsValue6 = new HSSFRichTextString("计提方式");
		if (dataList1.get(0).get("SMETHOD").equals("0")) {
			jtfs = "An - Bn";
		} else if (dataList1.get(0).get("SMETHOD").equals("1")) {
			jtfs = "孰高模式";
		} else if (dataList1.get(0).get("SMETHOD").equals("2")) {
			jtfs = "累进模式";
		} else if (dataList1.get(0).get("SMETHOD").equals("3")) {
			jtfs = "年化实际收益率";
		}
		HSSFRow row6 = sheet.createRow(5);
		batchInsert(row6, rtsValue6, jtfs);

		// 前六行是固定的
		int rowNum = 6;
		// 业绩报酬基准 和 业绩报酬比例 可能是多行
		HSSFRichTextString rewardBLValue = new HSSFRichTextString("业绩报酬比例");
		for (int i = 0; i < dataList1.size(); i++) {
			HSSFRow rewardRow = sheet.createRow(rowNum + i);
			HSSFRichTextString rewardJZValue = null;
			if (dataList1.size() == 1) {
				rewardJZValue = new HSSFRichTextString("业绩报酬基准");
			} else {
				rewardJZValue = new HSSFRichTextString("业绩报酬基准" + (i + 1));
			}
			batchInsertBai(rewardRow, rewardJZValue, dataList1.get(i).get(
					"GROUPNAME"), rewardBLValue, Double.parseDouble(dataList1
					.get(i).get("RATIO")));
		}
		rowNum += dataList1.size();

		// 从rowNum行开始是 管理费最高限额比例(%)
		HSSFRichTextString rewardGLBLValue = new HSSFRichTextString("管理费最高限额比例");
		HSSFRow glRow = sheet.createRow(rowNum);
		batchInsertBai(glRow, rewardGLBLValue, Double.parseDouble(dataList1
				.get(0).get("LIMITSCALE")));
		rowNum += 1;

		// 考核期初
		HSSFRichTextString rewardBdateValue = new HSSFRichTextString("考核期初");
		HSSFRow beginDateRow = sheet.createRow(rowNum);
		batchInsert(beginDateRow, rewardBdateValue, beginDate);
		rowNum += 1;

		// 考核期末
		HSSFRichTextString rewardEdateValue = new HSSFRichTextString("考核期末");
		HSSFRow endDateRow = sheet.createRow(rowNum);
		batchInsert(endDateRow, rewardEdateValue, endDate);
		rowNum += 1;

		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum, "");

		// 第二个模块 资金流情况
		HSSFRichTextString rewardZJValue = new HSSFRichTextString("二、资金流情况");
		HSSFRow zjRow = sheet.createRow(rowNum);
		batchInsert(zjRow, rewardZJValue);
		sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
				(short) (COLUMNS - 1)));
		rowNum += 1;

		// grid的列名
		HSSFRichTextString rewardZJcolumnValue1 = new HSSFRichTextString("序号");
		HSSFRichTextString rewardZJcolumnValue2 = new HSSFRichTextString("基准名称");
		HSSFRichTextString rewardZJcolumnValue3 = new HSSFRichTextString(
				"资金流(追加为正，提取为负)");
		HSSFRichTextString rewardZJcolumnValue4 = new HSSFRichTextString(
				"追加提取日期");
		HSSFRichTextString rewardZJcolumnValue5;
		HSSFRichTextString rewardZJcolumnValue6;
		if (dataList1.get(0).get("SMETHOD").equals("3")) {
			rewardZJcolumnValue5 = new HSSFRichTextString("资金运作天数");
			rewardZJcolumnValue6 = new HSSFRichTextString("资金流*运作天数");
		} else {
			rewardZJcolumnValue5 = new HSSFRichTextString("基准利率");
			rewardZJcolumnValue6 = new HSSFRichTextString("每笔现金流的Bn值");
		}
		HSSFRow rewardZJcolumnRow = sheet.createRow(rowNum);
		batchInsertC(rewardZJcolumnRow, rewardZJcolumnValue1,
				rewardZJcolumnValue2, rewardZJcolumnValue3,
				rewardZJcolumnValue4, rewardZJcolumnValue5,
				rewardZJcolumnValue6);
		rowNum += 1;

		// List 列表值
		@SuppressWarnings("unused")
		DecimalFormat df = new DecimalFormat("#,##0.00#");
		double rewardZJLValue = 0;
		double rewardZJLdayValue = 0;
		double rewardBnValue = 0;
		String rnincome;
		Boolean flag = true;
		String oldGroupName = dataList2.get(0).get("GROUPNAME");
		int count = rowNum;
		for (int i = 0; i < dataList2.size(); i++) {
			@SuppressWarnings("unused")
			HSSFRichTextString rewardZJListValue1 = new HSSFRichTextString(
					dataList2.get(i).get("NORDER"));
			HSSFRichTextString rewardZJListValue2 = new HSSFRichTextString(
					dataList2.get(i).get("GROUPNAME"));
			HSSFRichTextString rewardZJListValue3 = new HSSFRichTextString(
					dataList2.get(i).get("NMONEY"));
			// rewardZJLValue+=Double.parseDouble(dataList2.get(i).get("NMONEY"));
			HSSFRichTextString rewardZJListValue4 = new HSSFRichTextString(
					dataList2.get(i).get("TDATE"));
			HSSFRichTextString rewardZJListValue5;
			HSSFRichTextString rewardZJListValue6;
			if (dataList1.get(0).get("SMETHOD").equals("3")) {
				rewardZJListValue5 = new HSSFRichTextString(dataList2.get(i)
						.get("DAYS"));
				rewardZJListValue6 = new HSSFRichTextString(dataList2.get(i)
						.get("PRONMONEY"));
				rnincome = dataList2.get(i).get("DAYS");
			} else {
				rewardZJListValue5 = new HSSFRichTextString(dataList2.get(i)
						.get("RT"));
				rewardZJListValue6 = new HSSFRichTextString(dataList2.get(i)
						.get("BN"));
				rnincome = Double.parseDouble(rewardZJListValue5.toString())
						+ "";
			}

			HSSFRow rewardZJListRow = sheet.createRow(count + i);

			if (dataList2.get(i).get("GROUPNAME").equals(oldGroupName)) {
				if (flag) {
					batchInsertS(rewardZJListRow,
							dataList1,
							Double.parseDouble(dataList2.get(i).get("NORDER")),
							// rewardZJListValue1.toString(),
							rewardZJListValue2,
							Double.parseDouble(rewardZJListValue3.toString()),
							rewardZJListValue4, Double.parseDouble(rnincome),
							Double.parseDouble(rewardZJListValue6.toString()));
					rewardZJLValue += Double.parseDouble(dataList2.get(i).get(
							"NMONEY"));
					rewardZJLdayValue += Double.parseDouble(dataList2.get(i)
							.get("PRONMONEY"));
					rewardBnValue += Double.parseDouble(dataList2.get(i).get(
							"BN"));
					flag = false;
				} else {
					batchInsertS(rewardZJListRow, dataList1, Double
							.parseDouble(dataList2.get(i).get("NORDER")), "",
							Double.parseDouble(rewardZJListValue3.toString()),
							rewardZJListValue4, Double.parseDouble(rnincome),
							Double.parseDouble(rewardZJListValue6.toString()));
					rewardZJLValue += Double.parseDouble(dataList2.get(i).get(
							"NMONEY"));
					rewardZJLdayValue += Double.parseDouble(dataList2.get(i)
							.get("PRONMONEY"));
					rewardBnValue += Double.parseDouble(dataList2.get(i).get(
							"BN"));
				}

			} else {
				// 合计
				HSSFRichTextString totalValue1 = new HSSFRichTextString("总计");
				HSSFRichTextString totalValue2 = new HSSFRichTextString("");
				HSSFRichTextString totalValue3 = new HSSFRichTextString(
						rewardZJLValue + "");
				HSSFRichTextString totalValue4 = new HSSFRichTextString("");
				HSSFRichTextString totalValue5 = new HSSFRichTextString("");
				HSSFRichTextString totalValue6;
				if (dataList1.get(0).get("SMETHOD").equals("3")) {
					totalValue6 = new HSSFRichTextString(rewardZJLdayValue + "");
				} else {
					totalValue6 = new HSSFRichTextString(rewardBnValue + "");
				}
				batchInsertS2(rewardZJListRow, dataList1, totalValue1,
						totalValue2,
						Double.parseDouble(totalValue3.toString()),
						totalValue4, totalValue5, Double
								.parseDouble(totalValue6.toString()));

				count = count + 1;
				rewardZJLValue = 0;
				rewardZJLdayValue = 0;
				rewardBnValue = 0;
				// System.out.println("222222222222222222222:"+count);
				HSSFRow rewardZJListRow2 = sheet.createRow(count + i);
				// System.out.println("222222222222222222222:"+count);
				rewardZJLValue = Double.parseDouble(dataList2.get(i).get(
						"NMONEY"));
				rewardZJLdayValue = Double.parseDouble(dataList2.get(i).get(
						"PRONMONEY"));
				rewardBnValue = Double.parseDouble(dataList2.get(i).get("BN"));
				batchInsertS(rewardZJListRow2, dataList1, Double
						.parseDouble(dataList2.get(i).get("NORDER")),
						rewardZJListValue2, Double
								.parseDouble(rewardZJListValue3.toString()),
						rewardZJListValue4, Double
								.parseDouble(rewardZJListValue5.toString()),
						Double.parseDouble(rewardZJListValue6.toString()));
				flag = false;
				System.out.println("oldGroupName1:" + oldGroupName);
				oldGroupName = dataList2.get(i).get("GROUPNAME");
				System.out.println("oldGroupName2:" + oldGroupName);
			}

		}
		rowNum = dataList2.size() + count;

		// 合计
		HSSFRichTextString totalValue1 = new HSSFRichTextString("总计");
		HSSFRichTextString totalValue2 = new HSSFRichTextString("");
		HSSFRichTextString totalValue3 = new HSSFRichTextString(rewardZJLValue
				+ "");
		HSSFRichTextString totalValue4 = new HSSFRichTextString("");
		HSSFRichTextString totalValue5 = new HSSFRichTextString("");
		HSSFRichTextString totalValue6;
		if (dataList1.get(0).get("SMETHOD").equals("3")) {
			totalValue6 = new HSSFRichTextString(rewardZJLdayValue + "");
		} else {
			totalValue6 = new HSSFRichTextString(rewardBnValue + "");
		}
		HSSFRow rewardZJListRow = sheet.createRow(rowNum);
		batchInsertS2(rewardZJListRow, dataList1, totalValue1, totalValue2,
				Double.parseDouble(totalValue3.toString()), totalValue4,
				totalValue5, Double.parseDouble(totalValue6.toString()));
		rowNum += 1;

		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum,
				"注:若年金是在本考核期前成立，则第一笔现金流为上一考核期末净值");

		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum, "");

		// 第三个模块 三、业绩报酬的初步计算
		HSSFRichTextString rewardBAccountValue = new HSSFRichTextString(
				"三、业绩报酬的初步计算");
		HSSFRow bAccountRow = sheet.createRow(rowNum);
		batchInsert(bAccountRow, rewardBAccountValue);
		sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
				(short) (COLUMNS - 1)));
		rowNum += 1;

		double rewardBNValue = 0;
		if (dataList1.get(0).get("SMETHOD").equals("3")) {
			// 年金期末实际净值An:
			HSSFRichTextString rewardAnValue = new HSSFRichTextString(
					"年金期末实际净值An");
			HSSFRow eAnRow = sheet.createRow(rowNum);
			batchInsertQ(eAnRow, rewardAnValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("AN")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// 考核期间天数T:
			HSSFRichTextString rewardCheckDayValue = new HSSFRichTextString(
					"考核期间天数T");
			HSSFRow rewardCheckDayRow = sheet.createRow(rowNum);
			batchInsert(rewardCheckDayRow, rewardCheckDayValue, "", "", "", "",
					Double.parseDouble(dataList3.get(0).get("REALDAY")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// 当年实际天数T0:
			HSSFRichTextString rewardActualDayValue = new HSSFRichTextString(
					"当年实际天数TO");
			HSSFRow rewardActualDayRow = sheet.createRow(rowNum);
			batchInsert(rewardActualDayRow, rewardActualDayValue, "", "", "",
					"", Double.parseDouble(dataList3.get(0).get("YEARDAY")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// 年化实际收益率Rt:
			HSSFRichTextString rewardRtValue = new HSSFRichTextString(
					"年化实际收益率Rt");
			HSSFRow rewardRtRow = sheet.createRow(rowNum);
			batchInsertBai(rewardRtRow, rewardRtValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("REALRN")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// 年化基准收益率rt:
			HSSFRichTextString rewardrtValue = new HSSFRichTextString(
					"年化基准收益率rt");
			HSSFRow rewardrtRow = sheet.createRow(rowNum);
			batchInsertBai(rewardrtRow, rewardrtValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("COMPRN")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// average:
			HSSFRichTextString rewardAverageValue = new HSSFRichTextString(
					"average");
			HSSFRow rewardAverageRow = sheet.createRow(rowNum);
			batchInsertQ(rewardAverageRow, rewardAverageValue, "", "", "", "",
					Double.parseDouble(dataList3.get(0).get("BN")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// 业绩报酬初步计算值=MAX((Rt-rt)*(T/T0)*average*业绩报酬比例)
			HSSFRichTextString rewardBeginAccountValue = new HSSFRichTextString(
					"业绩报酬初步计算值=MAX((Rt-rt)*(T/TO)*average*业绩报酬比例,0)");
			HSSFRow rewardBeginAccountRow = sheet.createRow(rowNum);
			batchInsertQ(rewardBeginAccountRow, rewardBeginAccountValue, "",
					"", "", "", Double.parseDouble(dataList3.get(0).get(
							"UNREWARD")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			// rowNum+=1;
		} else if (dataList1.get(0).get("SMETHOD").equals("0")
				|| dataList1.get(0).get("SMETHOD").equals("1")) {
			// 年金期末实际净值An:
			HSSFRichTextString rewardAnValue = new HSSFRichTextString(
					"年金期末实际净值An");
			HSSFRow eAnRow = sheet.createRow(rowNum);
			batchInsertQ(eAnRow, rewardAnValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("AN")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// Bn 和 业绩报酬
			HSSFRichTextString rewardBn2Value;
			// 孰高模式
			if (dataList1.get(0).get("SMETHOD").equals("1")) {
				rewardBn2Value = new HSSFRichTextString("Max(Bn)");
			} else {
				rewardBn2Value = new HSSFRichTextString("Bn");
			}
			HSSFRichTextString rewardValue = new HSSFRichTextString("初步计算业绩报酬");
			for (int i = 0; i < dataList3.size(); i++) {
				HSSFRow rewardBn2Row = sheet.createRow(rowNum + i);
				rewardBNValue += Double.parseDouble(dataList3.get(i).get(
						"UNREWARD"));

				batchInsertQ(rewardBn2Row, rewardBn2Value, "", Double
						.parseDouble(dataList3.get(i).get("BN")), rewardValue,
						"", Double
								.parseDouble(dataList3.get(i).get("UNREWARD")));
				sheet.addMergedRegion(new Region((rowNum + i), (short) 0,
						(rowNum + i), (short) 1));
				sheet.addMergedRegion(new Region((rowNum + i), (short) 3,
						(rowNum + i), (short) 4));
			}
		} else {

			// 年金期末实际净值An:
			HSSFRichTextString rewardAnValue = new HSSFRichTextString(
					"年金期末实际净值An");
			HSSFRow eAnRow = sheet.createRow(rowNum);
			batchInsertQ(eAnRow, rewardAnValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("AN")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			// Bn 和 业绩报酬
			HSSFRichTextString rewardBn2Value;
			// 孰高模式
			if (dataList1.get(0).get("SMETHOD").equals("1")) {
				rewardBn2Value = new HSSFRichTextString("Max(Bn)");
			} else {
				rewardBn2Value = new HSSFRichTextString("Bn");
			}
			// HSSFRichTextString rewardfName = new HSSFRichTextString("使用基准");
			HSSFRichTextString rewardValue = new HSSFRichTextString("初步计算业绩报酬");
			for (int i = 0; i < dataList3.size(); i++) {
				int j = i + 1;
				HSSFRichTextString rewardfName = new HSSFRichTextString("使用基准"
						+ j);
				HSSFRow rewardBn2Row = sheet.createRow(rowNum + i);
				rewardBNValue += Double.parseDouble(dataList3.get(i).get(
						"UNREWARD"));
				batchInsertQ(rewardBn2Row, rewardfName, dataList3.get(i).get(
						"GROUPNAME"), rewardBn2Value, Double
						.parseDouble(dataList3.get(i).get("BN")), rewardValue,
						Double.parseDouble(dataList3.get(i).get("UNREWARD")));
			}
		}

		rowNum += dataList3.size();

		// 累进模式才有总计,否则取消
		if (dataList1.get(0).get("SMETHOD").equals("2")) {
			HSSFRichTextString rewardtotalValue = new HSSFRichTextString("总计");
			HSSFRow rewardtotalRow = sheet.createRow(rowNum);
			batchInsertQ(rewardtotalRow, rewardtotalValue, "", "", "", "",
					rewardBNValue);
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;
		}

		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum, "");

		// 第四个模块 四、投资管理费限额的计算
		HSSFRichTextString rewardTJValue = new HSSFRichTextString(
				"四、投资管理费限额的计算");
		HSSFRow rewardTJRow = sheet.createRow(rowNum);
		batchInsert(rewardTJRow, rewardTJValue);
		sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
				(short) (COLUMNS - 1)));
		rowNum += 1;

		HSSFRichTextString rewardTotalGLFValue;
		HSSFRow rewardTotalGLFRow = sheet.createRow(rowNum);
		// 按管理费计提
		if (dataList1.get(0).get("LIMITTYPE").equals("0")) {
			rewardTotalGLFValue = new HSSFRichTextString("考核期内计提基本管理费总额");
			batchInsertQ(rewardTotalGLFRow, rewardTotalGLFValue, "", "", "",
					"", Double.parseDouble(dataList3.get(0).get("MANAGEFEE")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			rowNum = getRewardByTypeId(dataList1, dataList3, sheet, rowNum);
		}// 按平均资产净值1
		else if (dataList1.get(0).get("LIMITTYPE").equals("1")) {
			// rewardTotalGLFValue = new
			// HSSFRichTextString("考核期内平均资产净值("+RewardPublic.getPreviousStringDate(beginDate)+"至"+RewardPublic.getPreviousStringDate(endDate)+")");
			// batchInsertQ(rewardTotalGLFRow,rewardTotalGLFValue,"","","","",Double.parseDouble(dataList3.get(0).get("MANAGEFEE")));
			// sheet.addMergedRegion(new
			// Region(rowNum,(short)0,rowNum,(short)(COLUMNS-2)));
			rowNum += 1;

			rowNum = getRewardByTypeId(dataList1, dataList3, sheet, rowNum);
		}// 按平均资产净值2
		else if (dataList1.get(0).get("LIMITTYPE").equals("2")) {
			rewardTotalGLFValue = new HSSFRichTextString("考核期内平均资产净值("
					+ beginDate + "至" + endDate + ")");
			batchInsertQ(rewardTotalGLFRow, rewardTotalGLFValue, "", "", "",
					"", Double.parseDouble(dataList3.get(0).get("MANAGEFEE")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			rowNum = getRewardByTypeId(dataList1, dataList3, sheet, rowNum);
		}// 手工输入
		else if (dataList1.get(0).get("LIMITTYPE").equals("3")) {
			rewardTotalGLFValue = new HSSFRichTextString("手工输入管理费最高限额");
			batchInsertQ(rewardTotalGLFRow, rewardTotalGLFValue, "", "", "",
					"", Double.parseDouble(dataList3.get(0).get("MANAGEFEE")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;

			rowNum = getRewardByTypeId(dataList1, dataList3, sheet, rowNum);
		}// 无管理费最大限额
		else if (dataList1.get(0).get("LIMITTYPE").equals("4")) {
			rewardTotalGLFValue = new HSSFRichTextString("无管理费最大限额");
			batchInsert(rewardTotalGLFRow, rewardTotalGLFValue);
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 1)));
			rowNum += 1;
		}

		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum, "");

		// 第五个模块 五、应计提的业绩报酬
		HSSFRichTextString rewardJTValue = new HSSFRichTextString("五、应计提的业绩报酬");
		HSSFRow rewardJTRow = sheet.createRow(rowNum);
		batchInsert(rewardJTRow, rewardJTValue);
		sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
				(short) (COLUMNS - 1)));
		rowNum += 1;

		HSSFRichTextString rewardReultValue;
		HSSFRow rewardReult = sheet.createRow(rowNum);
		System.out.println("dataList3.get(0).get('REWARD')"
				+ dataList3.get(0).get("REWARD"));
		if (dataList3.get(0).get("REWARD").equals("0")) {
			// System.out.println("1111111111111");
			rewardReultValue = new HSSFRichTextString("应计提的业绩报酬");
			batchInsertQ(rewardReult, rewardReultValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("REWARD")));
			sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
					(short) (COLUMNS - 2)));
			rowNum += 1;
		} else {
			if (dataList1.get(0).get("LIMITTYPE").equals("4")) {
				rewardReultValue = new HSSFRichTextString(
						"由于无管理费最大限额，可计提业绩报酬=业绩报酬初步计算值");
				batchInsertQ(rewardReult, rewardReultValue, "", "", "", "",
						Double.parseDouble(dataList3.get(0).get("REWARD")));
				sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
						(short) (COLUMNS - 2)));
				rowNum += 1;
			} else if (dataList1.get(0).get("LIMITTYPE").equals("1")
					|| dataList1.get(0).get("LIMITTYPE").equals("2")) {
				if (Double.parseDouble(dataList3.get(0).get("LIMITVALUE")) > Double
						.parseDouble(dataList3.get(0).get("UNREWARD"))) {
					rewardReultValue = new HSSFRichTextString(
							"由于业绩报酬初步计算值<最高限额,可计提业绩报酬=业绩报酬初步计算值");
					batchInsertQ(rewardReult, rewardReultValue, "", "", "", "",
							Double.parseDouble(dataList3.get(0).get("REWARD")));
					sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
							(short) (COLUMNS - 2)));
					rowNum += 1;
				} else if (Double.parseDouble(dataList3.get(0)
						.get("LIMITVALUE")) <= Double.parseDouble(dataList3
						.get(0).get("UNREWARD"))) {
					rewardReultValue = new HSSFRichTextString(
							"由于业绩报酬初步计算值≥最高限额,可计提业绩报酬=最高限额");
					batchInsertQ(rewardReult, rewardReultValue, "", "", "", "",
							Double.parseDouble(dataList3.get(0).get("REWARD")));
					sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
							(short) (COLUMNS - 2)));
					rowNum += 1;
				}
			} else if (Double.parseDouble(dataList3.get(0).get("LIMITVALUE"))
					- Double.parseDouble(dataList3.get(0).get("MANAGEFEE")) > Double
					.parseDouble(dataList3.get(0).get("REWARD"))) {
				rewardReultValue = new HSSFRichTextString(
						"由于业绩报酬初步计算值+基本管理费<最高限额,可计提业绩报酬=业绩报酬初步计算值");
				batchInsertQ(rewardReult, rewardReultValue, "", "", "", "",
						Double.parseDouble(dataList3.get(0).get("REWARD")));
				sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
						(short) (COLUMNS - 2)));
				rowNum += 1;
			} else {
				rewardReultValue = new HSSFRichTextString(
						"由于业绩报酬初步计算值+基本管理费≥最高限额,可计提业绩报酬=最高限额-基本管理费");
				batchInsertQ(rewardReult, rewardReultValue, "", "", "", "",
						Double.parseDouble(dataList3.get(0).get("REWARD")));
				sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
						(short) (COLUMNS - 2)));
				rowNum += 1;
			}

		}

		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum, "");
		// 换行的时候 最后一个单元格要用双线
		rowNum = doubleLine(hb, sheet, rowNum, "");

		return hb;
	}

	private static int getRewardByTypeId(List<Map<String, String>> dataList1,
			List<Map<String, String>> dataList3, HSSFSheet sheet, int rowNum) {
		HSSFRichTextString rewardTZValue;
		HSSFRow rewardTZRow = sheet.createRow(rowNum);
		@SuppressWarnings("unused")
		DecimalFormat df = new DecimalFormat("#,##0.00#");
		// 按管理费计提
		if (dataList1.get(0).get("LIMITTYPE").equals("0")) {
			rewardTZValue = new HSSFRichTextString(
					"投资管理费最高限额=基本管理费总额/基本管理费率*最高限额比例");
			batchInsertQ(rewardTZRow, rewardTZValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("LIMITVALUE")));
		}// 按平均资产净值
		else if (dataList1.get(0).get("LIMITTYPE").equals("1")
				|| dataList1.get(0).get("LIMITTYPE").equals("2")) {
			rewardTZValue = new HSSFRichTextString(
					"投资管理费最高限额=∑考核期内每日资产净值*最高限额比例/当年实际天数");
			batchInsertQ(rewardTZRow, rewardTZValue, "", "", "", "", Double
					.parseDouble(dataList3.get(0).get("LIMITVALUE")));
		}// 手工输入
		else if (dataList1.get(0).get("LIMITTYPE").equals("3")) {
			rewardTZValue = new HSSFRichTextString("");
			batchInsert(rewardTZRow, rewardTZValue);
		}
		sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
				(short) (COLUMNS - 2)));
		rowNum += 1;
		return rowNum;
	}

	private static void borderSetting(HSSFCellStyle cellStyle, int... c) {
		short[] a = { 1, 2, 3, 4 };
		int i = 0;
		for (int d : c) {
			if (d == 0) {
				a[i] = HSSFCellStyle.BORDER_THIN;
			} else {
				a[i] = HSSFCellStyle.BORDER_DOUBLE;
			}
			i += 1;
		}
		cellStyle.setBorderTop(a[0]);
		cellStyle.setBorderBottom(a[1]);
		cellStyle.setBorderLeft(a[2]);
		cellStyle.setBorderRight(a[3]);
		cellStyle.setTopBorderColor(HSSFColor.GREEN.index);
		cellStyle.setLeftBorderColor(HSSFColor.GREEN.index);
		cellStyle.setRightBorderColor(HSSFColor.GREEN.index);
		cellStyle.setBottomBorderColor(HSSFColor.GREEN.index);
	}

	// 换行的时候 最后一个单元格要用双线
	
	@SuppressWarnings("deprecation")
	public static int doubleLine(HSSFWorkbook hb, HSSFSheet sheet, int rowNum,
			String value) {
		HSSFRow templ1Row = sheet.createRow(rowNum);
		// 前四格用下框线
		for (int i = 0; i < (COLUMNS - 1); i++) {
			HSSFCellStyle templ1CellStyle1 = hb.createCellStyle();
			templ1CellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			templ1CellStyle1.setBottomBorderColor(HSSFColor.GREEN.index);
			HSSFCell templ1Cell1 = templ1Row.createCell((short) i);
			if (i == 0) {
				templ1Cell1.setCellValue(value);
			}
			templ1Cell1.setCellStyle(templ1CellStyle1);
		}
		// 最后一格用下框和右双框线
		HSSFCell templ1Cell = templ1Row.createCell((short) (COLUMNS - 1));
		HSSFCellStyle templ1CellStyle = hb.createCellStyle();
		templ1CellStyle.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);
		templ1CellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		templ1CellStyle.setRightBorderColor(HSSFColor.GREEN.index);
		templ1CellStyle.setBottomBorderColor(HSSFColor.GREEN.index);
		templ1Cell.setCellStyle(templ1CellStyle);

		sheet.addMergedRegion(new Region(rowNum, (short) 0, rowNum,
				(short) (COLUMNS - 1)));
		rowNum += 1;
		return rowNum;
	}

	public static void batchInsert(HSSFRow row, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCell(row, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCell(row, (short) j, "");
		}
	}

	private static void insertCell(HSSFRow row, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		String[] templeS = (value + "").split("/d");
		if (cellNum == (short) (COLUMNS - 1)) {
			if (templeS.length > 1) {
				cell.setCellStyle(styles.get("everyFinalCellStyle2"));
			} else {
				try {
					if (Integer.parseInt((value + "")) != 0
							|| Integer.parseInt((value + "")) == 0) {
						cell.setCellStyle(styles.get("everyFinalCellStyle2"));
					}

				} catch (Exception e) {
					cell.setCellStyle(styles.get("everyFinalCellStyle"));
				}
				// cell.setCellStyle(styles.get("everyFinalCellStyle"));
			}
			/*
			 * if((value+"").indexOf(".")>0){
			 * cell.setCellStyle(styles.get("everyFinalCellStyle2")); }else {
			 * cell.setCellStyle(styles.get("everyFinalCellStyle")); }
			 */
		} else {
			if (templeS.length > 1) {
				cell.setCellStyle(styles.get("everyCellStyle2"));
			} else {
				try {
					if (Integer.parseInt((value + "")) != 0
							|| Integer.parseInt((value + "")) == 0) {
						cell.setCellStyle(styles.get("everyCellStyle2"));
					}

				} catch (Exception e) {
					cell.setCellStyle(styles.get("everyCellStyle"));
				}
			}
			/*
			 * if((value+"").indexOf(".")>0){
			 * cell.setCellStyle(styles.get("everyCellStyle2")); }else {
			 * cell.setCellStyle(styles.get("everyCellStyle")); }
			 */
		}

		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public static void batchInsertS(HSSFRow row,
			List<Map<String, String>> dataList1, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCellS(row, dataList1, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCellS(row, dataList1, (short) j, "");
		}
	}

	private static void insertCellS(HSSFRow row,
			List<Map<String, String>> dataList1, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		if (cellNum == (short) (0)) {
			cell.setCellStyle(styles.get("everyCellStyleXu"));
		} else if (cellNum == (short) (2)) {
			cell.setCellStyle(styles.get("everyCellStyleQ"));
		} else if (cellNum == (short) (4)) {
			if ((value + "").equals("")) {
				cell.setCellStyle(styles.get("everyCellStyle"));
			} else {
				if (dataList1.get(0).get("SMETHOD").equals("3")) {
					cell.setCellStyle(styles.get("everyCellStyle"));
				} else {
					cell.setCellStyle(styles.get("everyFinalCellStyle31"));
				}
			}
		} else if (cellNum == (short) (COLUMNS - 1)) {
			if (dataList1.get(0).get("BNTWO").equals("0")) {
				cell.setCellStyle(styles.get("everyFinalCellStyleQ2"));
			} else if (dataList1.get(0).get("BNTWO").equals("1")) {
				cell.setCellStyle(styles.get("everyFinalCellStyleQ2"));
			}
		} else {
			cell.setCellStyle(styles.get("everyCellStyle2"));
		}

		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public static void batchInsertBai(HSSFRow row, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCellBai(row, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCellBai(row, (short) j, "");
		}
	}

	private static void insertCellBai(HSSFRow row, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		if (cellNum == (short) (COLUMNS - 1)) {
			cell.setCellStyle(styles.get("everyFinalCellStyle32"));
		} else {
			cell.setCellStyle(styles.get("everyFinalCellStyle3"));
		}
		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public static void batchInsertT(HSSFRow row, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCellT(row, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCellT(row, (short) j, "");
		}
	}

	private static void insertCellT(HSSFRow row, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		cell.setCellStyle(styles.get("everyFinalCellStyle4"));
		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public static void batchInsertC(HSSFRow row, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCellC(row, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCellC(row, (short) j, "");
		}
	}

	private static void insertCellC(HSSFRow row, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		if (cellNum == (short) (COLUMNS - 1)) {
			cell.setCellStyle(styles.get("everyFinalCellStyle52"));
		} else {
			cell.setCellStyle(styles.get("everyFinalCellStyle51"));
		}
		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public static void batchInsertQ(HSSFRow row, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCellQ(row, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCellQ(row, (short) j, "");
		}
	}

	private static void insertCellQ(HSSFRow row, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		if (cellNum == (short) (1)) {
			cell.setCellStyle(styles.get("everyCellStyle2"));
		} else if (cellNum == (short) (2)) {
			if ((value + "").indexOf("Bn") > 0) {
				cell.setCellStyle(styles.get("everyCellStyle"));
			} else {
				cell.setCellStyle(styles.get("everyCellStyleQ"));
			}
		} else if (cellNum == (short) (3)) {
			cell.setCellStyle(styles.get("everyCellStyleQ"));
		} else if (cellNum == (short) (5)) {
			cell.setCellStyle(styles.get("everyFinalCellStyleQ2"));
		} else {
			cell.setCellStyle(styles.get("everyCellStyle"));
		}

		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public static void batchInsertS2(HSSFRow row,
			List<Map<String, String>> dataList1, Object... values) {
		int i = 0;
		for (Object value : values) {
			insertCellS2(row, dataList1, (short) i, value);
			i += 1;
		}
		for (int j = i; j <= (COLUMNS - 1); j++) {
			insertCellS2(row, dataList1, (short) j, "");
		}
	}

	private static void insertCellS2(HSSFRow row,
			List<Map<String, String>> dataList1, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);
		if (cellNum == (short) (0)) {
			cell.setCellStyle(styles.get("everyCellStyleXu"));
		} else if (cellNum == (short) (2)) {
			cell.setCellStyle(styles.get("everyCellStyleQ"));
		} else if (cellNum == (short) (4)) {
			if ((value + "").equals("")) {
				cell.setCellStyle(styles.get("everyCellStyle"));
			} else {
				if (dataList1.get(0).get("SMETHOD").equals("3")) {
					cell.setCellStyle(styles.get("everyCellStyle"));
				} else {
					cell.setCellStyle(styles.get("everyFinalCellStyle31"));
				}
			}
		} else if (cellNum == (short) (COLUMNS - 1)) {
			cell.setCellStyle(styles.get("everyFinalCellStyleQ2"));
		} else {
			cell.setCellStyle(styles.get("everyCellStyle2"));
		}

		if (value instanceof Double && value != null) {
			double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

}