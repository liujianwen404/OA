package com.ruoyi.hr.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.ruoyi.base.dingTalk.DingUserApi;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.domain.VO.HrEmpVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.OnlineStatus;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.framework.shiro.session.OnlineSession;
import com.ruoyi.framework.shiro.session.OnlineSessionDAO;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrEmpHistoryMapper;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserOnlineService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.utils.ImportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import static com.ruoyi.common.core.text.Convert.toInt;

/**
 * 员工Service业务层处理
 * 
 * @author vivi07
 * @date 2020-05-07
 */
@Slf4j
@Service
public class HrEmpServiceImpl implements HrEmpService
{


    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private HrEmpHistoryMapper hrEmpHistoryMapper;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysPostService iSysPostService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @Autowired
    private IHrContractService hrContractService;

    @Autowired
    private ISalaryStructureService salaryStructureService;

    @Autowired
    private DingUserApi dingUserApi;

    @Autowired
    private IHrEmpExperiencesService hrEmpExperiencesService;

    @Autowired
    private IHrEmpTransfersService hrEmpTransfersService;

    /**
     * 查询员工
     *
     * @param empId 员工ID
     * @return 员工
     */
    @Override
    public HrEmp selectTHrEmpById(Long empId)
    {
        return hrEmpMapper.selectTHrEmpById(empId);
    }

    @Override
    public String queryEmpNameByEmpId(Long empId) {
        return hrEmpMapper.queryEmpNameByEmpId(empId);
    }

    /**
     * 查询员工信息包括已删除的历史数据
     *
     * @param empId 员工ID
     * @return 员工
     */
    @Override
    public HrEmp selectHrEmpInfoByIdandFromHistory(Long empId)
    {
        HrEmp hrEmp = hrEmpMapper.selectTHrEmpById(empId);
        if (hrEmp == null ){
            Example example = new Example(HrEmpHistory.class);
            example.createCriteria().andEqualTo("empId",empId);
            HrEmpHistory hrEmpHistory = hrEmpHistoryMapper.selectSingleOneByExample(example);
            if (hrEmpHistory != null){
                hrEmp = new HrEmp();
                BeanUtil.copyProperties(hrEmpHistory,hrEmp);
            }
        }
        return hrEmp;
    }

    @Override
    public HrEmp selectTHrEmpByPhonenumber(String phonenumber) {
        return hrEmpMapper.selectByPhonenumber(phonenumber);
    }

    @Override
    public HrEmp selectTHrEmpByUserId(Long userId) {
        return hrEmpMapper.selectByUserId(userId);
    }

    /**
     * 查询员工列表
     * 
     * @param hrEmp 员工
     * @return 员工
     */
    @Override
    @DataScope(deptAlias = "d", menuAlias = "employee:emp:view")
    public List<HrEmp> selectTHrEmpList(HrEmp hrEmp)
    {
        hrEmp.setDelFlag("0");
        if ("不限".equals(hrEmp.getCity())){
            hrEmp.setCity(null);
        }

        List<HrEmp> hrEmps = hrEmpMapper.selectTHrEmpList(hrEmp);
        for (HrEmp emp : hrEmps) {
            emp.setDeptName(iSysDeptService.selectDeptById(emp.getDeptId()).getShowName());
        }

        return hrEmps;
    }



