package com.yudao.lianying.v1.basicConfig.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.v1.basicConfig.dao.Language;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 语言类型表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2019-08-29
 */
public interface LanguageMapper extends BaseMapper<Language> {

    List<Language> getQuickTranSourceLang();
    List<Language> getQuickTranTargetLang();
    List<Language> getAccountLangAbilityList();

    List<Language> getLang(@Param("purpose") Integer purpose);

    List<Language> selectListByIds(@Param("ids") String ids);

    String selectNamesByIds(@Param("ids") String ids);

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    Integer getId();
}
