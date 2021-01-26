package com.ruoyi.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.centre.service.WebTokenService;
import com.ruoyi.framework.shiro.token.CustomUsernamePasswordToken;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController
{
    @Autowired
    private WebTokenService webTokenService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.get("centreSysId") != null && parameterMap.get("centreSysId").length > 0){
            modelMap.put("centreSysId",parameterMap.get("centreSysId")[0]);
            logger.info("centreSysId="+parameterMap.get("centreSysId")[0]);
        }
        if (parameterMap.get("callBackUrl") != null && parameterMap.get("callBackUrl").length > 0){
            modelMap.put("callBackUrl",parameterMap.get("callBackUrl")[0]);
            logger.info("callBackUrl="+parameterMap.get("callBackUrl")[0]);
        }

        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(HttpServletResponse response,String username, String password, Boolean rememberMe,String centreSysId,String callBackUrl)
    {
        logger.info(StrUtil.format("centreSysId={},callBackUrl={}",centreSysId,callBackUrl));

        CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);

            if (StrUtil.isNotBlank(centreSysId)){

                if (username.equals("admin")){
                    return error("外部系统介入失败：admin账号不需要使用OA认证");
                }

                if (StrUtil.isBlank(callBackUrl)){
                    return error("外部系统介入失败：回调地址未设置");
                }

                SysUser sysUser = ShiroUtils.getSysUser();
                AjaxResult tokenJwt = webTokenService.getToken(sysUser, centreSysId);
                if (tokenJwt.get("code").equals(0)){
                    logger.info("jwt="+tokenJwt.get("data"));
                    Map<String, Object> returnMap = new HashMap<>();
                    returnMap.put("jwt",tokenJwt.get("data"));
                    returnMap.put("callBackUrl",callBackUrl);
                    return new AjaxResult(AjaxResult.Type.identificationOK,"认证成功",returnMap);
                }else {
                    logger.info("msg="+tokenJwt.get("msg"));
                    return error("外部系统介入失败：" + tokenJwt.get("msg"));
                }
            }

            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }

}
