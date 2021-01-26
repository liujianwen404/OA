package com.ruoyi.hr.service.impl;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.hr.domain.HrInterpolate;
import com.ruoyi.base.domain.VO.UserVO;
import com.ruoyi.hr.mapper.HrInterpolateMapper;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.HrRecruitService;
import com.ruoyi.hr.service.IHrInterpolateService;
import com.ruoyi.process.general.domain.HistoricActivity;
import com.ruoyi.process.general.service.IProcessService;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 内推申请Service业务层处理
 * 
 * @author vivi07
 * @date 2020-05-12
 */
@Service
public class HrInterpolateServiceImpl implements IHrInterpolateService 
{
    private static final Logger logger = LoggerFactory.getLogger(HrInterpolateServiceImpl.class);

    @Autowired
    private HrInterpolateMapper hrInterpolateMapper;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysPostService iSysPostService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IProcessService processService;

    @Autowired
    private IBizTodoItemService iBizTodoItemService;

    @Autowired
    private HrRecruitService hrRecruitService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IBizTodoItemService bizTodoItemService;


    /**
     * 查询内推申请
     * 
     * @param interpolateId 内推申请ID
     * @return 内推申请
     */
    @Override
    public HrInterpolate selectHrInterpolateById(Long interpolateId)
    {
        return hrInterpolateMapper.selectByPrimaryKey(interpolateId);
    }

    /**
     * 查询内推申请列表
     * 
     * @param hrInterpolate 内推申请
     * @return 内推申请
     */
    @Override
    public List<HrInterpolate> selectHrInterpolateList(HrInterpolate hrInterpolate)
    {
        hrInterpolate.setDelFlag("0");
        SysUser sysUser = ShiroUtils.getSysUser();
        Integer hrFlag = sysUser.hrFlag();
        Long userId = sysUser.getUserId();

        return hrInterpolateMapper.selectHrInterpolateList(hrInterpolate,hrFlag,sysUser.getDeptId(),userId);
    }

    /**
     * 新增内推申请
     * 
     * @param hrInterpolate 内推申请
     * @param multipart
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertHrInterpolate(HrInterpolate hrInterpolate, MultipartFile multipart) throws IOException {

        String auditId = hrInterpolate.getAuditId();
        if (StringUtils.isEmpty(auditId) || !iSysUserService.checkUserIsOK(Long.parseLong(auditId))){
            return AjaxResult.error("审核人不可用，请重新指定审核人");
        }

        HrEmp hrEmp = hrEmpService.selectTHrEmpByPhonenumber(hrInterpolate.getPhonenumber());
        if (hrEmp != null){
            return AjaxResult.error("手机号已存在！");
        }

        String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrInterpolate", multipart);
        hrInterpolate.setResume(fileName);
        hrInterpolate.setCreateId(ShiroUtils.getUserId());
        hrInterpolate.setCreateBy(ShiroUtils.getLoginName());
        hrInterpolate.setCreateTime(DateUtils.getNowDate());
        logger.info("新增内推申请:"+ hrInterpolate.toString());
        int i = hrInterpolateMapper.insertSelective(hrInterpolate);
        if (i > 0 ){

            identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
            HashMap<String, Object> variables = new HashMap<>();
            SysUser sysUser = iSysUserService.selectUserById(Long.parseLong(auditId));
            variables.put("deptLeader",sysUser.getLoginName());
            // 启动流程时设置业务 key
            ProcessInstance processInstance = runtimeService
                    .startProcessInstanceByKey(ProcessKey.interpolate, hrInterpolate.getInterpolateId()+"",variables);

            //更新业务住数据
            hrInterpolate.setInstanceId(processInstance.getProcessInstanceId());
            hrInterpolate.setAuditStatus("1");
            hrInterpolateMapper.updateByPrimaryKeySelective(hrInterpolate);

            //记录代办表
            iBizTodoItemService.insertTodoItem(hrInterpolate.getInstanceId(),"内推申请",ProcessKey.interpolate,processInstance.getStartUserId());

            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 修改内推申请
     * 
     * @param hrInterpolate 内推申请
     * @return 结果
     */
    @Override
    @Transactional
    public int updateHrInterpolate(HrInterpolate hrInterpolate)
    {
        hrInterpolate.setUpdateTime(DateUtils.getNowDate());
        hrInterpolate.setUpdateId(ShiroUtils.getUserId());
        hrInterpolate.setUpdateBy(ShiroUtils.getLoginName());
        return hrInterpolateMapper.updateByPrimaryKeySelective(hrInterpolate);
    }

