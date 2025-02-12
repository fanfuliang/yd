package com.yudao.lianying.v1.basicConfig.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.lianying.v1.basicConfig.dao.Language;
import com.yudao.lianying.v1.basicConfig.dao.mapper.LanguageMapper;
import com.yudao.lianying.v1.basicConfig.vo.LanguageModifyVO;
import com.yudao.lianying.v1.basicConfig.vo.LanguageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 账号的附加信息 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-08-03
 */
@Service
@Slf4j
public class LangBusiness extends ServiceImpl<LanguageMapper, Language> {

    private Map<String, String> id2CodeMap = new HashMap<>();
    private Map<String, String> code2IdMap = new HashMap<>();
    private Map<String, String> nameZh2IdMap = new HashMap<>();

    public void init() {
        id2CodeMap.clear();
        code2IdMap.clear();
        nameZh2IdMap.clear();
        List<Language> languageList = selectList(new EntityWrapper<>());
        for (Language translateLanguage : languageList) {
            id2CodeMap.put(translateLanguage.getKeyid().toString(), translateLanguage.getCode());
            code2IdMap.put(translateLanguage.getCode(), translateLanguage.getKeyid().toString());
            nameZh2IdMap.put(translateLanguage.getNameZh(), translateLanguage.getKeyid().toString());
        }
    }

    public List<Language> getLang(Integer purpose) {
        return baseMapper.getLang(purpose);
    }


    public String getCodeById(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        Language language = this.selectById(id);
        if (null == language) {
            log.error("Error language_id: {}", id);
            throw new YudaoException(Result.ERROR, "Error language_id");
        }
        return language.getCode();
    }

    public String getIdByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        Language language = this.selectOne(new EntityWrapper<Language>()
                .eq("code", code)
                .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue()));

        if (null == language) {
            log.error("Error language_code: {}", code);
            throw new YudaoException(Result.ERROR, "Error language_code");
        }
        return language.getKeyid().toString();
    }

    public String getIdByNameZh(String nameZh) {
        if (StringUtils.isBlank(nameZh)) {
            return "";
        }
        Language language = this.selectOne(new EntityWrapper<Language>()
                .eq("name_zh", nameZh)
                .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue()));

        if (null == language) {
            return "";
        }
        return language.getKeyid().toString();
    }

    public String getIdByNameEn(String nameEn) {
        if (StringUtils.isBlank(nameEn)) {
            return "";
        }
        Language language = this.selectOne(new EntityWrapper<Language>()
                .eq("name_en", nameEn)
                .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue()));

        if (null == language) {
            log.error("Error language_name: {}", nameEn);
            return "";
        }
        return language.getKeyid().toString();
    }

    public String getCodeByName(String nameZh) {
        if (StringUtils.isBlank(nameZh)) {
            return "";
        }
        Language language = this.selectOne(new EntityWrapper<Language>()
                .eq("name_zh", nameZh)
                .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue()));

        if (null == language) {
            log.error("Error language_name: {}", nameZh);
            throw new YudaoException(Result.ERROR, "Error language_name");
        }
        return language.getCode();
    }

    public List<Language> selectByIds(String ids) {
        return baseMapper.selectListByIds(ids);
    }




    public Object saveAuto(LanguageVO entityVO) {
        Language entity = CommonUtils.copyProperties(entityVO, Language.class);
        entity.setKeyid(baseMapper.getId());
        entity.setStatus(StatusValueEnum.normal.getValue());
        entity.setCreateTime(DateUtils.getNow());
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Language> list = new ArrayList<>();
        for (String id : set) {
            Language entity = new Language(Integer.parseInt(id), StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(LanguageModifyVO modifyVO1) {
        Language entity = selectById(modifyVO1.getKeyid());
        BeanUtils.copyProperties(modifyVO1, entity);
        entity.setUpdateTime(DateUtils.getNow());
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyByIdAuto(List<String> idList, String[] modifyFieldNameArray, String[] modifyFieldValueArray) {
        baseMapper.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
    }

    public Language getByIdAuto(String id) {
        Language entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Language> page, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Language> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        if (null != beginTime) {
            entityWrapper.ge(FieldNameEnum.create_time.name(), beginTime);
        }
        if (null != endTime) {
            entityWrapper.lt(FieldNameEnum.create_time.name(), endTime);
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(keyword)) {
            entityWrapper.like(FieldNameEnum.content.name(), keyword);
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        return selectPage(page, entityWrapper);
    }


}
