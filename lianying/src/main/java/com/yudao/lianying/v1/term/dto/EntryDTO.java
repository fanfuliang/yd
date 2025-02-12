package com.yudao.lianying.v1.term.dto;

import com.yudao.lianying.v1.term.dao.Entry;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/08/23 22:20 <br>
 * @see com.yudao.lianying.v1.term.dto <br>
 */
@Data
public class EntryDTO extends Entry {
    private String tbName;
}
