package com.yudao.lianying.v1.process.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/06/24 22:18 <br>
 * @see com.yudao.lianying.v1.process.dto <br>
 */
@Data
public class TermProcessManageDTO {
    private String id;
    private String entryId;
    private String field;
    private String fieldName;
    private String termText;
    private String state;
    private Integer readed;
    private String suggestion;
    private String lastestCheck;
    private String parentId;
    private List<TermProcessManageDTO> forwardProcessList;

    private Date createTime;
    private Date updateTime;
    private String creator;


}
