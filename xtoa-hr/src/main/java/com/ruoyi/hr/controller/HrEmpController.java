package com.ruoyi.hr.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.ruoyi.base.domain.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.framework.web.service.PostService;
import com.ruoyi.base.domain.VO.HrEmpVo;
import com.ruoyi.hr.service.*;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工Controller
 *
 * @author vivi07
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/employee/emp")
public class HrEmpController extends BaseController {
    private String prefix = "employee/emp";

    @Autowired
    private HrEmpService tHrEmpService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private PostService postService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private IHrContractService hrContractService;

    @Autowired
    private ISalaryStructureService salaryStructureService;

    @Autowired
    private IHrEmpExperiencesService hrEmpExperiencesService;

    @Autowired
    private IHrEmpTransfersService hrEmpTransfersService;


    @RequiresPermissions("employee:emp:view")
    @GetMapping()
    public String emp() {
        return prefix + "/emp";
    }

    /**
     * 查询员工列表
     */
    @RequiresPermissions("employee:emp:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrEmp hrEmp) {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrEmp.setCreateBy(ShiroUtils.getLoginName());
//        }
        startPage();
        List<HrEmp> list = tHrEmpService.selectTHrEmpList(hrEmp);
        return getDataTable(list);
    }

    @Log(title = "员工导入", businessType = BusinessType.IMPORT)
    @RequiresPermissions("employee:emp:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, HttpServletRequest request)
    {
        try {
            return AjaxResult.success(tHrEmpService.importUser(file,request));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败："+e.getMessage());
        }
    }


    /**
     * 导出员工列表
     */
    @RequiresPermissions("employee:emp:export")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public synchronized AjaxResult export(HrEmp hrEmp, HttpServletResponse response) throws IOException {
        List<HrEmp> list = tHrEmpService.selectTHrEmpList(hrEmp);
        List<SysPost> postAll = postService.getPostAll();

        Date date = new Date();
        SysDept sysDept;
        String[] ancestors;
        List<String> dName = null;
        int ml = 0;
        int maxContract = 0;
        int maxSalary = 0;
        int maxExperience = 0;
        Example example;
        SalaryStructure salaryStructure;
        SalaryStructure selectSalary;
        List<SalaryStructure> salaryStructures;
        SysUser sysUser;
        for (HrEmp emp : list) {
            if (emp.getNonManagerDate() != null) {
                emp.setSiLing(DateUtils.getTermBetween(emp.getNonManagerDate(), date));
            }
            if (emp.getGraduationDate() != null) {
                emp.setSeniority(DateUtils.getTermBetween(emp.getGraduationDate(), date));
            }


            sysDept = sysDeptService.selectDeptById(emp.getDeptId());
            if (sysDept != null) {
                ancestors = sysDept.getAncestors().split(",");
                int length = ancestors.length;
                if (length > ml){
                    ml = length;
                }
                dName = new ArrayList<>();
                for (String ancestor : ancestors) {
                    if (ancestor.equals("0") || ancestor.equals("100")){
                        continue;
                    }
                    sysDept = sysDeptService.selectDeptById(Long.parseLong(ancestor));
                    if (sysDept != null){
                        if (StringUtils.isEmpty(emp.getArea())){
                            emp.setArea(sysDept.getDeptName());
                        }
                        dName.add(sysDept.getDeptName());
                    }
                }
                sysDept = sysDeptService.selectDeptById(100L);
                if (StringUtils.isEmpty(emp.getArea())){
                    emp.setArea(sysDept.getDeptName());
                }


                sysDept = sysDeptService.selectDeptById(emp.getDeptId());
                dName.add(sysDept.getDeptName());
                emp.setDeptNameVOList(dName);

            }

            for (SysPost sysPost : postAll) {
                if (emp.getPostId().equals(sysPost.getPostId())) {
                    emp.setPostName(sysPost.getPostName());
                }
            }

            emp.setAge(DateUtil.ageOfNow(emp.getBirthday()) + "");
            emp.setEmpStatus2(emp.getEmpStatus());

            emp.setContractList(hrContractService.selectHrContractByEmpIdForCount(emp.getEmpId()));
            if (maxContract < (emp.getContractList() == null ? 0 : emp.getContractList().size())){
                maxContract = emp.getContractList().size();
            }

            selectSalary = new SalaryStructure();
            selectSalary.setEmpId(emp.getEmpId());
            selectSalary.setDelFlag("0");
            selectSalary.setAuditStatus(2);
            selectSalary.setIsHistory(0);
            salaryStructure = salaryStructureService.selectOneBySalaryStructure(selectSalary);
            if (salaryStructure != null){
                emp.setComprehensive(salaryStructure.getComprehensive());
                emp.setBasic(salaryStructure.getBasic());
                emp.setOvertimePay(salaryStructure.getOvertimePay());
                emp.setAllowance(salaryStructure.getAllowance());
                emp.setOtherSubsidies(salaryStructure.getOtherSubsidies());
                emp.setPerformanceBonus(salaryStructure.getPerformanceBonus());
            }

            salaryStructure = new SalaryStructure();
            salaryStructure.setEmpId(emp.getEmpId());
            salaryStructure.setDelFlag("0");
            salaryStructure.setAuditStatus(2);
            salaryStructure.setIsHistory(1);
            salaryStructures = salaryStructureService.selectSalaryStructureListEx(salaryStructure);
            emp.setSalaryStructures(salaryStructures);
            if (maxSalary < (emp.getSalaryStructures() == null ? 0 : emp.getSalaryStructures().size())){
                maxSalary = emp.getSalaryStructures().size();
            }

            sysUser = iSysUserService.selectUserById(emp.getEmpId());
            if (sysUser != null){
                emp.setDingUserIdVO(sysUser.getDingUserId());
            }

            HrEmpExperiences experiences = new HrEmpExperiences();
            experiences.setParentId(emp.getEmpId());
            experiences.setDelFlag("0");
            List<HrEmpExperiences> experiencesList = hrEmpExperiencesService.selectHrEmpExperiencesList(experiences);
            emp.setHrEmpExperiencesList(experiencesList);
            maxExperience = 3;//导出3条工作经历记录
            /*if(maxExperience < (emp.getHrEmpExperiencesList() == null ? 0 : emp.getHrEmpExperiencesList().size())){
                maxExperience = emp.getHrEmpExperiencesList().size();
            }*/

        }
        return getEmpExcl(list,ml-=2,maxContract,maxSalary,maxExperience);

    }

    private String[] depN = new String[]{"一级部门","二级部门","三级部门","四级部门","五级部门",
                                            "六级部门"};
    private String[] contractN = new String[]{"劳动合同首签","劳动合同第一次续签","劳动合同第二次续签","劳动合同第三次续签","劳动合同第四次续签",
            "劳动合同第五次续签","劳动合同第六次续签","劳动合同第七次续签","劳动合同第八次续签","劳动合同第九次续签"};
    private String[] salaryN = new String[]{"第一次调薪","第二次调薪","第三次调薪","第四次调薪","第五次调薪",
            "第六次调薪","第七次调薪","第八次调薪","第九次调薪","第十次调薪",
            "第十一次调薪","第十二次调薪","第十三次调薪","第十四次调薪","第十五次调薪",
            "第十六次调薪","第十七次调薪","第十八次调薪","第十九次调薪","第二十次调薪"};
    private String[] experienceN = new String[]{"工作经历（1）","工作经历（2）","工作经历（3）","工作经历（4）","工作经历（5）",
            "工作经历（6）","工作经历（7）","工作经历（8）","工作经历（9）","工作经历（10）"};
    private AjaxResult getEmpExcl(List<HrEmp> list, int maxDept, int maxContract, int maxSalary, int maxExperience){
        // 通过工具类创建writer
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        //头样式
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        headCellStyle.setBorderRight(BorderStyle.THIN);
        headCellStyle.setRightBorderColor(IndexedColors.BLACK1.getIndex());
        headCellStyle.setBorderLeft(BorderStyle.THIN);
        headCellStyle.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
        headCellStyle.setBorderTop(BorderStyle.THIN);
        headCellStyle.setTopBorderColor(IndexedColors.BLACK1.getIndex());
        headCellStyle.setBorderBottom(BorderStyle.THIN);
        headCellStyle.setBottomBorderColor(IndexedColors.BLACK1.getIndex());

        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headCellStyle.setFont(headerFont);

        //列宽
        sheet.setColumnWidth(-1,20);
        HSSFRow rowHeader= sheet.createRow(0);
        HSSFRow rowHeader1= sheet.createRow(1);
        HSSFRow rowHeader2= sheet.createRow(2);

        Integer i = 0;
        setHeaderCell(sheet, headCellStyle, rowHeader, "工号",0,2,i,i,workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "工作地",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "公司归属",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "在职/离职状态",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "员工姓名",0,2,i+=1,i, workbook);

        for (int j = 0; j < depN.length; j++) {
            setHeaderCell(sheet, headCellStyle, rowHeader, depN[j],0,2,i+=1,i, workbook);
        }

        setHeaderCell(sheet, headCellStyle, rowHeader, "职等",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "职级",0,2,i+=1,i, workbook);

        setHeaderCell(sheet, headCellStyle, rowHeader, "职务",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "入职日期",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "性别",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "民族",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "联系电话",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "身份证号码",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "出生年月日",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "出生月",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "年龄",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "司龄",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "员工状态",0,2,i+=1,i, workbook);
//        writer.merge(0, 0, 17, 19, "最高学历",true);
        i+=1;Integer egm1 = i;
        i+=2;Integer egm2 = i;
        HSSFCell educationCell = setHeaderCell(sheet, headCellStyle, rowHeader, "教育经历", 0, 0, egm1, egm2, workbook);

        setHeaderCell(sheet, headCellStyle, rowHeader1, "最高学历",1, 2, egm1, egm1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "毕业院校",1, 2, egm1+=1, egm1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "所学专业",1, 2, egm1+=1, egm1, workbook);

        i+=1;Integer naamccc1 = i;
        i+=7;Integer naamccc2 = i;
        setHeaderCell(sheet, headCellStyle, rowHeader, "其他个人信息",0, 0, naamccc1, naamccc2, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "籍贯",1, 2, naamccc1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "户口性质",1, 2, naamccc1+=1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "身份证住址",1, 2, naamccc1+=1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "婚姻状况",1, 2, naamccc1+=1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "紧急联系人",1, 2, naamccc1+=1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "紧急联系人关系",1, 2, naamccc1+=1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "紧急联系人电话",1, 2, naamccc1+=1, naamccc1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "个人邮箱",1,2, naamccc1+=1,naamccc1, workbook);



        i+=1;Integer ct1 = i;
        i+=3;Integer ct2 = i;
        setHeaderCell(sheet, headCellStyle, rowHeader, "试用与转正",0, 0, ct1, ct2, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "试用期限（月）",1, 2, ct1, ct1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "应转正日期",1, 2, ct1+=1, ct1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "转正到期提醒（天）",1, 2, ct1+=1, ct1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "实际转正日期",1, 2, ct1+=1, ct1, workbook);


        for (int j = 0; j < maxContract; j++) {
            i+=1;Integer contract1 = i;
            i+=3;Integer contract2 = i;
            setHeaderCell(sheet, headCellStyle, rowHeader, contractN[j],0, 0, contract1, contract2, workbook);
//            setHeaderCell(sheet, headCellStyle, rowHeader, contractN[j],0,2,i+=1,i, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "合同主体",1, 2, contract1, contract1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "合同年限（年）",1, 2, contract1+=1, contract1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "合同起始日",1, 2, contract1+=1, contract1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "合同终止日",1, 2, contract1+=1, contract1, workbook);
        }


//        writer.merge(0, 0, 27, 28, "社保公积金",true);
        i+=1;Integer sp1 = i;
        i+=1;Integer sp2 = i;
        setHeaderCell(sheet, headCellStyle, rowHeader, "社保公积金",0, 0, sp1, sp2, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "社保账号",1, 2, sp1, sp1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "公积金账号",1, 2, sp2, sp2, workbook);


        setHeaderCell(sheet, headCellStyle, rowHeader, "开户行支行",0, 2, i+=1, i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "银行卡号",0,2,i+=1,i, workbook);

        setHeaderCell(sheet, headCellStyle, rowHeader, "试用期薪资",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "转正薪资",0,2,i+=1,i, workbook);

        if (maxSalary > 0){
            int maxStar = i + 1;
            int maxEnd = i + 2 + ((maxSalary-1) * 2) ;
            setHeaderCell(sheet, headCellStyle, rowHeader, "调薪",0, 0, maxStar, maxEnd, workbook);
            for (int j = 0; j < maxSalary; j++) {
                i+=1;Integer maxSalary1 = i;
                i+=1;Integer maxSalary2 = i;
                setHeaderCell(sheet, headCellStyle, rowHeader1, salaryN[j],1, 1, maxSalary1, maxSalary2, workbook);
                setHeaderCell(sheet, headCellStyle, rowHeader2, "日期",2, 2, maxSalary1, maxSalary1, workbook);
                setHeaderCell(sheet, headCellStyle, rowHeader2, "调薪情况",2, 2, maxSalary1+=1, maxSalary1, workbook);
            }
        }

        i+=1;Integer salary1 = i;
        i+=5;Integer salary2 = i;
        setHeaderCell(sheet, headCellStyle, rowHeader, "薪资结构",0, 0, salary1, salary2, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "当月综合薪资",1, 2, salary1, salary1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "基本工资",1, 2, salary1+=1, salary1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "加班费",1, 2, salary1+=1, salary1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "岗位补贴",1, 2, salary1+=1, salary1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "其他补贴",1, 2, salary1+=1, salary1, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader1, "绩效工资基数",1, 2, salary1+=1, salary1, workbook);

        for (int j = 0; j < maxExperience; j++) {
            i+=1;Integer experience1 = i;
            i+=4;Integer experience2 = i;
            setHeaderCell(sheet, headCellStyle, rowHeader, experienceN[j],0, 0, experience1, experience2, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "入职日期",1, 2, experience1, experience1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "离职日期",1, 2, experience1+=1, experience1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "公司",1, 2, experience1+=1, experience1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "职位",1, 2, experience1+=1, experience1, workbook);
            setHeaderCell(sheet, headCellStyle, rowHeader1, "离职原因",1, 2, experience1+=1, experience1, workbook);
        }


        setHeaderCell(sheet, headCellStyle, rowHeader, "招聘专员",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "内部推荐人",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "备注",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "离职日期",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "离职原因",0,2,i+=1,i, workbook);
        setHeaderCell(sheet, headCellStyle, rowHeader, "离职手续是否办结",0,2,i+=1,i, workbook);

//        setHeaderCell(sheet, headCellStyle, rowHeader, "钉钉userId",0,2,i+=1,i, workbook);

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = workbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(dataFont);

        HSSFRow row;
        HrEmp hrEmp;
        Date date = new Date();
        List<String> deptNameVOList;
        List<HrContract> contractList;
        List<SalaryStructure> salaryStructures;
        List<HrEmpExperiences> empExperiences;
        for (int j = 0; j < list.size(); j++) {
            row = sheet.createRow(j + 3);
            hrEmp = list.get(j);
            int c = 0;
            setRowValue(cellStyle, row, hrEmp.getEmpNum(), c,workbook);
            setRowValue(cellStyle, row, hrEmp.getCity(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getArea(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getEmpStatus2VO(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getEmpName(), c+=1, workbook);

            deptNameVOList = hrEmp.getDeptNameVOList();
            for (int k = 1; k <= depN.length; k++) {
                if (deptNameVOList != null && deptNameVOList.size() > 0){
                    if (deptNameVOList.size() > k){
                        setRowValue(cellStyle, row, deptNameVOList.get(k), c+=1, workbook);
                    }else {
                        setRowValue(cellStyle, row, "-", c+=1, workbook);
                    }
                }else {
                    setRowValue(cellStyle, row, "-", c+=1, workbook);
                }
            }

            setRowValue(cellStyle, row, hrEmp.getPostLevel(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getPostRank(), c+=1, workbook);

            setRowValue(cellStyle, row, hrEmp.getPostName(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getNonManagerDate(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getSexVO(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getEthnic(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getPhonenumber(), c+=1, workbook);
//            setRowValue(cellStyle, row, hrEmp.getEmail(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getIdNumber(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getBirthday(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getBirthdayVO() != null ?
                    DateUtil.format(hrEmp.getBirthdayVO(),"MM") : null, c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getAge(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getSiLing(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getEmpStatusVO(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getEducation(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getGraduation(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getMajor(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getNationality(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getCategor(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getAddress(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getMarriage(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getContactsName(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getContactsRelation(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getContactsPhone(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getEmail(), c+=1, workbook);

            setRowValue(cellStyle, row, hrEmp.getTrial(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getTrialEnd(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getTrialEnd() == null ?
                    null : hrEmp.getTrialEnd().getTime() <= date.getTime() ?
                    null :  DateUtil.betweenDay(date,hrEmp.getTrialEnd(),true), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getPositiveDate(), c+=1, workbook);


            contractList = hrEmp.getContractList();
            for (int k = 0; k < maxContract; k++) {
                if (contractList != null && contractList.size() > k){
                    setRowValue(cellStyle, row, contractList.get(k).getSubjectContract(), c+=1, workbook);
                    setRowValue(cellStyle, row, contractList.get(k).getContractYear(), c+=1, workbook);
                    setRowValue(cellStyle, row, contractList.get(k).getContractStar(), c+=1, workbook);
                    setRowValue(cellStyle, row, contractList.get(k).getContractEnd(), c+=1, workbook);
                }else {
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                }
            }

            setRowValue(cellStyle, row, hrEmp.getSocialSecurity(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getProvidentFund(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getBankBranch(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getBankNumber(), c+=1, workbook);

            setRowValue(cellStyle, row, hrEmp.getTrialSalary(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getConversionSalary(), c+=1, workbook);

            salaryStructures = hrEmp.getSalaryStructures();
            for (int k = 0; k < maxSalary; k++) {
                if (salaryStructures != null && salaryStructures.size() > k){
                    setRowValue(cellStyle, row, salaryStructures.get(k).getAdjustDate(), c+=1, workbook);
                    setRowValue(cellStyle, row, salaryStructures.get(k).getSalaryContent(), c+=1, workbook);
                }else {
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                }
            }

            setRowValue(cellStyle, row, hrEmp.getComprehensive(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getBasic(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getOvertimePay(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getAllowance(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getOtherSubsidies(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getPerformanceBonus(), c+=1, workbook);

            empExperiences = hrEmp.getHrEmpExperiencesList();
            for (int k = 0; k < maxExperience; k++) {
                if (empExperiences != null && empExperiences.size() > k){
                    setRowValue(cellStyle, row, empExperiences.get(k).getEntryDate(), c+=1, workbook);
                    setRowValue(cellStyle, row, empExperiences.get(k).getQuitDate(), c+=1, workbook);
                    setRowValue(cellStyle, row, empExperiences.get(k).getCompany(), c+=1, workbook);
                    setRowValue(cellStyle, row, empExperiences.get(k).getPosition(), c+=1, workbook);
                    setRowValue(cellStyle, row, empExperiences.get(k).getRemark(), c+=1, workbook);
                }else {
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                    setRowValue(cellStyle, row, null, c+=1, workbook);
                }
            }

            setRowValue(cellStyle, row, hrEmp.getRecruiter(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getInternaler(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getRemark(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getQuitDate(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getQuitReason(), c+=1, workbook);
            setRowValue(cellStyle, row, hrEmp.getIsQuitVO(), c+=1, workbook);
//            setRowValue(cellStyle, row, hrEmp.getDingUserIdVO(), c+=1, workbook);
        }



        String filename = UUID.randomUUID().toString() + "_" + "emp" + ".xlsx";
        OutputStream out = null;
        try {
            out = new FileOutputStream(getAbsoluteFile(filename));

            workbook.write(out);
            workbook.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return AjaxResult.success(filename);
    }

    private void setRowValue(HSSFCellStyle cellStyle, HSSFRow row, Object obj, Integer column, HSSFWorkbook workbook) {
        HSSFCell cell;
        cell = row.createCell(column);
        cell.setCellStyle(cellStyle);
        if (obj == null){
            cell.setCellValue("-");
            return;
        }
        if (!(obj instanceof Date)){
            if (StringUtils.isEmpty(obj+"")){
                cell.setCellValue("-");
                return;
            }
            cell.setCellValue(obj+"");
        }else if (obj instanceof Date){
            cell.setCellValue(DateUtil.format((Date) obj ,"yyyy-MM-dd"));
        }
    }

    private HSSFCell setHeaderCell(HSSFSheet sheet, HSSFCellStyle headCellStyle, HSSFRow rowHeader, String cellValue, int firstRow, int lastRow, int firstCol, int lastCol, HSSFWorkbook workbook) {
        HSSFCell cell;
        CellRangeAddress region;
        cell = rowHeader.createCell(firstCol);
        cell.setCellValue(cellValue);
        region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        if (firstRow != lastRow || firstCol != lastCol){
            sheet.addMergedRegion(region);
        }
        cell.setCellStyle(headCellStyle);
        sheet.setColumnWidth(firstCol,15*256);

        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);  //下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);     //左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);    //右边框
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);      //上边框
        return cell;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = Global.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }


    private AjaxResult getEmpPoi(List<HrEmp> list) {

        ExcelUtil<HrEmp> util = new ExcelUtil<HrEmp>(HrEmp.class);
        return util.exportExcel(list, "emp");
    }

    /**
     * 新增员工
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存员工
     */
    @RequiresPermissions("employee:emp:add")
    @Log(title = "员工", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated HrEmp hrEmp, HttpServletRequest request) {
        try {
            return tHrEmpService.insertTHrEmp(hrEmp,request);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改员工
     */
    @GetMapping("/edit/{empId}")
    public String edit(@PathVariable("empId") Long empId, ModelMap mmap) {
        HrEmp hrEmp = tHrEmpService.selectTHrEmpById(empId);
        SalaryStructure structure = new SalaryStructure();
        structure.setEmpId(empId);
        SalaryStructure salaryStructure = salaryStructureService.selectOneBySalaryStructure(structure);
        HrEmpExperiences hrEmpExperiences = new HrEmpExperiences();
        hrEmpExperiences.setParentId(hrEmp.getEmpId());
        List<HrEmpExperiences> experiencesList = hrEmpExperiencesService.selectHrEmpExperiencesList(hrEmpExperiences);
        HrEmpTransfers hrEmpTransfers = new HrEmpTransfers();
        hrEmpTransfers.setParentId(hrEmp.getEmpId());
        List<HrEmpTransfers> transfersList = hrEmpTransfersService.selectHrEmpTransfersList(hrEmpTransfers);
        mmap.put("hrEmp", hrEmp);
        mmap.put("salaryStructure", salaryStructure);
        mmap.put("experiencesList", experiencesList);
        mmap.put("transfersList", transfersList);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工
     */
    @RequiresPermissions("employee:emp:edit")
    @Log(title = "员工", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(HrEmp hrEmp,HttpServletRequest request) {
        HrEmp oldEmp = tHrEmpService.selectTHrEmpById(hrEmp.getEmpId());

        if(Objects.nonNull(hrEmp.getQuitDate())){
            if(hrEmp.getNonManagerDate().getTime()> hrEmp.getQuitDate().getTime()){
                throw new BusinessException("离职日期不能小于入职日期");
            }
        }

        int i = tHrEmpService.updateTHrEmp(hrEmp,request);
        if (i > 0){

            HrEmp nowEmp = tHrEmpService.selectTHrEmpById(hrEmp.getEmpId());

            SysUser sysUser = iSysUserService.selectUserById(nowEmp.getUserId());
            if (sysUser != null){
                sysUser.setSex(hrEmp.getSex());
                sysUser.setPhonenumber(hrEmp.getPhonenumber());
                sysUser.setEmail(hrEmp.getEmail());
                sysUser.setUserName(hrEmp.getEmpName());
                iSysUserService.updateUser(sysUser);
                tHrEmpService.logoutUser(sysUser.getLoginName());
            }

            //如果存在员工状态变更
            hrEmpTransfersService.insertEmpTransfers(oldEmp,nowEmp);
        }

        return toAjax(i);
    }

    /**
     * 删除员工
     */
    @RequiresPermissions("employee:emp:remove")
    @Log(title = "员工", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(tHrEmpService.deleteTHrEmpByIds(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<HrEmpVo> treeData() {
        List<HrEmpVo> ztrees = tHrEmpService.selectUserTree(new HrEmp());
        return ztrees;
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/selectTree")
    public String selectUserTree() {
        return prefix + "/tree";
    }


    @PostMapping("/checkEmpNumIsExist")
    @ResponseBody
    public Boolean checkEmpNumIsExist(String empNum) {
           HrEmp hrEmp= tHrEmpService.selectTHrEmpByNumber(empNum);
           if(Objects.nonNull(hrEmp)){
               return true;
           }
            return false;
    }

    @RequiresPermissions("employee:emp:import")
    @GetMapping("/toUploadZIP")
    public String toUploadZIP() {
        return prefix + "/uploadZIP";
    }

    /**
     * 上传照片压缩包
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping(value = "/uploadZIP")
    public List<HrEmp> uploadZIP(MultipartFile file) {
        return tHrEmpService.importPhotos(file);
    }
}
