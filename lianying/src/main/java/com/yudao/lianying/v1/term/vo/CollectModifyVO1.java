package com.yudao.lianying.v1.term.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.*;
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
public class CollectModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "Qterm里的entry_id")
    private String entryId;


}
