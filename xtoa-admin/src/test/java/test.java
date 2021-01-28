import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetByMobileRequest;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.ruoyi.XtHrmApplication;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.utils.DingDingUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XtHrmApplication.class)
public class test {

    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private ISysUserService sysUserService;

    @Test
    public void test01() throws ApiException {
        List<SysUser> userList = sysUserService.selectUserListWithDingUserIdNull();
        Map<String,String> phoneMap = new HashMap<>();
        for (SysUser user : userList) {
            String userName = user.getUserName();
            String phonenumber = user.getPhonenumber();
            String dingDingUserId = null;
            try {
                //单个接口的频率不可超过40次/秒，否则返回错误码90018
                Thread.sleep(100);
                dingDingUserId = DingDingUtil.getDingDingUserIdByPhone(phonenumber);
                if(StringUtils.isNotBlank(dingDingUserId)) {
                    user.setDingUserId(dingDingUserId);
                    sysUserService.updateUserInfo(user);
                    log.info("同步员工dingdingUserId成功,userName:{},phonenumber:{}", userName, phonenumber);
                }else{
                    phoneMap.put(userName,phonenumber);
                }
            } catch (ApiException e) {
                e.printStackTrace();
                log.error("同步dingDingUserId失败,userName:{},phonenumber:{}", userName, phonenumber);
            } catch (InterruptedException e) {
                log.error("同步dingDingUserId接口调用超时");
                e.printStackTrace();
            }
        }
        log.info("钉钉userid获取不到的手机号集合：{}",phoneMap.toString());
    }
}
