package com.yudao.lianying.v1.term.dao.mapper;

import com.yudao.lianying.v1.term.dao.Collect;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.utils.ModifyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 术语收藏 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2021-02-02
 */
public interface CollectMapper extends BaseMapper<Collect> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);
}
