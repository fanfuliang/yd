package com.yudao.lianying.v1.basicConfig.api;

import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.basicConfig.vo.LanguageModifyVO;
import com.yudao.lianying.v1.basicConfig.vo.LanguageVO;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * <p>
 * 语言服务
 * </p>
 *
 * @author liudong
 * @since 2019-05-14
 */
@Api(value="基础数据_语种",tags={"基础数据_语种"})
@RequestMapping(value="/lang",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface LangServiceApi {

    @ApiOperation(value="获取语种",notes="注意问题点：")
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET}, origins="*")
    @RequestMapping(value = "/getLang", method = RequestMethod.GET)
    Result getLang(@ApiParam(name = "语种用途", value = "传入int") Integer purpose);

    @ApiOperation(value="根据id获取编码",notes="注意问题点：")
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET}, origins="*")
    @RequestMapping(value = "/getCodeById", method = RequestMethod.GET)
    Result getCodeById(@ParameterValidtion(notEmpty = true, name = "id") String id);

    @ApiOperation(value="根据名称获取编码",notes="注意问题点：")
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET}, origins="*")
    @RequestMapping(value = "/getCodeByName", method = RequestMethod.GET)
    Result getCodeByName(@ParameterValidtion(notEmpty = true, name = "name") String name);

    @ApiOperation(value="根据编码获取id",notes="注意问题点：")
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET}, origins="*")
    @RequestMapping(value = "/getIdByCode", method = RequestMethod.GET)
    Result getIdByCode(@ParameterValidtion(notEmpty = true, name = "code") String code);


    @ApiOperation(value = "保存单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveAuto", method = {RequestMethod.POST})
    Result saveAuto(LanguageVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "通知表主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "修改对象包含的全部字段-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyAllColumnInObjectAuto", method = {RequestMethod.POST})
    Result modifyAllColumnInObjectAuto(LanguageModifyVO modifyVO);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "不适用情况：\n1. 字段修改为空或空串\n2. 字段值里包含逗号\n3. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "通知表主键，多个id以逗号分隔", required = true),
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
            @ApiImplicitParam(name = "id", value = "通知表主键", required = true),
    })
    Result getByIdAuto(@ParameterValidtion(notEmpty = true, name = "id") String id);

    @ApiOperation(value = "分页查询-自动生成", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号\n2. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageAuto", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "filterFieldNames", value = "筛选字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "filterFieldValues", value = "筛选字段值，不同字段的值以分号分隔，同字段的值以逗号分隔"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageAuto(String keyword, Date beginTime, Date endTime, String filterFieldNames, String filterFieldValues, String orderBy,
                    @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                    @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);
}
