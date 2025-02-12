package com.yudao.lianying.v1.searchHistory.dao.mapper;

import com.yudao.lianying.v1.searchHistory.dao.SearchHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 查询历史表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<Object> getAccount(@Param("category") String category);
}
