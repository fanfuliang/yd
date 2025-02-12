package com.yudao.lianying.v1.email.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liudong
 * @since 2021-10-11
 */
@Data
@ToString
public class EmailConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "")
    private String host;
    @ApiModelProperty(value = "")
    private String port;
    @ApiModelProperty(value = "")
    private String fromEmail;
    @ApiModelProperty(value = "")
    private String username;
    @ApiModelProperty(value = "")
    private String nickname;
    @ApiModelProperty(value = "")
    private String password;
}
