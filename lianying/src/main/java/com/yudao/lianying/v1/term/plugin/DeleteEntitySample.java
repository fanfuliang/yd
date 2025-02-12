package com.yudao.lianying.v1.term.plugin;

import com.lark.oapi.Client;
import com.lark.oapi.service.lingo.v1.model.*;

import java.util.List;
import java.util.Map;

import com.yudao.lianying.v1.term.plugin.vo.EntryParamVO;

// SDK 使用文档：https://github.com/larksuite/oapi-sdk-java/tree/v2_main
// 复制该 Demo 后, 需要将 "YOUR_APP_ID", "YOUR_APP_SECRET" 替换为自己应用的 APP_ID, APP_SECRET.
public class DeleteEntitySample {

    public static DeleteEntityResp DeleteFeiShuEntity(List<EntryParamVO> tobeDeletedParams, String appId, String appSecret) throws Exception {
        if (tobeDeletedParams == null || tobeDeletedParams.isEmpty()) {
            return null;
        }
        Client client = Client.newBuilder(appId, appSecret).build();
        for (EntryParamVO param : tobeDeletedParams) {
            if (param.getFeiShuId() == null || param.getFeiShuId().length() == 0) {
                continue;
            }
            // 创建请求对象
            DeleteEntityReq req = DeleteEntityReq.newBuilder()
                    .entityId(param.getFeiShuId())
                    .build();

            // 发起请求
            DeleteEntityResp resp = client.lingo().entity().delete(req);

            // 处理服务端错误
            if (!resp.success()) {
                System.out.println(String.format("code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
                return resp;
            }
            // 业务数据处理
            return resp;
        }

        return null;
    }
}
