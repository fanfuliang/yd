package com.yudao.lianying.v1.process.api;


import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.process.vo.ForwardBatchVO;
import com.yudao.lianying.v1.process.vo.ProcessVO;
import com.yudao.lianying.v1.process.vo.ProcessModifyVO1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * <p>
 * 流程表 前端控制器
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Api(tags = "流程表")
@RequestMapping(value = "/process", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ProcessServiceApi {

    @ApiOperation(value = "保存单条-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/saveAuto", method = {RequestMethod.POST})
    Result saveAuto(ProcessVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "流程表主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @ApiOperation(value = "修改对象包含的全部字段-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyAllColumnInObjectAuto", method = {RequestMethod.POST})
    Result modifyAllColumnInObjectAuto(ProcessModifyVO1 modifyVO1);

    @ApiOperation(value = "根据id修改单条-自动生成", notes = "不适用情况：\n1. 字段修改为空或空串\n2. 字段值里包含逗号\n3. 字段数据类型不是String")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/modifyByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "流程表主键，多个id以逗号分隔", required = true),
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
            @ApiImplicitParam(name = "id", value = "流程表主键", required = true),
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

    @ApiOperation(value = "管理员-单次转发", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/forward", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "管理员流程id", required = true),
            @ApiImplicitParam(name = "accountId", value = "被转发账号id", required = true),
            @ApiImplicitParam(name = "email", value = "邮件"),
            @ApiImplicitParam(name = "remark", value = "备注信息"),
    })
    Result forward(@ParameterValidtion(notEmpty = true, name = "processId") String processId,
                   @ParameterValidtion(notEmpty = true, name = "accountId") String accountId,
                   String email, String remark);

    @ApiOperation(value = "管理员-批量转发", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/forwardBatch", method = {RequestMethod.POST})
    Result forwardBatch(ForwardBatchVO forwardBatchVO);

    @ApiOperation(value = "普通用户-给出评审", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/suggest", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "转发流程id", required = true),
            @ApiImplicitParam(name = "suggestion", value = "建议"),
    })
    Result suggest(@ParameterValidtion(notEmpty = true, name = "processId") String processId,
                   String suggestion);

    @ApiOperation(value = "管理员-拒绝/接受/退回/公开显示", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/processed", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "管理员流程id", required = true),
            @ApiImplicitParam(name = "state", value = "状态，拒绝/接受/退回", required = true),
            @ApiImplicitParam(name = "publicShow", value = "公开显示"),
            @ApiImplicitParam(name = "suggestion", value = "建议"),
            @ApiImplicitParam(name = "tbId", value = "术语库ID"),
    })
    Result processed(@ParameterValidtion(notEmpty = true, name = "processId") String processId,
                     @ParameterValidtion(notEmpty = true, name = "state") String state, Boolean publicShow, String suggestion,String tbId);

    @ApiOperation(value = "流程历史", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/processHistory", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程id", required = true),
    })
    Result processHistory(@ParameterValidtion(notEmpty = true, name = "processId") String processId);

    @ApiOperation(value = "纠错的流程历史", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/processHistory4Comment", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "纠错id", required = true),
    })
    Result processHistory4Comment(@ParameterValidtion(notEmpty = true, name = "commentId") String commentId);

    @ApiOperation(value = "用户态-我提交的术语", notes = "提交的/被转发的评审，根据parent_id区分")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageMySubmittedTerms", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "field", value = "领域"),
            @ApiImplicitParam(name = "state", value = "状态"),
            @ApiImplicitParam(name = "account", value = "普通用户的用户名", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageMySubmittedTerms(String keyword, Date beginTime, Date endTime, String orderBy, String field, String state,
                                @ParameterValidtion(notEmpty = true, name = "account") String account,
                                @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                                @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

    @ApiOperation(value = "分页-我的纠错", notes = "提交的/被转发的评审，根据parent_id区分")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageMySubmittedComment", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "field", value = "领域"),
            @ApiImplicitParam(name = "place", value = "纠错位置"),
            @ApiImplicitParam(name = "state", value = "状态"),
            @ApiImplicitParam(name = "account", value = "普通用户的用户名", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
    })
    Result pageMySubmittedComment(String keyword, Date beginTime, Date endTime, String orderBy, String field, String place, String state,
                                  @ParameterValidtion(notEmpty = true, name = "account") String account,
                                  @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                                  @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize);

    @ApiOperation(value = "普通用户（被转发的）查看评审术语流程", notes = "待处理/已处理根据状态区分")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageMyForwardTerms", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "field", value = "领域"),
            @ApiImplicitParam(name = "state", value = "状态", required = true),
            @ApiImplicitParam(name = "account", value = "普通用户的用户名", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
            @ApiImplicitParam(name = "creator", value = "创建人筛选"),
    })
    Result pageMyForwardTerms(String keyword, Date beginTime, Date endTime, String orderBy, String field,
                              @ParameterValidtion(notEmpty = true, name = "state") String state,
                              @ParameterValidtion(notEmpty = true, name = "account") String account,
                              @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                              @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize,
                              String creator
                              );

    @ApiOperation(value = "普通用户(被转发的)查看评审纠错流程", notes = "待处理/已处理根据状态区分")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageMyForwardComment", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "field", value = "领域"),
            @ApiImplicitParam(name = "place", value = "纠错位置"),
            @ApiImplicitParam(name = "state", value = "状态", required = true),
            @ApiImplicitParam(name = "account", value = "普通用户的用户名", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
            @ApiImplicitParam(name = "creator", value = "创建人筛选"),
    })
    Result pageMyForwardComment(String keyword, Date beginTime, Date endTime, String orderBy, String field, String place,
                                @ParameterValidtion(notEmpty = true, name = "state") String state,
                                @ParameterValidtion(notEmpty = true, name = "account") String account,
                                @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                                @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize,
                                String creator
    );

    @ApiOperation(value = "管理态-普通管理员的评审术语流程", notes = "待处理/已处理根据状态区分")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageManagerTerms", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "field", value = "领域"),
            @ApiImplicitParam(name = "state", value = "状态", required = true),
            @ApiImplicitParam(name = "account", value = "普通管理员的账号id", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
            @ApiImplicitParam(name = "creator", value = "创建人筛选"),
    })
    Result pageManagerTerms(String keyword, Date beginTime, Date endTime, String orderBy, String field,
                            @ParameterValidtion(notEmpty = true, name = "state") String state,
                            @ParameterValidtion(notEmpty = true, name = "account") String account,
                            @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                            @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize,String creator);

    @ApiOperation(value = "管理态-普通管理员的评审纠错流程", notes = "待处理/已处理根据状态区分")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/pageManagerComment", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字，查找字段"),
            @ApiImplicitParam(name = "beginTime", paramType = "query", value = "开始时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", paramType = "query", value = "结束时间，yyyy-MM-dd"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，如：create_time asc"),
            @ApiImplicitParam(name = "field", value = "领域"),
            @ApiImplicitParam(name = "place", value = "纠错位置"),
            @ApiImplicitParam(name = "state", value = "状态", required = true),
            @ApiImplicitParam(name = "account", value = "普通管理员的账号id", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面尺寸", required = true),
            @ApiImplicitParam(name = "creator", value = "创建人筛选"),
    })
    Result pageManagerComment(String keyword, Date beginTime, Date endTime, String orderBy, String field, String place,
                              @ParameterValidtion(notEmpty = true, name = "state") String state,
                              @ParameterValidtion(notEmpty = true, name = "account") String account,
                              @ParameterValidtion(gt = 0, name = "pageNum") Integer pageNum,
                              @ParameterValidtion(gt = 0, name = "pageSize") Integer pageSize,String creator);

    @ApiOperation(value = "贡献术语", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/contributeTermList", method = {RequestMethod.GET})
    @ApiImplicitParams({

    })
    Result contributeTermList();

    @ApiOperation(value = "新词列表", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getNewTerms", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "显示数量", required = true),
    })
    Result getNewTerms(@ParameterValidtion(gt = 0, name = "num") Integer num);

    @ApiOperation(value = "test", notes = "")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "显示数量", required = true),
    })
    Result test(@ParameterValidtion(gt = 0, name = "num") Integer num);

    /* zhangry 2021-03-19*/
    @ApiOperation(value = "查询未处理术语的数量", notes = "待处理的未读术语")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/myUnDealedTermNum", method = {RequestMethod.GET})
    @ApiImplicitParams({@ApiImplicitParam(name = "account", value = "普通用户的用户名", required = true)})
    Result myUnDealedTermNum(@ParameterValidtion(notEmpty = true, name = "account") String account);


    @ApiOperation(value = "查询未处理纠错的数量", notes = "待处理的未读纠错")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/myUnDealedCommentNum", method = {RequestMethod.GET})
    @ApiImplicitParams({@ApiImplicitParam(name = "account", value = "普通用户的用户名", required = true)})
    Result myUnDealedCommentNum(@ParameterValidtion(notEmpty = true, name = "account") String account);

    /* zhangry 2021-03-19*/
    @ApiOperation(value = "获取用户能看到的entry的创建人")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getAllCreatorByAccount", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "用户id", required = true),
            @ApiImplicitParam(name = "type", value = "类型", required = true),
    })
    Result getAllCreatorByAccount(@ParameterValidtion(notEmpty = true, name = "accountId") String accountId,@ParameterValidtion(notEmpty = true, name = "type") String type);

    @ApiOperation(value = "获取管理员能看的entry的创建人")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @RequestMapping(value = "/getManagerAllCreator", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "用户id", required = true),
            @ApiImplicitParam(name = "type", value = "类型", required = true),
    })
    Result getManagerAllCreator(@ParameterValidtion(notEmpty = true, name = "accountId") String accountId,@ParameterValidtion(notEmpty = true, name = "type") String type);
}

