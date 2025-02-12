package com.yudao.lianying.v1.basicConfig.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.*;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.common.yudaocommon.enums.OrderByEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.lianying.v1.basicConfig.dao.mapper.SysConfigMapper;
import com.yudao.lianying.v1.basicConfig.dao.SysConfig;
import com.yudao.lianying.v1.basicConfig.dto.SysConfigData;
import com.yudao.lianying.v1.basicConfig.vo.SysConfigVO;
import com.yudao.lianying.v1.basicConfig.vo.SysConfigModifyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fanfl
 * @since 2024-04-06
 */
@Service
@Slf4j
public class SysConfigBusiness extends ServiceImpl<SysConfigMapper, SysConfig> {

    public SysConfig save(SysConfigVO entityVO) {
        SysConfig entity = CommonUtils.copyPropertiesAndCreateId(entityVO, SysConfig.class);
        entity.setStatus(StatusValueEnum.normal.getValue());
        if (StringUtils.isNotBlank(entityVO.getId())) {//编辑
            SysConfig oldData = selectById(entityVO.getId());
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
        List<SysConfig> list = new ArrayList<>();
        for (String id : set) {
            SysConfig entity = new SysConfig(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyByIdAuto(SysConfigModifyVO modifyVO) {
        SysConfig entity = selectById(modifyVO.getId());
        ParameterValidUtils.entityExist(modifyVO.getId(), entity);
        BeanUtils.copyProperties(modifyVO, entity);
        entity.setUpdateTime(DateUtils.getNow());
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public Object getByIdAuto(String id) {
        SysConfig entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }
    public  String getByName(String name)
    {
        SysConfig sysConfig = this.selectOne(new EntityWrapper<SysConfig>().eq("name", name));
        if(sysConfig!=null)
        {
            return sysConfig.getValue();
        }
        return "";
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
        Page<SysConfig> page = PageUtils.page(pageParam.getPageNum(), pageParam.getPageSize());
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


    public SysConfigData getSysConfigData(){
        SysConfigData sysConfigData=new SysConfigData();
        List<SysConfig> conData = selectList(new EntityWrapper<>());
        Map<String, String> keyVas = conData.stream().collect(Collectors.toMap(m -> m.getName(), n -> n.getValue()));
        keyVas.forEach((k,v)->{
            if(k.equals("feishu_token_url")){
                sysConfigData.setFeishuTokenUrl(v);
            }
            else if(k.equals("app_id")){
                sysConfigData.setAppId(v);
            }
            else if(k.equals("app_secret")){
                sysConfigData.setAppSecret(v);
            }
            else if(k.startsWith("uih_fs_repo")){
                String[] ufs = v.split("<>");
                sysConfigData.getUihFsRepoList().add(ufs);
                sysConfigData.getUih2FsMap().put(ufs[0],ufs[1]);
            }
        });
        return sysConfigData;
    }
}
