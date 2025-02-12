package com.yudao.lianying.v1.term.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.lianying.utils.FtpUtils;
import com.yudao.lianying.utils.MailUtils;
import com.yudao.lianying.utils.ProcessStateEnum;
import com.yudao.lianying.utils.ProcessTypeEnum;
import com.yudao.lianying.v1.account.biz.AccountBusiness;
import com.yudao.lianying.v1.account.dao.Account;
import com.yudao.lianying.v1.basicConfig.biz.LangBusiness;
import com.yudao.lianying.v1.basicConfig.dao.Language;
import com.yudao.lianying.v1.codeInfo.biz.CodeInfoBusiness;
import com.yudao.lianying.v1.field.biz.ApplicableToBusiness;
import com.yudao.lianying.v1.field.biz.CategoryBusiness;
import com.yudao.lianying.v1.field.biz.FieldBusiness;
import com.yudao.lianying.v1.field.dao.ApplicableTo;
import com.yudao.lianying.v1.field.dao.Field;
import com.yudao.lianying.v1.process.biz.ProcessBusiness;
import com.yudao.lianying.v1.process.dao.Process;
import com.yudao.lianying.v1.term.dao.*;
import com.yudao.lianying.v1.term.dao.mapper.EntryMapper;
import com.yudao.lianying.v1.term.dto.*;
import com.yudao.lianying.v1.term.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-08
 */
@Service
@Slf4j
public class EntryBusiness extends ServiceImpl<EntryMapper, Entry> {

    @Autowired
    private TermbaseBusiness termbaseBusiness;

    @Autowired
    private EntryLangBusiness entryLangBusiness;

    @Autowired
    private ItemBusiness itemBusiness;

    @Autowired
    private ProcessBusiness processBusiness;

    @Autowired
    private FieldBusiness fieldBusiness;

    @Autowired
    private ApplicableToBusiness applicableToBusiness;

    @Autowired
    private CategoryBusiness categoryBusiness;

    @Autowired
    private CodeInfoBusiness codeInfoBusiness;

    @Autowired
    private LangBusiness langBusiness;

    @Autowired
    private AccountBusiness accountBusiness;

    @Autowired
    private FeiShuPluginBusiness feiShuPluginBusiness;

