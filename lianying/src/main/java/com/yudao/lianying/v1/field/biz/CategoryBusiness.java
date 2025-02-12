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
import com.yudao.lianying.v1.field.dao.Category;
import com.yudao.lianying.v1.field.dao.mapper.CategoryMapper;
import com.yudao.lianying.v1.field.vo.CategoryModifyVO;
import com.yudao.lianying.v1.field.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 类别 服务类
 * </p>
 *
 * @author liudong
 * @since 2024-12-01
 */
@Service
@Slf4j
public class CategoryBusiness extends ServiceImpl<CategoryMapper, Category> {



    public Object saveAuto(CategoryVO entityVO) {
        Category entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Category.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Category> list = new ArrayList<>();
        for (String id : set) {
            Category entity = new Category(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllObjectFeildAuto(CategoryModifyVO modifyVO1) {
        Category entity = selectById(modifyVO1.getId());
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

    public Category getByIdAuto(String id) {
        Category entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Category> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Category> entityWrapper = new EntityWrapper<>();
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

    public String getIdByName(String name) {
        Category field= this.selectOne(new EntityWrapper<Category>().eq("name",name));
        if(field!=null) {
            return field.getId();
        }
        return "";
    }
    public String getNameById(String id) {
        Category field= this.selectById(id);
        if(field!=null) {
            return field.getName();
        }
        return "";
    }

    public List<String> getAllNameList() {
        return baseMapper.selectAllNameList();
    }
}
