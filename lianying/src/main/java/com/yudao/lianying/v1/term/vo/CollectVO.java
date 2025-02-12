package com.yudao.lianying.v1.term.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 术语收藏
 * </p>
 *
 * @author liudong
 * @since 2021-02-02
 */
@Data
@ToString
public class CollectVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "Qterm里的entry_id")
    private String entryId;


}
