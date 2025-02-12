package com.yudao.lianying.v1.term.plugin;

import com.google.gson.JsonParser;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.lingo.v1.model.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lark.oapi.core.request.RequestOptions;
import com.yudao.lianying.utils.HttpUtils;

public class ListRepoSample {

    public static String ListRepo(String tokenUrl, String appId, String appSecret) throws Exception {

        String token = HttpUtils.getTenantAccessToken(tokenUrl, appId, appSecret);

        // 构建client
        Client client = Client.newBuilder(appId, appSecret).build();
        ListRepoResp resp = client.lingo().repo().list(RequestOptions.newBuilder()
                .userAccessToken(token)
                .build());

        // 处理服务端错误
        if (!resp.success()) {
            return "飞书服务报错！";
        }

        // 业务数据处理
        return Jsons.DEFAULT.toJson(resp.getData());
    }
}