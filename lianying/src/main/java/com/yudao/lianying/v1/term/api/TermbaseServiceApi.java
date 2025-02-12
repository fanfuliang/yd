package com.yudao.lianying.v1.term.api;

import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.PageParam2;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.vo.TermbaseVO;
import com.yudao.lianying.v1.term.vo.TermbaseModifyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 知识库 前端控制器
 * </p>
 *
 * @author fanfl
 * @since 2023-05-13
 */
@Api(tags = "术语库")
@RequestMapping(value = "/termbase", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface TermbaseServiceApi {

    @ApiOperation(value = "保存")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    Result save(TermbaseVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "知识库主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "全量修改对象所有字段-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyByIdAuto", method = {RequestMethod.POST})
    Result modifyByIdAuto(TermbaseModifyVO modifyVO);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "时间格式：yyyy-MM-dd HH:mm:ss，布尔值：0/1")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyBatchByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
    })
    Result modifyBatchByIdAuto(List<ModifyVO> modifyVOList);

    @ApiOperation(value = "根据id获取单条术语库-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getByIdAuto", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "知识库主键", required = true),
    })
    Result getByIdAuto(@ParameterValidtion(notEmpty = true, name = "id") String id);


    @ApiOperation(value = "分页查询-工作台知识、词库、句库使用", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号\n2. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "filterFieldNames", value = "筛选字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "filterFieldValues", value = "筛选字段值，不同字段的值以分号分隔，同字段的值以逗号分隔"),
            @ApiImplicitParam(name = "localIds", value = "筛选语言"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "tag", value = "标签"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
            @ApiImplicitParam(name = "accountId", value = "登录用户ID")
    })
    Result page(String keyword, String filterFieldNames, String filterFieldValues, String localIds, String orderBy,
                String tag, Date beginTime, Date endTime,
                @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize,
                String accountId);

    @ApiOperation(value = "合并术语库")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/merge", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mainId", value = "", required = true),
            @ApiImplicitParam(name = "slaveIds", value = "", required = true),
    })
    Result merge(@ParameterValidtion(notEmpty = true, name = "mainId") String mainId,
                 @ParameterValidtion(notEmpty = true, name = "slaveIds") String slaveIds);



    @ApiOperation(value = "获取飞书词库集合")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getFeishuRepoList", method = {RequestMethod.GET})
    Result getFeishuRepoList();


    @ApiOperation(value = "获取联影词库集合")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getUihRepoList", method = {RequestMethod.GET})
    Result getUihRepoList();
}

