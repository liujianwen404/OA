package com.ruoyi.base.domain.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

import java.util.Date;

/**
 * 日期运算对象
 */
@Data
@ToString
@NoArgsConstructor
public class DateOperation {

    /**
     * 原始开始和结束
     */
    private Date originalStart;
    private Date originalEnd;
    //休息时间
    private Date originalRestStartDate;
    private Date originalRestEndDate;

    //班次临界点时间,上班前
    private Date shiftCriticalPointStartDate;
    //班次临界点时间，下班后
    private Date shiftCriticalPointEndDate;
    //班次日前
    private Date attendanceDay;
    //是否为公休日
    private Boolean isPublic;

    /**
     * 当前开始和结束
     */
    private Date startTime;
    private Date endTime;

    public DateOperation(Date originalStart, Date originalEnd, Date originalRestStartDate, Date originalRestEndDate, Date startTime, Date endTime) {
        this.originalStart = originalStart;
        this.originalEnd = originalEnd;
        this.originalRestStartDate = originalRestStartDate;
        this.originalRestEndDate = originalRestEndDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
