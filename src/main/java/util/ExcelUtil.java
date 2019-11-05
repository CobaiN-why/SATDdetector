package main.java.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

import static java.lang.Integer.max;

public class ExcelUtil {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static Workbook getWorkbok(File file) throws IOException{
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith(EXCEL_XLS)){     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    public void saveExcel(List<String> dataList, String projectPath){
        String path = projectPath + ".xls";
        File file = new File(path);
        OutputStream out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // 读取Excel文档
            Workbook workBook = new HSSFWorkbook();
            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();

            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j);
                // 得到要插入的每一条记录
                String comment = dataList.get(j);
                Cell first = row.createCell(0);
                first.setCellValue(comment);
            }
            out =  new FileOutputStream(file);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDiff(String filePath, String lastTag, String nowTag, List<String> lastComments, List<String> nowComments, int lastSize, int nowSize){
        String path = filePath + lastTag + "-->" + nowTag + ".xls";
        File file = new File(path);
        OutputStream out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // 读取Excel文档
            Workbook workBook = new HSSFWorkbook();
            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();

            int lastLen = lastComments.size();
            int nowLen = nowComments.size();

            Row row0 = sheet.createRow(0);
            Cell first0 = row0.createCell(0);
            first0.setCellValue("The number of SATD in version " + lastTag + " is " + lastSize);
            Cell second0 = row0.createCell(1);
            second0.setCellValue("The number of SATD in version " + nowTag + " is " + nowSize);

            Row row1 = sheet.createRow(1);
            Cell first1 = row1.createCell(0);
            first1.setCellValue("- -");
            Cell second1 = row1.createCell(1);
            second1.setCellValue("+ +");

            for (int i = 1; i <= max(lastLen, nowLen) ; i++){
                Row r = sheet.createRow(i+1);
                Cell a = r.createCell(0);
                if (i <= lastLen)
                    a.setCellValue(lastComments.get(i-1));
                Cell b = r.createCell(1);
                if (i <= nowLen)
                    b.setCellValue(nowComments.get(i-1));
            }

            out =  new FileOutputStream(file);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
