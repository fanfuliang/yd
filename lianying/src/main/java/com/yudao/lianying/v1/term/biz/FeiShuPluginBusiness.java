package com.yudao.lianying.v1.term.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.lark.oapi.service.lingo.v1.model.CreateEntityResp;
import com.lark.oapi.service.lingo.v1.model.DeleteEntityResp;
import com.lark.oapi.service.lingo.v1.model.Entity;
import com.lark.oapi.service.lingo.v1.model.UpdateEntityResp;
import com.yudao.lianying.v1.basicConfig.biz.SysConfigBusiness;
import com.yudao.lianying.v1.basicConfig.dto.SysConfigData;
import com.yudao.lianying.v1.field.biz.ApplicableToBusiness;
import com.yudao.lianying.v1.field.biz.FieldBusiness;
import com.yudao.lianying.v1.term.dao.Entry;
import com.yudao.lianying.v1.term.dao.EntryLang;
import com.yudao.lianying.v1.term.dao.Item;
import com.yudao.lianying.v1.term.plugin.*;
import com.yudao.lianying.v1.term.plugin.vo.EntryLanItemVO;
import com.yudao.lianying.v1.term.plugin.vo.EntryLanVO;
import com.yudao.lianying.v1.term.plugin.vo.EntryParamVO;
import com.yudao.lianying.v1.term.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2024/04/06 12:55 <br>
 * @see com.yudao.lianying.v1.term.biz <br>
 */
@Service
@Slf4j
public class FeiShuPluginBusiness {

    @Autowired
    private EntryBusiness entryBusiness;

    @Autowired
    private EntryLangBusiness entryLangBusiness;

    @Autowired
    private ItemBusiness itemBusiness;

    @Autowired
    private SysConfigBusiness sysConfigBusiness;

    @Autowired
    private FieldBusiness fieldBusiness;

    @Autowired
    private ApplicableToBusiness applicableToBusiness;

    @Async
    public void saveFeiShu(Entry entry, List<EntryLang> entryLangList, List<Item> itemList, SysConfigData sysConfigData) {
        //termBase -> termEntry -> termEntryLan -> termItem
        List<EntryParamVO> params = getFeiShuParams(entry, entryLangList, itemList);
        if (params == null) {
            return;
        }

        if (sysConfigData == null) {
            sysConfigData = sysConfigBusiness.getSysConfigData();
        }
        String fsId = sysConfigData.getUih2FsMap().getOrDefault(entry.getTbGuid(), "fly");
        if (fsId.equals("fly")) {
            return;
        }

        List<EntryParamVO> newAdd = params.stream().filter(m -> m.getFeiShuId() == null || m.getFeiShuId().length() == 0).collect(Collectors.toList());
        List<EntryParamVO> tobeUpdate = params.stream().filter(m -> m.getFeiShuId() != null && m.getFeiShuId().length() > 0).collect(Collectors.toList());

        // 新增
        Map<String, CreateEntityResp> resAdd = CreateEntitySample.CreateFeiShuEntity(newAdd, sysConfigData.getFeishuTokenUrl(),
                sysConfigData.getAppId(), sysConfigData.getAppSecret(), fsId);
        if (resAdd != null) {
            for (String enId : resAdd.keySet()) {
                CreateEntityResp resp = resAdd.get(enId);
                updateEntryFeiShu(enId, null, resp);
            }
        }

        // 编辑
        Map<String, UpdateEntityResp> resUpdate = UpdateEntitySample.UpdateFeiShuEntity(tobeUpdate, sysConfigData.getFeishuTokenUrl(),
                sysConfigData.getAppId(), sysConfigData.getAppSecret());
        if (resUpdate != null) {
            for (String enId : resUpdate.keySet()) {
                UpdateEntityResp resp = resUpdate.get(enId);
                updateEntryFeiShu(enId, resp, null);
            }
        }
    }

    @Async
    public void deleteFeiShu(List<String> entryIds) {
        List<Entry> ens = entryBusiness.selectBatchIds(entryIds);
        SysConfigData sysConfigData = sysConfigBusiness.getSysConfigData();

        for (Entry enTmp : ens) {
            if (StringUtils.isEmpty(enTmp.getFeiShuId())) {
                continue;
            }
            EntryParamVO tmp = EntryParamVO.builder().feiShuId(enTmp.getFeiShuId()).build();
            List<EntryParamVO> params = new ArrayList<>();
            params.add(tmp);
            DeleteEntityResp resp = new DeleteEntityResp();
            try {
                resp = DeleteEntitySample.DeleteFeiShuEntity(params, sysConfigData.getAppId(), sysConfigData.getAppSecret());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
            }
            // 清一下feiShuId
            entryBusiness.modifyByIdAuto(Arrays.asList(enTmp.getId()),
                    new String[]{"fei_shu_id"}, new String[]{""});
        }

    }

