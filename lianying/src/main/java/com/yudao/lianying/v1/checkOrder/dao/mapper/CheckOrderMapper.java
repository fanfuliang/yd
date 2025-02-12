package com.yudao.lianying.v1.checkOrder.dao.mapper;

import com.yudao.lianying.v1.checkOrder.dao.CheckOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.utils.ModifyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2021-05-11
 */
public interface CheckOrderMapper extends BaseMapper<CheckOrder> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);
}
