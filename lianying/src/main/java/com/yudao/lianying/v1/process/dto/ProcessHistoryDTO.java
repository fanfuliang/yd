package com.yudao.lianying.v1.process.dto;

import lombok.Data;

import java.util.Date;

/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2021-02-02 18:40:24
 */
@Data
public class ProcessHistoryDTO {
    private String type;
    private Object detail;
    private String creator;
    private Date time;

    public ProcessHistoryDTO() {
    }

    public ProcessHistoryDTO(String type, Object detail, String creator, Date time) {
        this.type = type;
        this.detail = detail;
        this.creator = creator;
        this.time = time;
    }
}
