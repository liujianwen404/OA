package com.xtoa.web.controller;

import com.ruoyi.base.domain.HrOff;
import com.ruoyi.base.provider.hrService.IHrOffService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/webApi")
public class ProcessPushController {
    private static final Logger logger = LoggerFactory.getLogger(ProcessPushController.class);

    @Reference(retries = 0,check=false)
    private IHrOffService hrOffService;

    @RequestMapping("/findHrOffer")
    public AjaxResult findHrOffer(String id){
        try {
            logger.info("findHrOffer:"+id);
            HrOff hrOff = hrOffService.selectHrOffById(Long.parseLong(id));
            return AjaxResult.success(hrOff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("操作失败");
    }

}
