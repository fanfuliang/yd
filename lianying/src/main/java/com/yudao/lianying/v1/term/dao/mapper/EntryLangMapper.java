package com.yudao.lianying.v1.term.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.v1.term.dao.EntryLang;
import com.yudao.lianying.v1.term.dto.EntryLangDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
public interface EntryLangMapper extends BaseMapper<EntryLang> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<String> getCommonLang(@Param("account") String account);

    List<EntryLangDTO> selectListByEntryId(@Param("entryId") String entryId);

    void deleteByEntryIdList(@Param("entryIdList") List<String> entryIdList);
}
