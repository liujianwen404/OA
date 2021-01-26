package com.ruoyi.hr.service.impl;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.hr.mapper.InfoReportMapper;
import com.ruoyi.hr.service.IInfoReportService;
import com.ruoyi.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工Service业务层处理
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Service
public class InfoReportServiceImpl implements IInfoReportService
{
    private static final Logger log = LoggerFactory.getLogger(InfoReportServiceImpl.class);

    @Autowired
    private InfoReportMapper infoReportMapper;

    @Autowired
    private ISysConfigService configService;


    /**
     * 查询部门人数统计列表
     *
     * @return 部门人数统计集合
     */
    @Override
    public List<Map<Integer, String>> selectEmpList()
    {
        return infoReportMapper.selectEmpList();
    }

    /**
     * 查询部门转正人数统计列表
     *
     * @return 部门转正人数统计集合
     */
    @Override
    public List<Map<Integer, String>> selectRegularList() {
        return infoReportMapper.selectRegularList();
    }

    @Override
    public List<Map<Integer, String>> selectRecruitList() {
        return infoReportMapper.selectRecruitList();
    }

    @Override
    @DataScope(deptAlias = "d", menuAlias = "hr:report:quit:view")
    public List<HrEmp> selectQuitList(HrEmp emp) {
        return infoReportMapper.selectQuitList(emp);
    }

    @Override
    public List<Map<Integer, String>> selectQuitReportList() {
        return infoReportMapper.selectQuitReportList();
    }

    @Override
    public List<Map<String, Object>> selectJobTransferReportList() {
        return infoReportMapper.selectJobTransferReportList();
    }

    @Override
    @DataScope(deptAlias = "d", menuAlias = "hr:report:regular:view")
    public List<HrEmp> selectEmpRegularList(HrEmp emp) {
        return infoReportMapper.selectEmpRegularList(emp);
    }


}
