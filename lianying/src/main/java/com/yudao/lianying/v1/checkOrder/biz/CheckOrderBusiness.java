package com.yudao.lianying.v1.checkOrder.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.lianying.v1.checkOrder.dao.mapper.CheckOrderMapper;
import com.yudao.lianying.v1.checkOrder.dao.CheckOrder;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.checkOrder.vo.CheckOrderVO;
import com.yudao.lianying.v1.checkOrder.vo.CheckOrderModifyVO1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liudong
 * @since 2021-05-11
 */
@Service
@Slf4j
public class CheckOrderBusiness extends ServiceImpl<CheckOrderMapper, CheckOrder> {

    public Object saveAuto(CheckOrderVO entityVO) {
        CheckOrder entity = CommonUtils.copyPropertiesAndCreateId(entityVO, CheckOrder.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitByCommaToListUnique(ids);
        List<CheckOrder> list = new ArrayList<>();
        for (String id : set) {
            CheckOrder entity = new CheckOrder(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(CheckOrderModifyVO1 modifyVO1) {
        CheckOrder entity = selectById(modifyVO1.getId());
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
        CheckOrder entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<CheckOrder> page, String searchFieldNames, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<CheckOrder> entityWrapper = new EntityWrapper<>();
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

    public Object getLastOrder(String account) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setStatus(StatusValueEnum.normal.getValue());
        checkOrder.setAccount(account);
        checkOrder = selectOne(new EntityWrapper<>(checkOrder).orderBy(FieldNameEnum.create_time.name(), false));
        if (null == checkOrder) {
            return new ArrayList<>();
        }
        return CommonUtils.splitByCommaToListUnique(checkOrder.getBaseOrderId());
    }
}
