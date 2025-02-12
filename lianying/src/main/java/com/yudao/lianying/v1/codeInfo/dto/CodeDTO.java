package com.yudao.lianying.v1.codeInfo.dto;

import com.yudao.lianying.v1.codeInfo.dao.CodeInfo;
import com.yudao.lianying.v1.codeInfo.dao.CodeKind;
import lombok.Data;

import java.util.List;

/**
 * @author ：liudong
 * @date ：Created in 2019/6/13 11:55
 * @description：
 * @modified By：
 * @version: $
 */
@Data
public class CodeDTO {
    private CodeKind codeKind;
    private List<CodeInfo> codeInfoList;
}
