package com.xtoa.web.webToken;

import com.ruoyi.centre.domain.CentrePublicConfig;
import com.ruoyi.centre.domain.CentreSysConfig;
import com.ruoyi.centre.service.ICentrePublicConfigService;
import com.ruoyi.centre.service.ICentreSysConfigService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.centre.service.WebTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/webApi")
public class WebTokenApi {

    @Autowired
    private WebTokenService webTokenService;

    @Autowired
    private ICentreSysConfigService centreSysConfigService;
    @Autowired
    private ICentrePublicConfigService centrePublicConfigService;
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Value("${dingMsgUrl}")
    private String serverUrl;
/*

    */
/**
     * 认证接口
     * @param user
     * @return
     *//*

    @GetMapping("/getTokenTest")
    public RedirectView getTokenTest(SysUser user, String sysId){
        try {
             return webTokenService.getTokenTest(user,sysId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RedirectView redirectTarget = new RedirectView();
        redirectTarget.setContextRelative(true);
        redirectTarget.setUrl("http://192.168.10.30:8080/admin/loginOA");
        return redirectTarget;
    }
*/
/**
     * 认证接口
     * @param user
     * @return
     *//*

    @PostMapping("/getToken")
    @ResponseBody
    public AjaxResult getToken(SysUser user,String sysId){
        try {
            return webTokenService.getToken(user,sysId);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("系统异常");
        }
    }
*/

    @PostMapping("/parseToken")
    @ResponseBody
    public AjaxResult parseToken(String token,String sysId) throws Exception {
        return webTokenService.parseToken(token, sysId);
    }

    /**
     * 获取第三方系统列表
     * @param centreSysConfig
     * @return
     */
    @GetMapping("/getCentreSysConfigList")
    @ResponseBody
    public AjaxResult getCentreSysConfigList(CentreSysConfig centreSysConfig) {
        try {
            centreSysConfig.setEnable(true);
            List<CentreSysConfig> list = centreSysConfigService.selectCentreSysConfigList(centreSysConfig);
            for (CentreSysConfig sysConfig : list) {
                sysConfig.setSecretKey(null);
//                sysConfig.setLoginLogo(serverUrl + sysConfig.getLoginLogo());
                sysConfig.setLogo(serverUrl + sysConfig.getLogo());
            }
            return AjaxResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error("系统异常");
    }

    /**
     * 获取公共服务列表
     * @param centrePublicConfig
     * @return
     */
    @GetMapping("/getCentrePublicConfigList")
    @ResponseBody
    public AjaxResult getCentrePublicConfigList(CentrePublicConfig centrePublicConfig) {
        try {
            centrePublicConfig.setEnable(true);
            List<CentrePublicConfig> list = centrePublicConfigService.selectCentrePublicConfigList(centrePublicConfig);
            for (CentrePublicConfig sysConfig : list) {
                sysConfig.setLogo(serverUrl + sysConfig.getLogo());
            }
            return AjaxResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error("系统异常");
    }

    /**
     * 获取字典数据
     * @param dictType
     * @return
     */
    @GetMapping("/getDictByType")
    @ResponseBody
    public AjaxResult getDictByType(String dictType) {
        try {
            List<SysDictData> sysDictData = dictTypeService.selectDictDataByType(dictType);
            List<Map<String , String>> list = new ArrayList<>();
            Map<String , String> map = null;
            for (SysDictData sysDictDatum : sysDictData) {
                map = new HashMap<>();
                map.put("dictLabel",sysDictDatum.getDictLabel());
                map.put("dictValue",sysDictDatum.getDictValue());
                map.put("isDefault",sysDictDatum.getIsDefault());
                list.add(map);
            }
            return AjaxResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error("系统异常");
    }


}
