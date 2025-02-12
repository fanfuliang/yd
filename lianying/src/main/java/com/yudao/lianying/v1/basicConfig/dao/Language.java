package com.yudao.lianying.v1.basicConfig.dao;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 翻译语言表
 * </p>
 *
 * @author liudong
 * @since 2019-08-29
 */
@TableName("t_language")
@ApiModel(value="翻译语言信息", description="翻译语言实体")
@Entity
@Data
@Table(name = "t_language")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "keyid")
    @ApiModelProperty(value="主键", required=true)
    @Id
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT COMMENT '主键'")
    private Integer keyid;
    /**
     * 中文名称
     */
    @TableField("name_zh")
    @ApiModelProperty(value="中文名称", required=true, example="中文名称")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin NOT NULL COMMENT '中文名称'")
    private String nameZh;
    /**
     * 英文名称
     */
    @TableField("name_en")
    @ApiModelProperty(value="英文名称", required=true, example="英文名称")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin NOT NULL COMMENT '英文名称'")
    private String nameEn;
    /**
     * 编码
     */
    @TableField("code")
    @ApiModelProperty(value="编码", required=true, example="编码")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin NOT NULL COMMENT '编码'")
    private String code;

    /**
     * 用途
     */
    @TableField("purpose")
    @ApiModelProperty("用途（1-备用，2-快译源语言，4-快译目标语言），位运算，每种用途占用一位的1，对逻辑与运算的结果进行判断，例：3 & 2 == 2，说明有2这个类型")
    @Column(columnDefinition = "int(11) DEFAULT NULL COMMENT '用途（1-备用，2-快译源语言，4-快译目标语言），位运算，对逻辑与运算的结果进行判断，例：3 & 2 == 2，说明有2这个类型'")
    private Integer purpose;

    @TableField("pinyin")
    @ApiModelProperty(value="拼音")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '拼音'")
    private String pinyin;

    @TableField("lang_family_id")
    @ApiModelProperty(value="语系id")
    @Column(columnDefinition = "varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '语系id'")
    private String langFamilyId;


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

    @TableField("remark")
    @Column(columnDefinition = "text COLLATE utf8_bin COMMENT '备注'")
    @ApiModelProperty("备注")
    private String remark;

    public Language(Integer id, String statusValue) {
        this.keyid = id;
        this.status = statusValue;
    }

    public Language() {
    }
}
