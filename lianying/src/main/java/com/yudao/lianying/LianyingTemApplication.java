package com.yudao.lianying;

import com.yudao.lianying.utils.MailUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@MapperScan("com.yudao.lianying.v1.*.dao.mapper")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
public class LianyingTemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LianyingTemApplication.class, args);
        System.out.println(MailUtils.getProfile());
    }

    //上传pdf会用到，否则报错
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/data/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
