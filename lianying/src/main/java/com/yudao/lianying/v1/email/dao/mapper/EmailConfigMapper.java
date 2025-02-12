package com.yudao.lianying.v1.email.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.common.yudaocommon.model.page.Many2One;
import com.yudao.common.yudaocommon.model.page.One2Many;
import com.yudao.common.yudaocommon.model.page.One2One;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.email.dao.EmailConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2021-10-11
 */
public interface EmailConfigMapper extends BaseMapper<EmailConfig> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);

    List<EmailConfig> page(Page<EmailConfig> page,
                           @Param("whereLike") Many2One whereLike,
                           @Param("whereInList") List<One2Many> whereInList,
                           @Param("whereEqualsList") List<One2One> whereEqualsList,
                           @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                           @Param("orderByList") List<String> orderByList);

    EmailConfig pageDefault();
}
