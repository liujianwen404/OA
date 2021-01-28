package com.ruoyi.centre.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.centre.domain.CentreSysConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.JwtUtil;
import com.ruoyi.system.domain.CentreSysRole;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ICentreSysRoleService;
import com.ruoyi.system.service.ISysUserService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WebTokenService {

    private Logger logger = LoggerFactory.getLogger(WebTokenService.class);

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ICentreSysConfigService centreSysConfigService;
    @Autowired
    private ICentreSysRoleService centreSysRoleService;

    public AjaxResult getToken(SysUser user, String sysId){

        Example example = new Example(CentreSysConfig.class);
        example.createCriteria().andEqualTo("sysId",sysId);
        CentreSysConfig centreSysConfig = centreSysConfigService.selectOneByExample(example);

        if (centreSysConfig == null || !centreSysConfig.getEnable()){
            return AjaxResult.error("无效的系统");
        }

        SysUser sysUser = sysUserService.selectUserByPhoneNumber(user.getPhonenumber());
        if (sysUser == null || !Objects.equals("0",sysUser.getStatus()) || !Objects.equals("0",sysUser.getDelFlag()) ){
            return AjaxResult.error("无效的用户");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("userName",sysUser.getLoginName());
//        map.put("id",sysUser.getUserId());
        map.put("name",sysUser.getUserName());
        map.put("email",sysUser.getEmail());
        map.put("mobile",sysUser.getPhonenumber());

        List<SysRole> roles = sysUser.getRoles();
        Set<Object> roleSet = new HashSet<>();
        Set<Object> wmsCodes = new HashSet<>();
//        Set<Object> areaSet = new HashSet<>();
        //OA角色映射系统角色集合
        CentreSysRole centreSysRole = new CentreSysRole();
        centreSysRole.setCentreSysId(centreSysConfig.getSysId());
        List<CentreSysRole> centreSysRoles = centreSysRoleService.selectCentreSysRoleList(centreSysRole);
        Map<Long, CentreSysRole> centreSysRoleMap = centreSysRoles.stream().collect(Collectors.toMap(CentreSysRole::getOaRoleId, csr -> csr));

        for (SysRole role : roles) {
            //收集区域
            /*if (StrUtil.isNotBlank(role.getAreaIds())){
                String[] split = role.getAreaIds().split(",");
                areaSet.addAll(Arrays.asList(split));
            }*/
            //收集wms仓库编号
            if (StrUtil.isNotBlank(role.getWmsCode())){
                String[] split = role.getWmsCode().split(",");
                wmsCodes.addAll(Arrays.asList(split));
            }
            //收集映射角色
            if (centreSysRoleMap.get(role.getRoleId()) != null){
                roleSet.add(centreSysRoleMap.get(role.getRoleId()).getRoleId());
            }
        }
        map.put("roles",roleSet);
        map.put("areaIds",new int[]{centreSysConfig.getAreaId()});
        map.put("wmsCodes",wmsCodes);
        map.put("ext",new HashMap<String, Object>());

        String jsonString = JSON.toJSONString(map);
        logger.info("jsonString:" + jsonString);

        String jwt = JwtUtil.createJWT(sysUser.getUserId()+"", jsonString, null,centreSysConfig.getSecretKey());
        return AjaxResult.success("操作成功",jwt);

    }


    public AjaxResult parseToken(String token, String sysId) throws Exception {
        Example example = new Example(CentreSysConfig.class);
        example.createCriteria().andEqualTo("sysId",sysId);
        CentreSysConfig centreSysConfig = centreSysConfigService.selectOneByExample(example);
        if (centreSysConfig == null || !centreSysConfig.getEnable()){
            return AjaxResult.error("无效的系统");
        }
        Claims claims = JwtUtil.parseJWT(token,centreSysConfig.getSecretKey());
        String subject = claims.getSubject();

        return AjaxResult.success("操作成功",JSON.parse(subject));
    }

    public RedirectView getTokenTest(SysUser user, String sysId) {
        RedirectView redirectTarget = new RedirectView();
        redirectTarget.setContextRelative(true);
        redirectTarget.setUrl("http://192.168.10.30:8080/admin/loginOA");
        return redirectTarget;
    }
}
