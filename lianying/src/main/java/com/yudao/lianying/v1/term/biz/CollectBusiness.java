package com.yudao.lianying.v1.term.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.lianying.utils.ModuleFieldNameEnum;
import com.yudao.lianying.v1.term.dao.mapper.CollectMapper;
import com.yudao.lianying.v1.term.dao.Collect;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.vo.CollectVO;
import com.yudao.lianying.v1.term.vo.CollectModifyVO1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * <p>
 * 术语收藏 服务类
 * </p>
 *
 * @author liudong
 * @since 2021-02-02
 */
@Service
@Slf4j
public class CollectBusiness extends ServiceImpl<CollectMapper, Collect> {

    @Autowired
    private EntryBusiness entryBusiness;

    public Object saveAuto(CollectVO entityVO) {
        Collect entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Collect.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitByCommaToListUnique(ids);
        List<Collect> list = new ArrayList<>();
        for (String id : set) {
            Collect entity = new Collect(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(CollectModifyVO1 modifyVO1) {
        Collect entity = selectById(modifyVO1.getId());
        ParameterValidUtils.entityExist(modifyVO1.getId(), entity);
        BeanUtils.copyProperties(modifyVO1, entity);
        entity.setUpdateTime(DateUtils.getNow());
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyBatchByIdAuto(List<ModifyVO> modifyVOList) {
        baseMapper.modifyBatchByIdAuto(modifyVOList);
    }

    public Object getByIdAuto(String id) {
        Collect entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Collect> page, String searchFieldNames, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Collect> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        if (null != beginTime) {
            entityWrapper.ge(FieldNameEnum.create_time.name(), beginTime);
        }
        if (null != endTime) {
            entityWrapper.lt(FieldNameEnum.create_time.name(), endTime);
        }
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (StringUtils.isNotBlank(keyword)) {
            List<String> list = CommonUtils.splitByCommaToListUnique(searchFieldNames);
            entityWrapper.andNew();
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if (i == 0) {
                    entityWrapper.like(s, keyword);
                }
                else {
                    entityWrapper.or().like(s, keyword);
                }
            }
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        return selectPage(page, entityWrapper);
    }

    public void collect(CollectVO entityVO) {
        Collect whereEntity = new Collect();
        whereEntity.setEntryId(entityVO.getEntryId());
        whereEntity.setCreateBy(entityVO.getCreateBy());
        whereEntity.setStatus(StatusValueEnum.normal.getValue());
        if (null == selectOne(new EntityWrapper<>(whereEntity))) {
            Collect collect = CommonUtils.copyPropertiesAndCreateId(entityVO, Collect.class);
            collect.setField(entryBusiness.selectById(collect.getEntryId()).getFieldName());
            collect.setTermText(entryBusiness.getTermText(collect.getEntryId()));
            insert(collect);
        }
    }

    public Object isCollected(String account, String entryId) {
        Collect collect = new Collect();
        collect.setStatus(StatusValueEnum.normal.getValue());
        collect.setEntryId(entryId);
        collect.setCreateBy(account);
        return selectOne(new EntityWrapper<>(collect)) != null;
    }

    public void cancelCollected(String account, String entryId) {
        Collect collect = new Collect();
        collect.setCreateBy(account);
        entryId = CommonUtils.formatSeparatedStr(entryId, ",");
        delete(new EntityWrapper<>(collect).in(ModuleFieldNameEnum.entry_id.name(), entryId));
    }
}
