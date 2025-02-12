package com.yudao.lianying.v1.checkConfig.biz;

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
import com.yudao.lianying.v1.checkConfig.dao.CheckConfig;
import com.yudao.lianying.v1.checkConfig.dao.mapper.CheckConfigMapper;
import com.yudao.lianying.v1.checkConfig.vo.CheckConfigModifyVO1;
import com.yudao.lianying.v1.checkConfig.vo.CheckConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 联影设置表 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-14
 */
@Service
@Slf4j
public class CheckConfigBusiness extends ServiceImpl<CheckConfigMapper, CheckConfig> {

    public Object saveAuto(CheckConfigVO entityVO) {
        CheckConfig entity = CommonUtils.copyPropertiesAndCreateId(entityVO, CheckConfig.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<CheckConfig> list = new ArrayList<>();
        for (String id : set) {
            CheckConfig entity = new CheckConfig(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(CheckConfigModifyVO1 modifyVO1) {
        CheckConfig entity = selectById(modifyVO1.getId());
        ParameterValidUtils.entityExist(modifyVO1.getId(), entity);
        BeanUtils.copyProperties(modifyVO1, entity);
        entity.setUpdateTime(DateUtils.getNow());
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyByIdAuto(List<String> idList, String[] modifyFieldNameArray, String[] modifyFieldValueArray) {
        baseMapper.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
    }

    public Object getByIdAuto(String id) {
        CheckConfig entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<CheckConfig> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<CheckConfig> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        return selectPage(page, entityWrapper);
    }

    public void saveBatch(List<CheckConfigVO> list) {
        String lang = list.get(0).getLang();
        CheckConfig deleteEntity = new CheckConfig();
        deleteEntity.setLang(lang);
        list.forEach(o -> o.setLang(lang));
        List<CheckConfig> configs = new ArrayList<>();
        for (CheckConfigVO checkConfigVO : list) {
            CheckConfig conf = CommonUtils.copyPropertiesAndCreateId(checkConfigVO, CheckConfig.class);
            configs.add(conf);
        }
        delete(new EntityWrapper<>(deleteEntity));
        insertBatch(configs);
    }

    public Object getConfigByLang(String lang, String langType) {
        CheckConfig entity = new CheckConfig();
        entity.setLang(lang);
        entity.setLangType(langType);
        entity.setStatus(StatusValueEnum.normal.getValue());
        List<CheckConfig> configs = selectList(new EntityWrapper<>(entity));
        if (CollectionUtils.isEmpty(configs)) {
            entity.setLang("0"); // 获取所有检查项的基础配置，以英文的配置为最初配置
            entity.setLangType("单语"); // 获取所有检查项的基础配置，以英文的配置为最初配置
            List<CheckConfig> enConfigs = selectList(new EntityWrapper<>(entity));
            List<CheckConfig> list = new ArrayList<>();
            for (CheckConfig enConfig : enConfigs) {
                CheckConfig checkConfig = CommonUtils.copyPropertiesAndCreateId(enConfig, CheckConfig.class);
                checkConfig.setEnable(false); // 所以配置初始化
                checkConfig.setLevel("");
                checkConfig.setFileType("");
                checkConfig.setRuleType("");
                checkConfig.setLang(lang);
                checkConfig.setLangType(langType);
                list.add(checkConfig);
            }
            insertBatch(list);
            return list;
        }
        return configs;
    }


    public Object getVaildIndexName(String lang, String fileType, String ruleType, String langType) {
        fileType = CommonUtils.blankToEmpty(fileType);
        ruleType = CommonUtils.blankToEmpty(ruleType);
        langType = CommonUtils.blankToEmpty(langType);
        return baseMapper.getVaildIndexName(lang, fileType, ruleType,langType);
    }
}
