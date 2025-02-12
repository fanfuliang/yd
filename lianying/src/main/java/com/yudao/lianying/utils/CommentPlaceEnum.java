package com.yudao.lianying.utils;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-07-15 17:49:48
 */
public enum CommentPlaceEnum {
    entry("202012160101", "词条"), example("202012160102", "例句"),
    note("202012160103", "备注");
    private String name;
    private String id;

    CommentPlaceEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
