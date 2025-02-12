package com.yudao.lianying.v1.file.dao;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <p>
 * 会议文件资源
 * </p>
 *
 * @author wufei
 * @since 2019-04-26
 */
@Entity
@Data
@ToString
@TableName("file")
@Table(name = "file")
public class FileInfo {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键", required = true, example = "fc31a05b-70ad-4f2a-952b-3dff50bcd727")
    @Id
    @Column(columnDefinition = "varchar(36) COLLATE utf8_bin NOT NULL COMMENT '主键'")
    private String id;

    /**
     * 原文件名称
     */
    @TableField("originalname")
    @Column(columnDefinition = "varchar(500) COLLATE utf8_bin NOT NULL COMMENT '原文件名称'")
    @ApiModelProperty("原文件名称")
    private String originalname;
    /**
     * 名称
     */
    @TableField("name")
    @Column(columnDefinition = "varchar(50) COLLATE utf8_bin NOT NULL COMMENT '保存的文件名称（uuid）'")
    @ApiModelProperty("保存的文件名称")
    private String name;

    /**
     * 文件后缀
     */
    @TableField("file_suffix")
    @Column(columnDefinition = "varchar(32) COLLATE utf8_bin NOT NULL COMMENT '文件后缀'")
    @ApiModelProperty("文件后缀")
    private String fileSuffix;
    /**
     * 本地路径
     */
    @TableField("local_path")
    @Column(columnDefinition = "varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '本地路径'")
    @ApiModelProperty("本地路径")
    private String localPath;
    /**
     * http路径
     */
    @TableField("http_path")
    @Column(columnDefinition = "varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'http路径'")
    @ApiModelProperty("http路径")
    private String httpPath;
    /**
     * 文件路径
     */
    @TableField("ftp_path")
    @Column(columnDefinition = "varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'ftp路径'")
    @ApiModelProperty("ftp路径")
    private String ftpPath;
    /**
     * 文件大小
     */
    @TableField("size")
    @Column(columnDefinition = "bigint(11) DEFAULT NULL COMMENT '文件大小'")
    @ApiModelProperty("文件大小")
    private Long size;

    /**
     * 关联app名称
     */
    @TableField("app_name")
    @Column(columnDefinition = "varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '关联app的名称'")
    @ApiModelProperty("关联app的名称")
    private String appName;

//    /**
//     * 上传文件的地点（主要是图片）
//     */
//    @TableField("place")
//    @Column(columnDefinition = "varchar(1000) CHARACTER SET utf8 DEFAULT NULL COMMENT '上传文件的地点(主要用于图片)'")
//    @ApiModelProperty("上传文件的地点")
//    private String place;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 状态：T正常 D删除 F禁用
     */
    @TableField("status")
    @Column(columnDefinition = "varchar(32) DEFAULT 'T' COMMENT '状态：T正常 D删除 F禁用'")
    @ApiModelProperty("状态：T正常 D删除 F禁用")
    private String status;

    @TableField("word_num")
    @Column(columnDefinition = "int(11) DEFAULT NULL COMMENT '字数'")
    @ApiModelProperty("字数")
    private Integer wordNum;
}
