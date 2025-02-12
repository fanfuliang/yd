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
@Table(name = "term_entry")
@TableName("term_entry")
public class Entry implements Serializable {

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
     * 用于筛选的状态
     */
    @TableField(value = "status")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用于筛选的状态'")
    @ApiModelProperty("用于筛选的状态")
    private String status;

    @TableField("tb_guid")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库id'")
    @ApiModelProperty("库id")
    private String tbGuid;

    @TableField("category_id")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类别ID'")
    @ApiModelProperty("类别ID")
    private String categoryId;

    @TableField("category_name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类别名称'")
    @ApiModelProperty("类别名称")
    private String categoryName;

    @TableField("field")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '领域，产品线'")
    @ApiModelProperty("领域，产品线")
    private String field;

    @TableField("field_name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '领域，产品线'")
    @ApiModelProperty("领域，产品线")
    private String fieldName;

    @TableField("applicable_to")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '具体产品型号或形态，例如：U82、Mammo'")
    @ApiModelProperty("具体产品型号或形态，例如：U82、Mammo")
    private String applicableTo;

    @TableField("applicable_to_name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '具体产品型号或形态，例如：U82、Mammo'")
    @ApiModelProperty("具体产品型号或形态，例如：U82、Mammo")
    private String applicableToName;

    @TableField("part_of_speech")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '词性'")
    @ApiModelProperty("词性")
    private String partOfSpeech;

    @TableField("part_of_speech_name")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '词性名名称'")
    @ApiModelProperty("词性名称")
    private String partOfSpeechName;

    @TableField("note")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注'")
    @ApiModelProperty("备注")
    private String note;

    @TableField("image")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片url'")
    @ApiModelProperty("图片url")
    private String image;

    @TableField("image_second")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片2url'")
    @ApiModelProperty("图片2url")
    private String imageSecond;

    @TableField("image_third")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片3url'")
    @ApiModelProperty("图片3url")
    private String imageThird;

    @TableField("image_caption")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片说明'")
    @ApiModelProperty("图片说明")
    private String imageCaption;

    @TableField("image_second_caption")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片2说明'")
    @ApiModelProperty("图片2说明")
    private String imageSecondCaption;

    @TableField("image_third_caption")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片3说明'")
    @ApiModelProperty("图片3说明")
    private String imageThirdCaption;

    @TableField("state")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '审核状态'")
    @ApiModelProperty("审核状态")
    private String state;

    @TableField("created")
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '创建时间'")
    @ApiModelProperty("创建时间")
    private Date created;

    @TableField("creator")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人'")
    @ApiModelProperty("创建人")
    private String creator;

    @TableField("modified")
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '修改时间'")
    @ApiModelProperty("修改时间")
    private Date modified;

    @TableField("modifier")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人'")
    @ApiModelProperty("修改人")
    private String modifier;

    @TableField("fei_shu_id")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '飞书词条同步Id'")
    @ApiModelProperty("飞书词条同步Id")
    private String feiShuId;

    @TableField("custom_field_value")
    @Column(columnDefinition = "longtext COLLATE utf8_bin COMMENT '自定义字段值'")
    @ApiModelProperty("自定义字段值")
    private String customFieldValue;

    public Entry(String id, String statusValue) {
        this.id = id;
        this.status = statusValue;
    }

    public Entry() {
    }
}
