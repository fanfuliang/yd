package com.yudao.lianying.v1.field.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.lianying.utils.ModuleFieldNameEnum;
import com.yudao.lianying.v1.field.dao.ApplicableTo;
import com.yudao.lianying.v1.field.dao.Field;
import com.yudao.lianying.v1.field.dao.FieldApplicableRelation;
import com.yudao.lianying.v1.field.dao.mapper.FieldMapper;
import com.yudao.lianying.v1.field.vo.FieldModifyVO1;
import com.yudao.lianying.v1.field.vo.FieldVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品线 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
@Service
@Slf4j
public class FieldBusiness extends ServiceImpl<FieldMapper, Field> {

    @Autowired
    private FieldApplicableRelationBusiness fieldApplicableRelationBusiness;
    @Autowired
    private ApplicableToBusiness applicableToBusiness;


    public Object saveAuto(FieldVO entityVO) {
        Field entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Field.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Field> list = new ArrayList<>();
        for (String id : set) {
            Field entity = new Field(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllObjectFeildAuto(FieldModifyVO1 modifyVO1) {
        Field entity = selectById(modifyVO1.getId());
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

    public Field getByIdAuto(String id) {
        Field entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Field> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Field> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        Page<Field> fieldPage = selectPage(page, entityWrapper);
        List<Field> records = fieldPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return fieldPage;
        }
        Set<String> fieldIdList = records.stream().map(Field::getId).collect(Collectors.toSet());
        FieldApplicableRelation entity = new FieldApplicableRelation();
        entity.setStatus(StatusValueEnum.normal.getValue());
        List<FieldApplicableRelation> relations = fieldApplicableRelationBusiness.selectList(new EntityWrapper<>(entity).in(ModuleFieldNameEnum.field_id.name(), fieldIdList));
        Map<String, List<FieldApplicableRelation>> fieldId2RelationsMap = relations.stream().collect(Collectors.groupingBy(FieldApplicableRelation::getFieldId));
        Set<String> applicableIds = relations.stream().map(FieldApplicableRelation::getApplicableToId).collect(Collectors.toSet());
        List<ApplicableTo> applicableTos = applicableToBusiness.selectBatchIds(applicableIds);
        Map<String, List<ApplicableTo>> applicableId2ObjMap = applicableTos.stream().collect(Collectors.groupingBy(ApplicableTo::getId));
        List<JSONObject> list = new ArrayList<>();
        for (Field record : records) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(record);
            String fieldId = record.getId();
            List<FieldApplicableRelation> fieldApplicableRelations = fieldId2RelationsMap.get(fieldId);
            if (!CollectionUtils.isEmpty(fieldApplicableRelations)) {
                JSONArray jsonArray = new JSONArray();
                for (FieldApplicableRelation fieldApplicableRelation : fieldApplicableRelations) {
                    String applicableToId = fieldApplicableRelation.getApplicableToId();
                    if (!CollectionUtils.isEmpty(applicableId2ObjMap.get(applicableToId))) {
                        jsonArray.add(applicableId2ObjMap.get(applicableToId).get(0));
                    }
                }
                jsonObject.put("applicableToList", jsonArray);
            }
            list.add(jsonObject);
        }
        Page<JSONObject> objectPage = new Page<>();
        BeanUtils.copyProperties(fieldPage, objectPage);
        objectPage.setRecords(list);
        return objectPage;
    }

    public Set<String> getManagerId(String name) {
        List<String> fieldNameList = CommonUtils.splitToListUnique(name, ";");
        Field entity = new Field(null, StatusValueEnum.normal.getValue());
        List<Field> fieldList = selectList(new EntityWrapper<>(entity).in(FieldNameEnum.name.name(), fieldNameList).isNotNull(FieldNameEnum.account_id.name()).ne(FieldNameEnum.account_id.name(), ""));
        Collections.shuffle(fieldList); // 打乱集合内对象顺序
        return fieldList.stream().map(Field::getAccountId).collect(Collectors.toSet());
    }

    public void relateApplicable(String fieldId, String applicableToIds) {
        FieldApplicableRelation fieldApplicableRelation = new FieldApplicableRelation();
        fieldApplicableRelation.setFieldId(fieldId);

        List<String> list = CommonUtils.splitToListUnique(applicableToIds, ",");
        List<FieldApplicableRelation> applicableRelations = new ArrayList<>();
        for (String applicableId : list) {
            FieldApplicableRelation insertEntity = new FieldApplicableRelation();
            insertEntity.setId(CommonUtils.uuid());
            insertEntity.setFieldId(fieldId);
            insertEntity.setApplicableToId(applicableId);
            applicableRelations.add(insertEntity);
        }
        fieldApplicableRelationBusiness.delete(new EntityWrapper<>(fieldApplicableRelation));
        if (!CollectionUtils.isEmpty(applicableRelations)) {
            fieldApplicableRelationBusiness.insertBatch(applicableRelations);
        }

    }

    public String getIdByName(String name) {
        Field field= this.selectOne(new EntityWrapper<Field>().eq("name",name));
        if(field!=null) {
            return field.getId();
        }
        return "";
    }
    public String getNameById(String id) {
        Field field= this.selectById(id);
        if(field!=null) {
            return field.getName();
        }
        return "";
    }

    public List<String> getAllNameList() {
        return baseMapper.selectAllNameList();
    }
}
