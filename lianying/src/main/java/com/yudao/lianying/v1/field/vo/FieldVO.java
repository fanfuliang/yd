package com.yudao.lianying.v1.field.vo;

import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品线
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
@Data
@ToString
public class FieldVO implements Serializable {
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
