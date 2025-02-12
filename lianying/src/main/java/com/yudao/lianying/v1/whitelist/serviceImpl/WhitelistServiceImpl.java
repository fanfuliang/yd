package com.yudao.lianying.v1.whitelist.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.whitelist.api.WhitelistServiceApi;
import com.yudao.lianying.v1.whitelist.biz.WhitelistBusiness;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.whitelist.vo.WhitelistVO;
import com.yudao.lianying.v1.whitelist.vo.WhitelistModifyVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 白名单 服务实现类
 * </p>
 *
 * @author liudong
 * @since 2021-11-09
 */
@RestController
@Slf4j
public class WhitelistServiceImpl implements WhitelistServiceApi {
    @Autowired
    private WhitelistBusiness whitelistBusiness;

    @Override
    public Result saveAuto(WhitelistVO entityVO) {
        try {
            return ResultUtil.success(whitelistBusiness.saveAuto(entityVO));
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
            whitelistBusiness.removeByIdAuto(ids);
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
    public Result modifyAllColumnInObjectAuto(WhitelistModifyVO1 modifyVO1) {
        try {
            whitelistBusiness.modifyAllColumnInObjectAuto(modifyVO1);
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
            whitelistBusiness.modifyBatchByIdAuto(modifyVOList);
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
            return ResultUtil.success(whitelistBusiness.getByIdAuto(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageAuto(String searchFieldNames, String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String filterFieldNames, String filterFieldValues, String orderBy, Integer pageNum, Integer pageSize) {
        try {
            if (StringUtils.isNotBlank(keyword) && StringUtils.isBlank(searchFieldNames)) {
                throw new YudaoException(Result.ERROR, "No search field name");
            }
            String[] filterFieldNameArray = CommonUtils.splitToArray(filterFieldNames, ",");
            String[] filterFieldValueArray = CommonUtils.splitToArray(CommonUtils.formatSeparatedStr(filterFieldValues, ","), ";");
            if (filterFieldNameArray.length != filterFieldValueArray.length) {
                throw new YudaoException(Result.ERROR, "The number of field names and field values is different");
            }
            ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
            endTime = DateUtils.tomorrowStartDate(endTime);
            return ResultUtil.success(whitelistBusiness.pageAuto(new Page<>(pageNum, pageSize), searchFieldNames, keyword, beginTime, endTime, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result canCheck(String loginName) {
        try {
            return ResultUtil.success(whitelistBusiness.canCheck(loginName));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
