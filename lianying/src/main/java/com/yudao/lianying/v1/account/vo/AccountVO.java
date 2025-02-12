package com.yudao.lianying.v1.account.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 账号信息表 
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
@Data
@ToString
public class AccountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String loginName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "登录密码")
    private String password;
    @ApiModelProperty(value = "无权限-0，质检-1，术语-2，质检术语-3，未启用-5，超管-100")
    private String level;
    @ApiModelProperty(value = "真实姓名")
    private String realname;

    private List<String> fieldIdList;
}