    /**
     * 新增员工
     * 
     * @param hrEmp 员工
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertTHrEmp(HrEmp hrEmp, HttpServletRequest request)
    {

        if (StringUtils.isNotEmpty(hrEmp.getSubjectContract())
                || hrEmp.getContractYear() != null
                || hrEmp.getContractStar() != null
                || hrEmp.getContractEnd() != null ){
            if (StringUtils.isEmpty(hrEmp.getSubjectContract())
                    || hrEmp.getContractYear() == null
                    || hrEmp.getContractStar() == null
                    || hrEmp.getContractEnd() == null ){
                return AjaxResult.error("如需录入合同信息请填写完整");
            }
        }

        hrEmp.setCreateTime(DateUtils.getNowDate());
        hrEmp.setCreateBy(ShiroUtils.getLoginName());
        hrEmp.setCreateId(ShiroUtils.getUserId());
        int i = insertEmp(hrEmp,request);

        if (StringUtils.isNotEmpty(hrEmp.getSubjectContract())
                && hrEmp.getContractYear() != null
                && hrEmp.getContractStar() != null
                && hrEmp.getContractEnd() != null ){
            HrContract hrContract = new HrContract();
            hrContract.setEmpId(hrEmp.getEmpId());
            hrContract.setSubjectContract(hrEmp.getSubjectContract());
            hrContract.setContractYear(hrEmp.getContractYear());
            hrContract.setContractEnd(hrEmp.getContractEnd());
            hrContract.setContractStar(hrEmp.getContractStar());
            hrContract.setAuditStatus(2);
            hrContract.setRenewCount(0);
            hrContract.setRemark("员工数据初始化录入");
            hrContractService.insertHrContract(hrContract);
        }


        SalaryStructure salaryStructure = new SalaryStructure();
        salaryStructure.setEmpId(hrEmp.getEmpId());
        salaryStructure.setComprehensive(hrEmp.getComprehensive());
        salaryStructure.setBasic(hrEmp.getBasic());
        salaryStructure.setOvertimePay(hrEmp.getOvertimePay());
        salaryStructure.setAllowance(hrEmp.getAllowance());
        salaryStructure.setOtherSubsidies(hrEmp.getOtherSubsidies());
        salaryStructure.setPerformanceBonus(hrEmp.getPerformanceBonus());
        salaryStructure.setSalaryContent("员工数据初始化录入");
        salaryStructure.setAuditStatus(2);
        salaryStructure.setAdjustDate(hrEmp.getNonManagerDate());
        salaryStructure.setMonthDate(new Date());
        salaryStructure.setPayGrade(hrEmp.getPayGrade());
        salaryStructure.setEmpName(hrEmp.getEmpName());

        salaryStructure.setCreateId(ShiroUtils.getUserId());
        salaryStructure.setCreateBy(ShiroUtils.getLoginName());

        salaryStructureService.insertSalaryStructure(salaryStructure);
        if (i > 0 ){
            insertUser(hrEmp,false);
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    private int insertEmp(HrEmp hrEmp, HttpServletRequest request) {

        checkEmpData(hrEmp);

        int i = hrEmpMapper.insertTHrEmp(hrEmp);

        insertExperience(hrEmp,request);

        return i;
    }

    private void insertExperience (HrEmp hrEmp, HttpServletRequest request) {
        //员工工作经历数据插入到子表
        try {
            int count = Integer.parseInt(request.getParameter("count"));
            for (int i = 1; i <= count; i++) {
                HrEmpExperiences hrEmpExperiences = new HrEmpExperiences();
                hrEmpExperiences.setCreateId(ShiroUtils.getUserId());
                hrEmpExperiences.setCreateBy(ShiroUtils.getLoginName());
                hrEmpExperiences.setCreateTime(DateUtils.getNowDate());
                hrEmpExperiences.setParentId(hrEmp.getEmpId());
                String entryDate = request.getParameter("entryDate" + i);
                String quitDate = request.getParameter("quitDate" + i);
                String company = request.getParameter("company" + i);
                String position = request.getParameter("position" + i);
                String remark = request.getParameter("remark" + i);
                if (StringUtils.isNotBlank(entryDate)) {
                    hrEmpExperiences.setEntryDate(DateUtil.parseDate(entryDate));
                }
                if (StringUtils.isNotBlank(quitDate)) {
                    hrEmpExperiences.setQuitDate(DateUtil.parseDate(quitDate));
                }
                if (StringUtils.isNotBlank(company)) {
                    hrEmpExperiences.setCompany(company);
                }
                if (StringUtils.isNotBlank(position)) {
                    hrEmpExperiences.setPosition(position);
                }
                if (StringUtils.isNotBlank(remark)) {
                    hrEmpExperiences.setRemark(remark);
                }
                hrEmpExperiencesService.insertHrEmpExperiences(hrEmpExperiences);
            }
        } catch (Exception e) {
            log.info(hrEmp.getEmpName()+"的工作经历数据插入失败：{}",e.getMessage());
        }
    }

    private void checkEmpData(HrEmp hrEmp) {
        Long hrEmpId = hrEmp.getEmpId() == null ? -1 : hrEmp.getEmpId();

        HrEmp hrEmpP = hrEmpMapper.selectByPhonenumber(hrEmp.getPhonenumber());
        if (hrEmpP != null && !hrEmpP.getEmpId().equals(hrEmpId)){
            throw  new BusinessException("这个手机号已经被使用了:"+hrEmp.getPhonenumber());
        }

        HrEmp hrEmpID = hrEmpMapper.selectByIdNumber(hrEmp.getIdNumber());
        if (hrEmpID != null && !hrEmpID.getEmpId().equals(hrEmpId)){
            throw  new BusinessException("这个身份证已经被使用了:"+hrEmp.getIdNumber());
        }




        HrEmp hrEmps = hrEmpMapper.selectByIdNumberAndDelete(hrEmp.getIdNumber());
        if (hrEmps != null && !hrEmps.getEmpId().equals(hrEmpId)){
            Date nowDate = new Date();
                if (hrEmps.getEmpStatus().equals(4)
                        && hrEmp.getQuitDate() != null
                        && DateUtil.offsetMonth(hrEmp.getQuitDate(),6).after(nowDate)){
                    throw  new BusinessException("离职六个月内不能再次入职:"+hrEmp.getIdNumber());
                }
        }

        if (StringUtils.isNotBlank(hrEmp.getEmpNum())){
            HrEmp hrEmpEmpNum = hrEmpMapper.selectHrEmpByEmpNum(hrEmp.getEmpNum());
            if (hrEmpEmpNum != null && !hrEmpEmpNum.getEmpId().equals(hrEmpId)){
                throw  new BusinessException("该工号在系统已存在");
            }
        }
    }

    /**
     * 导入时插入user数据
     * @param hrEmp
     * @param isImp   是否为导入
     */
    private void insertUser(HrEmp hrEmp,Boolean isImp) {

        if (!isImp){
            //不是导入，需要在添加员工是通过钉钉获取dingUserId
            /*SysUser sysUser = new SysUser();
//            sysUser.setDingUserId(hrEmp.getPhonenumber());
            String dingUserId = getDingUserId(sysUser, false);
            hrEmp.setDingUserIdVO(dingUserId);*/
        }
        insertUser(hrEmp);
    }

    private void insertUser(HrEmp hrEmp) {
        //插入user数据
        SysUser user = new SysUser();
        user.setUserId(hrEmp.getEmpId());
        user.setDeptId(hrEmp.getDeptId());
        user.setUserName(hrEmp.getEmpName());
        user.setEmail(hrEmp.getEmail());
        user.setPhonenumber(hrEmp.getPhonenumber());
        user.setSex(hrEmp.getSex());
        user.setRemark(hrEmp.getRemark());
        user.setPostIds(new Long[]{hrEmp.getPostId()});
        //写死为2：普通角色
        user.setRoleIds(new Long[]{2L});
        String empName = hrEmp.getEmpName();
        String pingYin = getLoginName(empName);

        user.setLoginName(pingYin);
        user.setPassword("123456");
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());

        user.setDingUserId(hrEmp.getDingUserIdVO());

