package com.yudao.lianying.v1.term.vo;

import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Data
@ToString
public class ItemModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "")
    private String termEntryLangId;
    @ApiModelProperty(value = "例句")
    private String example;
    @ApiModelProperty(value = "是否为规范术语")
    private Integer forDoc;
    @ApiModelProperty(value = "文本")
    private String text;
    @ApiModelProperty("标签，多个以逗号分隔")
    private String tag;
    @ApiModelProperty("是否多译")
    private Integer multiTrans;
    @ApiModelProperty("区分大小写")
    private Integer caseSensitivity;
    @ApiModelProperty("匹配规则")
    private String matching;
    @ApiModelProperty("是否禁用")
    private Integer forbidden;


}
