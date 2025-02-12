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
 * code字典表
 * </p>
 *
 * @author liudong
 * @since 2019-06-13
 */
@TableName("code_kind")
@Data
@ToString
@Entity
@Table(name = "code_kind")
public class CodeKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @Id
    @Column(columnDefinition = "varchar(36) COLLATE utf8_bin NOT NULL")
    @ApiModelProperty("id")
    private String id;

    @TableField(value = "code")
    @Column(columnDefinition = "varchar(255) NOT NULL")
    @ApiModelProperty("")
    private String code;

    @TableField("remark")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL")
    @ApiModelProperty("")
    private String remark;

    @TableField("name")
    @Column(columnDefinition = "varchar(255) NOT NULL")
    @ApiModelProperty("")
    private String name;
}
