package com.yudao.lianying.v1.term.plugin;

import com.lark.oapi.Client;
import com.lark.oapi.service.lingo.v1.model.*;

import java.util.*;
import java.util.stream.Collectors;

import com.lark.oapi.core.request.RequestOptions;
import com.yudao.lianying.utils.HttpUtils;
import com.yudao.lianying.v1.basicConfig.dao.SysConfig;
import com.yudao.lianying.v1.term.plugin.vo.EntryLanItemVO;
import com.yudao.lianying.v1.term.plugin.vo.EntryLanVO;
import com.yudao.lianying.v1.term.plugin.vo.EntryParamVO;

// SDK 使用文档：https://github.com/larksuite/oapi-sdk-java/tree/v2_main
// 复制该 Demo 后, 需要将 "YOUR_APP_ID", "YOUR_APP_SECRET" 替换为自己应用的 APP_ID, APP_SECRET.
public class UpdateEntitySample {

    public static Map<String, UpdateEntityResp> UpdateFeiShuEntity(List<EntryParamVO> updateParams, String tokenUrl, String appId, String appSecret) {
        if (updateParams == null || updateParams.isEmpty()) {
            return new HashMap<>();
        }
        // 构建client
        Client client = Client.newBuilder(appId, appSecret).build();
        String token = HttpUtils.getTenantAccessToken(tokenUrl, appId, appSecret);

        Map<String, UpdateEntityResp> res = new HashMap<>();
        for (EntryParamVO param : updateParams) {
            UpdateEntityResp tmp = updateToFeiShu(param, client, token);
            res.put(param.getId(), tmp);
        }
        return res;
    }

    private static UpdateEntityResp updateToFeiShu(EntryParamVO param, Client client, String token) {
        List<EntryLanVO> entryLans = param.getEntryLanVOList();
        if (entryLans.isEmpty()) {
            return null;
        }
        // TODO :lanId 换成中文id
        Optional<EntryLanVO> chinese = entryLans.stream().filter(m -> "-2".equals(m.getLanId())).findFirst();
        if (!chinese.isPresent() || chinese.get() == null) {
            return null;
        }
        List<EntryLanItemVO> chineseItems = chinese.get().getEntryLanItemVOList();
        if (chineseItems.isEmpty()) {
            return null;
        }

        List<EntryLanItemVO> engItems = new ArrayList<>();
        Optional<EntryLanVO> english = entryLans.stream().filter(m -> "0".equals(m.getLanId())).findFirst();
        if (english.isPresent() && english.get() != null) {
            engItems = english.get().getEntryLanItemVOList();
        }
        // description-纯文本--仅同步中英文
        /*
         * 产品线: entry.proLine
         * 项目: entry.proType
         * 同义词: entryLanItem.cont(取lan=中文,排除词条key后的所有,多个分号隔开)
         * 禁用词: 暂无
         * 定义: entrylAN.define(lan=中文)
         * English: entryLanItem.content(取lan=英文的首词)
         * Synonym: entryLanItem.cont(取lan=英文,排除English后的所有,多个分号隔开)
         * Forbidden: 暂无
         * Definition: entrylAN.define(lan=英文)
         * Notes: XXXXXX
         *
         * */
        String tongyi = chineseItems.subList(1, chineseItems.size()).stream().map(m -> m.getContent()).collect(Collectors.joining("; "));
        String firEng = engItems.isEmpty() ? "" : engItems.get(0).getContent();
        String tongyiEng = engItems.isEmpty() ? "" : engItems.subList(1, engItems.size()).stream().map(m -> m.getContent()).collect(Collectors.joining("; "));

        String desc = String.format("产品线: %s    项目: %s    同义词: %s    禁用词: %s    定义: %s    English: %s    Synonym: %s    Forbidden: %s    Definition: %s    Notes: %s",
                param.getProLine(),//产品线
                param.getProType(),//项目
                tongyi,//同义词
                "", // 禁用词: 暂无
                chinese.get().getDefine(),//定义
                firEng,//English
                tongyiEng,//Synonym
                "",//Forbidden 暂无
                english.isPresent() && english.get() != null ? english.get().getDefine() : "",//Definition
                ""//Notes 暂空
        );

        String richTxt = String.format("<p>产品线: %s</p><p>项目: %s</p><p>同义词: %s</p><p>禁用词: %s</p><p>定义: %s</p><p>English: %s</p><p>Synonym: %s</p><p>Forbidden: %s</p><p>Definition: %s</p><p>Notes: %s</p>",
                param.getProLine(),//产品线
                param.getProType(),//项目
                tongyi,//同义词
                "", // 禁用词: 暂无
                chinese.get().getDefine(),//定义
                firEng,//English
                tongyiEng,//Synonym
                "",//Forbidden 暂无
                english.isPresent() && english.get() != null ? english.get().getDefine() : "",//Definition
                ""//Notes 暂空
        );


        // 创建请求对象
        UpdateEntityReq req = UpdateEntityReq.newBuilder()
                .entityId(param.getFeiShuId())
                .entity(Entity.newBuilder()
                        .mainKeys(new Term[]{
                                Term.newBuilder()
                                        .key(chineseItems.get(0).getContent()) // 待更新的词条name
                                        .displayStatus(DisplayStatus.newBuilder()
                                                .allowHighlight(true)
                                                .allowSearch(true)
                                                .build())
                                        .build()
                        })
                        .description(desc)
                        .richText(richTxt)
                        .outerInfo(OuterInfo.newBuilder()
                                .provider("yudao")
                                .outerId(param.getId())
                                .build())
                        .build())
                .build();

        // 发起请求
        UpdateEntityResp resp = null;
        try {
            resp = client.lingo().entity().update(req, RequestOptions.newBuilder()
                    .userAccessToken(token)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 处理服务端错误
        if (!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
        }
        return resp;
    }


    private static EntryParamVO saveToLocal(Entity en) {
        return null;
    }
}