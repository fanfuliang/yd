package com.yudao.lianying.v1.term.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.term.api.ItemServiceApi;
import com.yudao.lianying.v1.term.biz.ItemBusiness;
import com.yudao.lianying.v1.term.vo.ItemVO;
import com.yudao.lianying.v1.term.vo.ItemModifyVO1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@RestController
@Slf4j
public class ItemServiceImpl implements ItemServiceApi {
    @Autowired
    private ItemBusiness itemBusiness;

    @Override
    public Result saveAuto(ItemVO entityVO) {
        try {
            return ResultUtil.success(itemBusiness.saveAuto(entityVO));
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
            itemBusiness.removeByIdAuto(ids);
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
    public Result modifyAllObjectFeildAuto(ItemModifyVO1 modifyVO1) {
        try {
            itemBusiness.modifyAllObjectFeildAuto(modifyVO1);
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
            itemBusiness.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
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
            return ResultUtil.success(itemBusiness.getByIdAuto(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageAuto(String keyword, String filterFieldNames, String filterFieldValues, String orderBy, Integer pageNum, Integer pageSize) {
        try {
            String[] filterFieldNameArray = CommonUtils.splitToArray(filterFieldNames, ",");
            String[] filterFieldValueArray = CommonUtils.splitToArray(CommonUtils.formatSeparatedStr(filterFieldValues, ","), ";");
            if (filterFieldNameArray.length != filterFieldValueArray.length) {
                throw new YudaoException(Result.ERROR, "The number of field names and field values is different");
            }
            return ResultUtil.success(itemBusiness.pageAuto(new Page<>(pageNum, pageSize), keyword, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result pageByLetter(String letter,String field,String applicableTo, String orderBy, Integer pageNum, Integer pageSize) {
        try {
              return ResultUtil.success(itemBusiness.pageByLetter(new Page<>(pageNum, pageSize), letter,field,applicableTo, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
