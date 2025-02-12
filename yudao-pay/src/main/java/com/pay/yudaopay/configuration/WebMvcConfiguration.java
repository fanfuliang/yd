package com.pay.yudaopay.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * WebMVC 配置.
 * <p>
 * 添加路径和页面的映射关系
 *
 * @author Mengday Zhang
 * @version 1.0
 * @since 2018/6/13
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/gotoWapPage").setViewName("gotoWapPay");
        registry.addViewController("/gotoPagePage").setViewName("gotoPagePay");
        registry.addViewController("/gotoH5Page").setViewName("gotoH5Page");
        registry.addViewController("/wxPayList").setViewName("wxPayList");
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/static/*/**").addResourceLocations("classpath:/static/");
        //重写这个方法，映射静态资源文件
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
        super.addResourceHandlers(registry);

    }
}
