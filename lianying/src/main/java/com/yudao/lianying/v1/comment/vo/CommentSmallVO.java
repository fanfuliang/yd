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
public class CommentSmallVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "反馈的问题")
    private String text;
    @ApiModelProperty(value = "纠错位置")
    private String place;
//    @ApiModelProperty("反馈的问题")
//    private String question;
    @ApiModelProperty("建议/回答")
    private String advice;
}
