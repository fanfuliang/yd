package com.yudao.lianying.v1.term.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-12-09 10:39:50
 */
@Data
public class EntryWholeVO {
    private EntryVO entryVO;
    private List<EntryLangWholeVO> list;
}
