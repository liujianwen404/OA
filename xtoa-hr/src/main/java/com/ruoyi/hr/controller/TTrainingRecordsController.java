package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.VO.TTrainingRecordsTemplate;
import com.ruoyi.common.enums.ExamFlagEnum;
import com.ruoyi.common.enums.TeachingTypeEnum;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.mapper.TTrainingRecordsMapper;
import com.ruoyi.system.domain.SysUser;
import java.util.List;
import java.util.Objects;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.TTrainingRecords;
import com.ruoyi.hr.service.ITTrainingRecordsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 培训档案Controller
 * 
 * @author xt
 * @date 2021-01-04
 */
@Controller
@RequestMapping("/hr/records")
public class TTrainingRecordsController extends BaseController
{
    private String prefix = "hr/records";

    @Resource
    private ITTrainingRecordsService tTrainingRecordsService;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private TTrainingRecordsMapper tTrainingRecordsMapper;


    @RequiresPermissions("hr:records:view")
    @GetMapping()
    public String records()
    {
        return prefix + "/records";
    }

    /**
     * 查询培训档案列表
     */
    @RequiresPermissions("hr:records:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TTrainingRecords tTrainingRecords)
    {
        startPageBy();
        List<TTrainingRecords> list = tTrainingRecordsService.selectTTrainingRecordsList(tTrainingRecords);
        return getDataTableBy(list);
    }


    /**
     * 下载模板
     *
     */
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<TTrainingRecordsTemplate> util = new ExcelUtil<>(TTrainingRecordsTemplate.class);
        return util.importTemplateExcel("培训档案导入模板");
    }


    /**
     * 导入培训档案数据
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<TTrainingRecordsTemplate> util = new ExcelUtil<>(TTrainingRecordsTemplate.class);
        List<TTrainingRecordsTemplate> userList = util.importExcel(file.getInputStream());
        String message = importTrainingRecords(userList);
        return AjaxResult.success(message);
    }

    /**
     * 导入
     * @param trainingRecordsList
     * @return
     */
    public String importTrainingRecords(List<TTrainingRecordsTemplate> trainingRecordsList)
    {
        if (StringUtils.isNull(trainingRecordsList) || trainingRecordsList.size() == 0)
        {
            throw new BusinessException("导入档案数据不能为空！");
        }
        HrEmp hrEmpInfo = null;
        TTrainingRecords tTrainingRecords=null;
        for (TTrainingRecordsTemplate tTrainingRecordsTemplate : trainingRecordsList) {
            if (StringUtils.isBlank(tTrainingRecordsTemplate.getEmpNum())) {
                throw new BusinessException("员工工号不能为空,请检查数据");
            }
            //获取员工基本信息
            hrEmpInfo = hrEmpMapper.selectHrEmpByEmpNum(tTrainingRecordsTemplate.getEmpNum());
            if (Objects.nonNull(hrEmpInfo)) {
                if (StringUtils.isBlank(tTrainingRecordsTemplate.getExamFlagName())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】是否考试不能为空");
                }

                if (StringUtils.isBlank(tTrainingRecordsTemplate.getPassFlagName())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】是否通过不能为空");
                }

                if (StringUtils.isBlank(tTrainingRecordsTemplate.getTrainingContent())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训内容不能为空");
                }
                if (StringUtils.isBlank(tTrainingRecordsTemplate.getTrainingSite())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训地点不能为空");
                }
                if (StringUtils.isBlank(tTrainingRecordsTemplate.getTrainingTypeName())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训方式不能为空");
                }

                if (Objects.isNull(tTrainingRecordsTemplate.getTrainingTime())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训时长不能为空");
                }

                if (Objects.isNull(tTrainingRecordsTemplate.getTrainingScore())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训成绩不能为空");
                }

                if (Objects.isNull(tTrainingRecordsTemplate.getTrainingStartTime()) || Objects.isNull(tTrainingRecordsTemplate.getTrainingEndTime())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训时间不能为空");
                }

                if(tTrainingRecordsTemplate.getTrainingStartTime().after(tTrainingRecordsTemplate.getTrainingEndTime())) {
                    throw new BusinessException("员工:【" + hrEmpInfo.getEmpNum() + "】培训开始时间不能大于结束时间");
                }
                tTrainingRecords=new TTrainingRecords();
                BeanUtils.copyProperties(tTrainingRecordsTemplate,tTrainingRecords);
                tTrainingRecords.setDeptId(hrEmpInfo.getDeptId());
                tTrainingRecords.setPostId(hrEmpInfo.getPostId());
                tTrainingRecords.setEmpName(hrEmpInfo.getEmpName());
                tTrainingRecords.setExamFlag(ExamFlagEnum.getByName(tTrainingRecordsTemplate.getExamFlagName()));
                tTrainingRecords.setPassFlag(ExamFlagEnum.getByName(tTrainingRecordsTemplate.getPassFlagName()));
                tTrainingRecords.setTrainingType(TeachingTypeEnum.getByName(tTrainingRecordsTemplate.getTrainingTypeName()));
                tTrainingRecordsMapper.insertSelective(tTrainingRecords);
            } else {
                throw new BusinessException("员工工号+"+tTrainingRecordsTemplate.getEmpNum()+"不存在,请校对数据");
            }
        }
        return "导入成功";
    }


    /**
     * 导出培训档案列表
     */
    @RequiresPermissions("hr:records:export")
    @Log(title = "培训档案导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TTrainingRecords tTrainingRecords)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            tTrainingRecords.setCreateId(ShiroUtils.getUserId());
        }
        List<TTrainingRecords> list = tTrainingRecordsService.selectTTrainingRecordsList(tTrainingRecords);
        ExcelUtil<TTrainingRecords> util = new ExcelUtil<>(TTrainingRecords.class);
        return util.exportExcel(list, "records");
    }

    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增培训档案
     */
    @RequiresPermissions("hr:records:add")
    @Log(title = "培训档案新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated TTrainingRecords tTrainingRecords)
    {
        return toAjax(tTrainingRecordsService.insertTTrainingRecords(tTrainingRecords));
    }

    /**
     * 修改培训档案
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TTrainingRecords tTrainingRecords = tTrainingRecordsService.selectTTrainingRecordsById(id);
        mmap.put("tTrainingRecords", tTrainingRecords);
        return prefix + "/edit";
    }

    /**
     * 修改保存培训档案
     */
    @RequiresPermissions("hr:records:edit")
    @Log(title = "培训档案", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TTrainingRecords tTrainingRecords)
    {
        return toAjax(tTrainingRecordsService.updateTTrainingRecords(tTrainingRecords));
    }

    /**
     * 删除培训档案
     */
    @RequiresPermissions("hr:records:remove")
    @Log(title = "培训档案删除", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tTrainingRecordsService.deleteTTrainingRecordsByIds(ids));
    }
}
