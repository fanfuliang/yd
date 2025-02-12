package com.yudao.lianying.v1.term.dto;

import com.yudao.lianying.v1.term.dao.EntryLang;
import lombok.Data;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/09/17 8:56 <br>
 * @see com.yudao.lianying.v1.term.dto <br>
 */
@Data
public class EntryLangDTO extends EntryLang {
    private String languageNameZh;
    private String languageNameEn;
}
