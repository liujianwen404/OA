package com.ruoyi.hr.controller;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.DTO.OverTimeDTO;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.domain.HrAttendanceGroup;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import com.ruoyi.hr.utils.AttendanceGroupSonEasyExcelListener;
import com.ruoyi.hr.utils.GroupSonSheetWriteHandler;
import com.ruoyi.hr.utils.OverTimeEasyExcelListener;
import com.ruoyi.system.domain.SysUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.HrAttendanceGroupSon;
import com.ruoyi.hr.service.IHrAttendanceGroupSonService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

/**
 * 考勤组排班子Controller
 * 
 * @author liujianwen
 * @date 2020-10-29
 */
@Controller
@RequestMapping("/hr/groupSon")
public class HrAttendanceGroupSonController extends BaseController
{
    private String prefix = "hr/attendanceGroupSon";

    @Autowired
    private IHrAttendanceGroupSonService hrAttendanceGroupSonService;

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;


    @RequiresPermissions("hr:groupSon:view")
    @GetMapping()
    public String son()
    {
        return prefix + "/groupSon";
    }

    /**
     * 查询考勤组排班子列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceGroupSon.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrAttendanceGroupSon> list = hrAttendanceGroupSonService.selectHrAttendanceGroupSonList(hrAttendanceGroupSon);
        return getDataTable(list);
    }

    /**
     * 导出考勤组排班子列表
     */
    @Log(title = "考勤组排班子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String groupId, String scheduDate)
    {
        Long id = Long.parseLong(StringUtils.strip(groupId, "''"));
        HrAttendanceGroup group = hrAttendanceGroupService.selectHrAttendanceGroupById(id);
        String scheduClassId = group.getScheduClassId();
        String scheduClassName = group.getScheduClassName();
        String[] classIds = scheduClassId.split(",");
        String[] classNames = scheduClassName.split(",");
        List<String> stringList = new ArrayList<>();
        for(int i=0;i<classIds.length;i++){
            StringBuilder sb = new StringBuilder();
            sb.append(classNames[i]).append(":").append(classIds[i]);
            stringList.add(sb.toString());
        }
        if(StringUtils.isBlank(scheduDate)){
            scheduDate = DateUtils.dateTimeNow("yyyy-MM");
        }
        Example example = new Example(HrAttendanceGroupSon.class);
        example.createCriteria().andEqualTo("parentId",id)
                .andEqualTo("scheduDate",scheduDate)
                .andEqualTo("delFlag","0");
        List<HrAttendanceGroupSon> list = hrAttendanceGroupSonService.selectByExample(example);
        List<HrAttendanceGroupSon> exportList = new ArrayList<>();
        list.forEach(son->{
            HrAttendanceGroupSon groupSon = new HrAttendanceGroupSon();
            groupSon.setParentId(id);
            groupSon.setEmpId(Long.valueOf(son.getEmpId()));
            groupSon.setEmpName(son.getEmpName());
            groupSon.setScheduDate(son.getScheduDate());
            if(son.getClassId1() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId1())){
                        groupSon.setClassId1(classes);
                    }
                });
            }if(son.getClassId2() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId2())){
                        groupSon.setClassId2(classes);
                    }
                });
            }if(son.getClassId3() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId3())){
                        groupSon.setClassId3(classes);
                    }
                });
            }if(son.getClassId4() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId4())){
                        groupSon.setClassId4(classes);
                    }
                });
            }if(son.getClassId5() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId5())){
                        groupSon.setClassId5(classes);
                    }
                });
            }if(son.getClassId6() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId6())){
                        groupSon.setClassId6(classes);
                    }
                });
            }if(son.getClassId7() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId7())){
                        groupSon.setClassId7(classes);
                    }
                });
            }if(son.getClassId8() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId8())){
                        groupSon.setClassId8(classes);
                    }
                });
            }if(son.getClassId9() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId9())){
                        groupSon.setClassId9(classes);
                    }
                });
            }if(son.getClassId10() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId10())){
                        groupSon.setClassId10(classes);
                    }
                });
            }if(son.getClassId11() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId11())){
                        groupSon.setClassId11(classes);
                    }
                });
            }if(son.getClassId12() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId12())){
                        groupSon.setClassId12(classes);
                    }
                });
            }if(son.getClassId13() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId13())){
                        groupSon.setClassId13(classes);
                    }
                });
            }if(son.getClassId14() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId14())){
                        groupSon.setClassId14(classes);
                    }
                });
            }if(son.getClassId15() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId15())){
                        groupSon.setClassId15(classes);
                    }
                });
            }if(son.getClassId16() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId16())){
                        groupSon.setClassId16(classes);
                    }
                });
            }if(son.getClassId17() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId17())){
                        groupSon.setClassId17(classes);
                    }
                });
            }if(son.getClassId18() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId18())){
                        groupSon.setClassId18(classes);
                    }
                });
            }if(son.getClassId19() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId19())){
                        groupSon.setClassId19(classes);
                    }
                });
            }if(son.getClassId20() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId20())){
                        groupSon.setClassId20(classes);
                    }
                });
            }if(son.getClassId21() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId21())){
                        groupSon.setClassId21(classes);
                    }
                });
            }if(son.getClassId22() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId22())){
                        groupSon.setClassId22(classes);
                    }
                });
            }if(son.getClassId23() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId23())){
                        groupSon.setClassId23(classes);
                    }
                });
            }if(son.getClassId24() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId24())){
                        groupSon.setClassId24(classes);
                    }
                });
            }if(son.getClassId25() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId25())){
                        groupSon.setClassId25(classes);
                    }
                });
            }if(son.getClassId26() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId26())){
                        groupSon.setClassId26(classes);
                    }
                });
            }if(son.getClassId27() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId27())){
                        groupSon.setClassId27(classes);
                    }
                });
            }if(son.getClassId28() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId28())){
                        groupSon.setClassId28(classes);
                    }
                });
            }if(son.getClassId29() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId29())){
                        groupSon.setClassId29(classes);
                    }
                });
            }if(son.getClassId30() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId30())){
                        groupSon.setClassId30(classes);
                    }
                });
            }if(son.getClassId31() != null){
                stringList.forEach(classes->{
                    if(classes.split(":")[1].equals(son.getClassId31())){
                        groupSon.setClassId31(classes);
                    }
                });
            }

            exportList.add(groupSon);
        });

        String fileName = "考勤排班表" + scheduDate + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        String[] classArrays = stringList.toArray(new String[stringList.size()]);
        EasyExcel.write(filePath, HrAttendanceGroupSon.class)
                .registerWriteHandler(new GroupSonSheetWriteHandler(exportList.size(),classArrays))
                .sheet("sheet1").doWrite(exportList);
        return AjaxResult.success(fileName);
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file)
    {
        StringBuilder message = new StringBuilder();
        //doReadAll()方法导入所有sheet
        try {
            EasyExcel.read(file.getInputStream(), HrAttendanceGroupSon.class,
                    new AttendanceGroupSonEasyExcelListener(hrAttendanceGroupSonService)).doReadAll();
        } catch (IOException e) {
            e.printStackTrace();
            message.append("导入失败");
        }
        message.append("导入成功");
        return AjaxResult.success();
    }

    /**
     * 新增考勤组排班子
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考勤组排班子
     */
    @Log(title = "考勤组排班子", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        return toAjax(hrAttendanceGroupSonService.insertHrAttendanceGroupSon(hrAttendanceGroupSon));
    }

    /**
     * 修改考勤组排班子
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrAttendanceGroupSon hrAttendanceGroupSon = hrAttendanceGroupSonService.selectHrAttendanceGroupSonById(id);
        HrAttendanceGroup attendanceGroup = hrAttendanceGroupService.selectHrAttendanceGroupById(hrAttendanceGroupSon.getParentId());
        String[] classIds = attendanceGroup.getScheduClassId().split(",");
        String[] classNames = attendanceGroup.getScheduClassName().split(",");
        List<HrAttendanceClass> classList = new ArrayList<>();
        for(int i=0;i<classIds.length;i++){
            HrAttendanceClass attendanceClass = new HrAttendanceClass();
            attendanceClass.setId(Long.valueOf(classIds[i]));
            attendanceClass.setName(classNames[i]);
            classList.add(attendanceClass);
        }
        mmap.put("classList",classList);
        mmap.put("hrAttendanceGroupSon", hrAttendanceGroupSon);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤组排班子
     */
    @Log(title = "考勤组排班子", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        return toAjax(hrAttendanceGroupSonService.updateHrAttendanceGroupSon(hrAttendanceGroupSon));
    }

    /**
     * 修改保存考勤组排班子
     */
    @Log(title = "考勤组排班子", businessType = BusinessType.UPDATE)
    @PostMapping("/editOne")
    @ResponseBody
    public AjaxResult editOne(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        return toAjax(hrAttendanceGroupSonService.updateGroupSonOne(hrAttendanceGroupSon));
    }

    /**
     * 删除考勤组排班子
     */
    @Log(title = "考勤组排班子", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrAttendanceGroupSonService.deleteHrAttendanceGroupSonByIds(ids));
    }

    /**
     * 查询考勤组排班人员列表
     */
    @PostMapping("/scheduClassList")
    @ResponseBody
    @Transactional
    public TableDataInfo scheduClassList(String groupId, String scheduDate)
    {
        Long id = Long.parseLong(StringUtils.strip(groupId, "''"));
        if(StringUtils.isBlank(scheduDate)){
            scheduDate = DateUtils.dateTimeNow("yyyy-MM");
        }
        startPage();
        Example example = new Example(HrAttendanceGroupSon.class);
        example.createCriteria().andEqualTo("parentId",id)
                .andEqualTo("scheduDate",scheduDate)
                .andEqualTo("delFlag","0");
        List<HrAttendanceGroupSon> list = hrAttendanceGroupSonService.selectByExample(example);
        //如果list为空 说明考勤组没有当月的排班信息 先在数据库生成当月该考勤组员工的数据 再返回页面
        if(list == null || list.size() == 0){
            Example example1 = new Example(HrAttendanceGroupSon.class);
            example1.createCriteria().andEqualTo("parentId",id)
                    .andEqualTo("delFlag","0");
            HrAttendanceGroup attendanceGroup = hrAttendanceGroupService.selectHrAttendanceGroupById(id);
            String empIds = attendanceGroup.getEmpId();
            String empNames = attendanceGroup.getEmpName();
            List<String> list1 = Arrays.asList(empIds.split(","));
            List<String> list2 = Arrays.asList(empNames.split(","));
            List<HrAttendanceGroupSon> insertList = new ArrayList<>();
            for(int i=0;i<list1.size();i++){
                HrAttendanceGroupSon groupSon = new HrAttendanceGroupSon();
                groupSon.setCreateId(ShiroUtils.getUserId());
                groupSon.setCreateBy(ShiroUtils.getLoginName());
                groupSon.setCreateTime(DateUtils.getNowDate());
                groupSon.setParentId(id);
                groupSon.setEmpId(Long.valueOf(list1.get(i)));
                groupSon.setEmpName(list2.get(i));
                groupSon.setScheduDate(scheduDate);
                groupSon.setDelFlag("0");
                insertList.add(groupSon);
            }
            hrAttendanceGroupSonService.insertList(insertList);
            Example example2 = new Example(HrAttendanceGroupSon.class);
            example2.createCriteria().andEqualTo("parentId",id)
                    .andEqualTo("scheduDate",scheduDate)
                    .andEqualTo("delFlag","0");
            List<HrAttendanceGroupSon> returnList = hrAttendanceGroupSonService.selectByExample(example2);
            return getDataTable(returnList);
        }
        return getDataTable(list);
    }

}
