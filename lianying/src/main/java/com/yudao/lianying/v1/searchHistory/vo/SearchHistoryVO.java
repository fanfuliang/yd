package com.yudao.lianying.v1.searchHistory.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 查询历史表
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Data
@ToString
public class SearchHistoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "领域，反映的是Qterm里的，所以不用id")
    private String field;
    @ApiModelProperty(value = "具体产品型号或形态")
    private String applicableTo;
    @ApiModelProperty(value = "筛选状态")
    private String status;
    @ApiModelProperty(value = "检索内容")
    private String text;
    @ApiModelProperty(value = "结果数量")
    private Integer resultNum;
    @ApiModelProperty("类型")
    private String category;
    @ApiModelProperty("订单id")
    private String orderId;

}
