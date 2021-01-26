package com.ruoyi.framework.util;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.shiro.session.OnlineSessionDAO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.shiro.realm.UserRealm;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * shiro 工具类
 * 
 * @author ruoyi
 */
@Component
public class ShiroUtils
{

    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }

    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout()
    {
        getSubject().logout();
    }

    public static SysUser getSysUser()
    {
//        if (Global.getName().contains("web")){
//            //web项目
//            return new SysUser();
//        }

        SysUser user = null;
        Object obj = getSubject().getPrincipal();
        if (StringUtils.isNotNull(obj))
        {
            user = new SysUser();
            BeanUtils.copyBeanProp(user, obj);
        }
        return user;
    }

    public static void setSysUser(SysUser user)
    {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static Long getUserId()
    {
        Long userId = getSysUser().getUserId();
        return userId == null ? null : userId.longValue();
    }

    public static String getLoginName()
    {
        return getSysUser().getLoginName();
    }

    public static String getUserName()
    {
        return getSysUser().getUserName();
    }

    public static String getIp()
    {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId()
    {
        return String.valueOf(getSubject().getSession().getId());
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt()
    {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }

    /**
     *
     * <p>description: 获取SysUser并保存至session中一份</p>
     * @return
     * @date 2020年10月20日 下午16:12:23
     * @author liujianwen
     */
    public static SysUser getSysUserBySession(String sessionId, HttpServletRequest request, HttpServletResponse response){
        SessionKey key = new WebSessionKey(sessionId,request,response);
        Session session = SecurityUtils.getSecurityManager().getSession(key);
        //取出身份信息
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
        SysUser sysUser = (SysUser) coll.getPrimaryPrincipal();
        if(sysUser!=null){
            SysUser user = (SysUser) session.getAttribute("user");
            if(user==null){
                session.setAttribute("user", sysUser);
            }
            return sysUser;
        }else{
            return null;
        }
    }

    /**
     * 根据sessionId 获取用户信息
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    /*public static SysUser getSysUserBySession(String sessionId, HttpServletRequest request, HttpServletResponse response){
        boolean status = false;
        OnlineSessionDAO onlineSessionDAO = SpringUtils.getBean(OnlineSessionDAO.class);
        Session se = onlineSessionDAO.readSession(sessionId);
        Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        //org.apache.shiro.subject.SimplePrincipalCollection cannot be cast to com.hncxhd.bywl.entity.manual.UserInfo
        SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
        SysUser sysUser = (SysUser) coll.getPrimaryPrincipal();

        if (sysUser != null) {
            SysUser user = (SysUser) se.getAttribute("user");
            if (user == null) {
                se.setAttribute("user", sysUser);
            }
            return sysUser;
        } else {
            return null;
        }
    }*/


    /**
     * 验证是否登陆
     *
     * org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY ; true
     * org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY ; com.hncxhd.bywl.entity.manual.UserInfo@533752b2
     */
    /*public static boolean isAuthenticated(String sessionID,HttpServletRequest request,HttpServletResponse response){
        boolean status = false;

        OnlineSessionDAO onlineSessionDAO = SpringUtils.getBean(OnlineSessionDAO.class);
        SessionKey key = new WebSessionKey(sessionID,request,response);
        try{
            Session se = onlineSessionDAO.readSession(sessionID);
            Object obj = se.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
            if(obj != null){
                status = (Boolean) obj;
            }
        }catch(UnknownSessionException e){
            e.printStackTrace();
        }finally{
            Session se = null;
            Object obj = null;
        }

        return status;
    }*/

    public static boolean isAuthenticated(String sessionID,HttpServletRequest request,HttpServletResponse response){
        boolean status = false;

        SessionKey key = new WebSessionKey(sessionID,request,response);
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(key);
            Object obj = se.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
            if(obj != null){
                status = (Boolean) obj;
            }
        }catch(UnknownSessionException e){
            e.printStackTrace();
        }finally{
            Session se = null;
            Object obj = null;
        }

        return status;
    }


}
