package com.yudao.lianying.v1.checkConfig.dao;

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
 * 联影设置表
 * </p>
 *
 * @author liudong
 * @since 2020-12-14
 */
@Entity
@Data
@ToString
@Table(name = "check_config")
@TableName("check_config")
public class CheckConfig implements Serializable {

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

    @TableField("index_name")
    @Column(columnDefinition = "")
    @ApiModelProperty("检查项名称")
    private String indexName;

    @TableField("index_id")
    @Column(columnDefinition = "")
    @ApiModelProperty("检查项id")
    private String indexId;

    @TableField("enable")
    @Column(columnDefinition = "")
    @ApiModelProperty("是否启用")
    private Boolean enable;

    @TableField("level")
    @Column(columnDefinition = "")
    @ApiModelProperty("等级")
    private String level;

    @TableField("file_type")
    @Column(columnDefinition = "")
    @ApiModelProperty("文件类型")
    private String fileType;

    @TableField("rule_type")
    @Column(columnDefinition = "")
    @ApiModelProperty("规则")
    private String ruleType;

    @TableField("lang_type")
    @Column(columnDefinition = "")
    @ApiModelProperty("语种类型")
    private String langType;

    @TableField("lang")
    @Column(columnDefinition = "")
    @ApiModelProperty("语种")
    private String lang;



    public CheckConfig(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public CheckConfig() {
    }
}
