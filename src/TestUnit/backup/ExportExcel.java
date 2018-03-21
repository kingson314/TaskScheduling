package TestUnit.backup;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportExcel {

	public static HSSFWorkbook produceExcel(List<Map<String, String>> dataList,
			Map<String, String> fieldMap) {

		HSSFWorkbook hb = new HSSFWorkbook();
		HSSFSheet sheet = hb.createSheet("sheet1");
		int rowNum = 0;
		System.out.println("fieldMap :" + fieldMap);
		for (Map<String, String> data : dataList) {
			HSSFRow row = sheet.createRow(rowNum);
			@SuppressWarnings("unused")
			Short cellNum = 0;
			if (rowNum == 0) {
				for (Entry<String, String> entry : data.entrySet()) {
					String key = entry.getKey();
					if (fieldMap != null && fieldMap.get(key) != null) {
						insertCell(row, Short.valueOf(fieldMap.get(key)), key);
					}/*
						 * else{ insertCell(row, cellNum, key); cellNum++; }
						 */
				}
				rowNum++;
				row = sheet.createRow(rowNum);
				cellNum = 0;
			}
			for (Entry<String, String> entry : data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (fieldMap != null && fieldMap.get(key) != null) {
					insertCell(row, Short.valueOf(fieldMap.get(key)), value);
				}/*
					 * else{ insertCell(row, cellNum, value); cellNum++; }
					 */
			}
			rowNum++;
		}
		return hb;
	}

	private static void insertCell(HSSFRow row, Short cellNum, Object value) {
		HSSFCell cell = row.createCell(cellNum);

		if (value instanceof Double && value != null) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			Double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			HSSFRichTextString rtsValue = new HSSFRichTextString(value
					.toString());
			cell.setCellValue(rtsValue);
		}
	}

}