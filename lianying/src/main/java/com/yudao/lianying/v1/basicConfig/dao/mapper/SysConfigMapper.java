package com.yudao.lianying.v1.basicConfig.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.model.page.Many2One;
import com.yudao.common.yudaocommon.model.page.One2Many;
import com.yudao.common.yudaocommon.model.page.One2One;
import com.yudao.lianying.v1.basicConfig.dao.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fanfl
 * @since 2024-04-06
 */
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    List<SysConfig> page(Page<SysConfig> page,
                         @Param("whereLike") Many2One whereLike,
                         @Param("whereInList") List<One2Many> whereInList,
                         @Param("whereEqualsList") List<One2One> whereEqualsList,
                         @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                         @Param("orderByList") List<String> orderByList);
}
