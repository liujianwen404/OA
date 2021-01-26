package com.ruoyi.hr.utils;

import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.domain.DTO.*;
import com.ruoyi.hr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ImportAttendanceUtil {

    @Autowired
    private IHrLeaveService hrLeaveService;

    @Autowired
    private IHrOvertimeService overtimeService;

    @Autowired
    private IHrFillClockService clockService;

    @Autowired
    private IHrGooutService gooutService;

    @Autowired
    private IHrBusinessTripService businessService;

    /**
     * 导入请假数据
     *
     * @return 结果
     */
    @Transactional
    public String importLeaveAttendance(List<HrLeaveDTO> list)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new BusinessException("请添加请假申请数据！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i=0 ; i<list.size(); i++)
        {
            try
            {
                HrLeaveDTO hrLeaveDTO = list.get(i);
                if (hrLeaveDTO != null) {
                    int num = hrLeaveService.insertHrLeaveDTO(hrLeaveDTO);
                    if(num>0){
                        successNum++;
                        successMsg.append("<br/>" + successNum + hrLeaveDTO.getApplyUserName() + "请假申请： " + hrLeaveDTO.getTitle() + " 导入成功");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                failureNum++;
                String msg = "<br/>" + failureNum + " 请假申请数据导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        successMsg.insert(0, "成功导入 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }

    /**
     * 导入加班数据
     *
     * @return 结果
     */
    @Transactional
    public String importOverTimeAttendance(List<OverTimeDTO> list)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new BusinessException("请添加加班申请数据！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i=0 ; i<list.size(); i++)
        {
            try
            {
                OverTimeDTO overTimeDTO = list.get(i);
                if (overTimeDTO != null) {
                    int num = overtimeService.insertOvertimeDTO(overTimeDTO);
                    if(num>0){
                        successNum++;
                        successMsg.append("<br/>" + successNum + overTimeDTO.getApplyUserName() + "加班申请： " + overTimeDTO.getTitle() + " 导入成功");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                failureNum++;
                String msg = "<br/>" + failureNum + " 加班申请数据导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        successMsg.insert(0, "成功导入 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }

    /**
     * 导入补卡数据
     *
     * @return 结果
     */
    @Transactional
    public String importFillClockAttendance(List<FillClockDTO> list)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new BusinessException("请添加补卡申请数据！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i=0 ; i<list.size(); i++)
        {
            try
            {
                FillClockDTO fillClockDTO = list.get(i);
                if (fillClockDTO != null) {
                    int num = clockService.insertFillClockDTO(fillClockDTO);
                    if(num>0){
                        successNum++;
                        successMsg.append("<br/>" + successNum + fillClockDTO.getApplyUserName() + "补卡申请： " + fillClockDTO.getTitle() + " 导入成功");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                failureNum++;
                String msg = "<br/>" + failureNum + " 补卡申请数据导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        successMsg.insert(0, "成功导入 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }

    /**
     * 导入外出数据
     *
     * @return 结果
     */
    @Transactional
    public String importGooutAttendance(List<GooutDTO> list)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new BusinessException("请添加外出申请数据！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i=0 ; i<list.size(); i++)
        {
            try
            {
                GooutDTO gooutDTO = list.get(i);
                if (gooutDTO != null) {
                    int num = gooutService.insertGooutDTO(gooutDTO);
                    if(num>0){
                        successNum++;
                        successMsg.append("<br/>" + successNum + gooutDTO.getApplyUserName() + "外出申请： " + gooutDTO.getTitle() + " 导入成功");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                failureNum++;
                String msg = "<br/>" + failureNum + " 外出申请数据导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        successMsg.insert(0, "成功导入 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }

    /**
     * 导入出差数据
     *
     * @return 结果
     */
    @Transactional
    public String importBusinessTripAttendance(List<BusinessTripDTO> list)
    {
        if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new BusinessException("请添加出差申请数据！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i=0 ; i<list.size(); i++)
        {
            try
            {
                BusinessTripDTO businessTripDTO = list.get(i);
                if (businessTripDTO != null) {
                    int num = businessService.insertBusinessTripDTO(businessTripDTO);
                    if(num>0){
                        successNum++;
                        successMsg.append("<br/>" + successNum + businessTripDTO.getApplyUserName() + "出差申请： " + businessTripDTO.getTitle() + " 导入成功");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                failureNum++;
                String msg = "<br/>" + failureNum + " 出差申请数据导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        successMsg.insert(0, "成功导入 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }


}
