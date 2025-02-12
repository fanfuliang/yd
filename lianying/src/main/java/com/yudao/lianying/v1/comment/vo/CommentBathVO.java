package com.yudao.lianying.v1.comment.vo;

import com.yudao.lianying.v1.term.vo.EntryLangWholeVO;
import com.yudao.lianying.v1.term.vo.EntryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-12-09 10:39:50
 */
@Data
public class CommentBathVO {

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "Qterm里的entry_id")
    private String entryId;
//    @ApiModelProperty(value = "语言")
//    private String language;
    @ApiModelProperty(value = "状态")
    private String state;

    private List<CommentSmallVO> commentVOList;
}
