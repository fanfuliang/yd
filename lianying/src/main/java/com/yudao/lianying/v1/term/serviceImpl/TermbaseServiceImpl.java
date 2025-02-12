package com.yudao.lianying.v1.term.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.PageParam2;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.term.api.TermbaseServiceApi;
import com.yudao.lianying.v1.term.biz.FeiShuPluginBusiness;
import com.yudao.lianying.v1.term.biz.TermbaseBusiness;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.vo.TermbaseVO;
import com.yudao.lianying.v1.term.vo.TermbaseModifyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 知识库 服务实现类
 * </p>
 *
 * @author fanfl
 * @since 2023-05-13
 */
@RestController
@Slf4j
public class TermbaseServiceImpl implements TermbaseServiceApi {

    @Autowired
    private TermbaseBusiness termbaseBusiness;

    @Autowired
    private FeiShuPluginBusiness feiShuPluginBusiness;

    @Override
    public Result save(TermbaseVO entityVO) {
        try {
            return ResultUtil.success(termbaseBusiness.save(entityVO));
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
            termbaseBusiness.removeByIdAuto(ids);
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
    public Result modifyByIdAuto(TermbaseModifyVO modifyVO) {
        try {
            termbaseBusiness.modifyByIdAuto(modifyVO);
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
    public Result modifyBatchByIdAuto(@RequestBody List<ModifyVO> modifyVOList) {
        try {
            termbaseBusiness.modifyBatchByIdAuto(modifyVOList);
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
            return ResultUtil.success(termbaseBusiness.getById(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }


    @Override
    public Result page(String keyword, String filterFieldNames, String filterFieldValues, String localIds, String orderBy, String tag, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                       Integer pageNum, Integer pageSize, String accountId) {
        try {
            String allowedFilterField = "a.create_by,state";
            List<String> filterFieldNameList = CommonUtils.splitByCommaToList(allowedFilterField);
            String[] filterFieldNameArray = CommonUtils.splitToArray(filterFieldNames, ",");
            String[] filterFieldValueArray = CommonUtils.splitToArray(CommonUtils.formatSeparatedStr(filterFieldValues, ","), ";");
            if (filterFieldNameArray.length != filterFieldValueArray.length) {
                throw new YudaoException(Result.ERROR, "The number of field names and field values is different");
            }
            boolean flag = false;
            for (String s : filterFieldNameArray) {
                if (!filterFieldNameList.contains(s)) {
                    throw new YudaoException(Result.ERROR, "Has wrong filter field");
                }
            }
            ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
            endTime = DateUtils.tomorrowStartDate(endTime);
            return ResultUtil.success(termbaseBusiness.page(new Page<>(pageNum, pageSize), keyword, filterFieldNameArray, filterFieldValueArray, localIds, orderBy, tag, beginTime, endTime, accountId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result merge(String mainId, String slaveIds) {
        try {
            termbaseBusiness.merge(mainId, slaveIds);
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
    public Result getFeishuRepoList() {
        try {
            String res = feiShuPluginBusiness.getTermRepoList();
            return ResultUtil.success(res);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getUihRepoList() {
        try {
            return ResultUtil.success(termbaseBusiness.getUihRepoList());
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
