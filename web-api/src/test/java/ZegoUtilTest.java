import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.dingTalk.DingUserApi;
import com.ruoyi.base.service.MQService;
import com.ruoyi.system.domain.SysUser;
import com.taobao.api.ApiException;
import com.xtoa.web.WebApiApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApiApplication.class)//这里是springBoot启动类
//@ContextConfiguration(classes = {I18Config.class, MybatisConfig.class, MybatisMapperScannerConfig.class, PropertiesConfig.class, QuartzConfiguration.class, RedisConfig.class})
public class ZegoUtilTest {

    private static Logger logger = LoggerFactory.getLogger(ZegoUtilTest.class);

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }
    
    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private MQService mqService;


    @Autowired
    private DingUserApi dingUserApi;


    @Test
    public void test01() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest req = new OapiDepartmentListRequest();
        req.setLang("1");
        req.setFetchChild(true);
        req.setId("1");
        req.setHttpMethod("GET");
        OapiDepartmentListResponse rsp = client.execute(req, dingConfig.getAccessToken());
        System.out.println(rsp.getBody());
    }

}