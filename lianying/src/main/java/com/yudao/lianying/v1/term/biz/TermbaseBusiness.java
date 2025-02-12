package com.yudao.lianying.v1.term.biz;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.*;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.common.yudaocommon.enums.OrderByEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.lianying.v1.term.dao.mapper.TermbaseMapper;
import com.yudao.lianying.v1.term.dao.Termbase;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.dto.TermbaseDTO;
import com.yudao.lianying.v1.term.dto.UihTermDTO;
import com.yudao.lianying.v1.term.vo.TermbaseVO;
import com.yudao.lianying.v1.term.vo.TermbaseModifyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 知识库 服务类
 * </p>
 *
 * @author fanfl
 * @since 2023-05-13
 */
@Service
@Slf4j
public class TermbaseBusiness extends ServiceImpl<TermbaseMapper, Termbase> {


    @Autowired
    private EntryBusiness entryBusiness;

    public Termbase save(TermbaseVO entityVO) {
        Termbase entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Termbase.class);
        entity.setStatus(StatusValueEnum.normal.getValue());
        if (StringUtils.isNotBlank(entityVO.getId())) {//编辑
            Termbase oldData = selectById(entityVO.getId());
            entity.setId(oldData.getId());
            entity.setCreateBy(oldData.getCreateBy());
            entity.setCreateTime(oldData.getCreateTime());
            entity.setUpdateTime(DateUtils.getNow());
            updateById(entity);
        }
        else {//新建
            entity.setCreateTime(DateUtils.getNow());
            insert(entity);
        }
        return entity;
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitByCommaToListUnique(ids);
        List<Termbase> list = new ArrayList<>();
        for (String id : set) {
            Termbase entity = new Termbase(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyByIdAuto(TermbaseModifyVO modifyVO) {
        Termbase entity = selectById(modifyVO.getId());
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

    public Object getById(String id) {
        return baseMapper.getById(id);
    }

    public Object page(Page<TermbaseDTO> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String localIds, String orderBy, String tag,  Date beginTime, Date endTime, String accountId) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        orderBy = orderBy.replace("create_time", "a.create_time");
        orderBy = orderBy.replace("createTime", "a.create_time");
        List<String> tags = CommonUtils.splitByCommaToList(tag);
        for (int i = 0; i < filterFieldValueArray.length; i++) {
            filterFieldValueArray[i] = CommonUtils.quotesSeparatedStr(filterFieldValueArray[i]);
        }
        List<String> localIdList = CommonUtils.splitByCommaToListUnique(localIds);
        return page.setRecords(baseMapper.page(page, keyword, filterFieldNameArray, filterFieldValueArray, localIdList, orderBy, tags,  beginTime, endTime, null));
    }

    public Object page2(PageParam2 pageParam) {
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
        Page<TermbaseDTO> page = PageUtils.page(pageParam.getPageNum(), pageParam.getPageSize());
        return page.setRecords(baseMapper.page2(page, whereLike, whereInList, whereEqualsList, beginTime, endTime, orderBy));
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


    public void merge(String mainId, String slaveIds) {
        List<String> list = CommonUtils.splitByCommaToListUnique(slaveIds);
        list.add(0, mainId);
        List<Termbase> knowledges = selectBatchIds(list);

        List<String> idsTmp = new ArrayList<>();
        knowledges.stream().forEach(t -> idsTmp.addAll(CommonUtils.splitByCommaToListUnique(t.getLocalId())));
        String newlocalId = idsTmp.stream().distinct().collect(Collectors.joining(","));
        baseMapper.updateLocalId(mainId,newlocalId);

//        if (knowledges.stream().anyMatch(Objects::isNull) || knowledges.size() < list.size()) {
//            throw new YudaoException(Result.ERROR, "库id错误");
//        }
//        Termbase knowledge = knowledges.get(knowledges.size() - 1);
//        String industryId = knowledge.getIndustryId();
//        if ((StringUtils.isBlank(industryId) && knowledges.stream().anyMatch(o -> StringUtils.isNotBlank(o.getIndustryId())))
//                || (StringUtils.isNotBlank(industryId) && knowledges.stream().anyMatch(o -> !industryId.equals(o.getIndustryId())))) {
//            throw new YudaoException(Result.ERROR, "领域不同");
//        }
        entryBusiness.updateTBId(mainId, slaveIds);
        list.remove(0);
        deleteBatchIds(list);
    }

    public List<UihTermDTO> getUihRepoList() {
        return baseMapper.getUihTermList();
    }
}
