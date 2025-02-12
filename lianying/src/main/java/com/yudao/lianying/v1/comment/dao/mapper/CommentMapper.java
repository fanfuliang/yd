package com.yudao.lianying.v1.comment.dao.mapper;

import com.yudao.lianying.v1.comment.dao.Comment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 纠错表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
public interface CommentMapper extends BaseMapper<Comment> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);
}
