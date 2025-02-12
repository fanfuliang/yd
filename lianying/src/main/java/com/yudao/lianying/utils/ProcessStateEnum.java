package com.yudao.lianying.utils;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-07-15 17:49:48
 */
public enum ProcessStateEnum {
    to_process("202012120201", "待处理"), forwarded("202012120202", "已转发"),
    to_approve("202012120206", "待评审"),
    approved("202012120203", "已评审"),
    refused("202012120204", "已拒绝"),
    accepted("202012120205", "已接受"),
//    manageAdd("202012120206", "管理员创建"),
    to_back("202012120207", "已退回"),;
    private String name;
    private String id;

    ProcessStateEnum(String id, String name) {
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
