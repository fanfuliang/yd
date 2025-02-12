package com.yudao.lianying.v1.process.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.process.api.ProcessServiceApi;
import com.yudao.lianying.v1.process.biz.ProcessBusiness;
import com.yudao.lianying.v1.process.vo.ForwardBatchVO;
import com.yudao.lianying.v1.process.vo.ProcessModifyVO1;
import com.yudao.lianying.v1.process.vo.ProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程表 服务实现类
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@RestController
@Slf4j
public class ProcessServiceImpl implements ProcessServiceApi {
    @Autowired
    private ProcessBusiness processBusiness;

    @Override
    public Result saveAuto(ProcessVO entityVO) {
        try {
            return ResultUtil.success(processBusiness.saveAuto(entityVO));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result removeByIdAuto(String ids) {
        try {
            processBusiness.removeByIdAuto(ids);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result modifyAllColumnInObjectAuto(ProcessModifyVO1 modifyVO1) {
        try {
            processBusiness.modifyAllColumnInObjectAuto(modifyVO1);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result modifyByIdAuto(String ids, String modifyFieldNames, String modifyFieldValues) {
        try {
            String[] modifyFieldNameArray = CommonUtils.splitToArray(modifyFieldNames, ",");
            String[] modifyFieldValueArray = CommonUtils.splitToArray(modifyFieldValues, ",");
            if (modifyFieldNameArray.length != modifyFieldValueArray.length) {
                throw new YudaoException(Result.ERROR, "The number of field names and field values is different");
            }
            if (modifyFieldNameArray.length == 0) {
                throw new YudaoException(Result.ERROR, "Field cannot be null");
            }
            List<String> idList = CommonUtils.splitToList(ids, ",");
            processBusiness.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getByIdAuto(String id) {
        try {
            return ResultUtil.success(processBusiness.getByIdAuto(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageAuto(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                           String filterFieldNames, String filterFieldValues, String orderBy, Integer pageNum, Integer pageSize) {
        try {
            String[] filterFieldNameArray = CommonUtils.splitToArray(filterFieldNames, ",");
            String[] filterFieldValueArray = CommonUtils.splitToArray(CommonUtils.formatSeparatedStr(filterFieldValues, ","), ";");
            if (filterFieldNameArray.length != filterFieldValueArray.length) {
                throw new YudaoException(Result.ERROR, "The number of field names and field values is different");
            }
            ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
            endTime = DateUtils.tomorrowStartDate(endTime);
            return ResultUtil.success(processBusiness.pageAuto(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result forward(String processId, String accountId, String email, String remark) {
        try {
            processBusiness.forward(processId, accountId, email, remark);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result forwardBatch(@RequestBody ForwardBatchVO forwardBatchVO) {
        try {
            processBusiness.forwardBatch(forwardBatchVO);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result suggest(String processId, String suggestion) {
        try {
            processBusiness.suggest(processId, suggestion);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result processed(String processId, String state, Boolean publicShow, String suggestion,String tbId) {
        try {
            if (null == publicShow) {
                publicShow = false;
            }
            processBusiness.processed(processId, state, publicShow, suggestion,tbId);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result processHistory(String processId) {
        try {
            return ResultUtil.success(processBusiness.processHistory(processId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result processHistory4Comment(String commentId) {
        try {
            return ResultUtil.success(processBusiness.processHistory4Comment(commentId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageMySubmittedTerms(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, String field, String state, String account, Integer pageNum, Integer pageSize) {
        try {
            return ResultUtil.success(processBusiness.pageMySubmittedTerms(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, orderBy, field, state, account));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageMySubmittedComment(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, String field, String place, String state, String account, Integer pageNum, Integer pageSize) {
        try {
            return ResultUtil.success(processBusiness.pageMySubmittedComment(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, orderBy, field, place, state, account));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageMyForwardTerms(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, String field, String state, String account, Integer pageNum, Integer pageSize,String creator) {
        try {
            return ResultUtil.success(processBusiness.pageMyForwardTerms(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, orderBy, field, state, account,creator));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageMyForwardComment(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, String field, String place, String state, String account, Integer pageNum, Integer pageSize,String creator) {
        try {
            return ResultUtil.success(processBusiness.pageMyForwardComment(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, orderBy, field, place, state, account,creator));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageManagerTerms(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, String field, String state, String account, Integer pageNum, Integer pageSize,String creator) {
        try {
            return ResultUtil.success(processBusiness.pageManagerTerms(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, orderBy, field, state, account,creator));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageManagerComment(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, String field, String place, String state, String account, Integer pageNum, Integer pageSize,String creator) {
        try {
            return ResultUtil.success(processBusiness.pageManagerComment(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, orderBy, field, place, state, account,creator));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result contributeTermList() {
        try {
            return ResultUtil.success(processBusiness.contributeTermList());
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getNewTerms(Integer num) {
        try {
            return ResultUtil.success(processBusiness.getNewTerms(num));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result myUnDealedTermNum(String account) {
        try {
            return ResultUtil.success(processBusiness.myUnDealedTermNum(account));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result myUnDealedCommentNum(String account) {
        try {
            return ResultUtil.success(processBusiness.myUnDealedCommentNum(account));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getAllCreatorByAccount(String accountId,String type) {
        try {
            return ResultUtil.success(processBusiness.getAllCreatorByAccount(accountId,type));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getManagerAllCreator(String accountId,String type) {
        try {
            return ResultUtil.success(processBusiness.getManagerAllCreator(accountId,type));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result test(Integer num) {
        return null;
    }

    /**
     * 处理14天以上的待处理流程
     * <p>
     * <p>
     */
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨执行一次
//    @Scheduled(cron = "0 0 * * * ?") // 每小时执行一次
//    @Scheduled(fixedDelay = 60000) // 每分钟执行一次
    public void unprocessed() {
        processBusiness.unprocessed();
    }
}
