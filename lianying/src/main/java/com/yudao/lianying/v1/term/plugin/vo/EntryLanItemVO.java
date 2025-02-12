package com.yudao.lianying.v1.term.plugin.vo;

import lombok.Builder;

@Builder
public class EntryLanItemVO {
    private String id;
    private String entryLanId;

    private String content;//术语内容
    private String example;// 例句
    private boolean isValidCont;// 是否规范术语

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryLanId() {
        return entryLanId;
    }

    public void setEntryLanId(String entryLanId) {
        this.entryLanId = entryLanId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isValidCont() {
        return isValidCont;
    }

    public void setValidCont(boolean validCont) {
        isValidCont = validCont;
    }
}
