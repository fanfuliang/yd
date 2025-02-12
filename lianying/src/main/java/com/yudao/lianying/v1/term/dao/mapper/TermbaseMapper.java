package com.yudao.lianying.v1.term.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.model.page.Many2One;
import com.yudao.common.yudaocommon.model.page.One2Many;
import com.yudao.common.yudaocommon.model.page.One2One;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.term.dao.Termbase;
import com.yudao.lianying.v1.term.dto.TermbaseDTO;
import com.yudao.lianying.v1.term.dto.UihTermDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 知识库 Mapper 接口
 * </p>
 *
 * @author fanfl
 * @since 2023-05-13
 */
public interface TermbaseMapper extends BaseMapper<Termbase> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);

    List<TermbaseDTO> page2(Page<TermbaseDTO> page,
                           @Param("whereLike") Many2One whereLike,
                           @Param("whereInList") List<One2Many> whereInList,
                           @Param("whereEqualsList") List<One2One> whereEqualsList,
                           @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                           @Param("orderByList") List<String> orderByList);

    List<TermbaseDTO> page(Page<TermbaseDTO> page,
                                     @Param("keyword") String keyword, @Param("filterFieldNameArray") String[] filterFieldNameArray,
                                     @Param("filterFieldValueArray") String[] filterFieldValueArray, @Param("localIdList") List<String> localIdList,
                                     @Param("orderBy") String orderBy, @Param("tags") List<String> tags,
                                     @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                     @Param("accountId") String accountId);

    TermbaseDTO getById(String id);

    void updateLocalId(@Param("id") String id, @Param("newlocalId") String newlocalId);

    List<UihTermDTO> getUihTermList();
}

