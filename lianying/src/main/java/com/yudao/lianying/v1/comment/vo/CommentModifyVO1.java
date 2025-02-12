package com.yudao.lianying.v1.comment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 纠错表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Data
@ToString
public class CommentModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "Qterm里的entry_id")
    private String entryId;
    @ApiModelProperty(value = "内容")
    private String text;
    @ApiModelProperty(value = "产品线")
    private String field;
    @ApiModelProperty(value = "术语1")
    private String termText;
    @ApiModelProperty(value = "语言")
    private String language;
    @ApiModelProperty(value = "状态")
    private String state;
    @ApiModelProperty(value = "纠错位置")
    private String place;
    @ApiModelProperty("反馈的问题")
    private String question;
    @ApiModelProperty("建议/回答")
    private String advice;

}
