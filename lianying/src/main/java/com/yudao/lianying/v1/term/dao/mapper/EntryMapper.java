package com.yudao.lianying.v1.term.dao.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.lianying.v1.term.dao.Entry;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.v1.term.dto.EntryDTO;
import com.yudao.lianying.v1.term.dto.EntrySearchResult;
import com.yudao.lianying.v1.term.dto.EntryViewResult;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
public interface EntryMapper extends BaseMapper<Entry> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<EntrySearchResult> searchContain(@Param("keyword") String keyword,
                                          @Param("fieldList") List<String> fieldList,
                                          @Param("applicableToList") List<String> applicableToList);

    List<EntrySearchResult> searchAccurate(@Param("keyword") String keyword,
                                           @Param("fieldList") List<String> fieldList,
                                           @Param("applicableToList") List<String> applicableToList);

    List<EntryViewResult> page4Manage(Page<EntryViewResult> page,
                                      @Param("keyword") String keyword,
                                      @Param("fieldList") List<String> fieldList,
                                      @Param("tbId") String tbId,
                                      @Param("firstLang") String firstLang,
                                      @Param("secondLang") String secondLang,
                                      @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                      @Param("orderBy") String orderBy);


    List<String> getTermTextList(@Param("id") String id);

    void updateTBId(@Param("mainId") String mainId, @Param("slaveIdList") List<String> slaveIdList);

    void moveTo(@Param("newTBId") String newTBId, @Param("entryIdList") List<String> entryIdList);

    EntryDTO getById(@Param("id") String id);
}
