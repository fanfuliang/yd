package com.yudao.lianying.v1.codeInfo.dao;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liudong
 * @since 2019-06-13
 */
@TableName("code_info")
@Data
@ToString
@Entity
@Table(name = "code_info")
public class CodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @Id
    @Column(columnDefinition = "varchar(36) COLLATE utf8_bin NOT NULL")
    @ApiModelProperty("id")
    private String id;

    @TableField(value = "code_kind_id")
    @Column(name = "code_kind_id", columnDefinition = "varchar(36) COLLATE utf8_bin NOT NULL")
    @ApiModelProperty("关联的code_kind表id")
    private String codeKindId;

    @TableField(value = "code")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin NOT NULL")
    @ApiModelProperty("code值")
    private String code;
    /**
     * 值（中文）
     */
    @TableField(value = "value_zh")
    @Column(name = "value_zh", columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL")
    @ApiModelProperty("值（中文）")
    private String valueZh;
    /**
     * 值（英文）
     */
    @TableField(value = "value_en")
    @Column(name = "value_en", columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL")
    @ApiModelProperty("值（英文）")
    private String valueEn;
    /**
     * 值
     */
    @TableField(value = "value")
    @Column(columnDefinition = "varchar(255) NOT NULL")
    @ApiModelProperty("值")
    private String value;
    /**
     * 描述
     */
    @TableField(value = "remark")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL")
    @ApiModelProperty("描述")
    private String remark;
    /**
     * 账号id
     */
    @TableField(value = "account_id")
    @Column(name = "account_id", columnDefinition = "varchar(36) COLLATE utf8_bin NOT NULL")
    @ApiModelProperty("账号id")
    private String accountId;
    /**
     * 排序
     */
    @TableField(value = "sort")
    @Column(columnDefinition = "int(11) DEFAULT NULL")
    @ApiModelProperty("排序")
    private String sort;

    @TableField(value = "name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '名称'")
    @ApiModelProperty("名称")
    private String name;

    /**
     * 中文名称
     */
    @TableField(value = "zh_name")
    @Column(name = "zh_name", columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '中文名称'")
    @ApiModelProperty("中文名称")
    private String zhName;
    /**
     * 英文名称
     */
    @TableField(value = "en_name")
    @Column(name = "en_name", columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '英文名称'")
    @ApiModelProperty("英文名称")
    private String enName;

}
