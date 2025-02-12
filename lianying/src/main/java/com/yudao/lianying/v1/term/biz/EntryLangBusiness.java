package com.yudao.lianying.v1.term.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.lianying.v1.term.dao.EntryLang;
import com.yudao.lianying.v1.term.dao.mapper.EntryLangMapper;
import com.yudao.lianying.v1.term.dto.EntryLangDTO;
import com.yudao.lianying.v1.term.vo.EntryLangModifyVO1;
import com.yudao.lianying.v1.term.vo.EntryLangVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Service
@Slf4j
public class EntryLangBusiness extends ServiceImpl<EntryLangMapper, EntryLang> {

    public Object saveAuto(EntryLangVO entityVO) {
        EntryLang entity = CommonUtils.copyPropertiesAndCreateId(entityVO, EntryLang.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<EntryLang> list = new ArrayList<>();
        for (String id : set) {
            EntryLang entity = new EntryLang(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllObjectFeildAuto(EntryLangModifyVO1 modifyVO1) {
        EntryLang entity = selectById(modifyVO1.getId());
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            log.error("There is no the entity，id: {}", modifyVO1.getId());
            throw new YudaoException(Result.ERROR, "There is no the entity");
        }
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
        EntryLang entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<EntryLang> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<EntryLang> entityWrapper = new EntityWrapper<>();
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

    public Object getCommonLang(String account) {
        return baseMapper.getCommonLang(account);
    }

    public List<EntryLangDTO> selectDTOListByEntryId(String entryId) {
        /* (new EntityWrapper<EntryLang>()
                .eq("entry_id",entryId)
                .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue()));*/
        return baseMapper.selectListByEntryId(entryId);
    }

    public List<EntryLang> selectListByEntryId(String entryId) {
        /* (new EntityWrapper<EntryLang>()
                .eq("entry_id",entryId)
                .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue()));*/
        return selectList(new EntityWrapper<EntryLang>().eq("entry_id",entryId));
    }

    public void deleteByEntryIdList(List<String> entryIdList) {
        if(CollectionUtils.isEmpty(entryIdList)){
            return;
        }
        baseMapper.deleteByEntryIdList(entryIdList);
    }
}
