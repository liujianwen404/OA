package com.ruoyi.hr.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import com.ruoyi.base.domain.DTO.AttendanceExtraDTO;
import com.ruoyi.hr.service.IHrAttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AttendanceExtraEasyExcelListener extends AnalysisEventListener<AttendanceExtraDTO> {

    @Autowired
    private IHrAttendanceService hrAttendanceService;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceExtraEasyExcelListener.class);


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param hrAttendanceService
     */
    public AttendanceExtraEasyExcelListener(IHrAttendanceService hrAttendanceService) {
        this.hrAttendanceService = hrAttendanceService;
    }

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<AttendanceExtraDTO> list = new ArrayList<>();

    @Override
    public void invoke(AttendanceExtraDTO data, AnalysisContext context) {
        logger.info("解析到一条数据:{ "+ data.toString() +" }");
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            try {
                saveData( list );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.clear();
        }
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (EasyExcelHead.HEAD.get() == null) {
            String s = headMap.get(0);
            logger.info("解析到一条头数据:{}", JSON.toJSONString(headMap));
            EasyExcelHead.HEAD.set(s);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        try {
            saveData( list );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(List<AttendanceExtraDTO> list) throws ParseException {
        logger.info("开始存储数据库！");
        for (int i=0 ; i<list.size(); i++) {
            AttendanceExtraDTO attendanceExtraDTO = list.get(i);
            if (attendanceExtraDTO != null) {
                hrAttendanceService.insertAttendanceExtra(attendanceExtraDTO,EasyExcelHead.HEAD.get());
            }
        }
        logger.info("存储数据库成功！");
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        logger.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            logger.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
        }
    }
}