        if(iSysUserService.insertUser(user) == 0){
            throw new BusinessException("创建user失败");
        }else {
            hrEmp.setUserId(user.getUserId());
            if (hrEmpMapper.updateTHrEmpByEmpId(hrEmp.getUserId(),hrEmp.getEmpId()) == 0){
                throw new BusinessException("更新员工用户Id失败");
            }
        }
    }

    public String getLoginName(String empName) {
        int i = 0;
        String pingYin = StringUtils.getPingYin(empName);

        pingYin = getPY(i, pingYin);

        return pingYin;
    }

    public String getPY(int i, String pingYin) {
        SysUser sysUser = iSysUserService.selectUserByLoginName(pingYin);
        if (sysUser != null){
            i ++;
            pingYin = pingYin + i;
            pingYin = getPY(i,pingYin);
        }
        return pingYin;
    }

    /**
     * 修改员工
     * 
     * @param hrEmp 员工
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTHrEmp(HrEmp hrEmp,HttpServletRequest request)
    {
        hrEmp.setUpdateTime(DateUtils.getNowDate());
        hrEmp.setUpdateBy(ShiroUtils.getLoginName());
        hrEmp.setUpdateId(ShiroUtils.getUserId());
        hrEmp.setQuitDate(hrEmp.getQuitDate());
        HrEmp oldEmp = hrEmpMapper.selectTHrEmpById(hrEmp.getEmpId());

        int i = updateEmp(hrEmp,request);
        if (i > 0){
            if (oldEmp.getDeptId().longValue() != hrEmp.getDeptId().longValue()
                    || oldEmp.getPostId().longValue() != hrEmp.getPostId().longValue() ){
                iSysUserService.updateUserDeptPost(hrEmp.getDeptId(), hrEmp.getPostId(), hrEmp.getUserId());
                SysUser sysUser = iSysUserService.selectUserById(hrEmp.getUserId());
                if (sysUser != null){
                    logoutUser(sysUser.getLoginName());
                }
            }

        }
        return i;
    }

    private int updateEmp(HrEmp hrEmp,HttpServletRequest request) {

        checkEmpData(hrEmp);

        int i = hrEmpMapper.updateTHrEmp(hrEmp);

        updateEmpExperiences(hrEmp,request);
        return i;
    }

    private void updateEmpExperiences(HrEmp hrEmp, HttpServletRequest request) {
        try {
            //员工工作经历数据更新到子表
            //提交的工作经历数量
            int count = Integer.parseInt(request.getParameter("count"));
            //遍历所有工作经历
            for (int i = 1; i <= count; i++) {
                HrEmpExperiences hrEmpExperiences = new HrEmpExperiences();
                String experiencesId = request.getParameter("experiences"+i);
                String entryDate = request.getParameter("entryDate" + i);
                String quitDate = request.getParameter("quitDate" + i);
                String company = request.getParameter("company" + i);
                String position = request.getParameter("position" + i);
                String remark = request.getParameter("remark" + i);
                if (StringUtils.isNotBlank(experiencesId)) {
                    //如果工作经历ID不为空，则更新原工作经历
                    hrEmpExperiences.setId(Long.valueOf(experiencesId));
                    if (StringUtils.isNotBlank(entryDate)) {
                        hrEmpExperiences.setEntryDate(DateUtil.parseDate(entryDate));
                    }
                    if (StringUtils.isNotBlank(quitDate)) {
                        hrEmpExperiences.setQuitDate(DateUtil.parseDate(quitDate));
                    }
                    if (StringUtils.isNotBlank(company)) {
                        hrEmpExperiences.setCompany(company);
                    }
                    if (StringUtils.isNotBlank(position)) {
                        hrEmpExperiences.setPosition(position);
                    }
                    if (StringUtils.isNotBlank(remark)) {
                        hrEmpExperiences.setRemark(remark);
                    }
                    hrEmpExperiencesService.updateHrEmpExperiences(hrEmpExperiences);
                } else {
                    //否则插入新工作经历
                    hrEmpExperiences.setParentId(hrEmp.getEmpId());
                    if (StringUtils.isNotBlank(entryDate)) {
                        hrEmpExperiences.setEntryDate(DateUtil.parseDate(entryDate));
                    }
                    if (StringUtils.isNotBlank(quitDate)) {
                        hrEmpExperiences.setQuitDate(DateUtil.parseDate(quitDate));
                    }
                    if (StringUtils.isNotBlank(company)) {
                        hrEmpExperiences.setCompany(company);
                    }
                    if (StringUtils.isNotBlank(position)) {
                        hrEmpExperiences.setPosition(position);
                    }
                    if (StringUtils.isNotBlank(remark)) {
                        hrEmpExperiences.setRemark(remark);
                    }
                    hrEmpExperiencesService.insertHrEmpExperiences(hrEmpExperiences);
                }
            }
        } catch (Exception e) {
            log.info(hrEmp.getEmpName() + "的工作经历更新失败：{}",e.getMessage());
        }

    }


    @Override
    public void logoutUser(String nameUser) {
        //修改权限强退用户
        SysUserOnline userOnline = new SysUserOnline();
        userOnline.setLoginName(nameUser);
        List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        for (SysUserOnline sessionId : list)
        {
            SysUserOnline online = userOnlineService.selectOnlineById(sessionId.getSessionId());
            if (online != null)
            {
                OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
                if (onlineSession != null && !sessionId.equals(ShiroUtils.getSessionId()))
                {
                    onlineSession.setStatus(OnlineStatus.off_line);
                    onlineSessionDAO.update(onlineSession);
                    online.setStatus(OnlineStatus.off_line);
                    userOnlineService.saveOnline(online);
                }
            }
        }
    }

    /**
     * 删除员工对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTHrEmpByIds(String ids) throws Exception {
        String[] strings = Convert.toStrArray(ids);
       /* HrEmp hrEmpDel = null;
        for (String string : strings) {*/
            /*hrEmpDel = hrEmpMapper.selectTHrEmpById(new Long(string));
            if (hrEmpDel != null){
                HrEmpHistory hrEmpHistory = new HrEmpHistory();
                BeanUtil.copyProperties(hrEmpDel,hrEmpHistory);
                hrEmpHistoryMapper.insertSelective(hrEmpHistory);
            }*/
