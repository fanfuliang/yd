package com.yudao.lianying.v1.term.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/10/02 23:48 <br>
 * @see com.yudao.lianying.v1.account.dto <br>
 */
@Data
public class PluginSearchItem {
    private String Text;
    private String Example;
    private Integer forDoc;
}
