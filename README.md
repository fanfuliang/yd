# CloudMeeting
微服务开发基础框架

## 1. 项目结构介绍
### 1.1 meeting-eureka   
~~~
1. 主要用于服务的注册发现Eureka是Netflix开发的服务发现框架，本身是一个基于REST的服务，主要用于定位运行在AWS域中的中间层服务，以达到负载均衡和中间层服务故障转移的目的。SpringCloud将它集成在其子项目spring-cloud-netflix中，以实现SpringCloud的服务发现功能。
2. Eureka包含两个组件：Eureka Server和Eureka Client。
3. Eureka Server提供服务注册服务，各个节点启动后，会在Eureka Server中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到。
4. Eureka Client是一个java客户端，用于简化与Eureka Server的交互，客户端同时也就是一个内置的、使用轮询(round-robin)负载算法的负载均衡器。
5. 在应用启动后，将会向Eureka Server发送心跳,默认周期为30秒，如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，Eureka Server将会从服务注册表中把这个服务节点移除(默认90秒)。
6. Eureka Server之间通过复制的方式完成数据的同步，Eureka还提供了客户端缓存机制，即使所有的Eureka Server都挂掉，客户端依然可以利用缓存中的信息消费其他服务的API。综上，Eureka通过心跳检查、客户端缓存等机制，确保了系统的高可用性、灵活性和可伸缩性。
~~~
### 1.2 meeting-api
~~~
1. api接口
~~~


### 1.3 meeting-compose
~~~
1. 接口的实现
~~~


### 1.4  meeting-web
~~~
1. 调用实现好的接口
~~~

### 1.5  meeting-sso
~~~
1. 单点登录
~~~

### 1.6  meeting-client
~~~
1. 客户端
~~~

## 2. 注解介绍
~~~
@Data : 注解在类上, 为类提供读写属性, 此外还提供了 equals()、hashCode()、toString() 方法
@Getter/@Setter : 注解在类上, 为类提供读写属性
@ToString : 注解在类上, 为类提供 toString() 方法
@Slf4j : 注解在类上, 为类提供一个属性名为 log 的 log4j 的日志对象
@Log4j : 注解在类上, 为类提供一个属性名为 log 的 log4j 的日志对象
@Entity 表明该类 (UserEntity) 为一个实体类，它默认对应数据库中的表名是user_entity。这里也可以写成

　　　　　　@Entity(name = "xwj_user")

　　　　　　或者

　　　　　　@Entity
　　　　　　@Table(name = "xwj_user", schema = "test")
            查看@Entity注解，发现其只有一个属性name，表示其所对应的数据库中的表名
@Table 当实体类与其映射的数据库表名不同名时需要使用 @Table注解说明，该标注与 @Entity 注解并列使用，置于实体类声明语句之前，可写于单独语　　　　　　　　　　句行，也可与声明语句同行。 
　　　　　　@Table注解的常用选项是 name，用于指明数据库的表名 
　　　　　　@Table注解还有两个选项 catalog 和 schema 用于设置表所属的数据库目录或模式，通常为数据库名
@EnableGlobalMethodSecurity 来判断用户对某个控制层的方法是否具有访问权限

    @EnableGlobalMethodSecurity(securedEnabled=true) 开启@Secured 注解过滤权限
    @EnableGlobalMethodSecurity(jsr250Enabled=true)开启@RolesAllowed 注解过滤权限 
    @EnableGlobalMethodSecurity(d=true) 使用表达式时间方法级别的安全性         
    4个注解可用
        @PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
        @PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
        @PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
        @PreFilter 允许方法调用,但必须在进入方法之前过滤输入值

@PreAuthorize、@PostAuthorize、@PreFilter和@PostFilter。其中前两者可以用来在方法调用前或者调
    用后进行权限检查，后两者可以用来对集合类型的参数或者返回值进行过滤。
    要使它们的定义能够对我们的方法的调用产生影响我们需要设置global-method-security
    元素的pre-post-annotations=”enabled”，默认为disabled。
~~~

## 3.组件介绍
~~~
3.1 @FeignClient注解
    @FeignClient(name = "github-client", url = "https://api.github.com", configuration = GitHubExampleConfig.class)
    public interface GitHubClient {
        @RequestMapping(value = "/search/repositories", method = RequestMethod.GET)
        String searchRepo(@RequestParam("q") String queryStr);
    }
    声明接口之后，在代码中通过@Resource注入之后即可使用。@FeignClient标签的常用属性如下：
    
    name：指定FeignClient的名称，如果项目使用了Ribbon，name属性会作为微服务的名称，用于服务发现
    url: url一般用于调试，可以手动指定@FeignClient调用的地址
    decode404:当发生http 404错误时，如果该字段位true，会调用decoder进行解码，否则抛出FeignException
    configuration: Feign配置类，可以自定义Feign的Encoder、Decoder、LogLevel、Contract
    fallback: 定义容错的处理类，当调用远程接口失败或超时时，会调用对应接口的容错逻辑，fallback指定的类必须实现@FeignClient标记的接口
    fallbackFactory: 工厂类，用于生成fallback类示例，通过这个属性我们可以实现每个接口通用的容错逻辑，减少重复的代码
~~~

## 4. Swagger 2常用注解说明
~~~
    Swagger 2通过注解表明该API接口会生成文档，包括接口名称、请求方法、请求参数、返回信息的等等。
    Swagger 2.0使用的注解及其说明：
    @Api：用在类上，说明该类的作用。
    @ApiOperation：注解来给API增加方法说明。
    @ApiImplicitParams : 用在方法上包含一组参数说明。
    @ApiImplicitParam：用来注解来给方法入参增加说明。
    @ApiResponses：用于表示一组响应
    @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
    @ApiModel：描述一个Model的信息（一般用在请求参数无法使用@ApiImplicitParam注解进行描述的时候）
    @ApiIgnore：使用该注解忽略这个API
    @ApiError ：发生错误返回的信息
~~~

## 5. DepencyManagement应用场景
~~~
当我们的项目模块很多的时候，我们使用Maven管理项目非常方便，帮助我们管理构建、文档、报告、依赖、scms、发布、分发的方法。可以方便的编译代码、进行依赖管理、管理二进制库等等。
由于我们的模块很多，所以我们又抽象了一层，如下图抽出一个femicro来管理子项目的公共的依赖。为了项目的正确运行，必须让所有的子项目使用依赖项的统一版本，必须确保应用的各个项目的依赖项和版本一致，才能保证测试的和发布的是相同的结果。
在我们项目顶层的POM文件中，我们会看到dependencyManagement元素。通过它元素来管理jar包的版本，让子项目中引用一个依赖而不用显示的列出版本号。Maven会沿着父子层次向上走，直到找到一个拥有dependencyManagement元素的项目，然后它就会使用在这个dependencyManagement元素中指定的版本号。
简书例子：https://www.jianshu.com/p/e867ac845e11
~~~
