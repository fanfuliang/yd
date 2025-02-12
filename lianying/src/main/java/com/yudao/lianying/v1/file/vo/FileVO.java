package com.yudao.lianying.v1.file.vo;

import lombok.Data;

/**
 * <p>
 * 会议文件资源
 * </p>
 *
 * @author liudong
 * @since 2019-04-26
 */
@Data
public class FileVO{
    private String originalname;
    private String localPath;
    private String imgBaseStr;
    private String appName;
    private Integer type;
    private Integer wordNum;
}