//            iSysUserService.deleteUserByIds(hrEmpDel.getUserId()+"");
//        }

        int i = hrEmpMapper.deleteTHrEmpByIds(strings);
        if (i > 0){
            iSysUserService.deleteUserByIds(ids);
        }
        return i;
    }

    /**
     * 删除员工信息
     *
     * @return 结果
     */
   /* @Override
    @Transactional
    public int deleteTHrEmpById(Long empId) throws Exception {

        HrEmp hrEmp = hrEmpMapper.selectTHrEmpById(empId);
        int i = hrEmpMapper.deleteTHrEmpById(empId);
        if (i > 0){
            if (hrEmp != null){

                HrEmpHistory hrEmpHistory = new HrEmpHistory();
                BeanUtil.copyProperties(hrEmp,hrEmpHistory);
                hrEmpHistoryMapper.insertSelective(hrEmpHistory);

                iSysUserService.deleteUserByIds(hrEmp.getUserId()+"");
            }
        }

        return i;
    }*/

    @Override
    public List<HrEmpVo> selectUserTree(HrEmp hrEmp) {
        List<HrEmpVo> ztrees = new ArrayList<>();
        List<HrEmpVo> empList = hrEmpMapper.selectListZtree(hrEmp);
        HrEmpVo ztree = null;
        for (HrEmpVo emp : empList)
        {
            ztree = new HrEmpVo();
            String name =  emp.getDeptName();
            ztree.setDeptName(name);

            ztree.setPostName(emp.getPostName());
            ztree.setPostId(emp.getPostId());
            ztree.setDeptId(emp.getDeptId());
            ztree.setId(emp.getDeptId());
            ztree.setDeptPName(emp.getDeptPName());
            ztree.setParentId(emp.getParentId());
            ztree.setpId(emp.getParentId());

            ztree.setNonManagerDate(emp.getNonManagerDate());
            ztree.setUserId(emp.getUserId());
            ztree.setEmpId(emp.getEmpId());
            ztree.setEmpName(emp.getEmpName());

            ztree.setEmpId(emp.getEmpId());
            //展示
            ztree.setName(name + ":" + emp.getEmpName());
            ztree.setTitle(emp.getEmpName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    @Override
    public void updateEmpDeptAndPost(Long empId, Long jobTransferDeptId, Long jobTransferPostId,HttpServletRequest request) {
        log.info(StrUtil.format("修改员工部门与：{},{}.{}",empId,jobTransferDeptId,jobTransferPostId) );
        HrEmp hrEmp = hrEmpMapper.selectTHrEmpById(empId);
        if (hrEmp != null){
            hrEmp.setDeptId(jobTransferDeptId);
            hrEmp.setPostId(jobTransferPostId);
            updateEmp(hrEmp,request);
            Long userId = hrEmp.getUserId();

            iSysUserService.updateUserDeptPost(jobTransferDeptId, jobTransferPostId, userId);


        }
    }

    @Override
    public int deleteTHrEmpByUserIds(Long[] userIds) {

       /* HrEmp hrEmpDel = null;
        for (Long id : userIds) {
            hrEmpDel = hrEmpMapper.selectByUserId(id);
            if (hrEmpDel != null){
                HrEmpHistory hrEmpHistory = new HrEmpHistory();
                BeanUtil.copyProperties(hrEmpDel,hrEmpHistory);
                hrEmpHistoryMapper.insertSelective(hrEmpHistory);
            }
        }*/
        return hrEmpMapper.deleteTHrEmpByUserIds(userIds);
    }

    @Override
    @Transactional
    public String importUser(MultipartFile file, HttpServletRequest request) throws Exception {

        List<List<Object>>  list = ImportExcelUtil.getBankListByExcel(file.getInputStream(),file.getOriginalFilename());
        SysPost sysPost = null;
        SysDept sysDept = null;
        Map<String,SysDept> deptMap = new HashMap<>();//查询出的部门数据
        Map<String,SysPost> postMap = new HashMap<>();//查询出的岗位数据
        HrEmp hrEmp = null;
        List<HrEmp> hrEmpList = new ArrayList<>();
        HrContract hrContract = null;
        Map<String,HrContract> hrContractMap = new HashMap<>();//待插入的劳务合同数据

        SalaryStructure salaryStructure = null;
        Map<String,SalaryStructure> salaryStructureMap = new HashMap<>();//待插入的薪资结构数据

        HrEmpExperiences experiences = null;
        Map<String,HrEmpExperiences> empExperiencesMap = new HashMap<>();//待插入的工作经历数据
        List<Object> objects = null;
        for(int i = 0; i< list.size();i++) {
            hrEmp = new HrEmp();

            int c = 0;
            objects = list.get(i);
            String empNum = toStr(objects,c);
            String phoneNum = toStr(objects,17);
            String idNum = toStr(objects,18);
            if (StringUtils.isEmpty(empNum) || StringUtils.isEmpty(phoneNum) || StringUtils.isEmpty(idNum)
                    || empNum.contains("-") || phoneNum.contains("-") || idNum.contains("-") ){
                //工号为空，放弃这条数据
                continue;
            }
            hrEmp.setEmpNum(empNum);
            String city = toStr(objects, c += 1);
            hrEmp.setCity(StringUtils.isNotEmpty(city) ? city.contains("市") ? city : city + "市" : null);
            hrEmp.setArea(toStr(objects,c+=1));
            //在职/离职状态
            c+=1;
            hrEmp.setEmpName(toStr(objects,c+=1));

            //部门 岗位
            String deptName1 = toStr(objects, c+=1);
            String deptName2 = toStr(objects, c+=1);
            String deptName3 = toStr(objects, c+=1);
            String deptName4 = toStr(objects, c+=1);
            String deptName5 = toStr(objects, c+=1);
            String deptName6 = toStr(objects, c+=1);
            sysDept = getSysDept(hrEmp.getArea(),deptName1, deptName2, deptName3, deptName4, deptName5, deptName6, deptMap);
            hrEmp.setDeptId(sysDept == null ? null : sysDept.getDeptId());

            hrEmp.setPostLevel(toStr(objects,c+=1));
            hrEmp.setPostRank(toStr(objects,c+=1));

            sysPost = getSysPost(toStr(objects, c+=1),postMap);
            hrEmp.setPostId(sysPost == null ? null : sysPost.getPostId());

            hrEmp.setNonManagerDate(toDate(toStr(objects,c+=1),objects));
            c+=1;
            hrEmp.setEthnic(toStr(objects,c+=1));
            hrEmp.setPhonenumber(toStr(objects,c+=1));
            hrEmp.setIdNumber(toStr(objects,c+=1));
            hrEmp.setSex(toSex(hrEmp.getIdNumber()));
            c+=1;
            hrEmp.setBirthday(toBirthday(hrEmp.getIdNumber()));
            c+=3;
            String status = (objects.get(  c+=1)+"").trim();
//                0=试用期,1=转正,2=实习,3=返聘,4=离职
            hrEmp.setEmpStatus(status.contains("试用")?0:status.contains("转正")?1:status.contains("实习")?2:status.contains("返聘")?3:4);
            if (StringUtils.isEmpty(status) || "-".equals(status)){
                hrEmp.setEmpStatus(1);//1=转正
            }

            String education = toStr(objects, c += 1);
            hrEmp.setEducation(StringUtils.isNotEmpty(education) && education.equals("专科") ? "大专" : education);
            hrEmp.setGraduation(toStr(objects,c+=1));
            hrEmp.setMajor(toStr(objects,c+=1));
            hrEmp.setNationality(toStr(objects,c+=1));
            hrEmp.setCategor(toStr(objects,c+=1));
            hrEmp.setAddress(toStr(objects,c+=1));
            hrEmp.setMarriage(toStr(objects,c+=1));
            hrEmp.setContactsName(toStr(objects,c+=1));
            hrEmp.setContactsRelation(toStr(objects,c+=1));
            hrEmp.setContactsPhone(toStr(objects,c+=1));
            hrEmp.setEmail(toStr(objects,c+=1));

            hrEmp.setTrial(toStr(objects,c+=1));
            hrEmp.setTrialEnd(toDate(toStr(objects,c+=1), objects));
            c+=1;
            hrEmp.setPositiveDate(toDate(toStr(objects,c+=1), objects));

            hrContract = new HrContract();
            hrContract.setSubjectContract(toStr(objects,c+=1));
            hrContract.setContractYear(toInt(toStr(objects,c+=1)));
            hrContract.setContractStar(toDate(toStr(objects,c+=1), objects));
            hrContract.setContractEnd(toDate(toStr(objects,c+=1), objects));
            hrContract.setAuditStatus(2);
            hrContract.setRenewCount(0);
            hrContract.setRemark("导入初始化数据");
            hrContractMap.put(hrEmp.getEmpNum(),hrContract);

            c+=6;

            hrEmp.setSocialSecurity(toStr(objects,c+=1));
            hrEmp.setProvidentFund(toStr(objects,c+=1));
            hrEmp.setBankBranch(toStr(objects,c+=1));
            hrEmp.setBankNumber(toStr(objects,c+=1));

            hrEmp.setTrialSalary(toBig(toStr(objects,c+=1)));
            hrEmp.setConversionSalary(toBig(toStr(objects,c+=1)));
            c+=6;

            salaryStructure = new SalaryStructure();
            salaryStructure.setComprehensive(toBig(toStr(objects,c+=1)));
            salaryStructure.setBasic(toBig(toStr(objects,c+=1)));
            salaryStructure.setOvertimePay(toBig(toStr(objects,c+=1)));
            salaryStructure.setAllowance(toBig(toStr(objects,c+=1)));
            salaryStructure.setOtherSubsidies(toBig(toStr(objects,c+=1)));
            salaryStructure.setPerformanceBonus(toBig(toStr(objects,c+=1)));

            salaryStructure.setAuditStatus(2);
            salaryStructure.setAdjustDate(new Date());
            salaryStructure.setRemark("导入初始化数据");
            salaryStructureMap.put(hrEmp.getEmpNum(),salaryStructure);

            experiences = new HrEmpExperiences();
            experiences.setEntryDate(toDate(toStr(objects,c+=1), objects));
            experiences.setQuitDate(toDate(toStr(objects,c+=1), objects));
            experiences.setCompany(toStr(objects,c+=1));
            experiences.setPosition(toStr(objects,c+=1));
            experiences.setRemark(toStr(objects,c+=1));
            empExperiencesMap.put(hrEmp.getEmpNum(),experiences);

            hrEmp.setRecruiter(toStr(objects,c+=1));
            hrEmp.setInternaler(toStr(objects,c+=1));
            hrEmp.setQuitDate(toDate(toStr(objects,c+=1), objects));
            hrEmp.setQuitReason(toStr(objects,c+=1));
            String isQuit = toStr(objects, c += 1);
            hrEmp.setIsQuit( StringUtils.isNotEmpty(isQuit) ? isQuit.contains("未离职") ? "0" : "1" : "0");

//            hrEmp.setDingUserIdVO(toStr(objects,c+=1));
            hrEmp.setRemark(toStr(objects,c+=1));


            hrEmpList.add(hrEmp);
        }

        HrEmp selectEmp = null;
        SysUser sysUser = null;
        for (HrEmp emp : hrEmpList) {
            selectEmp = hrEmpMapper.selectHrEmpByEmpNum(emp.getEmpNum());
            if (selectEmp == null){

                if (StringUtils.isBlank(emp.getEmpName())){
                    throw new BusinessException("员工名称不能为空");
                }

                if (StringUtils.isBlank(emp.getEmpNum())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】工号不能为空");
                }

                if (StringUtils.isBlank(emp.getSex())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】性别不能为空");
                }

                if (StringUtils.isBlank(emp.getPhonenumber())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】手机号码不能为空");
                }

                if (StringUtils.isBlank(emp.getIdNumber())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】身份证号码不能为空");
                }

                if (Objects.isNull(emp.getBirthday())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】出生日期不能为空");
                }

                if (StringUtils.isBlank(emp.getCity())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】工作地不能为空");
                }

                if (Objects.isNull(emp.getDeptId())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】部门不能为空");
                }

                if (StringUtils.isBlank(emp.getEducation())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】教育背景不能为空");
                }

                if (Objects.isNull(emp.getPostId())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】岗位不能为空");
                }

                if (Objects.isNull(emp.getNonManagerDate())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】入职日期不能为空");
                }



                if (Objects.isNull(emp.getEmpStatus())){
                    throw new BusinessException("员工:【"+hrEmp.getEmpName()+"】状态不能为空");
                }

                insertEmp(emp,request);
                insertUser(emp,true);
            }else {
                //更新
                emp.setEmpId(selectEmp.getEmpId());
                updateEmp(emp,request);
                //userS数据
                sysUser = iSysUserService.selectUserById(selectEmp.getUserId());
                if (sysUser == null){
                    insertUser(emp,true);
                }else {
                    sysUser.setUserName(emp.getEmpName());
                    sysUser.setSex(emp.getSex());
                    sysUser.setPhonenumber(emp.getPhonenumber());
                    sysUser.setEmail(emp.getEmail());
                    sysUser.setDeptId(emp.getDeptId());
                    sysUser.setPostIds(new Long[]{emp.getPostId()});
                    iSysUserService.updateUserFromImport(sysUser);
                }

                //如果存在员工状态变更
                hrEmpTransfersService.insertEmpTransfers(selectEmp,emp);

            }
            //合同数据
            updateContracts(hrContractMap, emp);
            //薪资数据
            updateSalary(salaryStructureMap, emp);
            //工作经历数据
            insertOrUpdateExperience(empExperiencesMap, emp);
        }

        for (SysDept dept : deptMap.values()) {
            if (dept.getDeptId() != null && StringUtils.isNotEmpty(dept.getDelFlag()) && "2".equals(dept.getDelFlag())){
                dept.setDelFlag("0");
                iSysDeptService.updateDept(dept);
            }
        }

        return "导入成功";

    }

    @Override
    public String getDingUserId(SysUser sysUser, boolean isSleep) {
        String dingUserId = sysUser.getDingUserId();
        if (StringUtils.isEmpty(dingUserId)){

            if (sysUser.getPhonenumber().contains("/")){
                //导入员工数据时有些是有多个的，这里做切割处理
                String[] mobiles = sysUser.getPhonenumber().split("/");
                for (String mobile : mobiles) {
                    if (StringUtils.isNotEmpty(mobile)){
                        OapiUserGetByMobileResponse dingUserApiByMobile = dingUserApi.getByMobile(mobile);
                        if (dingUserApiByMobile.isSuccess()){
                            dingUserId = dingUserApiByMobile.getUserid();
                            sysUser.setDingUserId(dingUserId);
                            iSysUserService.updateUserInfo(sysUser);
                            break;
                        }
                    }
                }
            }else {
                OapiUserGetByMobileResponse dingUserApiByMobile = dingUserApi.getByMobile(sysUser.getPhonenumber());
                if (dingUserApiByMobile.isSuccess()){
                    dingUserId = dingUserApiByMobile.getUserid();
                    sysUser.setDingUserId(dingUserId);
                    iSysUserService.updateUserInfo(sysUser);
                }
            }
            if (isSleep){
                try {
                    //配额原因（每个企业的每个appkey调用单个接口的频率不可超过40次/秒），这里sleep一下o(╥﹏╥)o
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return dingUserId;

    }

    @Override
    public SysUser getSysUserByDingUserId(String dingUserId, boolean isSleep) {
        SysUser sysUser = iSysUserService.getSysUserByDingUserId(dingUserId);
        if (sysUser == null){
            //根据用户钉钉userId获取用户手机号
            OapiUserGetResponse oapiUserGetResponse = dingUserApi.get(dingUserId);
            if (oapiUserGetResponse.isSuccess()){
                String mobile = oapiUserGetResponse.getMobile();
                //根据手机号获取OA系统用户数据
                sysUser = iSysUserService.selectUserByPhoneNumberLike(mobile);
                if (sysUser != null){
                    //保存钉钉UserId
                    sysUser.setDingUserId(dingUserId);
                    iSysUserService.updateUserInfo(sysUser);
                }
            }
            if (isSleep){
                try {
                    //配额原因（每个企业的每个appkey调用单个接口的频率不可超过40次/秒），这里sleep一下o(╥﹏╥)o
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return sysUser;
    }

    @Override
    public List<HrEmp> selectEmpList() {
        return hrEmpMapper.selectEmpList();
    }

    @Override
    public List<Map<String,Object>> selectOnTheJobEmpList(int year,int month) {
        return hrEmpMapper.selectOnTheJobEmpList(year,month);
    }

    @Override
    public List<HrEmp> selectCountAttEmpList(int year,int month) {
        return hrEmpMapper.selectCountAttEmpList(year,month);
    }

    @Override
    public List<HrEmp> selectOnTheEmpList() {
        return hrEmpMapper.selectOnTheEmpList();
    }

    @Override
    public HrEmp selectTHrEmpByNumber(String empNum) {
        return hrEmpMapper.selectHrEmpByEmpNum(empNum);
    }

    @Override
    public List<HrEmp> importPhotos(MultipartFile ZIPfile) {
        List<HrEmp> results = new ArrayList<>();
        // 文件全局上传路径
        String filePath = Global.getUploadPath();
        // 文件项目路径
        String path = System.getProperty("user.dir");
        File newZIPfile = null;
        try {
            newZIPfile = FileUtils.multipartFileToFile(ZIPfile);
            // 解压缩
            File rootDir = ZipUtil.unzip(newZIPfile,Charset.forName("gbk"));
            if (rootDir.exists()) {
                File files[] = rootDir.listFiles();
                for (File file : files){
                    String extension = file.getName();
                    if (extension.contains("bmp") || extension.contains("gif") || extension.contains("jpg")
                            || extension.contains("jpeg") || extension.contains("png")) {
                        FileInputStream input = new FileInputStream(file);
                        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
                        String oldname = multipartFile.getOriginalFilename();
                        Assert.notNull(oldname,"图片名不能为空");
                        String empNum = oldname.split("-")[0];
                        HrEmp hrEmp = selectTHrEmpByNumber(empNum);
                        if (hrEmp != null) {
                            // 上传并返回新文件路径
                            String fileName = FileUploadUtils.upload(filePath, multipartFile);
                            hrEmp.setPhoto(fileName);
                            int i = hrEmpMapper.updateTHrEmp(hrEmp);
                            if (i>0) {
                                results.add(hrEmp);
                            }
                            log.info("[{}]：文件上传成功",oldname);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败,[{}]",e.getMessage());
        } finally {
            //删除拉取到系统目录的原压缩文件
            String originalFilename = ZIPfile.getOriginalFilename();
            String delZIPPath = path +"\\"+ originalFilename;
            String fileNameNoEx = FileUtils.getFileNameNoEx(originalFilename);
            String delFolderPath = path +"\\"+ fileNameNoEx;
            FileUtils.deleteFile(delZIPPath);
            log.info("压缩文件删除成功，[{}]",delZIPPath);
            FileUtils.deleteDirectory(delFolderPath);
            log.info("解压缩文件夹删除成功，[{}]",delFolderPath);
        }
        return results;
    }

    private String toStr(List<Object> objects, int i) {
        if (!(objects.size() > i)){
            return null;
        }
        String trim = (objects.get(i) + "").trim();
        if (StringUtils.isEmpty(trim) || "null".equals(trim) || "-".equals(trim)){
            return null;
        }
        return trim;
    }

    private String toSex(String idNumber) {
        int i = Integer.parseInt(idNumber.substring(16, 17)) % 2;
        return i == 0 ? "1" : "0";
    }

    private void updateSalary(Map<String, SalaryStructure> salaryStructureMap, HrEmp emp) {
        //薪资数据
        SalaryStructure salary = salaryStructureMap.get(emp.getEmpNum());
        if (salary != null && salary.getComprehensive() != null){
            salary.setEmpId(emp.getEmpId());

            SalaryStructure selectSalary = new SalaryStructure();
            selectSalary.setEmpId(emp.getEmpId());
            selectSalary.setDelFlag("0");
            selectSalary.setAuditStatus(2);
            selectSalary.setIsHistory(0);
            selectSalary.setMonthDate(new Date());//这个月有没有数据
            SalaryStructure salaryExample = salaryStructureService.selectOneBySalaryStructure(selectSalary);
            if (salaryExample != null){
                salaryExample.setComprehensive(salary.getComprehensive());
                salaryExample.setBasic(salary.getBasic());
                salaryExample.setOvertimePay(salary.getOvertimePay());
                salaryExample.setAllowance(salary.getAllowance());
                salaryExample.setOtherSubsidies(salary.getOtherSubsidies());
                salaryExample.setPerformanceBonus(salary.getPerformanceBonus());
                salaryExample.setRemark("导入数据");
                salaryStructureService.updateSalary(salaryExample);
            }else {
                salary.setRemark("导入数据");
                salary.setMonthDate(new Date());
                salary.setEmpName(emp.getEmpName());
                salary.setCreateId(ShiroUtils.getUserId());
                salary.setCreateBy(ShiroUtils.getLoginName());
                salaryStructureService.insertSalaryStructure(salary);
            }
        }
    }

    private void updateContracts(Map<String, HrContract> hrContractMap, HrEmp emp) {
        HrContract selectContract;
        List<HrContract> hrContracts;//合同数据
        HrContract contract = hrContractMap.get(emp.getEmpNum());
        if (contract != null && StringUtils.isNotEmpty(contract.getSubjectContract()) ){
            contract.setEmpId(emp.getEmpId());
            selectContract = new HrContract();
            selectContract.setEmpId(emp.getEmpId());
            hrContracts = hrContractService.selectHrContractListIsFrom(selectContract);
            if (hrContracts != null && !hrContracts.isEmpty()){
                //更新数据
                hrContracts.get(0).setSubjectContract(contract.getSubjectContract());
                hrContracts.get(0).setContractYear(contract.getContractYear());
                hrContracts.get(0).setContractStar(contract.getContractStar());
                hrContracts.get(0).setContractEnd(contract.getContractEnd());
                hrContracts.get(0).setRemark("导入数据");
                hrContractService.updateContract(hrContracts.get(0));
            }else {
                //插入数据
                contract.setRemark("导入数据");
                hrContractService.insertHrContract(contract);
            }

        }
    }

    /**
     * 插入或更新工作经历数据
     * @param empExperiencesMap
     * @param emp
     */
    private void insertOrUpdateExperience(Map<String, HrEmpExperiences> empExperiencesMap, HrEmp emp) {
        HrEmpExperiences experiences = empExperiencesMap.get(emp.getEmpNum());
        if (experiences != null) {
            experiences.setParentId(emp.getEmpId());
            HrEmpExperiences selectExerience = new HrEmpExperiences();
            selectExerience.setParentId(emp.getEmpId());
            selectExerience.setDelFlag("0");
            List<HrEmpExperiences> hrEmpExperiencesList = hrEmpExperiencesService.selectHrEmpExperiencesList(selectExerience);
            if (hrEmpExperiencesList != null && !hrEmpExperiencesList.isEmpty()){
                //更新数据
                hrEmpExperiencesList.get(0).setEntryDate(experiences.getEntryDate());
                hrEmpExperiencesList.get(0).setQuitDate(experiences.getQuitDate());
                hrEmpExperiencesList.get(0).setCompany(experiences.getCompany());
                hrEmpExperiencesList.get(0).setPosition(experiences.getPosition());
                hrEmpExperiencesList.get(0).setRemark(experiences.getRemark());
                hrEmpExperiencesService.updateHrEmpExperiences(hrEmpExperiencesList.get(0));
            }else {
                //插入数据
                experiences.setRemark("导入数据");
                hrEmpExperiencesService.insertHrEmpExperiences(experiences);
            }

        }
    }
    
    
    private SysDept getSysDept(String deptName0,
                               String deptName1,
                               String deptName2,
                               String deptName3,
                               String deptName4,
                               String deptName5,
                               String deptName6,
                               Map<String,SysDept> deptMap) {
        String[] deptNameS = new String[]{deptName0,deptName1,deptName2,deptName3,deptName4,deptName5,deptName6};
        String deptName = "";
        int count = -1;
        for (int i = deptNameS.length - 1; i >= 0; i--) {
            //寻找部门位置在第几级别
            if (StringUtils.isNotEmpty(deptNameS[i]) && !"-".equals(deptNameS[i])){
                if (StringUtils.isEmpty(deptName)){
                    deptName = deptNameS[i];
                    count = i;
                    break;
                }
            }
        }
        Long pId = 100L;
        SysDept pDept = null;
        for (int i = 0; i < count; i++) {
            if (StringUtils.isNotEmpty(deptNameS[i]) && !"-".equals(deptNameS[i])){
                pDept = deptMap.get(deptNameS[i]+pId );
                if (pDept == null){
                    pDept = iSysDeptService.selectOneDeptByName(deptNameS[i],pId);
                    if (pDept != null && pDept.getDelFlag().equals("2")){
                        //查询到的数据是已删除的数据，需要插入新数据
                        pDept = null;
                    }
                    if (Objects.isNull(pDept)){
                        pDept = new SysDept();
                        pDept.setDeptName(deptNameS[i]);
                        pDept.setOrderNum("100");
                        pDept.setLeader(ShiroUtils.getUserId());
                        pDept.setPhone(ShiroUtils.getSysUser().getPhonenumber());
                        pDept.setEmail(ShiroUtils.getSysUser().getEmail());
                        pDept.setCreateBy(ShiroUtils.getLoginName());
                        pDept.setParentId(pId);
                        iSysDeptService.insertDept(pDept, ShiroUtils.getLoginName());
                    }
                    deptMap.put(deptNameS[i]+pDept.getParentId(),pDept);
                }
                pId = pDept.getDeptId();
            }
        }

        SysDept sysDept = deptMap.get(deptName+pId);
        if (sysDept == null){

            sysDept = getSysDeptDiGui(deptMap, deptNameS, deptName, count,pId);

            deptMap.put(deptName+sysDept.getDeptId(),sysDept);
        }
        return sysDept;
    }

    private SysDept getSysDeptDiGui(Map<String, SysDept> deptMap, String[] deptNameS, String deptName, int count, Long pId) {
        SysDept sysDept;
        sysDept = iSysDeptService.selectOneDeptByName(deptName, pId);
        if (sysDept != null && sysDept.getDelFlag().equals("2")){
            //查询到的数据是已删除的数据，需要插入新数据
            sysDept = null;
        }
        if (sysDept == null){
            sysDept = new SysDept();
            sysDept.setDeptName(deptName);
            sysDept.setOrderNum("100");
            sysDept.setLeader(ShiroUtils.getUserId());
            sysDept.setPhone(ShiroUtils.getSysUser().getPhonenumber());
            sysDept.setEmail(ShiroUtils.getSysUser().getEmail());
            sysDept.setCreateBy(ShiroUtils.getLoginName());
            sysDept.setParentId(pId);
            iSysDeptService.insertDept(sysDept, ShiroUtils.getLoginName());
        }
        return sysDept;
    }


    private SysPost getSysPost(String postName, Map<String, SysPost> postMap) {
        SysPost sysPost = postMap.get(postName);
        if (sysPost == null){
            sysPost = iSysPostService.selectOnePostByName(postName);
            //
            if (sysPost == null){
                sysPost = new SysPost();
                sysPost.setPostSort("100");
                sysPost.setPostName(postName);
                sysPost.setStatus("0");
                sysPost.setPostCode(getLoginName(postName));
                sysPost.setRemark("导入数据");
                sysPost.setCreateBy(ShiroUtils.getLoginName());
                sysPost.setCreateId(ShiroUtils.getUserId());
                iSysPostService.insertPost(sysPost);
            }
            postMap.put(postName,sysPost);
        }
        return sysPost;
    }

    private Date toBirthday(String trim) {
        if (StringUtils.isEmpty(trim) || "null".equals(trim) || "-".equals(trim)){
            return null;
        }
        String year = trim.substring(6, 10);
        String m = trim.substring(10, 12);
        String d = trim.substring(12, 14);
        return DateUtil.parse(year+"-"+m+"-"+d,"yyyy-MM-dd");
    }

    private Date toDate(String dateStr, List<Object> objects){
        if (StringUtils.isEmpty(dateStr) || "null".equals(dateStr) || "-".equals(dateStr)){
            return null;
        }
        try {

            return DateUtil.parse(dateStr,"yyyy-MM-dd");
        }catch (Exception e){
            throw new RuntimeException("objects:"+Arrays.toString(objects.toArray()) + " - " + e.getMessage());
        }
    }

    private static Pattern p = Pattern.compile("^[1-9]\\d*$");
    private BigDecimal toBig(String str){
        if (StringUtils.isEmpty(str) || "null".equals(str) || "-".equals(str)){
            return null;
        }
        Matcher matcher = p.matcher(str);
        if (matcher.find()){
            String group = matcher.group(0);
            if (StringUtils.isNotEmpty(group)){
                return new BigDecimal(group);
            }
        }

        return null;
    }

}
