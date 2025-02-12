package com.yudao.lianying.v1.email.dao;

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
 * @since 2021-10-11
 */
@Entity
@Data
@ToString
@Table(name = "c_email_config")
@TableName("c_email_config")
public class EmailConfig implements Serializable {

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

    @TableField("host")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String host;

    @TableField("port")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String port;

    @TableField("from_email")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String fromEmail;

    @TableField("username")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String username;

    @TableField("nickname")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String nickname;

    @TableField("password")
    @Column(columnDefinition = "")
    @ApiModelProperty("")
    private String password;

    public EmailConfig(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public EmailConfig() {
    }
}
