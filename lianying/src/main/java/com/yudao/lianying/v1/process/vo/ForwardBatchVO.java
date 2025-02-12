package com.yudao.lianying.v1.process.vo;

import com.yudao.lianying.v1.comment.vo.CommentSmallVO;
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
public class ForwardBatchVO {

    @ApiModelProperty(value = "processId")
    private String processId;
    @ApiModelProperty(value = "remark")
    private String remark;

    private List<ForwardBatchSmallVO> forwardList;
}
