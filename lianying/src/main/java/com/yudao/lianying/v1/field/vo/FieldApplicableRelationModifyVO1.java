package com.yudao.lianying.v1.field.vo;

import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品线和具体型号关系表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Data
@ToString
public class FieldApplicableRelationModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "")
    private String field;
    @ApiModelProperty(value = "")
    private String applicableTo;


}
