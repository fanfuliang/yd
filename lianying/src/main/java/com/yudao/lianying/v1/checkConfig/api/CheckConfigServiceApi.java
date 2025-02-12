package com.yudao.lianying.v1.checkConfig.api;


import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.checkConfig.vo.CheckConfigModifyVO1;
import com.yudao.lianying.v1.checkConfig.vo.CheckConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <p>
 * 质检配置表 前端控制器
 * </p>
 *
 * @author liudong
 * @since 2020-12-14
 */
@Api(tags = "质检配置表")
@RequestMapping(value = "/checkConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface CheckConfigServiceApi {

    @ApiOperation(value = "保存单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveAuto", method = {RequestMethod.POST})
    Result saveAuto(CheckConfigVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "联影设置表主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "修改对象包含的全部字段")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyAllColumnInObject", method = {RequestMethod.POST})
    Result modifyAllColumnInObject(CheckConfigModifyVO1 modifyVO1);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "不适用情况：\n1. 字段修改为空或空串\n2. 字段值里包含逗号\n3. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "联影设置表主键，多个id以逗号分隔", required = true),
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
            @ApiImplicitParam(name = "id", value = "联影设置表主键", required = true),
    })
    Result getByIdAuto(@ParameterValidtion(notEmpty = true, name = "id") String id);

    @ApiOperation(value = "分页查询-自动生成", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号\n2. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageAuto", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "filterFieldNames", value = "筛选字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "filterFieldValues", value = "筛选字段值，不同字段的值以分号分隔，同字段的值以逗号分隔"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageAuto(String keyword, String filterFieldNames, String filterFieldValues, String orderBy,
                    @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                    @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

    @ApiOperation(value = "批量保存")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveBatch", method = {RequestMethod.POST})
    Result saveBatch(List<CheckConfigVO> list);

    @ApiOperation(value = "根据语言获取config")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getConfigByLang", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lang", value = "语种", required = true),
            @ApiImplicitParam(name = "langType", value = "语种类型", required = true),
    })
    Result getConfigByLang(@ParameterValidtion(notEmpty = true, name = "lang") String lang,
                           @ParameterValidtion(notEmpty = true, name = "langType") String langType);

    @ApiOperation(value = "获取可用的检查项")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getVaildIndexName", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lang", value = "语种", required = true),
            @ApiImplicitParam(name = "fileType", value = "文件类型"),
            @ApiImplicitParam(name = "ruleType", value = "规则"),
            @ApiImplicitParam(name = "langType", value = "语种类型"),
    })
    Result getVaildIndexName(@ParameterValidtion(notEmpty = true, name = "lang") String lang,
                             String fileType, String ruleType, String langType);

}

