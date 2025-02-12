package com.yudao.lianying.v1.term.plugin.vo;

import lombok.Builder;

import java.util.List;

@Builder
public class EntryParamVO {

    private String id;
    private String proLine;//产品线
    private String proType;// 产品型号
    private String lanType;// 词性
    private String remark;

    // TODO 新建字段保存当前词条对应的飞书词条ID
    private String feiShuId; //建立对应关系的飞书词条ID


    private List<EntryLanVO> entryLanVOList;

    public String getFeiShuId() {
        return feiShuId;
    }

    public void setFeiShuId(String feiShuId) {
        this.feiShuId = feiShuId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProLine() {
        return proLine;
    }

    public void setProLine(String proLine) {
        this.proLine = proLine;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getLanType() {
        return lanType;
    }

    public void setLanType(String lanType) {
        this.lanType = lanType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<EntryLanVO> getEntryLanVOList() {
        return entryLanVOList;
    }

    public void setEntryLanVOList(List<EntryLanVO> entryLanVOList) {
        this.entryLanVOList = entryLanVOList;
    }
}
