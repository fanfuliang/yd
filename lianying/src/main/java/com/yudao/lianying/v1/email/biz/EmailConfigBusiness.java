package com.yudao.lianying.v1.email.biz;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.OrderByEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.*;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.PageUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.email.dao.EmailConfig;
import com.yudao.lianying.v1.email.dao.mapper.EmailConfigMapper;
import com.yudao.lianying.v1.email.vo.EmailConfigModifyVO;
import com.yudao.lianying.v1.email.vo.EmailConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liudong
 * @since 2021-10-11
 */
@Service
@Slf4j
public class EmailConfigBusiness extends ServiceImpl<EmailConfigMapper, EmailConfig> {

    public Object saveAuto(EmailConfigVO entityVO) {
        EmailConfig entity = CommonUtils.copyPropertiesAndCreateId(entityVO, EmailConfig.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitByCommaToListUnique(ids);
        List<EmailConfig> list = new ArrayList<>();
        for (String id : set) {
            EmailConfig entity = new EmailConfig(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyByIdAuto(EmailConfigModifyVO modifyVO) {
        EmailConfig entity = selectById(modifyVO.getId());
        ParameterValidUtils.entityExist(modifyVO.getId(), entity);
        BeanUtils.copyProperties(modifyVO, entity);
        entity.setUpdateTime(DateUtils.getNow());
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyBatchByIdAuto(List<ModifyVO> modifyVOList) {
        baseMapper.modifyBatchByIdAuto(modifyVOList);
    }

    public Object getByIdAuto(String id) {
        EmailConfig entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object page(PageParam2 pageParam) {
        Date beginTime = pageParam.getBeginTime();
        Date endTime = pageParam.getEndTime();
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);

        // 模糊搜索
        Many2One whereLike = pageParam.getWhereLike();
        if (null != whereLike) {
            List<String> names = whereLike.getNames();
            if (StringUtils.isNotBlank(whereLike.getValue())) {
                mapperLikeName(names);
                if (CollectionUtils.isEmpty(names)) {
                    throw new YudaoException(Result.ERROR, "No search field name");
                }
            }
            else {
                whereLike = null;
            }
        }

        // where in
        List<One2Many> whereInList = pageParam.getWhereInList();
        if (CollectionUtils.isEmpty(whereInList)) {
            whereInList = new ArrayList<>();
        }
        else {
            mapperInName(whereInList);
        }

        // where <=> 值可以为null
        List<One2One> whereEqualsList = pageParam.getWhereEqualsList();
        if (CollectionUtils.isEmpty(whereEqualsList)) {
            whereEqualsList = new ArrayList<>();
        }
        else {
            mapperEqualsName(whereEqualsList);
        }

        // 排序字段映射
        List<OrderBy> orderByList = pageParam.getOrderByList();
        if (CollectionUtils.isEmpty(orderByList)) {
            orderByList = new ArrayList<>();
        }
        else {
            mapperOrderBy(orderByList);
        }
        List<String> orderBy = getOrderByAuto(orderByList);
        Page<EmailConfig> page = PageUtils.page(pageParam.getPageNum(), pageParam.getPageSize());
        return page.setRecords(baseMapper.page(page, whereLike, whereInList, whereEqualsList, beginTime, endTime, orderBy));
    }

    private List<String> getOrderByAuto(List<OrderBy> orderByList) {
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderByList)) {
            for (OrderBy orderBy : orderByList) {
                String name = orderBy.getName();
                list.add(name + " " + (orderBy.getIsAsc() == null || orderBy.getIsAsc() ? OrderByEnum.ASC.name() : OrderByEnum.DESC.name()));
            }
        }
        return list;
    }

    private void mapperOrderBy(List<OrderBy> orderByList) {
        orderByList.removeIf(o -> o == null || StringUtils.isBlank(o.getName()));
    }

    private void mapperLikeName(List<String> list) {
        list.removeIf(StringUtils::isBlank);
        for (int i = 0; i < list.size(); i++) {
            String searchName = list.get(i);
            if ("xxx".equals(searchName)) {
                list.set(i, "a.xxx");
            }
//            else {
//                throw new YudaoException(Result.ERROR, "Wrong search name");
//            }
        }
    }

    private void mapperInName(List<One2Many> list) {
        list.removeIf(o -> o == null || StringUtils.isBlank(o.getName()) || CollectionUtils.isEmpty(o.getValues()));
        list.forEach(o -> {
            if ("xxx".equals(o.getName())) {
                o.setName("a.xxx");
            }
//            else {
//                throw new YudaoException(Result.ERROR, "Wrong filter");
//            }
        });
    }

    private void mapperEqualsName(List<One2One> list) {
        list.removeIf(o -> o == null || StringUtils.isBlank(o.getName()));
        list.forEach(o -> {
            if ("xxx".equals(o.getName())) {
                o.setName("a.xxx");
            }
//            else {
//                throw new YudaoException(Result.ERROR, "Wrong filter");
//            }
        });
    }

    public EmailConfig getDefaultEmailConfig() {
        return baseMapper.pageDefault();
    }
}
