package com.yudao.template;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MyGenerator {

    public static void main(String[] args) {

        String outputDir = "E:\\project\\new\\yudao\\lianying\\src\\main\\java\\";
        String dbUrl = "jdbc:mysql://192.168.0.212:13306/lianying?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String author = "fanfl";

        // 多个表生成在一个目录下
        String[] table = {"term_entry_custom_field","term_entry_custom_field_detail"};
        // 目录
        String dir = "term";
        // 生成类时想要去掉的前缀
        String tablePrefixToRemove = "";

        String implPackageName = "com.yudao.lianying.v1." + dir;
        String daoPackageName = implPackageName + ".dao";
        // 子项目名称，暂时用不到
        String moduleName = "";
        // 是否覆盖原文件，建议设为false，防止误启动，导致多个文件里的业务逻辑代码被覆盖
        boolean isFileOverride = false;
        generateByTables(implPackageName, daoPackageName, dbUrl, author, outputDir, isFileOverride, tablePrefixToRemove, table);
        implPackageName = implPackageName + ".vo";
        generateByTablesVO(implPackageName, null, dbUrl, author, outputDir, isFileOverride, tablePrefixToRemove, table);
    }

    /**
     * mysql
     *
     * @param daoPackageName
     * @param tableNames
     */
    private static void generateByTables(String implPackageName, String daoPackageName, String dbUrl, String author, String outputDir, boolean isFileOverride, String tablePrefixToRemove, String... tableNames) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("yudao@123456")
                .setDriverName("com.mysql.jdbc.Driver");

        StrategyConfig strategyConfig = new StrategyConfig().entityTableFieldAnnotationEnable(true);// 设置@TableFiled 即生成实体时，生成字段注解
        strategyConfig
                .setTablePrefix(tablePrefixToRemove)// 前缀，生成类时应该时去掉
                .setCapitalMode(true)
                .setEntityLombokModel(true)// 设置是否开启Lombok插件里的注解，vm模板里会根据这个值判断
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)// 命名策略：驼峰命名
//                .setTableFillList()
//                .setSuperEntityClass("com.example.common.SuperEntity")
//                .setSuperMapperClass("com.example.common.SuperMapper")
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组

        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false)
//                .setActiveRecord(true)// 是否开启AR模式
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(false)// XML columList
                .setAuthor(author)
                .setOutputDir(outputDir)
                .setFileOverride(isFileOverride)// 是否覆盖原文件
                .setBaseColumnList(true)// 基本的sql片段
                .setServiceName("%sBusiness")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sServiceApi")
                .setIdType(IdType.NONE);//主键策略

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig
                .setEntity("/entity2.java.vm") // 自定义模板
                .setMapper("/mapper.java.vm")
                .setXml("mapper.xml.vm")
                .setService("service.java.vm")
                .setServiceImpl("/serviceImpl.java.vm")
                .setController("controller.java.vm");

        PackageConfig packageConfig = new PackageConfig()
                // 设置名字
                .setParent(null)// 设置父包
//                .setModuleName(moduleName)
                //设置各层目录
                .setService(implPackageName + ".biz")
                .setServiceImpl(implPackageName + ".serviceImpl")
                .setController(implPackageName + ".api")
                .setEntity(daoPackageName)
                .setMapper(daoPackageName + ".mapper")
                .setXml(daoPackageName + ".mapper.xml");


        new AutoGenerator()
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setTemplate(templateConfig)
                .setPackageInfo(packageConfig)
                .execute();
    }

    /**
     * 生成vo，
     * 注意：不能当做实体生成，实体名字不能修改，会跟数据库表名保持一致，在vm模板中不管怎么修改类名，都无法真正改变类名
     *
     * @param daoPackageName
     * @param dbUrl
     * @param author
     * @param outputDir
     * @param implPackageName
     * @param tableNames
     */
    private static void generateByTablesVO(String implPackageName, String daoPackageName, String dbUrl, String author, String outputDir, boolean isFileOverride, String tablePrefixToRemove, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("yudao@123456")
                .setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig().entityTableFieldAnnotationEnable(true);// 设置@TableFiled 即生成实体时，生成字段注解
        strategyConfig
                .setTablePrefix(tablePrefixToRemove)// 前缀，生成类时去掉
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
//                .setTableFillList()
//                .setSuperEntityClass("com.example.common.SuperEntity")
//                .setSuperMapperClass("com.example.common.SuperMapper")
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组

        config.setActiveRecord(false)
//                .setActiveRecord(true)// 是否开启AR模式
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(false)// XML columList
                .setAuthor(author)
                .setOutputDir(outputDir)
                .setFileOverride(isFileOverride)// 是否覆盖原文件
                .setBaseColumnList(true)// 基本的sql片段
                .setIdType(IdType.NONE)//主键策略
                .setServiceName("%sVO")
                .setControllerName("%sModifyVO1")
        ;
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig
                .setEntity(null) // 自定义模板
                .setXml(null)
                .setMapper(null)
                .setController("entityModifyVO.java.vm")
                .setService("/entityVO.java.vm")
                .setServiceImpl(null)
        ;

        PackageConfig packageConfig = new PackageConfig()
                // 设置名字
                .setParent(null)// 设置父包
//                .setModuleName(moduleName)
                .setEntity(null)
                .setMapper(null)
                .setXml(null)
                .setService(implPackageName)
                .setController(implPackageName)
                .setServiceImpl(null);
        new AutoGenerator()
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setTemplate(templateConfig)
                .setPackageInfo(packageConfig)
                .execute();
    }

    /**
     * oracle
     *
     * @param packageName
     * @param tableNames
     */
    private static void generateByOracleTables(String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
//        String dbUrl = "jdbc:mysql://192.168.0.212:13306/meeting?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String dbUrl = "jdbc:mysql://192.168.0.212:13306/yudao_base?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.ORACLE)
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("yudao@123456")
                .setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
//                .setTablePrefix("表名前缀(生成的实体会省略这个前缀)")
                .setCapitalMode(true)//驼峰命名
                .setEntityLombokModel(true)//使用lombk
                .setDbColumnUnderline(true)//驼峰命名
                .setRestControllerStyle(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setSuperEntityClass("com.yudao.meeting.meetingcompose.v1.account.SuperEntity")
                .setSuperMapperClass("com.yudao.meeting.meetingcompose.v1.account.SuperMapper")
                .setSuperControllerClass("com.yudao.meeting.meetingcompose.v1.account.SuperController")
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组

        config.setActiveRecord(false)
                .setAuthor("wufei")
                .setOutputDir("E:\\project\\new\\Cloudmeeting\\meeting-compose\\src\\")
                .setEnableCache(false)
                .setBaseColumnList(true)
                .setBaseResultMap(true)
                .setFileOverride(true);

        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("controller")
                                .setService("api")
                                .setServiceImpl("serviceImp")
                                .setEntity("model")
                ).execute();

    }
}
