package com.ruoyi.hr.manager;

import com.ruoyi.hr.domain.CompanyDeptEnum;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcessManager {

    @Autowired
    private ISysDeptService sysDeptService;

    @Resource
    private ISysUserService userService;

    /**
     * 通过员工ID，返回其公司对应的数字类型
     * @param userId
     * @return
     */
    public int getCompany(Long userId){
        SysUser sysUser = userService.selectUserById(userId);
        SysDept sysDept = sysDeptService.selectCompanyByUserDeptId(sysUser.getDeptId());


        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.HQ.getName().equals(sysDept.getDeptName())) {
            //集团总部
            return CompanyDeptEnum.HQ.getNum();
        }
        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.SHENZHEN.getName().equals(sysDept.getDeptName())) {
            //深圳快马
            return CompanyDeptEnum.SHENZHEN.getNum();
        }
        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.DONGGUAN.getName().equals(sysDept.getDeptName())) {
            //东莞快马
            return CompanyDeptEnum.DONGGUAN.getNum();
        }
        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.ZHONGZHU.getName().equals(sysDept.getDeptName())) {
            //中珠快马
            return CompanyDeptEnum.ZHONGZHU.getNum();
        }
        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.GUANGFO.getName().equals(sysDept.getDeptName())) {
            //广佛快马
            return CompanyDeptEnum.GUANGFO.getNum();
        }
        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.WUHAN.getName().equals(sysDept.getDeptName())) {
            //武汉分公司
            return CompanyDeptEnum.WUHAN.getNum();
        }
        if ("0,100".equals(sysDept.getAncestors()) && CompanyDeptEnum.HK.getName().equals(sysDept.getDeptName())) {
            //香港公司
            return CompanyDeptEnum.HK.getNum();
        }

        return 0;
    }

    /**
     * 通过员工ID，返回其对应的一级部门数字类型
     * @param userId
     * @return
     */
    public int getFirstDept(Long userId){
        SysUser sysUser = userService.selectUserById(userId);
        SysDept sysDept = sysDeptService.selectDeptByIdSimple(sysUser.getDeptId());
        String ancestorArray = sysDept.getAncestors();
        String[] deptIds = ancestorArray.split(",");
        List<String> ids = new ArrayList<>(Arrays.asList(deptIds));
        ids.add(sysUser.getDeptId().toString());
        ids.remove("0");
        ids.remove("100");
        if(!ids.isEmpty() && ids.size() > 1){
            SysDept firstDept = sysDeptService.selectDeptByIdSimple(Long.valueOf(ids.get(1)));
            if (CompanyDeptEnum.YUNYING.getName().equals(firstDept.getDeptName())) {
                //运营部
                return CompanyDeptEnum.YUNYING.getNum();
            }
            if (CompanyDeptEnum.KEFU.getName().equals(firstDept.getDeptName())) {
                //客服部
                return CompanyDeptEnum.KEFU.getNum();
            }
            if (CompanyDeptEnum.XIAOSHOU.getName().equals(firstDept.getDeptName())) {
                //销售部
                return CompanyDeptEnum.XIAOSHOU.getNum();
            }
            if (CompanyDeptEnum.CAIGOU.getName().equals(firstDept.getDeptName())) {
                //采购部
                return CompanyDeptEnum.CAIGOU.getNum();
            }
            if (CompanyDeptEnum.HR.getName().equals(firstDept.getDeptName())) {
                //人力行政部
                return CompanyDeptEnum.HR.getNum();
            }
            if (CompanyDeptEnum.CAIWU.getName().equals(firstDept.getDeptName())) {
                //财务部
                return CompanyDeptEnum.CAIWU.getNum();
            }
            if (CompanyDeptEnum.CANGCHU.getName().equals(firstDept.getDeptName())) {
                //仓储配送
                return CompanyDeptEnum.CANGCHU.getNum();
            }
        }

        return 0;
    }

    /**
     * 通过员工ID，返回其对应的二级部门
     * @param userId
     * @return
     */
    public SysDept getSecondDept(Long userId){
        SysUser sysUser = userService.selectUserById(userId);
        SysDept sysDept = sysDeptService.selectDeptByIdSimple(sysUser.getDeptId());
        String ancestorArray = sysDept.getAncestors();
        String[] deptIds = ancestorArray.split(",");
        List<String> ids = new ArrayList<>(Arrays.asList(deptIds));
        ids.add(sysUser.getDeptId().toString());
        ids.remove("0");
        ids.remove("100");
        if(!ids.isEmpty() && ids.size() > 2){
            return sysDeptService.selectDeptByIdSimple(Long.valueOf(ids.get(2)));
        }
        return null;
    }

    /**
     * 返回指定个数的员工上级领导登录名（不包括admin账号，他自己还有陈德铭），最多返回3个
     * @param userId
     * @param num
     * @return
     */
    public List<String> getDeptLeaderLoginNames(Long userId,int num){
        if(num > 3){
            num = 3;
        }
        List<String> deptLeaders = new ArrayList<>();
        SysUser sysUser = userService.selectUserById(userId);
        String loginName = sysUser.getLoginName();
        String[] ancestors = sysUser.getDept().getAncestors().split(",");
        SysDept dept1 = sysDeptService.selectDeptById(sysUser.getDept().getDeptId());
        SysUser leader1 = userService.selectUserById(dept1.getLeader());
        deptLeaders.add(leader1.getLoginName());
        Arrays.asList(ancestors).stream().filter(a -> !"0".equals(a) && !"100".equals(a)).sorted(Comparator.reverseOrder()).forEach(deptId->{
            SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(deptId));
            SysUser leader = userService.selectUserById(sysDept.getLeader());
            String leaderLoginName = leader.getLoginName();
            deptLeaders.add(leaderLoginName);
        });
        List<String> deptLeaderLoginNames = deptLeaders.stream().
                filter(leader -> !loginName.equals(leader) && !"chendeming".equals(leader) && !"admin".equals(leader)).
                distinct().limit(num).collect(Collectors.toList());
        return deptLeaderLoginNames;
    }

    /**
     * 是否是部门负责人
     * @return
     */
    public boolean isDeptLeader(Long userId){
        SysUser sysUser = userService.selectUserById(userId);
        List<SysDept> deptList = sysDeptService.selectDeptListAll(new SysDept());
        if (!deptList.isEmpty()) {
            if (deptList.stream().anyMatch(dept -> {
                if (userId.equals(dept.getLeader())) {
                    return true;
                }
                return false;
            })) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是仓储东仓
     * @return
     */
    public boolean isEast(Long userId){
        boolean isEast = false;
        SysDept secondDept = getSecondDept(userId);
        if (secondDept != null && "深圳东仓".equals(secondDept.getDeptName())){
            isEast = true;
        }
        return isEast;
    }


}
