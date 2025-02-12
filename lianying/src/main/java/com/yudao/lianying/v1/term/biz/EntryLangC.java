package com.yudao.lianying.v1.term.biz;

import java.util.ArrayList;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2024/07/31 20:29 <br>
 * @see com.yudao.lianying.v1.term.biz <br>
 */
public class EntryLangC {

    public Integer getDefColumn() {
        return defColumn;
    }

    public void setDefColumn(Integer defColumn) {
        this.defColumn = defColumn;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }
    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }


    public List<Integer> getItemColumnList() {
        return itemColumnList;
    }

    public void setItemColumnList(List<Integer> itemColumnList) {
        this.itemColumnList = itemColumnList;
    }

    private Integer defColumn=0;
    private String langName ="";
    private String langId ="";
    private List<Integer> itemColumnList=new ArrayList<>();
}
