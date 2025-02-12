package com.yudao.lianying.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: yudao
 * @description: 启动配置类
 * @author: liudong
 * @create: 2020-12-03 19:43:32
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${springBoot.swagger.show}")
    private boolean swaggerShow;

    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     *
     * @return
     */
    @Bean
    public Docket createRestfulApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yudao.lianying"))  //暴露接口地址的包路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                //创建人
                .contact(new Contact("xxx系统", "xxx", "xxx"))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }


}
