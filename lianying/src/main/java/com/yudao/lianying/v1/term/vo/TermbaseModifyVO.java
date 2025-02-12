package com.yudao.lianying.v1.term.vo;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.*;
import java.io.Serializable;

/**
 * <p>
 * 知识库
 * </p>
 *
 * @author fanfl
 * @since 2023-05-13
 */
@Data
@ToString
public class TermbaseModifyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "客户")
    private String client;
    @ApiModelProperty(value = "领域")
    private String domain;
    @ApiModelProperty(value = "项目")
    private String project;
    @ApiModelProperty(value = "主题")
    private String subject;
    @ApiModelProperty(value = "")
    private String friendlyName;
    @ApiModelProperty(value = "")
    private String isUsedInProject;
    @ApiModelProperty(value = "")
    private String numEntries;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "语言id，多个以逗号分隔")
    private String localId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "状态,1启用，2停止")
    private String state;
    @ApiModelProperty(value = "标签，多个以逗号分隔")
    private String tag;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("自定义字段，多个以逗号分隔")
    private String customField;

}
