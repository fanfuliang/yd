package com.yudao.lianying.v1.term.dto;

import lombok.Data;

import java.util.List;

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
public class PluginSearchLanguage {
    private String Language;
    private String definition;

    private List<PluginSearchItem> TermItems;
}
