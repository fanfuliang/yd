package com.yudao.lianying.v1.field.dao.mapper;

import com.yudao.lianying.v1.field.dao.ApplicableTo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品线 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
public interface ApplicableToMapper extends BaseMapper<ApplicableTo> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<String> selectAllNameList();
}
