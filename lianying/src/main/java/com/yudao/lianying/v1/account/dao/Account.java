package com.yudao.lianying.v1.account.dao;

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
 * 账号信息表 
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
@Entity
@Data
@ToString
@Table(name = "account")
@TableName("account")
public class Account implements Serializable {

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

    @TableField("login_name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名'")
    @ApiModelProperty("用户名")
    private String loginName;

    @TableField("email")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱'")
    @ApiModelProperty("邮箱")
    private String email;

    @TableField("password")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin NOT NULL COMMENT '登录密码'")
    @ApiModelProperty("登录密码")
    private String password;

    @TableField("realname")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '真实姓名'")
    @ApiModelProperty("真实姓名")
    private String realname;

    @TableField("level")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT '0' COMMENT '无权限-0，质检-1，术语-2，质检术语-3，未启用-5，超管-100'")
    @ApiModelProperty("无权限-0，质检-1，术语-2，质检术语-3，未启用-5，超管-100")
    private String level;



    public Account(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public Account() {
    }
}
