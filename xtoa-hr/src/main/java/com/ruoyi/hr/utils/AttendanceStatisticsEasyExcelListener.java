package com.ruoyi.hr.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.fastjson.JSON;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTO;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTOError;
import com.ruoyi.hr.service.IHrAttendanceStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AttendanceStatisticsEasyExcelListener extends AnalysisEventListener<AttendanceStatisticsDTO> {


    public static String redisKey = "redisKey_AttendanceStaticsExcelListener";
    public static String redisKeyData = "redisKey_AttendanceStaticsExcelListener_data";

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IHrAttendanceStatisticsService hrAttendanceStatisticsService;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceStatisticsEasyExcelListener.class);


    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param hrAttendanceStatisticsService
     */
    public AttendanceStatisticsEasyExcelListener(IHrAttendanceStatisticsService hrAttendanceStatisticsService,RedisTemplate<String, Object> redisTemplate) {
        this.hrAttendanceStatisticsService = hrAttendanceStatisticsService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<AttendanceStatisticsDTO> list = new ArrayList<>();
    
    AtomicInteger successCount = new AtomicInteger(0);

    List<AttendanceStatisticsDTOError> errorList = new ArrayList<>();

    @Override
    public void invoke(AttendanceStatisticsDTO data, AnalysisContext context) {
        System.out.println("解析到一条数据:{ "+ data.toString() +" }");
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


        try {
            //删除导入标签
            redisTemplate.delete(redisKey);

            ImportInfoEntity<AttendanceStatisticsDTOError> importInfoEntity = new ImportInfoEntity();
            importInfoEntity.setImportDate(new Date());
            importInfoEntity.setImportInfo(StrUtil.format("导入成功{}条数据，失败{}条数据",successCount.intValue(),errorList.size()));
            importInfoEntity.setImportInfoList(errorList);
            redisTemplate.opsForZSet().add(redisKeyData,importInfoEntity,importInfoEntity.getImportDate().getTime());
            if (redisTemplate.opsForZSet().size(redisKeyData) > 5) {
                //数量大一五条删除多余的数据

                Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().rangeWithScores(redisKeyData,0,-1);
                Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
                while (iterator.hasNext())
                {
                    ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
                    System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
                    redisTemplate.opsForZSet().removeRangeByScore(redisKeyData,typedTuple.getScore().longValue(),typedTuple.getScore().longValue());
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(List<AttendanceStatisticsDTO> list) throws ParseException {
        System.out.println("开始存储数据库！");
        for (int i=0 ; i<list.size(); i++) {
            AttendanceStatisticsDTO attendanceStatisticsDTO = list.get(i);
            if (attendanceStatisticsDTO != null) {
                hrAttendanceStatisticsService.insertAttendanceStatistics(attendanceStatisticsDTO,successCount,errorList);
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
                    excelDataConvertException.getColumnIndex()); String errorInfo = StrUtil.format("第{}行解析异常:", excelDataConvertException.getRowIndex() );


            //获取错误的行数据
            LinkedHashMap<Integer,CellData> currentRowAnalysisResult = (LinkedHashMap<Integer,CellData>) context.readRowHolder().getCurrentRowAnalysisResult();
            //获取错误的行数据对应的字段数据
            Map<Integer, ExcelContentProperty> contentPropertyMap = context.currentReadHolder().excelReadHeadProperty().getContentPropertyMap();

            //遍历并封装到对象中去
            ExcelContentProperty excelContentProperty = null;
            AttendanceStatisticsDTOError attendanceStatisticsDTOError = new AttendanceStatisticsDTOError();
            Field field = null;
            for (Map.Entry<Integer, CellData> integerCellDataEntry : currentRowAnalysisResult.entrySet()) {
                try {
                    excelContentProperty = contentPropertyMap.get(integerCellDataEntry.getKey());
                    field = excelContentProperty.getField();
                    field.setAccessible(true);
                    if (Date.class.isAssignableFrom(field.getType())){
                        field.set(attendanceStatisticsDTOError,DateUtil.parse(integerCellDataEntry.getValue().getStringValue()));
                    }else if (Integer.class.isAssignableFrom(field.getType())){
                        field.set(attendanceStatisticsDTOError,Integer.parseInt(integerCellDataEntry.getValue().getStringValue()));
                    }else if (Double.class.isAssignableFrom(field.getType())){
                        field.set(attendanceStatisticsDTOError,integerCellDataEntry.getValue().getNumberValue().doubleValue());
                    }else if (BigDecimal.class.isAssignableFrom(field.getType())){
                        field.set(attendanceStatisticsDTOError,integerCellDataEntry.getValue().getNumberValue());
                    }else if (Number.class.isAssignableFrom(field.getType())){
                        field.set(attendanceStatisticsDTOError,Long.parseLong(integerCellDataEntry.getValue().getNumberValue()+""));
                    }else {
                        field.set(attendanceStatisticsDTOError,integerCellDataEntry.getValue().getStringValue());
                    }
                } catch (Exception e) {
                    errorInfo = errorInfo + " - " + integerCellDataEntry.getValue().getStringValue();
                    logger.info(errorInfo);
                }
            }
            //数据解析错误不能回显数据
            attendanceStatisticsDTOError.setErrorInfo(errorInfo);
            errorList.add(attendanceStatisticsDTOError);
            
        }
    }
}