    @Value("${web.host}")
    private String webHost;

    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.port}")
    private String ftpPort;
    @Value("${ftp.httpAddress}")
    private String ftpHttpAddress;

    @Value("${ftp.httpAddress2}")
    private String ftpHttpAddress2;

    //发送响应流方法
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache,no-store,must-revalidate");
            response.setDateHeader("Expires", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String setFileName(String fileName, String defaultName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = defaultName;
        }
        return DownloadUtil.filenameFilter(fileName);
    }

    public Object saveAuto(EntryVO entityVO) {
        Entry entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Entry.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Entry> list = new ArrayList<>();
        for (String id : set) {
            Entry entity = new Entry(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
        // 同步飞书词典删除
        feiShuPluginBusiness.deleteFeiShu(set);

    }

    public void modifyAllObjectFeildAuto(EntryModifyVO1 modifyVO1) {
        Entry entity = selectById(modifyVO1.getId());
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            log.error("There is no the entity，id: {}", modifyVO1.getId());
            throw new YudaoException(Result.ERROR, "There is no the entity");
        }
        BeanUtils.copyProperties(modifyVO1, entity);
        entity.setModified(null);
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyByIdAuto(List<String> idList, String[] modifyFieldNameArray, String[] modifyFieldValueArray) {
        baseMapper.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
    }

    public Object getByIdAuto(String id) {
        Entry entity = selectById(id);
        if (null == entity) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object page4Manage(Page<EntryViewResult> page, String keyword,String fields, String tbId, String firstLang, String secondLang, Date beginTime, Date endTime, String orderBy) {
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(fields);
        return page.setRecords(baseMapper.page4Manage(page, keyword,fieldList, tbId, firstLang, secondLang, beginTime, endTime, orderBy));
    }

    public Object pageAuto(Page<Entry> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Entry> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        return selectPage(page, entityWrapper);
    }

    @Transactional
    public void save(EntryWholeVO entryWholeVO, boolean isProcess, boolean fromFS) {
        EntryVO entryVO = entryWholeVO.getEntryVO();
        String fields = entryVO.getField();
        String fieldNames = entryVO.getFieldName();
        List<EntryLangWholeVO> entryLangVOList = entryWholeVO.getList();
        Entry entry = CommonUtils.copyPropertiesAndCreateId(entryVO, Entry.class);
        if (isProcess) {
            entry.setState(ProcessStateEnum.to_process.getId());
        }
        entry.setCreated(DateUtils.getNow());
        entry.setModified(DateUtils.getNow());
        entry.setStatus(StatusValueEnum.normal.getValue());
        List<EntryLang> entryLangList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        for (EntryLangWholeVO entryLangWholeVO : entryLangVOList) {
            EntryLangVO entryLangVO = entryLangWholeVO.getEntryLangVO();
            if (null == entryLangVO || StringUtils.isBlank(entryLangVO.getLanguage())) {
                throw new YudaoException(Result.ERROR, "语言数据错误");
            }
            EntryLang entryLang = CommonUtils.copyPropertiesAndCreateId(entryLangVO, EntryLang.class);
            entryLang.setEntryId(entry.getId());
            entryLang.setCreateTime(DateUtils.getNow());
            entryLang.setUpdateTime(DateUtils.getNow());
            entryLangList.add(entryLang);
            List<ItemVO> itemVOList = entryLangWholeVO.getList();

            for (ItemVO itemVO : itemVOList) {
                if (null == itemVO || null == itemVO.getForDoc() || StringUtils.isBlank(itemVO.getText())) {
                    throw new YudaoException(Result.ERROR, "术语数据错误");
                }
                Item item = CommonUtils.copyPropertiesAndCreateId(itemVO, Item.class);
                item.setTermEntryLangId(entryLang.getId());
                item.setCreateTime(DateUtils.getNow());
                item.setUpdateTime(DateUtils.getNow());
                itemList.add(item);
            }
        }
        if (isProcess) {
            Process submitted = new Process();
            submitted.setId(CommonUtils.uuid());
            submitted.setEntryId(entry.getId());
            submitted.setTermText(itemList.stream().map(Item::getText).collect(Collectors.joining(";")));
            submitted.setField(fields);
            submitted.setFieldName(fieldNames);
            submitted.setType(ProcessTypeEnum.term.name());
            submitted.setState(ProcessStateEnum.to_approve.getId());
            submitted.setCreateBy(entryVO.getCreator());
            Set<String> managerIdList = fieldBusiness.getManagerId(fieldNames);
            if (CollectionUtils.isNotEmpty(managerIdList)) {
                List<Account> managers = accountBusiness.selectBatchIds(managerIdList);
                if (managers.size() > 0) {
                    Account manager = managers.get(0);
                    managers.remove(0);
                    String cc = managers.stream().filter(o -> StatusValueEnum.normal.getValue().equals(o.getStatus()) && StringUtils.isNotBlank(o.getEmail())).map(Account::getEmail).collect(Collectors.joining(","));
                    if (manager != null && StatusValueEnum.normal.getValue().equals(manager.getStatus())) {
                        Process managerProcess = CommonUtils.copyPropertiesAndCreateId(submitted, Process.class);
                        managerProcess.setParentId(submitted.getId());
                        managerProcess.setState(ProcessStateEnum.to_process.getId());
                        managerProcess.setCreateBy(manager.getId());
                        String email = manager.getEmail();
//                    if (EmailUtils.validateEmail(email)) {
                        if (true) {
                            String titleStr = "【联影词典】新术语待评审；New terms to be reviewed";
                            String submittedBy = submitted.getCreateBy();
                            String bodyStr = "用户[" + submittedBy + "]已提交新术语：<br/>"
                                    + "The user [" + submittedBy + "] has submitted new terms:<br/><br/>"
                                    + "术语信息/Term info：" + submitted.getTermText()
                                    + "<br/><br/>请及时查看、处理。点此<a href=\"" + webHost + "/manageCompanyTrendsTypeunDeal?tabIndex=1\">查看详情</a>。<br/>"
                                    + "Please click <a href=\"" + webHost + "/manage/process/unDeal?tabIndex=1\">here</a> to review the terms.";
                            MailUtils.asynSendMail(email, cc, titleStr, bodyStr, null);
                        }
                        processBusiness.insert(managerProcess);
                    }
                }
            }
            processBusiness.insert(submitted);
        }
        this.insert(entry);
        entryLangBusiness.insertBatch(entryLangList);
        itemBusiness.insertBatch(itemList);

        // 同步飞书词典新增
        if (!fromFS && !isProcess) {
            feiShuPluginBusiness.saveFeiShu(entry, entryLangList, itemList, null);
        }

    }

    @Transactional
    public void saveBatch(List<EntryWholeVO> entryWholeVOList, boolean isProcess) {
        List<Entry> entryList = new ArrayList<>();
        List<EntryLang> entryLangList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        List<String> delIdList=new ArrayList<>();
        for (EntryWholeVO entryWholeVO : entryWholeVOList) {
            EntryVO entryVO = entryWholeVO.getEntryVO();
            String fields = entryVO.getField();
            String fieldNames = entryVO.getFieldName();
            List<EntryLangWholeVO> entryLangVOList = entryWholeVO.getList();
            Entry entry = CommonUtils.copyProperties(entryVO, Entry.class);
            if(StringUtils.isNotEmpty(entryVO.getId())){
                Entry oldEntry=this.selectById(entryVO.getId());
                if(oldEntry!=null){
                    entry=oldEntry;
                    //delete old data
                    delIdList.add(entryVO.getId());
                    entry.setTbGuid(entryVO.getTbGuid());
                    entry.setField(entryVO.getField());
                    entry.setFieldName(entryVO.getFieldName());
                    entry.setApplicableTo(entryVO.getApplicableTo());
                    entry.setApplicableToName(entryVO.getApplicableToName());
                    entry.setPartOfSpeech(entryVO.getPartOfSpeech());
                    entry.setPartOfSpeechName(entryVO.getPartOfSpeechName());
                    entry.setNote(entryVO.getNote());
                }
            }
            if(StringUtils.isEmpty(entry.getId())){
                entry.setId(UUID.randomUUID().toString());
            }
            if (isProcess) {
                entry.setState(ProcessStateEnum.to_process.getId());
            }
            entry.setCreated(DateUtils.getNow());
            entry.setModified(DateUtils.getNow());
            entry.setStatus(StatusValueEnum.normal.getValue());

            for (EntryLangWholeVO entryLangWholeVO : entryLangVOList) {
                EntryLangVO entryLangVO = entryLangWholeVO.getEntryLangVO();
                if (null == entryLangVO || StringUtils.isBlank(entryLangVO.getLanguage())) {
                    throw new YudaoException(Result.ERROR, "语言数据错误");
                }
                EntryLang entryLang = CommonUtils.copyPropertiesAndCreateId(entryLangVO, EntryLang.class);
                entryLang.setEntryId(entry.getId());
                entryLang.setCreateTime(DateUtils.getNow());
                entryLang.setUpdateTime(DateUtils.getNow());
                entryLangList.add(entryLang);
                List<ItemVO> itemVOList = entryLangWholeVO.getList();
                for (ItemVO itemVO : itemVOList) {
                    if (null == itemVO || null == itemVO.getForDoc() || StringUtils.isBlank(itemVO.getText())) {
                        throw new YudaoException(Result.ERROR, "术语数据错误");
                    }
                    Item item = CommonUtils.copyPropertiesAndCreateId(itemVO, Item.class);
                    item.setTermEntryLangId(entryLang.getId());
                    item.setCreateTime(DateUtils.getNow());
                    item.setUpdateTime(DateUtils.getNow());
                    itemList.add(item);
                }
            }
            entryList.add(entry);
            if (isProcess) {
                Process submitted = new Process();
                submitted.setId(CommonUtils.uuid());
                submitted.setEntryId(entry.getId());
                submitted.setTermText(itemList.stream().map(Item::getText).collect(Collectors.joining(";")));
                submitted.setField(fields);
                submitted.setFieldName(fieldNames);
                submitted.setType(ProcessTypeEnum.term.name());
                submitted.setState(ProcessStateEnum.to_approve.getId());
                submitted.setCreateBy(entryVO.getCreator());
                Set<String> managerIdList = fieldBusiness.getManagerId(fieldNames);
                if (CollectionUtils.isNotEmpty(managerIdList)) {
                    List<Account> managers = accountBusiness.selectBatchIds(managerIdList);
                    if (managers.size() > 0) {
                        Account manager = managers.get(0);
                        managers.remove(0);
                        String cc = managers.stream().filter(o -> StatusValueEnum.normal.getValue().equals(o.getStatus()) && StringUtils.isNotBlank(o.getEmail())).map(Account::getEmail).collect(Collectors.joining(","));
                        if (manager != null && StatusValueEnum.normal.getValue().equals(manager.getStatus())) {
                            Process managerProcess = CommonUtils.copyPropertiesAndCreateId(submitted, Process.class);
                            managerProcess.setParentId(submitted.getId());
                            managerProcess.setState(ProcessStateEnum.to_process.getId());
                            managerProcess.setCreateBy(manager.getId());
                            String email = manager.getEmail();
//                    if (EmailUtils.validateEmail(email)) {
                            if (true) {
                                String titleStr = "【联影词典】新术语待评审；New terms to be reviewed";
                                String submittedBy = submitted.getCreateBy();
                                String bodyStr = "用户[" + submittedBy + "]已提交新术语：<br/>"
                                        + "The user [" + submittedBy + "] has submitted new terms:<br/><br/>"
                                        + "术语信息/Term info：" + submitted.getTermText()
                                        + "<br/><br/>请及时查看、处理。点此<a href=\"" + webHost + "/manage/process/unDeal?tabIndex=1\">查看详情</a>。<br/>"
                                        + "Please click <a href=\"" + webHost + "/manage/process/unDeal?tabIndex=1\">here</a> to review the terms.";
                                MailUtils.asynSendMail(email, cc, titleStr, bodyStr, null);
                            }
                            processBusiness.insert(managerProcess);
                        }
                    }
                }
                processBusiness.insert(submitted);
            }
        }
        if(CollectionUtils.isNotEmpty(delIdList)){
            itemBusiness.deleteByEntryIdList(delIdList);
            entryLangBusiness.deleteByEntryIdList(delIdList);
            this.deleteBatchIds(delIdList);
        }
        this.insertBatch(entryList);
        entryLangBusiness.insertBatch(entryLangList);
        itemBusiness.insertBatch(itemList);

        if (!isProcess) {
            for (Entry et : entryList) {
                List<EntryLang> ellist = entryLangList.stream().filter(f -> f.getEntryId().equals(et.getId())).collect(Collectors.toList());
                List<String> elIdList = ellist.stream().map(f -> f.getId()).collect(Collectors.toList());
                List<Item> iList = itemList.stream().filter(f -> elIdList.contains(f.getTermEntryLangId())).collect(Collectors.toList());
                feiShuPluginBusiness.saveFeiShu(et, ellist, iList, null);
            }
        }
    }

    @Transactional
    public void modify(EntryWholeVO entryWholeVO) {
        EntryVO entryVO = entryWholeVO.getEntryVO();
        Entry oldEntry = selectById(entryVO.getId());
        List<EntryLangWholeVO> entryLangVOList = entryWholeVO.getList();
        Entry entry = CommonUtils.copyProperties(entryVO, Entry.class);
        entry.setStatus("T");
        entry.setCreator(oldEntry.getCreator());
        entry.setCreated(oldEntry.getCreated());
        entry.setModified(DateUtils.getNow());
        List<EntryLang> entryLangList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        for (EntryLangWholeVO entryLangWholeVO : entryLangVOList) {
            EntryLangVO entryLangVO = entryLangWholeVO.getEntryLangVO();
            if (null == entryLangVO || StringUtils.isBlank(entryLangVO.getLanguage())) {
                throw new YudaoException(Result.ERROR, "语言数据错误");
            }
            EntryLang entryLang = CommonUtils.copyProperties(entryLangVO, EntryLang.class);
            if (StringUtils.isEmpty(entryLangVO.getId())) {
                entryLang.setId(UUID.randomUUID().toString());
            }
            else {
                entryLang.setId(entryLangVO.getId());
            }
            entryLang.setEntryId(entry.getId());
            entryLang.setCreateTime(oldEntry.getCreated());
            entryLang.setCreateBy(oldEntry.getCreator());
            entryLang.setUpdateTime(DateUtils.getNow());
            entryLangList.add(entryLang);
            List<ItemVO> itemVOList = entryLangWholeVO.getList();
            for (ItemVO itemVO : itemVOList) {
                if (null == itemVO || null == itemVO.getForDoc() || StringUtils.isBlank(itemVO.getText())) {
                    throw new YudaoException(Result.ERROR, "术语数据错误");
                }
                Item item = CommonUtils.copyPropertiesAndCreateId(itemVO, Item.class);
                if (StringUtils.isEmpty(itemVO.getId())) {
                    item.setId(UUID.randomUUID().toString());
                }
                else {
                    item.setId(itemVO.getId());
                }
                item.setTermEntryLangId(entryLang.getId());
                item.setCreateTime(oldEntry.getCreated());
                item.setCreateBy(oldEntry.getCreator());
                item.setUpdateTime(DateUtils.getNow());
                itemList.add(item);
            }
        }

        List<EntryLang> oldentrylangList = entryLangBusiness.selectList(new EntityWrapper<EntryLang>().eq("entry_id", entryVO.getId()));
        if (CollectionUtils.isNotEmpty(oldentrylangList)) {
            List<String> oldentrylangIdList = new ArrayList<>();
            oldentrylangList.forEach(oel -> oldentrylangIdList.add(oel.getId()));
            itemBusiness.delete(new EntityWrapper<Item>().in("term_entry_lang_id", oldentrylangIdList));
            entryLangBusiness.deleteBatchIds(oldentrylangIdList);
            this.deleteById(entryVO.getId());
        }
        this.insert(entry);
        entryLangBusiness.insertBatch(entryLangList);
        itemBusiness.insertBatch(itemList);

        // 同步飞书词典编辑
        feiShuPluginBusiness.saveFeiShu(entry, entryLangList, itemList, null);

    }

    public EntryWholeDTO getById(String entryId) {
        EntryWholeDTO entryWholeDTO = new EntryWholeDTO();

        EntryDTO entry = baseMapper.getById(entryId);
        entryWholeDTO.setEntry(entry);
        List<EntryLangDTO> entryLangs = entryLangBusiness.selectDTOListByEntryId(entryId);

        List<EntryLangWholeDTO> list = new ArrayList<>();
        entryWholeDTO.setList(list);
        for (EntryLangDTO entrylang : entryLangs) {
            EntryLangWholeDTO entryLangWholeDTO = new EntryLangWholeDTO();
            entryLangWholeDTO.setEntryLang(entrylang);
            entryLangWholeDTO.setList(itemBusiness.selectList(new EntityWrapper<Item>()
                    .eq("term_entry_lang_id", entrylang.getId())
                    .eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue())));
            list.add(entryLangWholeDTO);
        }
        return entryWholeDTO;
    }

    public List<EntrySearchResult> search(String keyword, String field, String applicableTo, String searchType) {
        List<EntrySearchResult> results = null;
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        List<String> applicableToList = CommonUtils.splitByCommaToListUnique(applicableTo);
        if ("1".equals(searchType)) {
            results = baseMapper.searchContain(keyword, fieldList, applicableToList);
        }
        else {
            results = baseMapper.searchAccurate(keyword, fieldList, applicableToList);
        }
        return results;
    }

    public List<PluginSearchEntry> search4Plugin(String keyword) {
        List<PluginSearchEntry> results = null;
        return results;
    }

    public List<EntrySearchResult> relatedSearch(String entryId) {
        List<EntryLang> entryLangList = entryLangBusiness.selectList(new EntityWrapper<EntryLang>().eq("entry_id", entryId));
        List<String> entryLangIdList = entryLangList.stream().map(EntryLang::getId).collect(Collectors.toList());
        List<Item> itemList = itemBusiness.selectList(new EntityWrapper<Item>().in("term_entry_lang_id", entryLangIdList));
        List<String> tagList = new ArrayList<>();
        itemList.stream().forEach(t -> tagList.addAll(CommonUtils.splitByCommaToListUnique(t.getTag())));
        if (tagList.size() > 0)
            return itemBusiness.relatedSearch(tagList.stream().distinct().collect(Collectors.toList()));
        return new ArrayList<>();
    }

    public String getTermText(String entryId) {
        List<String> textList = baseMapper.getTermTextList(entryId);
        return String.join(";", textList);
    }

    public void updateTBId(String mainId, String slaveIds) {
        baseMapper.updateTBId(mainId, CommonUtils.splitByCommaToListUnique(slaveIds));
    }

    public void moveTo(String newTBId, String entryIds) {
        List<String> entryIdList = CommonUtils.splitByCommaToListUnique(entryIds);
        baseMapper.moveTo(newTBId, entryIdList);
    }

    public Object imports(String tbId, String accountId, String ftpPath, boolean isProcess) throws IOException {
        String creator=accountBusiness.getByIdAuto(accountId).getLoginName();
        if (StringUtils.isNotEmpty(tbId)) {
            Termbase knowledge = termbaseBusiness.selectById(tbId);
            ParameterValidUtils.entityExist(tbId, knowledge);
        }
        List<EntryWholeVO> list = new ArrayList<>();
        String format = ftpPath.toLowerCase().trim().substring(ftpPath.lastIndexOf(".") + 1);
        if ("xlsx".equals(format)) {
            try {
                InputStream inputStream = FtpUtils.download(ftpPath);
                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = wb.getSheetAt(0);

                XSSFRow firstRow = sheet.getRow(0);
                if(firstRow.getLastCellNum()<9){
                    throw new YudaoException(Result.ERROR, "导入文件模版错误");
                }
                if(!"ID".equals(getCellValue(firstRow.getCell(0)))||
                        !"产品线".equals(getCellValue(firstRow.getCell(1)))||
                        !"产品".equals(getCellValue(firstRow.getCell(2)))||
                        !"词性".equals(getCellValue(firstRow.getCell(3)))||
                        !"备注".equals(getCellValue(firstRow.getCell(4)))){
                    throw new YudaoException(Result.ERROR, "导入文件模版错误");
                }
                List<EntryLangC> entryLangCList = new ArrayList<>();
                int curLangIndex = 0;
                String curLang = "";
                EntryLangC entryLangC = null;
                Map<String, String> langNameIdMap = new HashMap<>();
                for (int t = 5; t < firstRow.getLastCellNum(); t++) {
                    String title = getCellValue(firstRow.getCell(t));
                    if ("定义".equals(title)) {
                        curLangIndex = 0;
                        curLang = "";
                        if (entryLangC != null && entryLangC.getItemColumnList().size() > 0) {
                            entryLangCList.add(entryLangC);
                        }
                        entryLangC = new EntryLangC();
                        entryLangC.setDefColumn(t);
                    }
                    else {
                        curLangIndex += 1;
                        if (curLangIndex % 3 == 1) {
                            if (StringUtils.isEmpty(curLang)) {
                                curLang = title;
                                entryLangC.setLangName(curLang);
                                entryLangC.setLangId(getLangId(langNameIdMap,curLang));
                                if("".equals(entryLangC.getLangId())){
                                    throw new YudaoException(Result.ERROR, "导入文件模版错误,语种:"+entryLangC.getLangName()+"不存在");
                                }
                            }
                            entryLangC.getItemColumnList().add(t);
                        }
                        else if (curLangIndex % 3 == 2 && !"例句".equals(title)){
                            throw new YudaoException(Result.ERROR, "导入文件模版错误");
                        }
                        else if (curLangIndex % 3 == 0 && !"是否规范".equals(title)){
                            throw new YudaoException(Result.ERROR, "导入文件模版错误");
                        }
                    }
                }
                if (entryLangC != null && entryLangC.getItemColumnList().size() > 0) {
                    entryLangCList.add(entryLangC);
                }

                EntryWholeVO tempEntryWholeVO = null;
                EntryLangWholeVO tempEntryLangWholeVO = null;
                Map<String, String> fieldNameIdMap = new HashMap<>();
                Map<String, String> applicableNameIdMap = new HashMap<>();
                Map<String, String> partOfSpeechNameIdMap = new HashMap<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    tempEntryWholeVO = new EntryWholeVO();
                    tempEntryWholeVO.setEntryVO(new EntryVO());
                    tempEntryWholeVO.getEntryVO().setTbGuid(tbId);
                    tempEntryWholeVO.getEntryVO().setCreator(creator);
                    tempEntryWholeVO.getEntryVO().setId(getCellValue(row.getCell(0)));
                    tempEntryWholeVO.getEntryVO().setFieldName(getCellValue(row.getCell(1)));
                    tempEntryWholeVO.getEntryVO().setField(getFieldIds(fieldNameIdMap, tempEntryWholeVO.getEntryVO().getFieldName()));
                    tempEntryWholeVO.getEntryVO().setApplicableToName(getCellValue(row.getCell(2)));
                    tempEntryWholeVO.getEntryVO().setApplicableTo(getApplicableIds(applicableNameIdMap, tempEntryWholeVO.getEntryVO().getApplicableToName()));
                    tempEntryWholeVO.getEntryVO().setPartOfSpeechName(getCellValue(row.getCell(3)));
                    tempEntryWholeVO.getEntryVO().setPartOfSpeech(getPartOfSpeechId(partOfSpeechNameIdMap,tempEntryWholeVO.getEntryVO().getPartOfSpeechName() ));
                    tempEntryWholeVO.getEntryVO().setNote(getCellValue(row.getCell(4)));

                    tempEntryWholeVO.setList(new ArrayList<>());
                    for (EntryLangC elc : entryLangCList) {
                        tempEntryLangWholeVO = new EntryLangWholeVO();
                        tempEntryLangWholeVO.setEntryLangVO(new EntryLangVO());
                        tempEntryLangWholeVO.getEntryLangVO().setLanguage(elc.getLangName());
                        tempEntryLangWholeVO.getEntryLangVO().setLanguageId(elc.getLangId());
                        tempEntryLangWholeVO.getEntryLangVO().setDefinition(getCellValue(row.getCell(elc.getDefColumn())));

                        tempEntryLangWholeVO.setList(new ArrayList<>());
                        for (Integer im : elc.getItemColumnList()) {
                            ItemVO tempItem = new ItemVO();
                            tempItem.setText(getCellValue(row.getCell(im)));
                            if (StringUtils.isEmpty(tempItem.getText())) {
                                continue;
                            }
                            tempItem.setExample(getCellValue(row.getCell(im + 1)));
                            tempItem.setForDoc(Boolean.valueOf(getCellValue(row.getCell(im + 2))) ? 1 : 0);
                            tempEntryLangWholeVO.getList().add(tempItem);
                        }

                        if (tempEntryLangWholeVO.getList().size() > 0) {
                            tempEntryWholeVO.getList().add(tempEntryLangWholeVO);
                        }

                    }

                    if (tempEntryWholeVO.getList().size() > 0) {
                        list.add(tempEntryWholeVO);
                    }
                }
                wb.close();
            } catch (Exception ex) {
                throw new YudaoException(Result.ERROR, "文件解析失败"+ex.getMessage());
            }
            if (CollectionUtils.isNotEmpty(list)) {
                saveBatch(list, isProcess);
            }
            return list.size();
        }
        else {
            throw new YudaoException(Result.ERROR, "上传文件的格式不支持");
        }
    }

    private String getCellValue(XSSFCell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        if (cell == null || StringUtils.isBlank(dataFormatter.formatCellValue(cell))) {
            return "";
        }
        return dataFormatter.formatCellValue(cell);
    }

    private String getFieldIds(Map<String, String> fieldNameIdMap, String names) {
        List<String> ids = new ArrayList<>();
        List<String> nameList = CommonUtils.splitToList(names, "；");
        for (String name : nameList) {
            if (!fieldNameIdMap.containsKey(name)) {
                fieldNameIdMap.put(name, fieldBusiness.getIdByName(name));
            }
            ids.add(fieldNameIdMap.get(name));
        }
        return String.join(",", ids);
    }
    private String getApplicableIds(Map<String, String> applicableNameIdMap, String names) {
        List<String> ids = new ArrayList<>();
        List<String> nameList = CommonUtils.splitToList(names, "；");
        for (String name : nameList) {
            if (!applicableNameIdMap.containsKey(name)) {
                applicableNameIdMap.put(name, applicableToBusiness.getIdByName(name));
            }
            ids.add(applicableNameIdMap.get(name));
        }
        return String.join(",", ids);
    }
    private String getPartOfSpeechId(Map<String, String> partOfSpeechNameIdMap, String name) {
        if (!partOfSpeechNameIdMap.containsKey(name)) {
            partOfSpeechNameIdMap.put(name, codeInfoBusiness.getIdByName("2020121402", name));
        }
        return partOfSpeechNameIdMap.get(name);
    }
    private String getLangId(Map<String, String> langNameIdMap, String name) {
        if (!langNameIdMap.containsKey(name)) {
            langNameIdMap.put(name, langBusiness.getIdByNameZh(name));
        }
        return langNameIdMap.get(name);
    }

    public void export(HttpServletResponse response, String tbId, String entryIds, String langIds, String format) {
        Termbase knowledge = termbaseBusiness.selectById(tbId);
        ParameterValidUtils.entityExist(tbId, knowledge);
        List<String> entryIdList = CommonUtils.splitByCommaToList(entryIds);
        List<Entry> entrieList = new ArrayList<>();
        if (CollectionUtils.isEmpty(entryIdList)) {
            entrieList = this.selectList(new EntityWrapper<Entry>().eq("tb_guid", tbId).eq("status", "T"));
            entryIdList = entrieList.stream().map(m -> m.getId()).collect(Collectors.toList());
        }
        else {
            entrieList = this.selectBatchIds(entryIdList);
        }
        Wrapper<EntryLang> entryLangWrapper = new EntityWrapper<EntryLang>().in("entry_id", entryIdList);
        if (StringUtils.isNotEmpty(langIds)) {
            entryLangWrapper.in("language_id", CommonUtils.splitByCommaToList(langIds));
        }
        List<EntryLang> entryLangList = entryLangBusiness.selectList(entryLangWrapper);
        if (CollectionUtils.isNotEmpty(entrieList)) {
            String basename = setFileName(knowledge.getName(), "termbase");
            switch (format) {
                case "xlsx":
                    exportXlsx(entrieList, entryLangList, basename, response);
                    break;
                case "tbx":
                    exportTbxZip(entrieList, entryLangList, basename, response);
                    break;
            }
        }
    }

    private void exportXlsx(List<Entry> entrieList, List<EntryLang> entryLangList, String basename, HttpServletResponse response)  {
        List<String> titleList = new ArrayList<>();
        Map<String, List<String>> contentMap = new HashMap<>();
        titleList.add("ID");
        titleList.add("产品线");
        titleList.add("产品");
        titleList.add("词性");
        titleList.add("备注");
        for (Entry entry : entrieList) {
            List<String> ss = new ArrayList<>();
            ss.add(StringUtils.defaultString(entry.getId()).trim());
            ss.add(StringUtils.defaultString(entry.getFieldName()).trim());
            ss.add(StringUtils.defaultString(entry.getApplicableToName()).trim());
            ss.add(StringUtils.defaultString(entry.getPartOfSpeechName()).trim());
            ss.add(StringUtils.defaultString(entry.getNote()).trim());
            contentMap.put(entry.getId(), ss);
        }
        Map<String, List<EntryLang>> entryLangMap = entryLangList.stream().collect(Collectors.groupingBy(EntryLang::getLanguage));
        for (String la : entryLangMap.keySet()) {
            addLang(la, titleList, contentMap, entryLangMap.get(la));
        }
        String fileName = basename + "_" + getTimeStamp() + ".xlsx";
        String sheetName = "sheet1";
        //excel标题
        String[] title = titleList.toArray(new String[0]);

        int rowNum = contentMap.values().size();
        String[][] content = new String[rowNum][];
        int i = -1;
        for (List<String> cc : contentMap.values()) {
            i++;
            content[i] = cc.toArray(new String[0]);
        }
        XSSFWorkbook wb = ExcelUtils.getXSSFWorkbook(sheetName, title, content, null);
        setResponseHeader(response, fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ByteArrayOutputStream baos =new ByteArrayOutputStream();

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            wb.write(baos);
            byte[] bytes= baos.toByteArray();
            response.setContentLength(bytes.length);
            os.write(bytes);
            //wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null != os) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addLang(String langName, List<String> titleList, Map<String, List<String>> contentMap, List<EntryLang> entryLangs) {
        Map<String, EntryLang> collect = entryLangs.stream().collect(Collectors.toMap(EntryLang::getEntryId, v -> v));
        Map<String, List<Item>> term_entry_lang_id = itemBusiness.selectList(new EntityWrapper<Item>()
                .in("term_entry_lang_id", entryLangs.stream().map(el -> el.getId()).collect(Collectors.toList())))
                .stream().collect(Collectors.groupingBy(Item::getTermEntryLangId));
        int maxItemCount = 0;
        Map<String, Integer> entryItemCountMap = new HashMap<>();
        for (String k : contentMap.keySet()) {
            entryItemCountMap.put(k, 0);
            if (collect.containsKey(k)) {
                contentMap.get(k).add(collect.get(k).getDefinition());
                if (term_entry_lang_id.containsKey(collect.get(k).getId())) {
                    List<Item> items = term_entry_lang_id.get(collect.get(k).getId());
                    entryItemCountMap.put(k, items.size());
                    maxItemCount = Math.max(maxItemCount, items.size());
                    for (Item item : items) {
                        contentMap.get(k).add(StringUtils.defaultString(item.getText()).trim());
                        contentMap.get(k).add(StringUtils.defaultString(item.getExample()).trim());
                        contentMap.get(k).add(item.getForDoc() == 0 ? "False" : "True");
                    }
                }
            }
            else {
                contentMap.get(k).add("");
            }
        }
        for (String k : contentMap.keySet()) {
            if (entryItemCountMap.get(k) < maxItemCount) {
                for (int i = 0; i < maxItemCount - entryItemCountMap.get(k); i++) {
                    contentMap.get(k).add("");
                    contentMap.get(k).add("");
                    contentMap.get(k).add("");
                }
            }
        }
        titleList.add("定义");
        for (int i = 0; i < maxItemCount; i++) {
            titleList.add(langName);
            titleList.add("例句");
            titleList.add("是否规范");
        }
    }


    private void exportTbxZip(List<Entry> entrieList, List<EntryLang> allEntryLangList, String basename, HttpServletResponse response) {
        List<Item> allItemList = itemBusiness.selectList(new EntityWrapper<Item>()
                .in("term_entry_lang_id", allEntryLangList.stream().map(el -> el.getId()).collect(Collectors.toList())));
        String fileName = basename + "_" + getTimeStamp() + ".zip";
        String tbxfileName = basename + ".tbx";
        String imageFolderName = "media_" + basename;
        setResponseHeader(response, fileName);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(os)) {
                Document tbxDocument = createTbxDocument();
                Element bodyelement = tbxDocument.getRootElement().addElement("text").addElement("body");

                Document schemaDocument = createSchemaxDocument();
                Element languagesElement = schemaDocument.getRootElement().addElement("languages");
                Element datCatSetElement = schemaDocument.getRootElement().addElement("datCatSet");

                String imageFtpPath = "";
                String imageFileName = "";
                Element termEntryElement = null;
                Element langsetElement = null;
                Map<String, String> fieldIdNameMap = new HashMap<>();
                Map<String, String> applicationToIdNameMap = new HashMap<>();
                Map<String, String> posIdCodeMap = new HashMap<>();
                Map<String, String> langIdCodeMap = new HashMap<>();
                Map<String, String> langCodeNameMap = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                List<String> tempList=new ArrayList<>();
                for (Entry entry : entrieList) {
                    List<EntryLang> entryLangList = allEntryLangList.stream().filter(l -> l.getEntryId().equals(entry.getId())).collect(Collectors.toList());
                    if (entryLangList.isEmpty()) {
                        log.info("---------no find entrylang--------------");
                        continue;
                    }
                    termEntryElement = bodyelement.addElement("termEntry").addAttribute("id", entry.getId());
                    addDescripGrp(termEntryElement,"Creator", entry.getCreator());
                    addDescripGrp(termEntryElement,"xDate_CreateTime", sdf.format(entry.getCreated()));
                    tempList = CommonUtils.splitToList(entry.getField(), ",");
                    for (String field: tempList) {
                        addDescripGrp(termEntryElement,"xMultiplePicklist_Field", getFieldName(fieldIdNameMap,field));
                    }
                    tempList = CommonUtils.splitToList(entry.getApplicableTo(), ",");
                    for (String applicationto: tempList) {
                        addDescripGrp(termEntryElement,"xMultiplePicklist_Applicable to", getApplicableName(applicationToIdNameMap,applicationto));
                    }
                    addDescripGrp(termEntryElement,"Category",entry.getCategoryName());
                    addDescripGrp(termEntryElement,"Part of speech",getPartOfSpeechCode(posIdCodeMap,entry.getPartOfSpeech()));
                    addDescripGrp(termEntryElement,"Notes",entry.getNote());

                    if (StringUtils.isNotEmpty(entry.getImage())) {
                        imageFtpPath = getFtpPath(entry.getImage());
                        imageFileName = imageFtpPath.substring(imageFtpPath.lastIndexOf("/") + 1);
                        compressFile(zos, FtpUtils.download(imageFtpPath), imageFileName, imageFolderName);
                        termEntryElement.addElement("xref").addAttribute("type", "xImage_Image").addAttribute("target", imageFolderName + "/" + imageFileName);
                    }
                    if (StringUtils.isNotEmpty(entry.getImageSecond())) {
                        imageFtpPath = getFtpPath(entry.getImageSecond());
                        imageFileName = imageFtpPath.substring(imageFtpPath.lastIndexOf("/") + 1);
                        compressFile(zos, FtpUtils.download(imageFtpPath), imageFileName, imageFolderName);
                        termEntryElement.addElement("xref").addAttribute("type", "xImage_Image 2").addAttribute("target", imageFolderName + "/" + imageFileName);
                    }

                    for (EntryLang el : entryLangList) {
                        langsetElement = termEntryElement.addElement("langSet").addAttribute("xml:lang", getLangCode(langIdCodeMap,langCodeNameMap, el.getLanguageId()));
                        addDescripGrp(langsetElement,"Definition",el.getDefinition());
                        addDescripGrp(langsetElement,"Status",StringUtils.isEmpty(el.getState())?"To_Be_Approved":el.getState());

                        List<Item> itemList = allItemList.stream().filter(t -> t.getTermEntryLangId().equals(el.getId())).collect(Collectors.toList());
                        for (Item item : itemList) {
                            Element tigElement = langsetElement.addElement("tig");
                            tigElement.addElement("term").addAttribute("id", item.getId()).setText(item.getText());
                            addDescripGrp(tigElement,"xBool_For doc",item.getForDoc() == 1 ? "True" : "False");
                            addDescripGrp(tigElement,"xBool_Multi-trans",item.getMultiTrans() == 1 ? "True" : "False");
                            addDescripGrp(tigElement,"xBool_Forbidden",item.getForbidden() == 1 ? "True" : "False");
                            if(!StringUtils.isEmpty(item.getExample())){
                                addDescripGrp(tigElement,"Example",item.getExample());
                            }
                            addDescripGrp(tigElement,"CaseSense", "Near");
                            addDescripGrp(tigElement,"PartialTreshold", "Half");
                        }
                    }
                }

                //must add
                for (String key : langCodeNameMap.keySet()) {
                    addLangInfo(languagesElement, key, langCodeNameMap.get(key));
                }
                addSpec(datCatSetElement, "descripSpec", "Creator", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "xDate_CreateTime", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "xMultiplePicklist_Field", "picklist", fieldBusiness.getAllNameList(), "termEntry");
                addSpec(datCatSetElement, "descripSpec", "xMultiplePicklist_Applicable to", "picklist",applicableToBusiness.getAllNameList(),"termEntry");
                addSpec(datCatSetElement, "descripSpec", "Part of speech", "picklist", codeInfoBusiness.getAllCodeByKindCode("part_of_speech"), "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Notes", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Domain", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Subject", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Client", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Project", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "ImageCaption", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Keyword", "plainText", null, "termEntry");
                addSpec(datCatSetElement, "descripSpec", "Category", "picklist", categoryBusiness.getAllNameList(), "termEntry");
                addSpec(datCatSetElement, "xrefSpec", "xImage_Image", "", null, "");
                addSpec(datCatSetElement, "xrefSpec", "xImage_Image 2", "", null, "");
                addSpec(datCatSetElement, "descripSpec", "Definition", "plainText", null, "langSet");
                addSpec(datCatSetElement, "descripSpec", "Status", "picklist", Arrays.asList(new String[]{"To_Be_Approved","Approved"}), "langSet");
                addSpec(datCatSetElement, "descripSpec", "xBool_For doc", "plainText", null, "term");
                addSpec(datCatSetElement, "descripSpec", "xBool_Multi-trans", "plainText", null, "term");
                addSpec(datCatSetElement, "descripSpec", "xBool_Forbidden", "plainText", null, "term");
                addSpec(datCatSetElement, "descripSpec", "xIntegerVector_PrefixBoundaries", "plainText", null, "term");
                addSpec(datCatSetElement, "descripSpec", "Example", "plainText", null, "term");
                addSpec(datCatSetElement, "descripSpec", "PartialTreshold", "picklist",Arrays.asList(new String[]{"Any", "Custom" ,"Half" ,"None"}), "term");
                addSpec(datCatSetElement, "descripSpec", "CaseSense", "picklist",Arrays.asList(new String[]{"Insensitive","Near","Strict"}), "term");


                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                format.setLineSeparator("\r\n");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(new byte[] {(byte)0xEF,(byte)0xBB,(byte)0xBF});
                XMLWriter writer = new XMLWriter(baos, format);
                writer.write(tbxDocument);
                writer.close();
                compressFile(zos, new ByteArrayInputStream(baos.toByteArray()), tbxfileName, "");


                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                baos2.write(new byte[] {(byte)0xEF,(byte)0xBB,(byte)0xBF});
                XMLWriter writer2 = new XMLWriter(baos2, format);
                writer2.write(schemaDocument);
                writer2.close();
                compressFile(zos, new ByteArrayInputStream(baos2.toByteArray()), "schema.xcs", "");
//                String schemaFile="ftp://"+ftpHost+":"+ftpPort+"/uih/s baos.write(new byte[] {(byte)0xEF,(byte)0xBB,(byte)0xBF});chema.xcs";
//                compressFile(zos,FtpUtils.download(schemaFile),"schema.xcs","");
            }
        } catch (IOException e) {
            log.info("--------create file fail");
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.info("--------create file too fail");
                    e.printStackTrace();
                }
            }
        }
    }



    private Document createTbxDocument() {
        Document document = DocumentHelper.createDocument();
        String rootElementName = "martif";
        document.setDocType(DocumentFactory.getInstance().createDocType(rootElementName, null, "TBXcoreStructV02.dtd"));
        Element rootElement = document.addElement(rootElementName).addAttribute("type", "TBX").addAttribute("xml:lang", "en");
        Element martifHeader = rootElement.addElement("martifHeader");
        Element fileDesc = martifHeader.addElement("fileDesc");
        fileDesc.addElement("titleStmt").addElement("title");
        fileDesc.addElement("sourceDesc").addElement("p");
        Element encodingDesc2p = martifHeader.addElement("encodingDesc").addElement("p");
        encodingDesc2p.addAttribute("type", "XCSURI");
        encodingDesc2p.setText("schema.xcs");
        return document;
    }

    private Document createSchemaxDocument() {
        Document document = DocumentHelper.createDocument();
        String rootElementName = "TBXXCS";
        document.setDocType(DocumentFactory.getInstance().createDocType(rootElementName, null, "tbxxcsdtd.dtd"));
        Element rootElement = document.addElement(rootElementName).addAttribute("name", "master").addAttribute("version", "0.4").addAttribute("lang", "en");
        rootElement.addElement("header").addElement("title").setText("TBX XCS file");
        return document;
    }

    private void addSpec(Element elemnt, String type, String name, String dataType, List<String> dataList, String levels) {
        Element descripSpecElement = elemnt.addElement(type).addAttribute("name", name).addAttribute("datcatId", "");
        Element contentsElement = descripSpecElement.addElement("contents");
        if (StringUtils.isNotEmpty(dataType)) {
            contentsElement.addAttribute("datatype", dataType);
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            contentsElement.setText(String.join(" ", dataList));
        }
        if (StringUtils.isNotEmpty(levels)) {
            descripSpecElement.addElement("levels").setText(levels);
        }
    }

    private void addLangInfo(Element elemnt, String code, String name) {
        Element langInfoElement = elemnt.addElement("langInfo");
        langInfoElement.addElement("langCode").setText(code);
        langInfoElement.addElement("langName").setText(name);
    }

    private  void addDescripGrp(Element elemnt,String typename,String value){
        if(StringUtils.isEmpty(value)){
            value=" ";
        }
        elemnt.addElement("descripGrp").addElement("descrip").addAttribute("type",typename).setText(value);
    }
    private void compressFile(ZipOutputStream zos, InputStream inputStream, String fileName, String folderName) {
        try {
            ZipEntry zipEntry = new ZipEntry((StringUtils.isEmpty(folderName) ? "" : folderName + "/") + fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[2024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("--------compressFile fail"+fileName);
        }

    }

    private String getFieldName(Map<String, String> fieldIdNameMap, String id) {
        if (!fieldIdNameMap.containsKey(id)) {
            Field ff=fieldBusiness.selectById(id);
            if(ff==null){
                fieldIdNameMap.put(id, "");
            }
            else{
                fieldIdNameMap.put(id, ff.getName());
            }
        }
       return fieldIdNameMap.get(id);
    }
    private String getApplicableName(Map<String, String> applicableIdNameMap, String id) {
        if (!applicableIdNameMap.containsKey(id)) {
            ApplicableTo tt=applicableToBusiness.selectById(id);
            if(tt==null){
                applicableIdNameMap.put(id,"");
            }else {
                applicableIdNameMap.put(id, tt.getName());
            }
        }
        return applicableIdNameMap.get(id);
    }
    private String getPartOfSpeechCode(Map<String, String> partOfSpeechIdCodeMap, String id) {
        if (!partOfSpeechIdCodeMap.containsKey(id)) {
            partOfSpeechIdCodeMap.put(id, codeInfoBusiness.getById(id).getCode());
        }
        return partOfSpeechIdCodeMap.get(id);
    }
    private String getLangCode(Map<String, String> langIdCodeMap, Map<String, String> langCodeNameMap,String id) {
        Language language=null;
        if (!langIdCodeMap.containsKey(id)) {
            language=langBusiness.getByIdAuto(id);
            langIdCodeMap.put(id, language.getCode());
        }
        String code=langIdCodeMap.get(id);
        if (!langCodeNameMap.containsKey(code)) {
            if(language==null){
                language=langBusiness.getByIdAuto(id);
            }
            langCodeNameMap.put(code, language.getNameEn());
        }
        return code;
    }

    private String getTimeStamp() {
        String format = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }
    private String getFtpPath(String httpPath) {
        httpPath = httpPath.replace(ftpHttpAddress, "");
        httpPath = httpPath.replace(ftpHttpAddress2, "");
        return "ftp://" + ftpHost + ":" + ftpPort + "/" + httpPath;

    }
}