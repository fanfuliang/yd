package com.yudao.lianying.v1.basicConfig.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.basicConfig.api.LangServiceApi;
import com.yudao.lianying.v1.basicConfig.biz.LangBusiness;
import com.yudao.lianying.v1.basicConfig.vo.LanguageModifyVO;
import com.yudao.lianying.v1.basicConfig.vo.LanguageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liudong
 * @since 2020-03-24
 */
@RestController
@Slf4j
public class LangServiceImpl implements LangServiceApi {

    @Autowired
    private LangBusiness langBusiness;

    @Override
    public Result getLang(Integer purpose) {
        try {
            return ResultUtil.success(langBusiness.getLang(purpose));
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("出错", e);
            return ResultUtil.error(Result.ERROR, "出错");
        }
    }

    @Override
    public Result getCodeById(String id) {
        try {
            return ResultUtil.success(langBusiness.getCodeById(id));
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error(ResultEnum.SERVER_ERROR.getMessage(), e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getCodeByName(String name) {
        try {
            return ResultUtil.success(langBusiness.getCodeByName(name));
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error(ResultEnum.SERVER_ERROR.getMessage(), e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getIdByCode(String code) {
        try {
            return ResultUtil.success(langBusiness.getIdByCode(code));
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error(ResultEnum.SERVER_ERROR.getMessage(), e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result saveAuto(LanguageVO entityVO) {
        try {
            return ResultUtil.success(langBusiness.saveAuto(entityVO));
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
            langBusiness.removeByIdAuto(ids);
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
    public Result modifyAllColumnInObjectAuto(LanguageModifyVO modifyVO) {
        try {
            langBusiness.modifyAllColumnInObjectAuto(modifyVO);
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
            langBusiness.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
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
            return ResultUtil.success(langBusiness.getByIdAuto(id));
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
            return ResultUtil.success(langBusiness.pageAuto(new Page<>(pageNum, pageSize), keyword, beginTime, endTime, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

}
