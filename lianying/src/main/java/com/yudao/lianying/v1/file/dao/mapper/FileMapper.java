package com.yudao.lianying.v1.file.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yudao.common.yudaocommon.annotation.TargetDataSource;
import com.yudao.lianying.v1.file.dao.FileInfo;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会议资源  Mapper 接口
 * </p>
 *
 * @author wufei
 * @since 2019-04-26
 */
@Repository
@TargetDataSource("yudao_base")
public interface FileMapper extends BaseMapper<FileInfo> {

}
