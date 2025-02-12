package com.yudao.lianying.v1.process.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 流程表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Data
@ToString
public class ProcessVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "")
    private String entryId;
    @ApiModelProperty(value = "流程类型，术语，纠错")
    private String type;
    @ApiModelProperty(value = "状态")
    private String state;
    @ApiModelProperty(value = "建议")
    private String suggestion;
    @ApiModelProperty(value = "产品线")
    private String field;
    @ApiModelProperty(value = "术语1")
    private String termText;
    @ApiModelProperty(value = "语言")
    private String language;
    @ApiModelProperty(value = "纠错")
    private String commentId;
    @ApiModelProperty(value = "纠错内容")
    private String commentText;
    @ApiModelProperty(value = "备注")
    private String remark;


}
