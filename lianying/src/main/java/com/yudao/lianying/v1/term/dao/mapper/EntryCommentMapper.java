package com.yudao.lianying.v1.term.dao.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.lianying.v1.term.dao.EntryComment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.dto.EntryCommentDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * entry_id评论表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2021-01-15
 */
public interface EntryCommentMapper extends BaseMapper<EntryComment> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);

    List<EntryCommentDTO> page(Page<EntryCommentDTO> page,
                               @Param("keyword") String keyword, @Param("filterFieldNameArray") String[] filterFieldNameArray,
                               @Param("filterFieldValueArray") String[] filterFieldValueArray,
                               @Param("orderBy") String orderBy,
                               @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    List<EntryComment> pageChildren(Page<EntryComment> page,
                               @Param("keyword") String keyword, @Param("rootId") String rootId,
                               @Param("orderBy") String orderBy);

}
