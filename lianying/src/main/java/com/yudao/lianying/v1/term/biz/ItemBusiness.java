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
import com.yudao.lianying.v1.account.biz.FlyBusiness;
import com.yudao.lianying.v1.term.dao.Item;
import com.yudao.lianying.v1.term.dao.mapper.ItemMapper;
import com.yudao.lianying.v1.term.dto.EntrySearchResult;
import com.yudao.lianying.v1.term.dto.ItemDTO;
import com.yudao.lianying.v1.term.vo.ItemModifyVO1;
import com.yudao.lianying.v1.term.vo.ItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Service
@Slf4j
public class ItemBusiness extends ServiceImpl<ItemMapper, Item> {
    @Autowired
    private FlyBusiness flyBusiness;

    public Object saveAuto(ItemVO entityVO) {
        Item entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Item.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Item> list = new ArrayList<>();
        for (String id : set) {
            Item entity = new Item(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllObjectFeildAuto(ItemModifyVO1 modifyVO1) {
        Item entity = selectById(modifyVO1.getId());
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
        Item entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Item> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Item> entityWrapper = new EntityWrapper<>();
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

    public Object pageByLetter(Page<ItemDTO> page, String letter, String field,String applicableTo, String orderBy) {
        flyBusiness.checkFly();
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        List<String> applicableToList = CommonUtils.splitByCommaToListUnique(applicableTo);
        return page.setRecords(baseMapper.pageByLetter(page, letter, fieldList,applicableToList, orderBy));
    }

    public List<EntrySearchResult> relatedSearch(List<String> tags) {
        return baseMapper.relatedSearch(tags);
    }

    public void deleteByEntryIdList(List<String> entryIdList) {
        if(CollectionUtils.isEmpty(entryIdList)){
            return;
        }
        baseMapper.deleteByEntryIdList(entryIdList);
    }
}