    /**
     * 删除内推申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteHrInterpolateByIds(String ids)
    {
        return hrInterpolateMapper.deleteHrInterpolateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除内推申请信息
     * 
     * @param interpolateId 内推申请ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteHrInterpolateById(Long interpolateId)
    {
        return hrInterpolateMapper.deleteHrInterpolateById(interpolateId);
    }

    @Override
    public UserVO getInfoForAdd() {
        SysUser sysUser = ShiroUtils.getSysUser();
        UserVO userVO = new UserVO();
        userVO.setLoginName(sysUser.getLoginName());
        userVO.setUserName(sysUser.getUserName());

        userVO.setDeptName(sysUser.getDept().getDeptName());
        userVO.setDeptId(sysUser.getDept().getDeptId());
        HrEmp hrEmp = hrEmpService.selectTHrEmpByUserId(sysUser.getUserId());
        if (hrEmp != null){
            userVO.setNonManagerDate(hrEmp.getNonManagerDate() == null ? "" : DateUtils.dateTime(hrEmp.getNonManagerDate()));
        }

        SysDept sysDept = iSysDeptService.selectDeptById(sysUser.getDept().getParentId());
        if (sysDept != null){
            userVO.setParentDeptName(sysDept.getDeptName());
        }

        List<SysPost> sysPosts = iSysPostService.selectPostsByUserId(sysUser.getUserId());
        String postNmae = "";
        for (SysPost sysPost : sysPosts) {
            postNmae = sysPost.getPostName() + ",";
        }
        userVO.setPostNmae(postNmae);
        return userVO;
    }

    @Override
    public void showVerifyDialog(String taskId, String module, String activitiId, ModelMap mmap, String instanceId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String businessKey ;
        String startUserId ;
        if (task == null){
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            businessKey = historicProcessInstance.getBusinessKey();
            startUserId = historicProcessInstance.getStartUserId();
        }else {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKey = processInstance.getBusinessKey();
            startUserId = processInstance.getStartUserId();
        }
        HrInterpolate hrInterpolate = hrInterpolateMapper.selectByPrimaryKey(new Long(businessKey));

        hrInterpolate.setApplyUserName(startUserId);
        hrInterpolate.setTaskId(taskId);
        mmap.put("entity", hrInterpolate);
        mmap.put("taskId", taskId);

        List<HistoricActivity> list = processService.selectHistoryList(instanceId, new HistoricActivity());
        mmap.put("historicActivity", list);

        UserVO userVO = getInfoForAdd();
        mmap.put("userVO",userVO);
    }

    @Override
    @Transactional
    public AjaxResult complete(String taskId, HttpServletRequest request, HrInterpolate hrInterpolate, String comment, String p_b_hrApproved) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String assignee = task.getAssignee();
        hrInterpolate = hrInterpolateMapper.selectByPrimaryKey(hrInterpolate.getInterpolateId());
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        Boolean isOk = true;
        if (!"true".equals(p_b_hrApproved)) {
            //不同意
            hrInterpolate.setAuditStatus("3");
            isOk = false;
        }

        taskService.claim(taskId, ShiroUtils.getLoginName());
        if (StringUtils.isNotEmpty(comment)) {
            taskService.addComment(taskId, hrInterpolate.getInstanceId(), comment);
        }
        taskService.complete(taskId);

        Long todoId = bizTodoItemService.updateTodoItem(taskId,"true".equals(p_b_hrApproved) ? "0" : "1");


        int item = iBizTodoItemService.insertTodoItem(hrInterpolate.getInstanceId(), "内推申请", ProcessKey.interpolate,isOk,todoId,processInstance.getStartUserId());

        if (item == 0) {
            //流程已经结束
            if (!hrInterpolate.getAuditStatus().equals("3")) {
                hrInterpolate.setAuditStatus("2");
            }
            hrInterpolateMapper.updateByPrimaryKeySelective(hrInterpolate);
            if (hrInterpolate.getAuditStatus().equals("2")){
                //维护招聘需求数据
                hrRecruitService.updateRecruitCountByHrInterpolate(hrInterpolate);
            }
        }
        return AjaxResult.success("任务已完成");
    }


}
