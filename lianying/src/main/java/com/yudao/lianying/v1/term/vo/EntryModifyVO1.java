package com.yudao.lianying.v1.term.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
public class EntryModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "库id")
    private String tbGuid;
    @ApiModelProperty(value = "领域，产品线")
    private String field;
    @ApiModelProperty(value = "具体产品型号或形态，例如：U82、Mammo")
    private String applicableTo;
    @ApiModelProperty(value = "领域，产品线")
    private String fieldName;
    @ApiModelProperty(value = "具体产品型号或形态，例如：U82、Mammo")
    private String applicableToName;
    @ApiModelProperty(value = "类别ID")
    private String categoryId;
    @ApiModelProperty(value = "类别名称")
    private String categoryName;
    @ApiModelProperty(value = "词性")
    private String partOfSpeech;
    @ApiModelProperty(value = "词性名称")
    private String partOfSpeechName;
    @ApiModelProperty(value = "备注")
    private String note;
    @ApiModelProperty(value = "图片url")
    private String image;
    @ApiModelProperty(value = "图片说明")
    private String imageCaption;
    @ApiModelProperty(value = "修改人")
    private String modifier;
    @ApiModelProperty("自定义字段值，多个以逗号分隔")
    private String customFieldValue;


}
