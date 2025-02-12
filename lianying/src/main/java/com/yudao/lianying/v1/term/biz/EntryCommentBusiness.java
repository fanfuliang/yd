package com.yudao.lianying.v1.term.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.lianying.v1.term.dao.mapper.EntryCommentMapper;
import com.yudao.lianying.v1.term.dao.EntryComment;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.dto.EntryCommentDTO;
import com.yudao.lianying.v1.term.dto.TermbaseDTO;
import com.yudao.lianying.v1.term.vo.EntryCommentVO;
import com.yudao.lianying.v1.term.vo.EntryCommentModifyVO1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * <p>
 * entry_id评论表 服务类
 * </p>
 *
 * @author liudong
 * @since 2021-01-15
 */
@Service
@Slf4j
public class EntryCommentBusiness extends ServiceImpl<EntryCommentMapper, EntryComment> {

    public Object saveAuto(EntryCommentVO entityVO) {
        EntryComment entity = CommonUtils.copyPropertiesAndCreateId(entityVO, EntryComment.class);
        entity.setCreateTime(new Date(entityVO.getCreateTime()));
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitByCommaToListUnique(ids);
        List<EntryComment> list = new ArrayList<>();
        for (String id : set) {
            EntryComment entity = new EntryComment(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(EntryCommentModifyVO1 modifyVO1) {
        EntryComment entity = selectById(modifyVO1.getId());
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
        EntryComment entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<EntryComment> page, String searchFieldNames, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<EntryComment> entityWrapper = new EntityWrapper<>();
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

    public Object page(Page<EntryCommentDTO> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray,  String orderBy, Date beginTime, Date endTime) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        orderBy = orderBy.replace("create_time", "a.create_time");
        orderBy = orderBy.replace("createTime", "a.create_time");
        for (int i = 0; i < filterFieldValueArray.length; i++) {
            filterFieldValueArray[i] = CommonUtils.quotesSeparatedStr(filterFieldValueArray[i]);
        }
        return page.setRecords(baseMapper.page(page, keyword, filterFieldNameArray, filterFieldValueArray,  orderBy,  beginTime, endTime));
    }

    public Object pageChildren(Page<EntryComment> page, String keyword, String rootId, String orderBy) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        orderBy = orderBy.replace("create_time", "a.create_time");
        orderBy = orderBy.replace("createTime", "a.create_time");
        return page.setRecords(baseMapper.pageChildren(page, keyword, rootId,  orderBy));
    }
}
