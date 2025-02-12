package com.yudao.lianying.v1.term.plugin;

import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.service.lingo.v1.model.*;
import com.yudao.lianying.utils.HttpUtils;
import com.yudao.lianying.v1.term.plugin.vo.EntryLanItemVO;
import com.yudao.lianying.v1.term.plugin.vo.EntryLanVO;
import com.yudao.lianying.v1.term.plugin.vo.EntryParamVO;

import java.util.*;
import java.util.stream.Collectors;

public class CreateEntitySample {


    public static Map<String, CreateEntityResp> CreateFeiShuEntity(List<EntryParamVO> toAddParams, String tokenUrl,String appId,String appSecret,String repoId) {
        if (toAddParams == null || toAddParams.isEmpty()) {
            return new HashMap<>();
        }

        Client client = Client.newBuilder(appId, appSecret).build();
        String token = HttpUtils.getTenantAccessToken(tokenUrl,appId,appSecret);

        // 创建请求对象
        Map<String, CreateEntityResp> respLi = new HashMap<>();
        for (EntryParamVO param : toAddParams) {
            CreateEntityResp resp = newAddToFeiShu(param, client, token, repoId);
            respLi.put(param.getId(), resp);
        }
        return respLi;
    }

    private static CreateEntityResp newAddToFeiShu(EntryParamVO param, Client client, String token, String repoId) {
        List<EntryLanVO> entryLans = param.getEntryLanVOList();
        if (entryLans.isEmpty()) {
            return null;
        }

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


        CreateEntityReq req = CreateEntityReq.newBuilder()
                .repoId(repoId)// 词库id
                .entity(Entity.newBuilder()
                        .mainKeys(new Term[]{
                                Term.newBuilder()
                                        .key(chineseItems.get(0).getContent())
                                        .displayStatus(DisplayStatus.newBuilder()
                                                .allowHighlight(true)
                                                .allowSearch(true)
                                                .build())
                                        .build()
                        })
//                        .aliases(new Term[] {
//                                Term.newBuilder()
//                                        .key("")
//                                        .displayStatus(DisplayStatus.newBuilder()
//                                                .allowHighlight(true)
//                                                .allowSearch(true)
//                                                .build())
//                                        .build()
//                        })
                        .description(desc)
//                        .relatedMeta(RelatedMeta.newBuilder()
//                                .users(new Referer[] {
//                                        Referer.newBuilder()
//                                                .id("ou_30b07b6****ea46518789914dac63d36")
//                                                .title("负责人")
//                                                .build(),
//                                        Referer.newBuilder()
//                                                .id("ou_b292c0****c14754639fa4501e80c36a")
//                                                .title("")
//                                                .build()
//                                })
//                                .chats(new Referer[] {
//                                        Referer.newBuilder()
//                                                .id("oc_c13831833e****92c52befa759ea4806")
//                                                .build(),
//                                        Referer.newBuilder()
//                                                .id("oc_c8161c910****a24127e73b10233b295")
//                                                .build()
//                                })
//                                .docs(new Referer[] {
//                                        Referer.newBuilder()
//                                                .title("猜你想问 / FAQs")
//                                                .url("https://example.feishu.cn/wiki/wikcnZ8Lq4f9DMCDOtdcIzCUjAh")
//                                                .build(),
//                                        Referer.newBuilder()
//                                                .title("快速了解飞书文档 | Introducing Feishu Docs")
//                                                .url("https://example.feishu.cn/docs/doccnxlVCCFjMsJE15I7PLAjIWc")
//                                                .build()
//                                })
//                                .oncalls(new Referer[] {
//                                        Referer.newBuilder()
//                                                .id("702368904****548034")
//                                                .build(),
//                                        Referer.newBuilder()
//                                                .id("70240637****0910850")
//                                                .build()
//                                })
//                                .links(new Referer[] {
//                                        Referer.newBuilder()
//                                                .title("飞书官网")
//                                                .url("https://feishu.cn")
//                                                .build()
//                                })
//                                .abbreviations(new Abbreviation[] {
//                                        Abbreviation.newBuilder()
//                                                .id("enterprise_44***90")
//                                                .build(),
//                                        Abbreviation.newBuilder()
//                                                .id("enterprise_70348****374354564")
//                                                .build(),
//                                        Abbreviation.newBuilder()
//                                                .id("enterprise_70365****3106796547")
//                                                .build()
//                                })
//                                .classifications(new Classification[] {
//                                        Classification.newBuilder()
//                                                .id("7049606926****37761")
//                                                .fatherId("7049606926****37777")
//                                                .build()
//                                })
//                                .images(new BaikeImage[] {
//                                        BaikeImage.newBuilder()
//                                                .token("boxbcEcmKiD3SGHvgqWTpvdc7jc")
//                                                .build()
//                                })
//                                .build())
                        .outerInfo(OuterInfo.newBuilder()
                                .provider("yudao")
                                .outerId(param.getId())
                                .build())
                        .richText(richTxt)
//                        .i18nDescs(new I18nEntryDesc[] {
//                                I18nEntryDesc.newBuilder()
//                                        .language(1)
//                                        .description("国际化中文释义")
//                                        .richText("<p>国际化中文释义</p>")
//                                        .build()
//                        })
                        .build())
                .build();

        // 发起请求
        CreateEntityResp resp = null;
        try {
            resp = client.lingo().entity().create(req, RequestOptions.newBuilder()
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

}