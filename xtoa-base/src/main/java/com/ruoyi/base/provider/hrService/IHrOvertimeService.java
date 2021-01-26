package com.ruoyi.base.provider.hrService;

import java.util.Date;
import java.util.List;

import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.DTO.OverTimeDTO;
import com.ruoyi.base.domain.HrOvertime;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;

/**
 * 加班申请Service接口
 * 
 * @author liujianwen
 * @date 2020-06-11
 */
public interface IHrOvertimeService 
{
    /**
     * 查询加班申请
     * 
     * @param id 加班申请ID
     * @return 加班申请
     */
    public HrOvertime selectHrOvertimeById(Long id);

    /**
     * 查询加班申请列表
     * 
     * @param hrOvertime 加班申请
     * @return 加班申请集合
     */
    public List<HrOvertime> selectHrOvertimeList(HrOvertime hrOvertime);

    /**
     * 新增加班申请
     * 
     * @param hrOvertime 加班申请
     * @return 结果
     */
    public int insertHrOvertime(HrOvertime hrOvertime);

    void saveHoliday(HrOvertime hrOvertime);

    /**
     * 获取加班时长
     * @param endTime
     * @param startTime
     */
    Double getHours(Date endTime, Date startTime);

    public int insertOvertimeDTO(OverTimeDTO overTimeDTO);


    /**
     * 修改加班申请
     * 
     * @param hrOvertime 加班申请
     * @return 结果
     */
    public int updateHrOvertime(HrOvertime hrOvertime);

    /**
     * 批量删除加班申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrOvertimeByIds(String ids);

    /**
     * 删除加班申请信息
     * 
     * @param id 加班申请ID
     * @return 结果
     */
    public int deleteHrOvertimeById(Long id);

    /**
     * 启动流程
     * @param hrOvertime
     * @param applyUserId
     * @return
     */
    AjaxResult submitApply(HrOvertime hrOvertime, String applyUserId);

    /**
     * 审批流程
     * @param hrOvertime
     * @param taskId
     * @param request
     */
    AjaxResult complete(HrOvertime hrOvertime, String taskId, HttpServletRequest request);

    /**
     * 撤销流程
     * @param instanceId
     * @param message
     * @param request
     */
    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    /**
     * 获取加班时长
     * @param request
     */
    AjaxResult getOverTimes(HttpServletRequest request);

    HrOvertime selectSingleOneByExample(Example example);

    List<HrOvertime> selectByExample(Example example);

    Double selectOvertimeByType(Long empId,int year,int month,String i);

    List<HrOvertime> selectOvertimeManageList(HrOvertime hrOvertime);
}
