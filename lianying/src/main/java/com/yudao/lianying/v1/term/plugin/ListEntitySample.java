package com.yudao.lianying.v1.term.plugin;

import com.lark.oapi.Client;
import com.lark.oapi.service.lingo.v1.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lark.oapi.core.request.RequestOptions;
import com.yudao.lianying.utils.HttpUtils;
import com.yudao.lianying.v1.basicConfig.dto.SysConfigData;

// SDK 使用文档：https://github.com/larksuite/oapi-sdk-java/tree/v2_main
// 复制该 Demo 后, 需要将 "YOUR_APP_ID", "YOUR_APP_SECRET" 替换为自己应用的 APP_ID, APP_SECRET.
public class ListEntitySample {

    public static List<Entity> ListFeiShuEntity(String tokenUrl,String appId,String appSecret,String repoId) {

        String token = HttpUtils.getTenantAccessToken(tokenUrl,appId,appSecret);

        // 构建client
        Client client = Client.newBuilder(appId,appSecret).build();
        List<Entity> resEntity = new ArrayList<>();
        String pageToken = request4List("", client, token, resEntity, repoId);

        // 业务数据处理
        while (pageToken != null && pageToken.length() > 0) {
            // 进行下一页数据请求
            pageToken = request4List(pageToken, client, token, resEntity, repoId);
        }
        return resEntity;
    }


    private static String request4List(String pageToken, Client client, String accessToken, List<Entity> resEntity, String repoId){
        // 创建请求对象
        ListEntityReq req = ListEntityReq.newBuilder()
                .repoId(repoId)// 词库id
                //.provider("yudao")  // 获取指定词库下所有词条， provider指定yudao，过滤所有yudao数据
                .pageSize(100)
                .pageToken(pageToken)
                .build();
        // 发起请求
        ListEntityResp resp = null;
        try {
            resp = client.lingo().entity().list(req, RequestOptions.newBuilder()
                    .userAccessToken(accessToken)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 处理服务端错误
        if(!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
        } else {
            resEntity.addAll(Arrays.stream(resp.getData().getEntities()).collect(Collectors.toList()));
            return resp.getData().getPageToken();
        }
        return "";
    }


}