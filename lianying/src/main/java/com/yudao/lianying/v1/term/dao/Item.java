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
@Table(name = "term_item")
@TableName("term_item")
public class Item implements Serializable {

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

    @TableField("term_entry_lang_id")
    @Column(columnDefinition = "varchar(36) DEFAULT NULL COMMENT 'termEntryLangId'")
    @ApiModelProperty("")
    private String termEntryLangId;

    @TableField("example")
    @Column(columnDefinition = "text COLLATE utf8mb4_bin COMMENT '例句'")
    @ApiModelProperty("例句")
    private String example;

    @TableField("for_doc")
    @Column(columnDefinition = "tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为规范术语'")
    @ApiModelProperty("是否为规范术语")
    private Integer forDoc;

    @TableField("multi_trans")
    @Column(columnDefinition = "tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否多译'")
    @ApiModelProperty("是否多译")
    private Integer multiTrans;

    @TableField("case_sensitivity")
    @Column(columnDefinition = "tinyint(1) NOT NULL DEFAULT '1' COMMENT '区分大小写,0no,1permissive,2yes'")
    @ApiModelProperty("区分大小写")
    private Integer caseSensitivity;

    @TableField("matching")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT '50% prefix' COMMENT '匹配规则'")
    @ApiModelProperty("匹配规则")
    private String matching;

    @TableField("forbidden")
    @Column(columnDefinition = "tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用'")
    @ApiModelProperty("是否禁用")
    private Integer forbidden;

    @TableField("text")
    @Column(columnDefinition = "text COLLATE utf8mb4_bin NOT NULL COMMENT '文本'")
    @ApiModelProperty("文本")
    private String text;

    @TableField("tag")
    @Column(columnDefinition = "varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '标签，多个以逗号分隔'")
    @ApiModelProperty("标签，多个以逗号分隔")
    private String tag;




    public Item(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public Item() {
    }
}
