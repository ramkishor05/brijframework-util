package org.brijframework.util.resouces;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.text.StringUtil;
import org.brijframework.util.validator.ValidationUtil;

public class ExcelUtil {
	
	public static HSSFWorkbook workbook(List<Map<String, Object>> rowList, String[] objectKeys, String[] headerKeys) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		sheet.createFreezePane(0, 1, 0, 1);
		HSSFRow rowhead = sheet.createRow((short) 0);
		headerKeys = validateHeaderWithObjKeys(objectKeys, headerKeys);
		HSSFCellStyle headerCellStyle = headerCellStyle(workbook);
		for (int i = 0; i < headerKeys.length; i++) {
			HSSFCell cell = rowhead.createCell(i);
			cell.setCellValue(ValidationUtil.friendlyNameForJavaVariable(headerKeys[i]));
			cell.setCellStyle(headerCellStyle);
		}

		HSSFCellStyle dataCellStyle = cellStyle(workbook, 12, false, false, IndexedColors.LIGHT_CORNFLOWER_BLUE);
		for (int i = 0; i < rowList.size(); i++) {
			HSSFRow row = sheet.createRow((short) (i + 1));
			Map<String, Object> map = rowList.get(i);
			for (int j = 0; j < objectKeys.length; j++) {
				Object rowData = map.get(objectKeys[j]);
				HSSFCell cell = row.createCell(j);
				if (rowData != null) {
					if(ValidationUtil.isClassANumber(rowData.getClass())) {
						cell.setCellValue(CastingUtil.intValue(rowData));
					} else {
						cell.setCellValue(CastingUtil.stringValue(rowData));
					}
				}
				cell.setCellStyle(dataCellStyle);
			}
		}
		for (int i = 0; i < objectKeys.length; i++) {
			sheet.autoSizeColumn(i);
		}
		return workbook;
	}

	private static String[] validateHeaderWithObjKeys(String[] objectKeys, String[] headerKeys) {
		if (headerKeys.length == objectKeys.length) {
			return headerKeys;
		}
		String[] newHeaderKeys = new String[objectKeys.length];
		for (int i = 0; i < headerKeys.length; i++) {
			newHeaderKeys[i] = headerKeys[i];
		}
		for (int i = headerKeys.length; i < objectKeys.length; i++) {
			newHeaderKeys[i] = objectKeys[i];
		}
		return newHeaderKeys;
	}

	public static HSSFWorkbook workbook(List<Map<String, Object>> rowList, String objectKeys, String headerKeys) {
		String[] objectkeyArr = StringUtil.parseTilde(objectKeys);
		String[] headerKeyArr =  StringUtil.parseTilde(headerKeys);
		return workbook(rowList, objectkeyArr, headerKeyArr);
	}

	public static HSSFWorkbook workbook(List<Map<String, Object>> rowList, String objectKeys) {
		return workbook(rowList, objectKeys, objectKeys);
	}

	@SuppressWarnings("deprecation")
	private static HSSFCellStyle headerCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle headerCellStyle = cellStyle(workbook, 14, true, true, IndexedColors.DARK_BLUE);
		headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerCellStyle.getFont(workbook).setColor(HSSFColor.WHITE.index);
		return headerCellStyle;
	}

	@SuppressWarnings({ "deprecation"})
	private static HSSFCellStyle cellStyle(HSSFWorkbook workbook, int fontSize, boolean isBold, boolean isItalic, IndexedColors bgColor) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) fontSize);
		if (isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		font.setItalic(isItalic);
		cellStyle.setFont(font);

		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

		cellStyle.setFillForegroundColor(bgColor.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}

	public static void createFile(List<Map<String, Object>> rowList, String objectKeys, String headerKeys, String fileName) {
		HSSFWorkbook workbook = workbook(rowList, objectKeys, headerKeys);
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(List<Map<String, Object>> rowList, String objectKeys, String fileName) {
		createFile(rowList, objectKeys, objectKeys, fileName);
	}

	public static byte[] byteArray(List<Map<String, Object>> rowList, String objectKeys, String headerKeys) {
		HSSFWorkbook workbook = workbook(rowList, objectKeys, headerKeys);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	public static byte[] byteArray(List<Map<String, Object>> rowList, String objectKeys) {
		return byteArray(rowList, objectKeys, objectKeys);
	}
	
	public static void main(String[] args) {
		List<Map<String, Object>> dataArray=new ArrayList<>();
		LinkedHashMap<String, Object> map=new LinkedHashMap<>();
		map.put("id","1");
		map.put("name","ram kishor");
		map.put("email","ram@mail.com");
		map.put("addres","noida");
		for(int i=0;i<=10;i++){
			dataArray.add(map);
		}
		String headerKey="id~name~email";
		createFile(dataArray,headerKey,"data.xls");
	}
}