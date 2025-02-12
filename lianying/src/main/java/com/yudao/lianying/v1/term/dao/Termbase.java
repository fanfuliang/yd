package com.yudao.lianying.v1.term.dao;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import io.swagger.annotations.*;

/**
 * <p>
 * 知识库
 * </p>
 *
 * @author fanfl
 * @since 2023-05-13
 */
@Entity
@Data
@ToString
@Table(name = "t_termbase")
@TableName("t_termbase")
public class Termbase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    @Id
    @Column(columnDefinition = "varchar(36) COLLATE utf8_bin NOT NULL COMMENT '主键'")
    private String id;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Column(name = "create_time", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @Column(name = "update_time", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    @Column(name = "create_by", columnDefinition = "varchar(36) DEFAULT NULL COMMENT '创建人id'")
    @ApiModelProperty("创建人id")
    private String createBy;
    /**
     * 更新人
     */
    @TableField(value = "update_by")
    @Column(name = "update_by", columnDefinition = "varchar(36) DEFAULT NULL COMMENT '更新人id'")
    @ApiModelProperty("更新人id")
    private String updateBy;
    /**
     * 状态
     */
    @TableField(value = "status")
    @Column(columnDefinition = "varchar(2) DEFAULT 'T' COMMENT '状态 T正常 D删除'")
    @ApiModelProperty("状态")
    private String status;

    @TableField("client")
    @Column(columnDefinition = "")
    @ApiModelProperty("客户")
    private String client;

    @TableField("domain")
    @Column(columnDefinition = "")
    @ApiModelProperty("领域")
    private String domain;

    @TableField("project")
    @Column(columnDefinition = "")
    @ApiModelProperty("项目")
    private String project;

    @TableField("subject")
    @Column(columnDefinition = "")
    @ApiModelProperty("主题")
    private String subject;

    @TableField("friendly_name")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String friendlyName;

    @TableField("is_used_in_project")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String isUsedInProject;

    @TableField("name")
    @Column(columnDefinition = "")
    @ApiModelProperty("名称")
    private String name;

    @TableField("code")
    @Column(columnDefinition = "")
    @ApiModelProperty("编码")
    private String code;

    @TableField("local_id")
    @Column(columnDefinition = "")
    @ApiModelProperty("语言id，多个以逗号分隔")
    private String localId;

    @TableField("description")
    @Column(columnDefinition = "")
    @ApiModelProperty("描述")
    private String description;

    @TableField("state")
    @Column(columnDefinition = "")
    @ApiModelProperty("状态,1启用，2停止")
    private String state;

    @TableField("tag")
    @Column(columnDefinition = "")
    @ApiModelProperty("标签，多个以逗号分隔")
    private String tag;

//    @TableField("num_entries")
//    @Column(columnDefinition = "int(11) DEFAULT NULL COMMENT '条目总数量'")
//    @ApiModelProperty("条目总数量")
//    private Integer numEntries;

    @TableField("remark")
    @Column(columnDefinition = "")
    @ApiModelProperty("备注")
    private String remark;

    @TableField("custom_field")
    @Column(columnDefinition = "longtext COLLATE utf8_bin COMMENT '自定义字段'")
    @ApiModelProperty("自定义字段")
    private String customField;



    public Termbase(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public Termbase() {
    }
}
