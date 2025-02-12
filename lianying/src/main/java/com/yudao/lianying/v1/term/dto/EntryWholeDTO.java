package com.yudao.lianying.v1.term.dto;

import com.yudao.lianying.v1.term.dao.Entry;
import lombok.Data;

import java.util.List;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-12-09 10:39:50
 */
@Data
public class EntryWholeDTO {
    private EntryDTO entry;
    private List<EntryLangWholeDTO> list;
}
