package com.yudao.lianying.v1.codeInfo.vo;

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
 * @since 2022-07-20
 */
@Data
@ToString
public class CodeInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty("关联的code_kind表id")
    private String codeKindId;

    @ApiModelProperty("code值")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("账号id")
    private String accountId;

    @ApiModelProperty("描述")
    private String remark;
}