    private List<EntryParamVO> getFeiShuParams(Entry entry, List<EntryLang> entryLangList, List<Item> itemList) {
        if (entry == null || entryLangList.isEmpty() || itemList.isEmpty()) {
            return null;
        }
        List<EntryParamVO> params = new ArrayList<>();
        List<EntryLanItemVO> enLanItemList = null;
        List<EntryLanVO> enLanList = new ArrayList<>();
        for (EntryLang lan : entryLangList) {
            List<Item> itemListTmp = itemList.stream().filter(m -> m.getTermEntryLangId().equals(lan.getId()))
                    .collect(Collectors.toList());
            enLanItemList = new ArrayList<>();
            for (Item curItem : itemListTmp) {
                EntryLanItemVO enLanItem = EntryLanItemVO.builder()
                        .id(curItem.getId())
                        .entryLanId(curItem.getTermEntryLangId())
                        .content(curItem.getText())
                        .example(curItem.getExample())
                        .isValidCont(curItem.getForDoc() != null && curItem.getForDoc().intValue() == 0)
                        .build();
                enLanItemList.add(enLanItem);
            }

            EntryLanVO enLan = EntryLanVO.builder()
                    .entryId(lan.getEntryId())
                    .lanId(lan.getLanguageId())
                    .define(lan.getDefinition())
                    .entryLanItemVOList(enLanItemList)
                    .build();
            enLanList.add(enLan);
        }

        EntryParamVO param = EntryParamVO.builder()
                .proLine(entry.getFieldName())
                .proType(entry.getApplicableToName())
                .lanType(entry.getPartOfSpeechName())
                .remark(entry.getNote())
                .entryLanVOList(enLanList)
                .feiShuId(entry.getFeiShuId())
                .id(entry.getId())
                .build();
        params.add(param);
        return params;
    }

    // 保存本地数据的fieShuId
    private void updateEntryFeiShu(String enId, UpdateEntityResp resUpdate, CreateEntityResp resAdd) {
        if (resUpdate != null && resUpdate.success()) {
            String feiShuId = resUpdate.getData().getEntity().getId();
            entryBusiness.modifyByIdAuto(Arrays.asList(enId),
                    new String[]{"fei_shu_id"}, new String[]{feiShuId});
        }
        if (resAdd != null && resAdd.success()) {
            String feiShuId = resAdd.getData().getEntity().getId();
            entryBusiness.modifyByIdAuto(Arrays.asList(enId),
                    new String[]{"fei_shu_id"}, new String[]{feiShuId});
        }
    }

    // 飞书2本地
    public void syncFeiShuToLocal() {
        // 取飞书待同步数据-Provider不为yudao
        SysConfigData sysConfigData = sysConfigBusiness.getSysConfigData();
        if (sysConfigData.getUihFsRepoList().size() > 0) {
            for (String[] ufr : sysConfigData.getUihFsRepoList()) {
                List<Entity> feiShuEns = ListEntitySample.ListFeiShuEntity(sysConfigData.getFeishuTokenUrl(),
                        sysConfigData.getAppId(), sysConfigData.getAppSecret(), ufr[1]);
                List<Entity> vaFeiShu = feiShuEns.stream().filter(m ->
                        m.getOuterInfo() != null && !"yudao".equals(m.getOuterInfo().getProvider()))
                        .collect(Collectors.toList());

                // 更新到本地
                for (Entity feishu : vaFeiShu) {
                    // 本地feiShuId已有的不处理
                    Wrapper<Entry> wrapper = new EntityWrapper<>();
                    wrapper.eq("fei_shu_id", feishu.getId());
                    List<Entry> tmp = entryBusiness.selectList(wrapper);
                    if (tmp != null && tmp.size() > 0) {
                        continue;
                    }
                    String description = feishu.getDescription();
                    if (StringUtils.isEmpty(description)) {
                        continue;
                    }
                    String[] descriptionArr = description.split("    ");
                    if (description.length() != 10) {
                        continue;
                    }
                    for (int i = 0; i < 10; i++) {
                        descriptionArr[i] = descriptionArr[i].substring(descriptionArr[i].indexOf(":") + 1);
                    }
                    String fieldId = fieldBusiness.getIdByName(descriptionArr[0]);
                    String applicableToId = applicableToBusiness.getIdByName(descriptionArr[1]);
                    if (StringUtils.isEmpty(fieldId) || StringUtils.isEmpty(applicableToId)) {
                        continue;
                    }

                    // 倒过来组装本地数据结构
                    EntryVO curEn = EntryVO.builder()
                            .field(fieldId)
                            .fieldName(descriptionArr[0])
                            .applicableTo(applicableToId)
                            .applicableToName(descriptionArr[1])
                            .partOfSpeech("202012140201")
                            .partOfSpeechName("名词")
                            .note(descriptionArr[9])
                            .feiShuId(feishu.getId())
                            .id(UUID.randomUUID().toString())
                            .tbGuid(ufr[0])
                            .build();

                    EntryWholeVO entryWholeVO = new EntryWholeVO();
                    entryWholeVO.setEntryVO(curEn);
                    entryWholeVO.setList(new ArrayList<>());

                    EntryLangWholeVO zhEntryLangWholeVO = new EntryLangWholeVO();
                    zhEntryLangWholeVO.setEntryLangVO(new EntryLangVO());
                    zhEntryLangWholeVO.getEntryLangVO().setLanguageId("-2");
                    zhEntryLangWholeVO.getEntryLangVO().setDefinition(descriptionArr[4]);
                    zhEntryLangWholeVO.setList(new ArrayList<>());
                    ItemVO itemVO1 = new ItemVO();
                    itemVO1.setText(feishu.getMainKeys()[0].getKey());
                    itemVO1.setForDoc(1);
                    zhEntryLangWholeVO.getList().add(itemVO1);
                    if (StringUtils.isNotEmpty(descriptionArr[2])) {
                        ItemVO itemVO2 = new ItemVO();
                        itemVO2.setText(descriptionArr[2]);
                        itemVO2.setForDoc(0);
                        zhEntryLangWholeVO.getList().add(itemVO2);
                    }
                    entryWholeVO.getList().add(zhEntryLangWholeVO);

                    if (StringUtils.isNotEmpty(descriptionArr[5])) {//说明有英文
                        EntryLangWholeVO enEntryLangWholeVO = new EntryLangWholeVO();
                        enEntryLangWholeVO.setEntryLangVO(new EntryLangVO());
                        enEntryLangWholeVO.getEntryLangVO().setLanguageId("0");
                        enEntryLangWholeVO.getEntryLangVO().setDefinition(descriptionArr[8]);
                        enEntryLangWholeVO.setList(new ArrayList<>());
                        ItemVO itemVO3 = new ItemVO();
                        itemVO3.setText(descriptionArr[5]);
                        itemVO3.setForDoc(1);
                        enEntryLangWholeVO.getList().add(itemVO3);
                        if (StringUtils.isNotEmpty(descriptionArr[6])) {
                            ItemVO itemVO4 = new ItemVO();
                            itemVO4.setText(descriptionArr[6]);
                            itemVO4.setForDoc(0);
                            enEntryLangWholeVO.getList().add(itemVO4);
                        }
                        entryWholeVO.getList().add(enEntryLangWholeVO);
                    }

                    entryBusiness.save(entryWholeVO, false, true);
                }
            }
        }

    }

