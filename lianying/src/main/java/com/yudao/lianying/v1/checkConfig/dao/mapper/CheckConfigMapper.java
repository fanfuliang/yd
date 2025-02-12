package com.yudao.lianying.v1.checkConfig.dao.mapper;

import com.yudao.lianying.v1.checkConfig.dao.CheckConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 联影设置表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-14
 */
public interface CheckConfigMapper extends BaseMapper<CheckConfig> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<String> getVaildIndexName(@Param("lang") String lang, @Param("fileType") String fileType, @Param("ruleType") String ruleType, @Param("langType") String langType);
}
