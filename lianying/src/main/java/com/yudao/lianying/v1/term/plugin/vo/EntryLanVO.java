package com.yudao.lianying.v1.term.plugin.vo;

import lombok.Builder;

import java.util.List;

@Builder
public class EntryLanVO {
    private String entryId;//外键，引用entry
    private String id;
    private String lanId;//语言
    private String define;// 定义
    private List<EntryLanItemVO> entryLanItemVOList;

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanId() {
        return lanId;
    }

    public void setLanId(String lanId) {
        this.lanId = lanId;
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public List<EntryLanItemVO> getEntryLanItemVOList() {
        return entryLanItemVOList;
    }

    public void setEntryLanItemVOList(List<EntryLanItemVO> entryLanItemVOList) {
        this.entryLanItemVOList = entryLanItemVOList;
    }
}
