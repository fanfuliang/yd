package com.yudao.lianying.v1.notice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 通知表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Data
@ToString
public class NoticeModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "")
    private String entryId;
    @ApiModelProperty(value = "评论id")
    private String commentId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "备注")
    private String remark;

}
