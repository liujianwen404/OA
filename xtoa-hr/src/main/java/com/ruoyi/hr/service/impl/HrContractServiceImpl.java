package com.ruoyi.hr.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.ruoyi.base.domain.HrContract;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrContractMapper;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.IHrContractService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.utils.SpringUtil;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 劳动合同Service业务层处理
 * 
 * @author xt
 * @date 2020-06-17
 */
@Service
public class HrContractServiceImpl implements IHrContractService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrContractServiceImpl.class);

    @Autowired
    private HrContractMapper hrContractMapper;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询劳动合同
     * 
     * @param id 劳动合同ID
     * @return 劳动合同
     */
    @Override
    public HrContract selectHrContractById(Long id)
    {

        HrContract hrContract = hrContractMapper.selectByPrimaryKey(id);
        if (hrContract != null){
            HrEmp hrEmp = hrEmpService.selectHrEmpInfoByIdandFromHistory(hrContract.getEmpId());
            if (hrEmp != null){
                hrContract.setEmpName(hrEmp.getEmpName());
            }
        }

        return hrContract;
    }


    @Override
    public List<HrContract> selectHrContractByEmpIdForCount(Long empid)
    {
        Example example = new Example(HrContract.class);
        example.createCriteria().andEqualTo("empId",empid)
                .andEqualTo("delFlag","0").andEqualTo("auditStatus",2);
        example.setOrderByClause(" renew_count asc ");
        return hrContractMapper.selectByExample(example);
    }

    /**
     * 查询劳动合同列表
     * 
     * @param hrContract 劳动合同
     * @return 劳动合同
     */
    @Override
    public List<HrContract> selectHrContractList(HrContract hrContract)
    {
        hrContract.setDelFlag("0");
        List<HrContract> hrContracts = hrContractMapper.selectHrContractList(hrContract);
        Date date = new Date();
        for (HrContract contract : hrContracts) {
            HrEmp hrEmp = hrEmpService.selectHrEmpInfoByIdandFromHistory(contract.getEmpId());
            if (hrEmp != null){
                contract.setEmpName(hrEmp.getEmpName());
            }

            contract.setTodoUserName(SpringUtil.getHandleNmae(contract.getInstanceId(), contract.getAuditStatus()));
            contract.setOverdueStr("");
            if (contract.getContractEnd() != null){

                if (DateUtil.offsetDay(date,30).isAfterOrEquals(contract.getContractEnd())){
                    contract.setOverdueStr("近30天到期");
                }
                if (DateUtil.date(date).isAfterOrEquals(contract.getContractEnd())){
                    contract.setOverdueStr("已到期");
                }
            }

        }
        return hrContracts;
    }

    /**
     * 新增劳动合同
     * 
     * @param hrContract 劳动合同
     * @return 结果
     */
    @Override
    public int insertHrContract(HrContract hrContract)
    {
        hrContract.setCreateId(ShiroUtils.getUserId());
        hrContract.setCreateBy(ShiroUtils.getLoginName());
        hrContract.setCreateTime(DateUtils.getNowDate());
        return hrContractMapper.insertSelective(hrContract);
    }

    /**
     * 修改劳动合同
     * 
     * @param hrContract 劳动合同
     * @return 结果
     */
    @Override
    public int updateHrContract(HrContract hrContract)
    {
        hrContract.setUpdateId(ShiroUtils.getUserId());
        hrContract.setUpdateBy(ShiroUtils.getLoginName());
        hrContract.setUpdateTime(DateUtils.getNowDate());
        return hrContractMapper.updateByPrimaryKeySelective(hrContract);
    }

    /**
     * 修改劳动合同
     *
     * @param hrContract 劳动合同
     * @return 结果
     */
    @Override
    public int updateContract(HrContract hrContract)
    {
        hrContract.setUpdateId(ShiroUtils.getUserId());
        hrContract.setUpdateBy(ShiroUtils.getLoginName());
        hrContract.setUpdateTime(DateUtils.getNowDate());
        return hrContractMapper.updateByPrimaryKey(hrContract);
    }

    /**
     * 删除劳动合同对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrContractByIds(String ids)
    {
        return hrContractMapper.deleteHrContractByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除劳动合同信息
     * 
     * @param id 劳动合同ID
     * @return 结果
     */
    @Override
    public int deleteHrContractById(Long id)
    {
        return hrContractMapper.deleteHrContractById(id);
    }

    @Override
    @Transactional
    public AjaxResult submitApply(HrContract hrContract, String applyUserId) {
        hrContract.setUpdateBy(ShiroUtils.getLoginName());
        hrContract.setUpdateId(ShiroUtils.getUserId());
        //修改流程状态为审核中
        hrContract.setAuditStatus(1);
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrContract.getId().toString();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("empApp",sysUserService.selectUserById(hrContract.getEmpId()).getLoginName());
        // 建立双向关系
        hrContract.setInstanceId(processHandleService.submitApply(ProcessKey.contract,businessKey,ShiroUtils.getLoginName(),"劳动合同",variables).getProcessInstanceId());
        hrContractMapper.updateByPrimaryKey(hrContract);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult complete(HrContract hrContract, String taskId, HttpServletRequest request) throws IOException {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        hrContract = hrContractMapper.selectByPrimaryKey(hrContract.getId());
        Map<String, Object> variables = new HashMap<String, Object>();
        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean appType = BooleanUtils.toBoolean(p_B_approved);

        if (task.getTaskDefinitionKey().equals("empApp")) {
           if (appType){
               variables.put("hrApp",hrContract.getCreateBy());
               variables.put("type",1);
           }else {
               variables.put("type",0);
               hrContract.setAuditStatus(3);
               hrContractMapper.updateByPrimaryKeySelective(hrContract);
           }
        }else if (task.getTaskDefinitionKey().equals("hrApp")){
            if (appType){
                hrContract.setAuditStatus(2);
            }else {
                hrContract.setAuditStatus(3);
            }
            hrContractMapper.updateByPrimaryKeySelective(hrContract);
        }

        int complete = processHandleService.complete(hrContract.getInstanceId(), ProcessKey.contract, "劳动合同", taskId, variables,
                hrContract.getAuditStatus() == 2, comment, appType);
        if (complete <= 0){
            hrContract = hrContractMapper.selectByPrimaryKey(hrContract.getId());
            if (hrContract.getAuditStatus() == 2){
                List<HrContract> hrContracts = selectHrContractByEmpIdForCount(hrContract.getEmpId());
                if (hrContracts == null || hrContracts.isEmpty()){
                    //合同首签
                    hrContract.setRenewCount(0);
                }else {
                    hrContract.setRenewCount(hrContracts.size());
                }

                // 本地资源路径
                String localPath = Global.getProfile();
                // 数据库资源地址
                String downloadPath = localPath + StringUtils.substringAfter(hrContract.getContractUrl(), Constants.RESOURCE_PREFIX);
                downloadPath = downloadPath.replaceAll("/","\\\\");
                BufferedImage bufferedImage = ImgUtil.read(getClass().getClassLoader().getResourceAsStream("static/logo.png"));
//                BufferedImage bufferedImage = ImgUtil.read(FileUtil.file("static/logo.png"));
                System.out.println("MinX:" +  bufferedImage.getMinX() + "MinY:" +  bufferedImage.getMinY());
                ImgUtil.pressImage(
                        FileUtil.file(downloadPath),
                        FileUtil.file(downloadPath.replace(".png","_ok.png")),
                        bufferedImage, //水印图片
                        100, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                        100, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                        0.1f
                );
                hrContract.setContractUrl(hrContract.getContractUrl().replace(".png","_ok.png"));
                hrContractMapper.updateByPrimaryKeySelective(hrContract);



            }
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrQuit.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrContract hrContract = hrContractMapper.selectSingleOneByExample(example);
        hrContract.setAuditStatus(4);
        hrContractMapper.updateByPrimaryKeySelective(hrContract);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    public List<HrContract> selectHrContractListIsFrom(HrContract hrContract) {

        hrContract.setDelFlag("0");
        Date date = new Date();
        List<HrContract> hrContracts = hrContractMapper.selectHrContractListIsFrom(hrContract);
        for (HrContract contract : hrContracts) {
            HrEmp hrEmp = hrEmpService.selectHrEmpInfoByIdandFromHistory(contract.getEmpId());
            if (hrEmp != null){
                contract.setEmpName(hrEmp.getEmpName());
            }

            contract.setOverdueStr("");
            if (contract.getContractEnd() != null){

                if (DateUtil.offsetDay(date,30).isAfterOrEquals(contract.getContractEnd())){
                    contract.setOverdueStr("近30天到期");
                }
                if (DateUtil.date(date).isAfterOrEquals(contract.getContractEnd())){
                    contract.setOverdueStr("已到期");
                }
            }


        }
        return hrContracts;
    }
}
