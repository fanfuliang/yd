package com.yudao.lianying.v1.term.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
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
public class EntryCommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "头像http地址")
    private String photo;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "")
    private String entryId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long createTime;
    @ApiModelProperty("根ID,根评论时为空")
    private String rootId;
    @ApiModelProperty("父级ID,根评论时为空")
    private String parentId;


}
