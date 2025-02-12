package com.yudao.lianying.v1.field.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 产品线
 * </p>
 *
 * @author liudong
 * @since 2024-12-01
 */
@Data
@ToString
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "账号id")
    private String accountId;
    @ApiModelProperty(value = "备注")
    private String remark;


}
