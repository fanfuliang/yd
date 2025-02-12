package com.yudao.lianying.v1.field.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.v1.field.dao.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品线 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2024-12-01
 */
public interface CategoryMapper extends BaseMapper<Category> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<String> selectAllNameList();
}
