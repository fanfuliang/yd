package com.yudao.lianying.v1.term.dao;

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
 * entry_id评论表
 * </p>
 *
 * @author liudong
 * @since 2021-01-15
 */
@Entity
@Data
@ToString
@Table(name = "term_entry_comment", catalog = "lianying")
@TableName("term_entry_comment")
public class EntryComment implements Serializable {

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

    @TableField("photo")
    @Column(columnDefinition = "")
    @ApiModelProperty("头像http地址")
    private String photo;

    @TableField("content")
    @Column(columnDefinition = "")
    @ApiModelProperty("内容")
    private String content;

    @TableField("entry_id")
    @Column(columnDefinition = "varchar(36) DEFAULT NULL COMMENT 'entryId'")
    @ApiModelProperty("")
    private String entryId;

    @TableField("root_id")
    @Column(columnDefinition = "varchar(36) DEFAULT NULL COMMENT 'rootId'")
    @ApiModelProperty("根ID,根为空")
    private String rootId;

    @TableField("parent_id")
    @Column(columnDefinition = "varchar(36) DEFAULT NULL COMMENT 'parentId'")
    @ApiModelProperty("父级ID,根为空")
    private String parentId;



    public EntryComment(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public EntryComment() {
    }
}
