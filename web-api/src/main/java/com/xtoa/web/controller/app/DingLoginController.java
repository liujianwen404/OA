package com.xtoa.web.controller.app;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.dingTalk.DingUserApi;
import com.ruoyi.base.provider.appApi.UserApi;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.token.CustomUsernamePasswordToken;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.taobao.api.ApiException;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/webApi")
public class DingLoginController {
    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private DingUserApi dingUserApi;

    @Reference(retries = 0,check = false)
    private UserApi userApi;

    private static final Logger log = LoggerFactory.getLogger(DingLoginController.class);

    @RequestMapping("/dingLogin")
    @ResponseBody
    public AjaxResult dingLogin(String code){
        OapiUserGetuserinfoResponse response = null;
        try {
            log.info("登录开始！");
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
            OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
            request.setCode(code);
            request.setHttpMethod("GET");
            response = client.execute(request, dingConfig.getAccessToken());
            if (response == null  || !response.isSuccess()){
                return AjaxResult.error("用户不存在");
            }
            String userId = response.getUserid();
//            SysUser sysUser = getSysUserByDingUserId(userId,false);
            SysUser sysUser = userApi.getSysUserByDingUserID(userId);
            if(sysUser == null){
                return AjaxResult.error("用户不存在");
            }
            //执行shiro免密登录
            CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(sysUser.getLoginName());
            Subject subject = SecurityUtils.getSubject();
            String sessionId = (String) subject.getSession().getId();
            try
            {
                subject.login(token);
                Session session = subject.getSession();
                session.setAttribute("user",sysUser);
                String userName = ShiroUtils.getUserName();
                //取出身份信息
                Map<String,Object> map = new HashMap<>();
                map.put("userId",sysUser.getUserId());
                map.put("userName",userName);
                map.put("sessionId",sessionId);
                return AjaxResult.success("登录成功",map);
            }
            catch (AuthenticationException e)
            {
                String msg = "用户或密码错误";
                if (StringUtils.isNotEmpty(e.getMessage()))
                {
                    msg = e.getMessage();
                }
                return AjaxResult.error(msg);
            }
//            Map<String,Object> map = new HashMap<>();
//            map.put("userId",sysUser.getUserId());
//            map.put("userName",sysUser.getUserName());
//            return AjaxResult.success("登录成功",map);
        } catch (ApiException e) {
            e.printStackTrace();
            return AjaxResult.error("调用钉钉获取用户失败");
        }
    }

    /**
     * 此接口只方便测试登录使用 因为要集成framework模块进来，所以后期废弃时尽量去掉此模块
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    @PostMapping("/appLogin")
    @ResponseBody
    public AjaxResult appLogin(String username, String password, Boolean rememberMe)
    {
        CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        String sessionId = (String) subject.getSession().getId();
        try
        {
            subject.login(token);
            Long userId = ShiroUtils.getUserId();
            SysUser sysUser = ShiroUtils.getSysUser();
//            SysUser sysUser = userApi.getSysUserByUserID(userId);
            if(sysUser == null){
                return AjaxResult.error("用户不存在");
            }
            Session session = subject.getSession();
            session.setAttribute("user",sysUser);
            String userName = ShiroUtils.getUserName();
            Map<String,Object> map = new HashMap<>();
            map.put("userId",userId);
            map.put("userName",userName);
            map.put("sessionId",sessionId);
            return AjaxResult.success("登录成功",map);
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

//    @HystrixCommand(fallbackMethod = "verifyError")
    @PostMapping("/verify")
    @ResponseBody
    public AjaxResult verify(String sessionId, HttpServletRequest request, HttpServletResponse response){
//        验证h5用户是否安全登录
        if(!ShiroUtils.isAuthenticated(sessionId, request, response)){
            return AjaxResult.error("非法访问！");
        }
        return AjaxResult.success();
    }

    /**
     * 熔断方法
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    public AjaxResult verifyError(String sessionId, HttpServletRequest request, HttpServletResponse response){
        return AjaxResult.error("登录状态验证出错！");
    }

    @RequestMapping("/dingLoginTest")
    @ResponseBody
    public AjaxResult dingLoginTest(String userId){
        SysUser sysUser = userApi.getSysUserByDingUserID(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",sysUser.getUserId());
        map.put("userName",sysUser.getUserName());
        return AjaxResult.success("登录成功",map);
    }

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
}
