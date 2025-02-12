package com.yudao.lianying.v1.basicConfig.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <Description> <br>
 *
 * @author fanfl<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2024/10/02 15:58 <br>
 * @see com.yudao.lianying.v1.basicConfig.dto <br>
 */
@Data
public class SysConfigData {
    private String feishuTokenUrl;
    private String appId;
    private String appSecret;
    private List<String[]> uihFsRepoList;
    private Map<String,String> uih2FsMap;

    public SysConfigData(){
        this.uihFsRepoList =new ArrayList<>();
        uih2FsMap=new HashMap<>();
    }
}
