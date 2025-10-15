package com.arisys.createUser.common;

import com.arisys.createUser.dao.User;
import com.arisys.createUser.dto.SearchCondition;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class CreateExcel {
    public static final Logger logger = LogManager.getLogger(CreateExcel.class);

    public static byte[] writeUserListToFile(String fileName, List<User> UserList, SearchCondition search) throws Exception{
        Workbook workbook = null;

        if(fileName.endsWith("xlsx")){
            workbook = new XSSFWorkbook();
        }else if(fileName.endsWith("xls")){
            workbook = new HSSFWorkbook();
        }else{
            throw new Exception("invalid file name, should be xls or xlsx");
        }
        //search data
        String searchConditionString = "search : " + search.getCategory() + "_" + search.getSearch()
                + "/ sort : " + search.getSortType() + "_" + search.getSending();

        Sheet sheet = workbook.createSheet("Users");

        Row rowTitle = sheet.createRow(0);
        rowTitle.createCell(0).setCellValue("id");
        rowTitle.createCell(1).setCellValue("name");
        rowTitle.createCell(2).setCellValue("age");
        rowTitle.createCell(3).setCellValue("gender");
        rowTitle.createCell(4).setCellValue("job");
        rowTitle.createCell(5).setCellValue(searchConditionString);

        Iterator<User> iterator = UserList.iterator();

        int rowIndex = 1;
        while(iterator.hasNext()){
            User User = iterator.next();
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(User.getId());
            row.createCell(1).setCellValue(User.getName());
            row.createCell(2).setCellValue(User.getAge());
            row.createCell(3).setCellValue(User.getGender());
            row.createCell(4).setCellValue(User.getJob());

        }
        //search data
//        if(search != null){
//            Row row1,row2,row4,row5;
//            if(sheet.getRow(1) ==null){
//                row1 = sheet.createRow(1);
//            }
//            if(sheet.getRow(2) ==null){
//                row2 = sheet.createRow(2);
//            }
//            if(sheet.getRow(4) ==null){
//                row4 = sheet.createRow(4);
//            }
//            if(sheet.getRow(5) ==null){
//                row5 = sheet.createRow(5);
//            }
//
//            row1 = sheet.getRow(1);
//            row1.createCell(7).setCellValue("search");
//            row1.createCell(8).setCellValue(search.getCategory());
//            row2 = sheet.getRow(2);
//            row2.createCell(8).setCellValue(search.getSearch());
//
//            row4 = sheet.getRow(4);
//            row4.createCell(7).setCellValue("sort");
//            row4.createCell(8).setCellValue(search.getSortType());
//            row5 = sheet.getRow(5);
//            row5.createCell(8).setCellValue(search.getSending());
//            logger.info("print search data cell", search);
//        }

        // createAt
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String time = simpleDateFormat.format(date);
        fileName.replaceAll(".xls", time + ".xls");


        //lets write the excel data to file now
        File createFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(createFile);
        workbook.write(fos);
        fos.close();
        System.out.println(fileName + " written successfully");

        //파일생성 바이트로 변환해 리턴
        byte[] fileBytes = Files.readAllBytes(createFile.toPath());

        return fileBytes;
    }

}
