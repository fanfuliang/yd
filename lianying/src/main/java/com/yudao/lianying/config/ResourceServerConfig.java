package com.yudao.lianying.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import static com.yudao.common.yudaocommon.utils.JsonConvert.GetEnvConfig;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-12-03 19:43:32
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = GetEnvConfig("spring-security.json", "notCertification");

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        for (String au : AUTH_WHITELIST) {
            http.authorizeRequests().antMatchers(au).permitAll();
        }
//        if(profilesActive.equals("dev")){
        http.authorizeRequests().antMatchers("/**").permitAll();
//        }

        http.csrf().disable()
                .authorizeRequests()
//                .antMatchers("/doc.html","/swagger-resources").permitAll()
//                .mvcMatchers("/v2/api-docs").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
