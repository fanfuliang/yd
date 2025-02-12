package com.yudao.lianying.v1.searchHistory.dao;

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
 * 查询历史表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Entity
@Data
@ToString
@Table(name = "search_history")
@TableName("search_history")
public class SearchHistory implements Serializable {

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
     * 创建人
     */
    @TableField(value = "create_by")
    @Column(name = "create_by", columnDefinition = "varchar(36) DEFAULT NULL COMMENT '创建人id'")
    @ApiModelProperty("创建人id")
    private String createBy;


    @TableField("field")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '领域，反映的是Qterm里的，所以不用id'")
    @ApiModelProperty("领域，反映的是Qterm里的，所以不用id")
    private String field;

    @TableField("applicable_to")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '具体产品型号或形态'")
    @ApiModelProperty("具体产品型号或形态")
    private String applicableTo;

    @TableField("status")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用于筛选的状态'")
    @ApiModelProperty("用于筛选的状态")
    private String status;

    @TableField("text")
    @Column(columnDefinition = "text COLLATE utf8mb4_bin COMMENT '检索内容'")
    @ApiModelProperty("检索内容")
    private String text;

    @TableField("result_num")
    @Column(columnDefinition = "int(11) DEFAULT NULL COMMENT '结果数量'")
    @ApiModelProperty("结果数量")
    private Integer resultNum;

    @TableField("category")
    @Column(columnDefinition = "varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型'")
    @ApiModelProperty("类型")
    private String category;

    @TableField("order_id")
    @Column(columnDefinition = "varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单id'")
    @ApiModelProperty("订单id")
    private String orderId;

    public SearchHistory() {
    }
}
