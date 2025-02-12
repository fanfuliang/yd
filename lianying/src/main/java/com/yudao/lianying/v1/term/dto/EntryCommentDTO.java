package com.yudao.lianying.v1.term.dto;

import com.yudao.lianying.v1.term.dao.EntryComment;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/05/25 22:42 <br>
 * @see com.yudao.lianying.v1.term.dto <br>
 */
@Data
public class EntryCommentDTO extends EntryComment {
    private Integer childCount;
}
