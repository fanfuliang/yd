package com.yudao.lianying.v1.process.dao;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 流程表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Entity
@Data
@ToString
@Table(name = "process")
@TableName("process")
public class Process implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    @Id
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键'")
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

    @TableField("type")
    @Column(columnDefinition = "")
    @ApiModelProperty("流程类型，术语，纠错")
    private String type;

    @TableField("state")
    @Column(columnDefinition = "")
    @ApiModelProperty("状态")
    private String state;

    @TableField("suggestion")
    @Column(columnDefinition = "")
    @ApiModelProperty("流程评审建议")
    private String suggestion;

    @TableField("remark")
    @Column(columnDefinition = "")
    @ApiModelProperty("备注")
    private String remark;

    @TableField("parent_id")
    @Column(columnDefinition = "")
    @ApiModelProperty("上级流程ID")
    private String parentId;

    @TableField("readed")
    @Column(columnDefinition = "")
    @ApiModelProperty("已阅读")
    private Integer readed;




    @TableField("comment_id")
    @Column(columnDefinition = "")
    @ApiModelProperty("纠错ID")
    private String commentId;

    @TableField("comment_text")
    @Column(columnDefinition = "")
    @ApiModelProperty("反馈的问题")
    private String commentText;

    @TableField("place")
    @Column(columnDefinition = "")
    @ApiModelProperty("纠错位置")
    private String place;


    @TableField("entry_id")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String entryId;

    @TableField("field")
    @Column(columnDefinition = "")
    @ApiModelProperty("产品线")
    private String field;

    @TableField("field_name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '领域，产品线'")
    @ApiModelProperty("领域，产品线")
    private String fieldName;

    @TableField("term_text")
    @Column(columnDefinition = "")
    @ApiModelProperty("术语，逗号分隔")
    private String termText;

    @TableField("language")
    @Column(columnDefinition = "")
    @ApiModelProperty("语言")
    private String language;

    public Process(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public Process() {
    }
}
