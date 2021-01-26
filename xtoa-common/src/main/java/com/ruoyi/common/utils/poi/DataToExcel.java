package com.ruoyi.common.utils.poi;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 将List<Map<String,Object>> 类型数据导出到Excel文件
 */
@Slf4j
public class DataToExcel {

    public AjaxResult createQuitExcel(List<Map<Integer, String>> list, String sheetName) {
        OutputStream out = null;
        try {
            // 定义一个新的工作簿
            XSSFWorkbook wb = new XSSFWorkbook();
            // 创建一个Sheet页
            XSSFSheet sheet = wb.createSheet("First sheet");
            //设置行高
            sheet.setDefaultRowHeight((short) (2 * 256));
            //设置列宽
            sheet.setColumnWidth(0, 4000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 4000);
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 16);
            //获得表格第一行
            XSSFRow row = sheet.createRow(0);
            //根据需要给第一行每一列设置标题
            XSSFCell cell = row.createCell(0);
            cell.setCellValue("姓名");
            cell = row.createCell(1);
            cell.setCellValue("工号");
            cell = row.createCell(2);
            cell.setCellValue("部门");
            cell = row.createCell(3);
            cell.setCellValue("岗位");
            cell = row.createCell(4);
            cell.setCellValue("入职时间");
            cell = row.createCell(5);
            cell.setCellValue("离职时间");
            cell = row.createCell(6);
            cell.setCellValue("在职天数");
            XSSFRow rows;
            XSSFCell cells;
            //循环拿到的数据给所有行每一列设置对应的值
            for (int i = 0; i < list.size(); i++) {
                // 在这个sheet页里创建一行
                rows = sheet.createRow(i + 1);
                // 该行创建一个单元格,在该单元格里设置值
                String empName = list.get(i).get("emp_name")==null?"":list.get(i).get("emp_name");
                String empNum = list.get(i).get("emp_num")==null?"":list.get(i).get("emp_num");
                String deptName = list.get(i).get("dept_name")==null?"":list.get(i).get("dept_name");
                String postName = list.get(i).get("post_name")==null?"":list.get(i).get("post_name");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String nonManagerDate = list.get(i).get("non_manager_date") == null ? "" :  sdf.format(list.get(i).get("non_manager_date"));
                String quitTime = list.get(i).get("quit_time") == null ? "" :sdf.format(list.get(i).get("quit_time"));
                String nums = String.valueOf(list.get(i).get("nums")==null?"":list.get(i).get("nums"));
                cells = rows.createCell(0);
                cells.setCellValue(empName);
                cells = rows.createCell(1);
                cells.setCellValue(empNum);
                cells = rows.createCell(2);
                cells.setCellValue(deptName);
                cells = rows.createCell(3);
                cells.setCellValue(postName);
                cells = rows.createCell(4);
                cells.setCellValue(nonManagerDate);
                cells = rows.createCell(5);
                cells.setCellValue(quitTime);
                cells = rows.createCell(6);
                cells.setCellValue(nums);
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        } catch (IOException e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
        finally {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename)
    {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = Global.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

}