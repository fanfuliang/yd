package com.yudao.lianying.v1.account.dto;

import com.yudao.lianying.v1.field.dao.Field;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 账号信息表
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */

@Data
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    private String id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 状态
     */
    private String status;

    private String loginName;

    private String email;

    private String password;

    private String level;

    private String realname;

    private Collection<Field> fields;

    public AccountDTO(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public AccountDTO() {
    }
}
