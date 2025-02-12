package com.yudao.lianying.v1.account.dao.mapper;

import com.yudao.lianying.v1.account.dao.Account;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 账号信息表  Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
public interface AccountMapper extends BaseMapper<Account> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    String getUserEmail(String createBy);
}
