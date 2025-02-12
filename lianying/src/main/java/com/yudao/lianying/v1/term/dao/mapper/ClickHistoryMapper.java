package com.yudao.lianying.v1.term.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.dao.ClickHistory;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 术语点击历史 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2021-02-01
 */
public interface ClickHistoryMapper extends BaseMapper<ClickHistory> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);

    List<Map> hotTerm(@Param("time") Date time);
}
