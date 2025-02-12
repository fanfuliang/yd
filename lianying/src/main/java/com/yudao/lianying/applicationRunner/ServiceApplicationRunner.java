package com.yudao.lianying.applicationRunner;

import com.yudao.lianying.v1.basicConfig.biz.LangBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ：刘栋
 * @date ：Created in 2019/7/9 17:11
 * @description： 项目启动时
 * @modified By：
 * @version: $
 */
@Component
@Order(value = 1)
@Slf4j
public class ServiceApplicationRunner implements ApplicationRunner {

    @Autowired
    private LangBusiness translateLanguageBusiness;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        translateLanguageBusiness.init();
    }
}
