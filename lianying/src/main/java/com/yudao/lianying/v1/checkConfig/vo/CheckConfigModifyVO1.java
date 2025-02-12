package com.yudao.lianying.v1.checkConfig.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 联影设置表
 * </p>
 *
 * @author liudong
 * @since 2020-12-14
 */
@Data
@ToString
public class CheckConfigModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "是否启用")
    private Boolean enable;
    @ApiModelProperty(value = "等级")
    private String level;
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    @ApiModelProperty(value = "规则")
    private String ruleType;
}
