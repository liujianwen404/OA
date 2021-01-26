package com.ruoyi.hr.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.ruoyi.base.domain.DTO.OverTimeDTO;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.base.domain.HrAttendanceGroupSon;
import com.ruoyi.hr.service.IHrAttendanceGroupSonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AttendanceGroupSonEasyExcelListener extends AnalysisEventListener<HrAttendanceGroupSon> {

    private IHrAttendanceGroupSonService hrAttendanceGroupSonService;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceGroupSonEasyExcelListener.class);


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param hrAttendanceGroupSonService
     */
    public AttendanceGroupSonEasyExcelListener(IHrAttendanceGroupSonService hrAttendanceGroupSonService) {
        this.hrAttendanceGroupSonService = hrAttendanceGroupSonService;
    }

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<HrAttendanceGroupSon> list = new ArrayList<>();

    @Override
    public void invoke(HrAttendanceGroupSon data, AnalysisContext context) {
        System.out.println("解析到一条数据:{ "+ data.toString() +" }");
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData( list );
            list.clear();
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData( list );
        System.out.println("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(List<HrAttendanceGroupSon> list) {
        System.out.println("开始存储数据库！");
        for (int i=0 ; i<list.size(); i++) {
            HrAttendanceGroupSon son = list.get(i);
            if (son != null) {
                hrAttendanceGroupSonService.importHrAttendanceGroupSon(son);
            }
        }
        System.out.println("存储数据库成功！");
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
