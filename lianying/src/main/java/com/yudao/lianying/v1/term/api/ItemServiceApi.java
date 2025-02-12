package com.yudao.lianying.v1.term.api;


import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.term.vo.ItemVO;
import com.yudao.lianying.v1.term.vo.ItemModifyVO1;
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
 *  前端控制器
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Api(tags = "术语_Entry_语言_条目")
@RequestMapping(value = "/item", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ItemServiceApi {

    @ApiOperation(value = "保存单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveAuto", method = {RequestMethod.POST})
    Result saveAuto(ItemVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "修改对象包含的全部字段")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyAllObjectFeildAuto", method = {RequestMethod.POST})
    Result modifyAllObjectFeildAuto(ItemModifyVO1 modifyVO1);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "不适用情况：\n1. 字段修改为空或空串\n2. 字段值里包含逗号\n3. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "主键，多个id以逗号分隔", required = true),
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
            @ApiImplicitParam(name = "id", value = "主键", required = true),
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



    @ApiOperation(value = "用户态-首页按字母查询", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageByLetter", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "letter", value = "字母，全部传空"),
            @ApiImplicitParam(name = "field", value = "产品线，多个字段以逗号分隔"),
            @ApiImplicitParam(name = "applicableTo", value = "产品型号，多个字段以逗号分隔"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageByLetter(String letter,String field,String applicableTo, String orderBy,
                    @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                    @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

}

