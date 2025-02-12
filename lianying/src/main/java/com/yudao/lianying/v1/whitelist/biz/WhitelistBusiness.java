package com.yudao.lianying.v1.whitelist.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.lianying.v1.whitelist.dao.mapper.WhitelistMapper;
import com.yudao.lianying.v1.whitelist.dao.Whitelist;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.whitelist.vo.WhitelistVO;
import com.yudao.lianying.v1.whitelist.vo.WhitelistModifyVO1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * <p>
 * 白名单 服务类
 * </p>
 *
 * @author liudong
 * @since 2021-11-09
 */
@Service
@Slf4j
public class WhitelistBusiness extends ServiceImpl<WhitelistMapper, Whitelist> {

    public Object saveAuto(WhitelistVO entityVO) {
        Whitelist entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Whitelist.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitByCommaToListUnique(ids);
        List<Whitelist> list = new ArrayList<>();
        for (String id : set) {
            Whitelist entity = new Whitelist(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(WhitelistModifyVO1 modifyVO1) {
        Whitelist entity = selectById(modifyVO1.getId());
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
        Whitelist entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Whitelist> page, String searchFieldNames, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Whitelist> entityWrapper = new EntityWrapper<>();
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

    public Object canCheck(String loginName) {
        return baseMapper.canCheck(loginName);
    }
}
