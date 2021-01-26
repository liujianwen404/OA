package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.domain.HrAttendanceGroup;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrAttendanceGroupSon;
import com.ruoyi.hr.mapper.HrAttendanceGroupSonMapper;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.hr.service.IHrAttendanceGroupSonService;
import com.ruoyi.system.domain.SysUser;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * 考勤组Controller
 * 
 * @author liujianwen
 * @date 2020-07-29
 */
@Controller
@RequestMapping("/hr/attendanceGroup")
public class HrAttendanceGroupController extends BaseController
{
    private String prefix = "hr/attendanceGroup";

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Resource
    private IHrAttendanceGroupSonService hrAttendanceGroupSonService;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;

    @RequiresPermissions("hr:attendanceGroup:view")
    @GetMapping()
    public String group()
    {
        return prefix + "/group";
    }

    /**
     * 查询考勤组列表
     */
    @RequiresPermissions("hr:attendanceGroup:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrAttendanceGroup hrAttendanceGroup)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceGroup.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrAttendanceGroup> list = hrAttendanceGroupService.selectHrAttendanceGroupList(hrAttendanceGroup);
        return getDataTable(list);
    }

    /**
     * 加载地图页面
     */
    @GetMapping("/map")
    public String map(String scope,ModelMap map)
    {
        map.put("scope",scope);
        return prefix + "/map";
    }

    /**
     * 导出考勤组列表
     */
    @RequiresPermissions("hr:attendanceGroup:export")
    @Log(title = "考勤组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrAttendanceGroup hrAttendanceGroup)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceGroup.setCreateId(ShiroUtils.getUserId());
        }
        List<HrAttendanceGroup> list = hrAttendanceGroupService.selectHrAttendanceGroupList(hrAttendanceGroup);
        ExcelUtil<HrAttendanceGroup> util = new ExcelUtil<HrAttendanceGroup>(HrAttendanceGroup.class);
        return util.exportExcel(list, "group");
    }

    /**
     * 新增考勤组
     */
    @GetMapping("/add")
    public String add(ModelMap mmap, HrAttendanceClass hrAttendanceClass)
    {
        List<HrAttendanceClass> classList = hrAttendanceClassService.selectHrAttendanceClassList(hrAttendanceClass);
        mmap.put("classList",classList);
        return prefix + "/add";
    }

    /**
     * 新增保存考勤组
     */
    @RequiresPermissions("hr:attendanceGroup:add")
    @Log(title = "考勤组", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrAttendanceGroup hrAttendanceGroup)
    {
        return toAjax(hrAttendanceGroupService.insertHrAttendanceGroup(hrAttendanceGroup));
    }

    /**
     * 修改考勤组
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap, HrAttendanceClass hrAttendanceClass)
    {
        List<HrAttendanceClass> classList = hrAttendanceClassService.selectHrAttendanceClassList(hrAttendanceClass);
        HrAttendanceGroup hrAttendanceGroup = hrAttendanceGroupService.selectHrAttendanceGroupById(id);
        mmap.put("classList",classList);
        mmap.put("hrAttendanceGroup", hrAttendanceGroup);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤组
     */
    @RequiresPermissions("hr:attendanceGroup:edit")
    @Log(title = "考勤组", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrAttendanceGroup hrAttendanceGroup)
    {
        return toAjax(hrAttendanceGroupService.updateHrAttendanceGroup(hrAttendanceGroup));
    }

    /**
     * 删除考勤组
     */
    @RequiresPermissions("hr:attendanceGroup:remove")
    @Log(title = "考勤组", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrAttendanceGroupService.deleteHrAttendanceGroupByIds(ids));
    }

    /**
     * 比较考勤组员工
     */
    @PostMapping( "/compareEmp")
    @ResponseBody
    public AjaxResult compareEmp(String empId,Long groupId)
    {
        List<String> list = hrAttendanceGroupService.compareEmp(empId,groupId);
        if (list != null && list.size() > 0) {
            return AjaxResult.error("exist",list);
        }
        return AjaxResult.success();
    }

    /**
     * 返回排班页面
     */
    @GetMapping("/openScheduClass/{groupId}")
    public String openScheduClass(@PathVariable("groupId") String groupId,ModelMap modelMap)
    {
        Long id = Long.parseLong(StringUtils.strip(groupId, "''"));
        HrAttendanceGroup attendanceGroup = hrAttendanceGroupService.selectHrAttendanceGroupById(id);
        String[] classIds = attendanceGroup.getScheduClassId().split(",");
        String[] classNames = attendanceGroup.getScheduClassName().split(",");
        List<HrAttendanceClass> classList = new ArrayList<>();
        for(int i=0;i<classIds.length;i++){
            HrAttendanceClass attendanceClass = new HrAttendanceClass();
            attendanceClass.setId(Long.valueOf(classIds[i]));
            attendanceClass.setName(classNames[i]);
            classList.add(attendanceClass);
        }
        String scheduDate = DateUtils.dateTimeNow("yyyy-MM");
        modelMap.put("classList",classList);
        modelMap.put("groupId",groupId);
        modelMap.put("scheduDate",scheduDate);
        return prefix + "/scheduClass";
    }

//    /**
//     * 查询考勤组排班人员列表
//     */
//    @PostMapping("/scheduClassList")
//    @ResponseBody
//    public TableDataInfo scheduClassList(String groupId)
//    {
//        Long id = Long.parseLong(StringUtils.strip(groupId, "''"));
//        startPage();
//        Example example = new Example(HrAttendanceGroupSon.class);
//        example.createCriteria().andEqualTo("parentId",id)
//                .andEqualTo("delFlag","0");
//        List<HrAttendanceGroupSon> list = hrAttendanceGroupSonService.selectByExample(example);
//        return getDataTable(list);
//    }
}
