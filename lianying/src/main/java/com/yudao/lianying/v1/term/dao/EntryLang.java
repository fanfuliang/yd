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
 * 
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Entity
@Data
@ToString
@Table(name = "term_entry_lang")
@TableName("term_entry_lang")
public class EntryLang implements Serializable {

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

    @TableField("entry_id")
    @Column(columnDefinition = "int(32) NOT NULL COMMENT '条目id'")
    @ApiModelProperty("条目id")
    private String entryId;

    @TableField("language")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '语言'")
    @ApiModelProperty("语言名称")
    private String language;

    @TableField("language_id")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '语言Id'")
    @ApiModelProperty("语言")
    private String languageId;

    @TableField("definition")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '定义'")
    @ApiModelProperty("定义")
    private String definition;

    @TableField(value = "state")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT 'To_Be_Approved' COMMENT '审核状态 1To Be Approved 2Approved'")
    @ApiModelProperty("审核状态")
    private String state;



    public EntryLang(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public EntryLang() {
    }
}
