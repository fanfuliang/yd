package com.yudao.lianying.v1.whitelist.dao.mapper;

import com.yudao.lianying.v1.whitelist.dao.Whitelist;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.lianying.utils.ModifyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 白名单 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2021-11-09
 */
public interface WhitelistMapper extends BaseMapper<Whitelist> {

    void modifyBatchByIdAuto(@Param("modifyVOList") List<ModifyVO> modifyVOList);

    int canCheck(String loginName);
}