    // 现有本地2飞书 （其他走界面新增编辑触发同步）
    public void syncLocalToFeiShu() {
        SysConfigData sysConfigData = sysConfigBusiness.getSysConfigData();
        if (sysConfigData.getUihFsRepoList().size() > 0) {
            for (String[] ufr : sysConfigData.getUihFsRepoList()) {
                // 取本地feiShuId为空的数据同步过去
                Wrapper<Entry> wrapper = new EntityWrapper<>();
                wrapper.eq("status", "T");
                wrapper.eq("tb_guid", ufr[0]);
                wrapper.andNew().isNull("fei_shu_id").or().eq("fei_shu_id", "");


                List<Entry> ensAll = entryBusiness.selectList(wrapper);
                //ensAll = ensAll.stream().filter(m-> StringUtils.isEmpty(m.getFeiShuId())).collect(Collectors.toList());
                for (Entry enTmp : ensAll) {
                    List<EntryLang> pEnLan = new ArrayList<>();
                    List<Item> pItem = new ArrayList<>();
                    getLocalParams(enTmp, pEnLan, pItem);

                    List<EntryParamVO> params = getFeiShuParams(enTmp, pEnLan, pItem);
                    if (params == null) return;
                    saveFeiShu(enTmp, pEnLan, pItem, sysConfigData);
                }
            }
        }
    }

    private void getLocalParams(Entry enTmp, List<EntryLang> pEnLan, List<Item> pItem) {
        // 根据entryId查找对应参数
        Wrapper<EntryLang> wrapper = new EntityWrapper<>();
        wrapper.eq("entry_id", enTmp.getId());
        wrapper.in("language_id", Arrays.asList("-2", "0"));
        List<EntryLang> entryLangList = entryLangBusiness.selectList(wrapper);
        pEnLan.addAll(entryLangList);

        Wrapper<Item> wrapper2 = new EntityWrapper<>();
        wrapper2.in("term_entry_lang_id",
                entryLangList.stream().map(m -> m.getId()).collect(Collectors.toList()));
        List<Item> itemList = itemBusiness.selectList(wrapper2);
        pItem.addAll(itemList);
    }

    public String getTermRepoList(){
        String res = null;
        try {
            SysConfigData sysConfigData = sysConfigBusiness.getSysConfigData();
            res = ListRepoSample.ListRepo(sysConfigData.getFeishuTokenUrl(),
                    sysConfigData.getAppId(), sysConfigData.getAppSecret());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return res;
    }
}
