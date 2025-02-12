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
public class EntryLangModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "条目id")
    private String entryId;
    @ApiModelProperty(value = "语言")
    private String language;
    @ApiModelProperty(value = "语言Id")
    private String languageId;
    @ApiModelProperty(value = "定义")
    private String definition;
    @ApiModelProperty("审核状态")
    private String state;


}
