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
 * entry_id评论表
 * </p>
 *
 * @author liudong
 * @since 2021-01-15
 */
@Data
@ToString
public class EntryCommentModifyVO1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "修改人id")
    private String updateBy;
    @ApiModelProperty(value = "头像http地址")
    private String photo;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "")
    private String entryId;
    @ApiModelProperty("根ID,根为空")
    private String rootId;
    @ApiModelProperty("父级ID,根为空")
    private String parentId;


}
