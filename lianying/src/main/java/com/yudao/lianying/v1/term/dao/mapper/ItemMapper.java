package com.yudao.lianying.v1.term.dao.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.lianying.v1.term.dao.Item;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.v1.term.dto.EntrySearchResult;
import com.yudao.lianying.v1.term.dto.ItemDTO;
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
public interface ItemMapper extends BaseMapper<Item> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<ItemDTO> pageByLetter(Page<ItemDTO> page,
                               @Param("letter")String letter,
                               @Param("fieldList") List<String> fieldList,
                               @Param("applicableToList") List<String> applicableToList,
                               @Param("orderBy") String orderBy);

    List<EntrySearchResult> relatedSearch(@Param("tags") List<String> tags);

    void deleteByEntryIdList(@Param("entryIdList") List<String> entryIdList);
}
