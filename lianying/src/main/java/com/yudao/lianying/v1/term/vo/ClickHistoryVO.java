package com.yudao.lianying.v1.term.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 术语点击历史
 * </p>
 *
 * @author liudong
 * @since 2021-02-01
 */
@Data
@ToString
public class ClickHistoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "文本")
    private String text;
    @ApiModelProperty("Qterm里的entry_id")
    private String entryId;

}
