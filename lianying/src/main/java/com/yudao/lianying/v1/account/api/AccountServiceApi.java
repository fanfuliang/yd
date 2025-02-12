package com.yudao.lianying.v1.account.api;


import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.account.vo.AccountModifyVO1;
import com.yudao.lianying.v1.account.vo.AccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 * 账号信息表  前端控制器
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
@Api(tags = "账号信息表 ")
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface AccountServiceApi {

    @ApiOperation(value = "保存单条")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    Result save(AccountVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeById", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "账号信息表 主键，多个id以逗号分隔", required = true),
    })
    Result removeById(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "修改对象包含的全部字段")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyAllColumnInObject", method = {RequestMethod.POST})
    Result modifyAllColumnInObject(AccountModifyVO1 modifyVO1);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "不适用情况：\n1. 字段修改为空或空串\n2. 字段值里包含逗号\n3. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "账号信息表 主键，多个id以逗号分隔", required = true),
            @ApiImplicitParam(name = "modifyFieldNames", value = "修改字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "modifyFieldValues", value = "修改字段值，多个以逗号分隔"),
    })
    Result modifyByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids,
                          @ParameterValidtion(notEmpty = true, name = "modifyFieldNames") String modifyFieldNames,
                          @ParameterValidtion(notEmpty = true, name = "modifyFieldValues") String modifyFieldValues);

    @ApiOperation(value = "根据id获取单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getByIdAuto", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账号信息表 主键", required = true),
    })
    Result getByIdAuto(@ParameterValidtion(notEmpty = true, name = "id") String id);

    @ApiOperation(value = "分页查询", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号\n2. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "filterFieldNames", value = "筛选字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "filterFieldValues", value = "筛选字段值，不同字段的值以分号分隔，同字段的值以逗号分隔"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result page(String keyword, String filterFieldNames, String filterFieldValues, String orderBy,
                @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

    @ApiOperation(value = "获取用户权限")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getLevel", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账号信息表 主键", required = true),
    })
    Result getLevel(@ParameterValidtion(notEmpty = true, name = "id") String id);

    @ApiOperation(value = "查询可用产品线")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getAvailFields", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账号信息表 主键"),
    })
    Result getAvailFields(String id);

    @ApiOperation(value = "登录", notes = "注意问题点：")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    Result login(@ParameterValidtion(name = "loginName", notEmpty = true) String loginName,
                 @ParameterValidtion(name = "password", notEmpty = true) String password);

    @ApiOperation(value = "根据id修改密码")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyPswById", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "账号id", required = true),
            @ApiImplicitParam(name = "psw", value = "初始密码", required = true),
    })
    Result modifyPswById(@ParameterValidtion(notEmpty = true, name = "accountId") String accountId,
                         @ParameterValidtion(notEmpty = true, name = "psw") String psw);

    @ApiOperation(value = "获取token")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/getToken", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "旧token"),
    })
    Result getToken(String token);

    @ApiOperation(value = "获取术语")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/getTerms", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchExpression", value = "搜索词"),
    })
    String getTerms(String searchExpression);

    @ApiOperation(value = "模糊查询用户(测试存储过程)")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/fuzzySelectUser", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "模糊的登录id"),
    })
    Result fuzzySelectUser(@ParameterValidtion(notEmpty = true, name = "keyword") String keyword);
}

