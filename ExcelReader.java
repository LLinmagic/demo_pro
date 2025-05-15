package Class6_3;

import org.apache.poi.ss.usermodel.*;       //POI知识>>>>>>>
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;         //文件操作>>>>>
import java.io.IOException;
import java.util.ArrayList;      //集合知识(之List)>>>
import java.util.List;
//import org.apache;
// Excel 读取类，负责从 Excel 文件中读取数据
public class ExcelReader {
	
	//从Excel 文件路径读取电影数据并返回一个电影列表方法：
    public List<Movie> readExcel(String filePath) {
        List<Movie> movies = new ArrayList<>();
        
        //使用 XSSFWorkbook 处理 .xlsx 格式的 Excel 文件
        Workbook workbook =new XSSFWorkbook();
        FileInputStream fis=new FileInputStream(filepath);
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
        	// 获取第一个工作表
        	Sheet sheet= workbook.get
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {// 遍历工作表的每一行
                if (row.getRowNum() == 0) {
                    continue; // 跳过表头行(第一行)
                 }
                // 检查是否是空白行
                if (isRowEmpty(row)) {
                    continue; // 跳过空白行
                }
                // 从每一行中读取电影数据
                String title = getCellValue(row, 0);
                String director = getCellValue(row, 11);
                //System.out.println(director);
                double score = getNumericCellValue(row, 1);
                int votes = (int) getNumericCellValue(row, 2);
                String mainActor = getCellValue(row, 13);
                String screenwriter = getCellValue(row, 12);
                String country = getCellValue(row, 14);
                String language = getCellValue(row, 15);
                String type = getCellValue(row, 10);
                String Year = getCellValue(row,16);
                int length = (int) getNumericCellValue(row, 17);
               
                // 创建 Movie 对象并添加到电影列表中
                movies.add(new Movie(title, director, score, votes, mainActor, screenwriter, country, language,type, Year,length));
            }
        } catch (IOException e) {
            e.printStackTrace();// 打印异常信息
        }
        return movies;// 返回电影列表
    }
    
    
    // 添加 isRowEmpty 方法
    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {//row.getLastCellNum()返回：当前行最后一列的索引+1
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
    
    // 获取单元格的字符串值的方法：
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);//下标，指定列的单元格
        if (cell == null) {
            return ""; // 处理空单元格，返回空字符串
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();// 返回单元格的字符串值
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());// 返回单元格的数值转化为字符串
            default:
                return "";
        }
    }
    
    
    //获取单元格的数值的方法：（重写函数方法getNumericCellValue）
    private double getNumericCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return 0.0; // 处理空单元格或其他类型的默认情况
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue(); // 返回单元格的数值
            default:
                return 0.0; // 非数值类型的默认情况
        }
    }
}
