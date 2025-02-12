package com.yudao.lianying.v1.checkOrder.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liudong
 * @since 2021-05-11
 */
@Data
@ToString
public class CheckOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "")
    private String account;
    @ApiModelProperty(value = "订单id，多个以逗号分隔")
    private String baseOrderId;


}
