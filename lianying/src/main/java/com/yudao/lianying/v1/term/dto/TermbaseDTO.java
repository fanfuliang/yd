package com.yudao.lianying.v1.term.dto;

import com.yudao.lianying.v1.basicConfig.dao.Language;
import com.yudao.lianying.v1.term.dao.Termbase;
import lombok.Data;

import java.util.List;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/05/14 9:30 <br>
 * @see com.yudao.lianying.v1.term.dto <br>
 */
@Data
public class TermbaseDTO extends Termbase {
    private Integer numEntries;
    private String creator;
    private String modifier;
    private List<Language> localObjectList;
}
