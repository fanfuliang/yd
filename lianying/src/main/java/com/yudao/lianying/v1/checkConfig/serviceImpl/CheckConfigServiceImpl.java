package com.yudao.lianying.v1.checkConfig.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.checkConfig.api.CheckConfigServiceApi;
import com.yudao.lianying.v1.checkConfig.biz.CheckConfigBusiness;
import com.yudao.lianying.v1.checkConfig.vo.CheckConfigModifyVO1;
import com.yudao.lianying.v1.checkConfig.vo.CheckConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 联影设置表 服务实现类
 * </p>
 *
 * @author liudong
 * @since 2020-12-14
 */
@RestController
@Slf4j
public class CheckConfigServiceImpl implements CheckConfigServiceApi {
    @Autowired
    private CheckConfigBusiness checkConfigBusiness;

    @Override
    public Result saveAuto(CheckConfigVO entityVO) {
        try {
            return ResultUtil.success(checkConfigBusiness.saveAuto(entityVO));
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
            checkConfigBusiness.removeByIdAuto(ids);
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
    public Result modifyAllColumnInObject(CheckConfigModifyVO1 modifyVO1) {
        try {
            checkConfigBusiness.modifyAllColumnInObjectAuto(modifyVO1);
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
            checkConfigBusiness.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
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
            return ResultUtil.success(checkConfigBusiness.getByIdAuto(id));
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
            return ResultUtil.success(checkConfigBusiness.pageAuto(new Page<>(pageNum, pageSize), keyword, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result saveBatch(@RequestBody List<CheckConfigVO> list) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                throw new YudaoException(Result.ERROR, "无新增内容");
            }
            if (StringUtils.isBlank(list.get(0).getLang())) {
                throw new YudaoException(Result.ERROR, "语种不明");
            }
            checkConfigBusiness.saveBatch(list);
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
    public Result getConfigByLang(String lang, String langType) {
        try {
            return ResultUtil.success(checkConfigBusiness.getConfigByLang(lang, langType));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getVaildIndexName(String lang, String fileType, String ruleType, String langType) {
        try {
            return ResultUtil.success(checkConfigBusiness.getVaildIndexName(lang, fileType, ruleType, langType));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }

    }
}
