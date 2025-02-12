package com.yudao.lianying.v1.term.api;


import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.term.vo.EntryVO;
import com.yudao.lianying.v1.term.vo.EntryModifyVO1;
import com.yudao.lianying.v1.term.vo.EntryWholeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Api(tags = "术语_Entry")
@RequestMapping(value = "/entry", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface EntryServiceApi {

    @ApiOperation(value = "保存单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveAuto", method = {RequestMethod.POST})
    Result saveAuto(EntryVO entityVO);

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
    Result modifyAllObjectFeildAuto(EntryModifyVO1 modifyVO1);

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

    @ApiOperation(value = "管理态-分页查询-库详情页", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号\n2. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/page4Manage", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "fields", value = "产品线，多个字段以逗号分隔"),
            @ApiImplicitParam(name = "tbId", value = "术语库ID",required = true),
            @ApiImplicitParam(name = "firstLang", value = "第一语言",required = true),
            @ApiImplicitParam(name = "secondLang", value = "第二语言",required = true),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result page4Manage(String keyword,String fields, String tbId, String firstLang, String secondLang, Date beginTime, Date endTime, String orderBy,
                       @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                       @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

    @ApiOperation(value = "检索", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "field", value = "产品线，多个字段以逗号分隔"),
            @ApiImplicitParam(name = "applicableTo", value = "产品型号，多个字段以逗号分隔"),
            @ApiImplicitParam(name = "searchType", value = "1包含 2精确查找"),
    })
    Result search(String keyword,String field,String applicableTo, String searchType);

    @ApiOperation(value = "相关术语", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/relatedSearch", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entryId", value = "entryId"),
    })
    Result relatedSearch(String entryId);

    @ApiOperation(value = "用户态-提交新术语")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    Result save(EntryWholeVO entryWholeVO);

    @ApiOperation(value = "管理态-新增术语")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/save4Manage", method = {RequestMethod.POST})
    Result save4Manage(EntryWholeVO entryWholeVO);

    @ApiOperation(value = "管理态-全量编辑术语")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modify4Manage", method = {RequestMethod.POST})
    Result modify4Manage(EntryWholeVO entryWholeVO);

    @ApiOperation(value = "获取单条")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/getById", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entryId", value = "", required = true),
    })
    Result getById(@ParameterValidtion(notEmpty = true, name = "entryId") String entryId);


    @ApiOperation(value = "移动")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @PostMapping(value = "/moveTo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newTBId", value = "移动到的知识库id", required = true),
            @ApiImplicitParam(name = "entryIds", value = "需要移动的组id，逗号分隔", required = true),
    })
    Result moveTo(@ParameterValidtion(notEmpty = true, name = "newTBId") String newTBId,
                  @ParameterValidtion(notEmpty = true, name = "entryIds") String entryIds);


    @ApiOperation(value = "用户态-导入")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @PostMapping(value = "/importEntrys")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "用户id", required = true),
            @ApiImplicitParam(name = "ftpPath", value = "文件ftp路径", required = true),
    })
    Result importEntrys(@ParameterValidtion(notEmpty = true, name = "accountId") String accountId,
                       @ParameterValidtion(notEmpty = true, name = "ftpPath") String ftpPath);

    @ApiOperation(value = "管理态-导入")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @PostMapping(value = "/importEntrys4Manage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbId", value = "术语库id", required = true),
            @ApiImplicitParam(name = "accountId", value = "用户id", required = true),
            @ApiImplicitParam(name = "ftpPath", value = "文件ftp路径", required = true),
    })
    Result importEntrys4Manage(@ParameterValidtion(notEmpty = true, name = "tbId") String tbId,
                        @ParameterValidtion(notEmpty = true, name = "accountId") String accountId,
                        @ParameterValidtion(notEmpty = true, name = "ftpPath") String ftpPath);

    @ApiOperation(value = "管理态-导出")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @GetMapping(value = "/export4Manage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbId", value = "术语库id", required = true),
            @ApiImplicitParam(name = "entryIds", value = "entryid，逗号分隔"),
            @ApiImplicitParam(name = "langIds", value = "语言Id,逗号分隔"),
            @ApiImplicitParam(name = "format", value = "xlsx,默认xlsxx"),
    })
    Result export4Manage(@ParameterValidtion(name = "tbId", notEmpty = true) String tbId,
                  String entryIds, String langIds, String format,
                  HttpServletResponse response);


    @ApiOperation(value = "手动同步历史数据到飞书")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @GetMapping(value = "/sync2Feishu")
    Result sync2Feishu();

    @ApiOperation(value = "手动同步飞书数据到本地")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @GetMapping(value = "/sync2Local")
    Result sync2Local();

}

