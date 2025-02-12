package com.yudao.lianying.v1.term.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.term.api.EntryServiceApi;
import com.yudao.lianying.v1.term.biz.EntryBusiness;
import com.yudao.lianying.v1.term.biz.FeiShuPluginBusiness;
import com.yudao.lianying.v1.term.vo.EntryModifyVO1;
import com.yudao.lianying.v1.term.vo.EntryVO;
import com.yudao.lianying.v1.term.vo.EntryWholeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@RestController
@Slf4j
public class EntryServiceImpl implements EntryServiceApi {
    @Autowired
    private EntryBusiness entryBusiness;

    @Autowired
    private FeiShuPluginBusiness feiShuPluginBusiness;

    @Override
    public Result saveAuto(EntryVO entityVO) {
        try {
            return ResultUtil.success(entryBusiness.saveAuto(entityVO));
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
            entryBusiness.removeByIdAuto(ids);
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
    public Result modifyAllObjectFeildAuto(EntryModifyVO1 modifyVO1) {
        try {
            entryBusiness.modifyAllObjectFeildAuto(modifyVO1);
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
            entryBusiness.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
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
            return ResultUtil.success(entryBusiness.getByIdAuto(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result search(String keyword, String field,String applicableTo,String searchType) {
        try {
            return ResultUtil.success(entryBusiness.search(keyword,field, applicableTo,searchType));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result relatedSearch(String entryId) {
        try {
            return ResultUtil.success(entryBusiness.relatedSearch(entryId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result page4Manage(String keyword, String fields,String tbId, String firstLang, String secondLang, @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, String orderBy, Integer pageNum, Integer pageSize) {
        try {
            ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
            endTime = DateUtils.tomorrowStartDate(endTime);
            return ResultUtil.success(entryBusiness.page4Manage(new Page<>(pageNum, pageSize), keyword, fields,tbId, firstLang, secondLang, beginTime, endTime, orderBy));
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
            return ResultUtil.success(entryBusiness.pageAuto(new Page<>(pageNum, pageSize), keyword, filterFieldNameArray, filterFieldValueArray, orderBy));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result save(@RequestBody EntryWholeVO entryWholeVO) {
        try {
            EntryVO entryVO = entryWholeVO.getEntryVO();
            if (null == entryVO || StringUtils.isBlank(entryVO.getField()) || StringUtils.isBlank(entryVO.getPartOfSpeech()) || StringUtils.isBlank(entryVO.getCreator())) {
                throw new YudaoException(Result.ERROR, "entry数据错误");
            }
            entryBusiness.save(entryWholeVO, true,false);
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
    public Result save4Manage(@RequestBody EntryWholeVO entryWholeVO) {
        try {
            EntryVO entryVO = entryWholeVO.getEntryVO();
            if (null == entryVO || StringUtils.isBlank(entryVO.getField()) || StringUtils.isBlank(entryVO.getPartOfSpeech()) || StringUtils.isBlank(entryVO.getCreator())) {
                throw new YudaoException(Result.ERROR, "entry数据错误");
            }
            entryBusiness.save(entryWholeVO, false,false);
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
    public Result modify4Manage(@RequestBody EntryWholeVO entryWholeVO) {
        try {
            EntryVO entryVO = entryWholeVO.getEntryVO();
            if (null == entryVO || StringUtils.isBlank(entryVO.getField()) || StringUtils.isBlank(entryVO.getPartOfSpeech()) || StringUtils.isBlank(entryVO.getCreator())) {
                throw new YudaoException(Result.ERROR, "entry数据错误");
            }
            entryBusiness.modify(entryWholeVO);
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
    public Result getById(String entryId) {
        try {
            return ResultUtil.success(entryBusiness.getById(entryId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result moveTo(String newTBId, String entryIds) {
        try {
            entryBusiness.moveTo(newTBId, entryIds);
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
    public Result importEntrys(String accountId, String ftpPath) {
        try {
            return ResultUtil.success(entryBusiness.imports("", accountId, ftpPath, true));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result importEntrys4Manage(String tbId, String accountId, String ftpPath) {
        try {
            return ResultUtil.success(entryBusiness.imports(tbId, accountId, ftpPath, false));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result export4Manage(String tbId, String entryIds, String langIds, String format, HttpServletResponse response) {
        try {
            String formatRange = "xlsx,tbx";
            format = ParameterValidUtils.getAllowedOrDefault(formatRange, format);
            entryBusiness.export(response, tbId, entryIds, langIds, format);
            return ResultUtil.success();
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error(ResultEnum.SERVER_ERROR.getMessage(), e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result sync2Feishu() {
        try {
            feiShuPluginBusiness.syncLocalToFeiShu();
            return ResultUtil.success();
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error(ResultEnum.SERVER_ERROR.getMessage(), e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result sync2Local() {
        try {
            feiShuPluginBusiness.syncFeiShuToLocal();
            return ResultUtil.success();
        } catch (YudaoException e) {
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error(ResultEnum.SERVER_ERROR.getMessage(), e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
