package main.java.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.List;

public class ExcelUtil {

    public static void saveExcel(List<String> dataList, String projectPath){
        String path = projectPath + ".xls";
        File file = new File(path);
        OutputStream out = null;
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            // 读取Excel文档
            Workbook workBook = new HSSFWorkbook();
            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();

            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
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
}
