package com.yudao.lianying.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: yudao
 * @description: mybatis-plus分页
 * @author: liudong
 * @create: 2020-12-03 19:43:32
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件(解决pages和total都为0的bug)
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

}