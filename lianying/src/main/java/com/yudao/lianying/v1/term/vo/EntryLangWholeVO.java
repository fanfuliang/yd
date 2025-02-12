package com.yudao.lianying.v1.term.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-12-09 10:43:59
 */
@Data
public class EntryLangWholeVO {
    private EntryLangVO entryLangVO;
    private List<ItemVO> list;
}
