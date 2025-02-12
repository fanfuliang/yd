package com.yudao.lianying.scheduled;

import com.yudao.lianying.v1.term.biz.FeiShuPluginBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ：fanfl
 * @date ：Created in 2024/3/9 17:21
 * @description：定时任务器
 * @modified By：
 * @version: $
 */
@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {

    @Lazy
    @Autowired
    private FeiShuPluginBusiness feiShuPluginBusiness;

    //每天0点
    @Scheduled(cron = "59 59 23 ? * *")
    public void feiShuToLocal() {
        feiShuPluginBusiness.syncFeiShuToLocal();
    }
}
