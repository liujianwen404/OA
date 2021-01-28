import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatGetRequest;
import com.dingtalk.api.request.OapiUserGetByMobileRequest;
import com.dingtalk.api.response.OapiChatGetResponse;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.ruoyi.XtHrmApplication;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.quartz.task.AttendanceStatisticsOfDayTask;
import com.ruoyi.quartz.task.ReissueTask;
import com.ruoyi.quartz.task.SalaryStructureTask;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.redisService.repository.SysDeptRedisRepository;
import com.taobao.api.ApiException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XtHrmApplication.class)//这里是springBoot启动类
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
    private IHrLeaveService hrLeaveService;

    @Autowired
    private IHrOvertimeService hrOvertimeService;

    @Autowired
    private AttendanceStatisticsOfDayTask task;

    @Autowired
    private DingConfig dingConfig;

    @Test
    public void test01() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_by_mobile");
        OapiUserGetByMobileRequest req = new OapiUserGetByMobileRequest();
        req.setMobile("18928255923");
        req.setHttpMethod("GET");
        OapiUserGetByMobileResponse rsp = client.execute(req, dingConfig.getAccessToken());
        System.out.println(rsp.getBody());
    }


    @Test
    public void test02(){
        //加班数据拆分测试
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(318L);
        hrOvertimeService.saveHoliday(hrOvertime);
    }

    @Autowired
    private ReissueTask reissueTask;

    @Test
    public void test03(){
        //补卡数据插入测试
        reissueTask.reissueTaskService();
    }
    @Autowired
    private SalaryStructureTask salaryStructureTask;

    @Test
    public void test04(){
        //薪资核算测试
        salaryStructureTask.salaryStructureTaskService();
    }


    @Autowired
    SysDeptRedisRepository sysDeptRedisRepository;


    @Autowired
    SysDeptMapper sysDeptMapper;

    @Test
    public void ttsD(){

    }




}