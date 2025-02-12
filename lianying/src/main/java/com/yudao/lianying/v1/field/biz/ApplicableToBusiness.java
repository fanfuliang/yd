package com.yudao.lianying.v1.field.biz;

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
import com.yudao.lianying.v1.field.dao.ApplicableTo;
import com.yudao.lianying.v1.field.dao.FieldApplicableRelation;
import com.yudao.lianying.v1.field.dao.mapper.ApplicableToMapper;
import com.yudao.lianying.v1.field.vo.ApplicableToModifyVO1;
import com.yudao.lianying.v1.field.vo.ApplicableToVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品型号 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Service
@Slf4j
public class ApplicableToBusiness extends ServiceImpl<ApplicableToMapper, ApplicableTo> {

    @Autowired
    private FieldApplicableRelationBusiness fieldApplicableRelationBusiness;

    public Object saveAuto(ApplicableToVO entityVO) {
        ApplicableTo entity = CommonUtils.copyPropertiesAndCreateId(entityVO, ApplicableTo.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<ApplicableTo> list = new ArrayList<>();
        for (String id : set) {
            ApplicableTo entity = new ApplicableTo(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(ApplicableToModifyVO1 modifyVO1) {
        ApplicableTo entity = selectById(modifyVO1.getId());
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

    public ApplicableTo getByIdAuto(String id) {
        ApplicableTo entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<ApplicableTo> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<ApplicableTo> entityWrapper = new EntityWrapper<>();
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

    public List<ApplicableTo> getByFeild(String fieldIds) {
        List<String> fieldIdList = CommonUtils.splitByCommaToListUnique(fieldIds);
        FieldApplicableRelation fieldApplicableRelation = new FieldApplicableRelation(null, StatusValueEnum.normal.getValue());
        List<FieldApplicableRelation> fieldApplicableRelations = fieldApplicableRelationBusiness.selectList(new EntityWrapper<>(fieldApplicableRelation).in("field_id", fieldIdList));
        Set<String> set = fieldApplicableRelations.stream().map(FieldApplicableRelation::getApplicableToId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(set)) {
            return new ArrayList<>();
        }
        return selectBatchIds(set);
    }

    public List<ApplicableTo> getAll() {
        ApplicableTo applicableTo = new ApplicableTo();
        applicableTo.setStatus(StatusValueEnum.normal.getValue());
        return selectList(new EntityWrapper<>(applicableTo));
    }

    public String getIdByName(String name) {
      ApplicableTo applicableTo= this.selectOne(new EntityWrapper<ApplicableTo>().eq("name",name));
     if(applicableTo!=null) {
         return applicableTo.getId();
     }
     return "";
    }

    public List<String> getAllNameList() {
        return baseMapper.selectAllNameList();
    }
}
