package com.yudao.lianying.v1.notice.dao.mapper;

import com.yudao.lianying.v1.notice.dao.Notice;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 通知表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    Integer myUnreadNoticeNum (@Param("account") String account);
}
