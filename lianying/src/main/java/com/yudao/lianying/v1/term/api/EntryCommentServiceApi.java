package com.yudao.lianying.v1.term.api;

import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.vo.EntryCommentVO;
import com.yudao.lianying.v1.term.vo.EntryCommentModifyVO1;
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
 * entry_id评论表 前端控制器
 * </p>
 *
 * @author liudong
 * @since 2021-01-15
 */
@Api(tags = "术语Entry评论")
@RequestMapping(value = "/entryComment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface EntryCommentServiceApi {

    @ApiOperation(value = "保存单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveAuto", method = {RequestMethod.POST})
    Result saveAuto(EntryCommentVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "entry_id评论表主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "全量修改对象所有字段-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyAllColumnInObjectAuto", method = {RequestMethod.POST})
    Result modifyAllColumnInObjectAuto(EntryCommentModifyVO1 modifyVO1);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "时间格式：yyyy-MM-dd HH:mm:ss，布尔值：0/1")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyBatchByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
    })
    Result modifyBatchByIdAuto(List<ModifyVO> modifyVOList);

    @ApiOperation(value = "根据id获取单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getByIdAuto", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "entry_id评论表主键", required = true),
    })
    Result getByIdAuto(@ParameterValidtion(notEmpty = true, name = "id") String id);

    @ApiOperation(value = "用户态-分页查询一级评论", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageAuto", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchFieldNames", value = "模糊查询字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "filterFieldNames", value = "筛选字段名称，多个以逗号分隔"),
            @ApiImplicitParam(name = "filterFieldValues", value = "筛选字段值，不同字段的值以分号分隔，同字段的值以逗号分隔"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageAuto(String searchFieldNames, String keyword, Date beginTime, Date endTime, String filterFieldNames, String filterFieldValues, String orderBy,
                    @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                    @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

    @ApiOperation(value = "用户态-分页查询子级评论", notes = "不适用情况：\n1. 筛选字段值不能为空或空串，也不能包含逗号或分号")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageChildren", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "rootId", value = "根ID"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageChildren(String keyword,String rootId,  String orderBy,
                    @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                    @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);



}

