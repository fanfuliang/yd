package com.yudao.lianying.v1.notice.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.notice.api.NoticeServiceApi;
import com.yudao.lianying.v1.notice.biz.NoticeBusiness;
import com.yudao.lianying.v1.notice.vo.NoticeModifyVO1;
import com.yudao.lianying.v1.notice.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@RestController
@Slf4j
public class NoticeServiceImpl implements NoticeServiceApi {
    @Autowired
    private NoticeBusiness noticeBusiness;

    @Override
    public Result saveAuto(NoticeVO entityVO) {
        try {
            return ResultUtil.success(noticeBusiness.saveAuto(entityVO));
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
            noticeBusiness.removeByIdAuto(ids);
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
    public Result modifyAllColumnInObjectAuto(NoticeModifyVO1 modifyVO1) {
        try {
            noticeBusiness.modifyAllColumnInObjectAuto(modifyVO1);
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
            noticeBusiness.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
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
            return ResultUtil.success(noticeBusiness.getByIdAuto(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageAuto(String keyword, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String filterFieldNames, String filterFieldValues, String orderBy, Integer pageNum, Integer pageSize) {
        try {
            String[] filterFieldNameArray = CommonUtils.splitToArray(filterFieldNames, ",");
            String[] filterFieldValueArray = CommonUtils.splitToArray(CommonUtils.formatSeparatedStr(filterFieldValues, ","), ";");
            if (filterFieldNameArray.length != filterFieldValueArray.length) {
                throw new YudaoException(Result.ERROR, "The number of field names and field values is different");
            }
            return ResultUtil.success(noticeBusiness.pageAuto(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result myUnreadNoticeNum(String account) {
        try {
            return ResultUtil.success(noticeBusiness.myUnreadNoticeNum(account));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

}
