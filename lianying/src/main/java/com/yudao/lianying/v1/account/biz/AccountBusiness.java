package com.yudao.lianying.v1.account.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.*;
import com.yudao.lianying.v1.account.dao.Account;
import com.yudao.lianying.v1.account.dao.mapper.AccountMapper;
import com.yudao.lianying.v1.account.dto.AccountDTO;
import com.yudao.lianying.v1.account.dto.TermDTO;
import com.yudao.lianying.v1.account.vo.AccountModifyVO1;
import com.yudao.lianying.v1.account.vo.AccountVO;
import com.yudao.lianying.v1.field.biz.FieldBusiness;
import com.yudao.lianying.v1.field.dao.Field;
import com.yudao.lianying.v1.term.biz.EntryBusiness;
import com.yudao.lianying.v1.term.dto.PluginSearchEntry;
import com.yudao.lianying.v1.term.dto.PluginSearchItem;
import com.yudao.lianying.v1.term.dto.PluginSearchLanguage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.sql.*;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 账号信息表  服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-11
 */
@Service
@Slf4j
public class AccountBusiness extends ServiceImpl<AccountMapper, Account> {

    private final String SUPER_MANAGER_LEVEL = "100";

    @Autowired
    private FieldBusiness fieldBusiness;

    @Autowired
    private EntryBusiness entryBusiness;

    @Value("${memoq.host}")
    private String memoqHost;

    @Value("${memoq.username}")
    private String memoqUsername;

    @Value("${memoq.password}")
    private String memoqPassword;

    @Value("${memoq.tbGuid}")
    private String tbGuid;

    public String getMemoqHost() {
        return memoqHost;
    }

    public String getMemoqUsername() {
        return memoqUsername;
    }

    public String getMemoqPassword() {
        return memoqPassword;
    }

    public Object saveAuto(AccountVO entityVO) {
        Account entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Account.class);
        // 密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        entity.setPassword(encoder.encode(entity.getPassword().trim()));

        List<String> fieldIdList = entityVO.getFieldIdList();
        List<Field> fieldList = new ArrayList<>();
        for (String s : fieldIdList) {
            Field field = new Field();
            field.setId(s);
            field.setAccountId(entity.getId());
            fieldList.add(field);
        }
        if (!CollectionUtils.isEmpty(fieldList)) {
            fieldBusiness.updateBatchById(fieldList);
        }
        insert(entity);
        return entity.getId();
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Account> list = new ArrayList<>();
        for (String id : set) {
            Account entity = new Account(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        List<Field> fieldList = getFieldsByAccounts(set);
        removeAccountOfField(fieldList);
        updateBatchById(list);
    }

    private void removeAccountOfField(List<Field> fieldList) {
        if (!CollectionUtils.isEmpty(fieldList)) {
            fieldList.forEach(field -> field.setAccountId(null));
            fieldBusiness.updateAllColumnBatchById(fieldList);
        }
    }

    @Transactional
    public void modifyAllColumnInObjectAuto(AccountModifyVO1 modifyVO1) {
        Account entity = selectById(modifyVO1.getId());
        ParameterValidUtils.entityExist(modifyVO1.getId(), entity);

        BeanUtils.copyProperties(modifyVO1, entity);
        entity.setUpdateTime(DateUtils.getNow());
        List<Field> fieldList = new ArrayList<>();
        for (String s : modifyVO1.getFieldIdList()) {
            Field field = new Field();
            field.setId(s);
            field.setAccountId(entity.getId());
            fieldList.add(field);
        }
        List<Field> toDeletefieldList = getFieldsByAccount(modifyVO1.getId());
        removeAccountOfField(toDeletefieldList);
        if (!CollectionUtils.isEmpty(fieldList)) {
            fieldBusiness.updateBatchById(fieldList);
        }
        updateAllColumnById(entity);
    }

    public void modifyByIdAuto(List<String> idList, String[] modifyFieldNameArray, String[] modifyFieldValueArray) {
        baseMapper.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
    }

    public Account getByIdAuto(String id) {
        Account entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Account> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Account> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        entityWrapper.ne(FieldNameEnum.level.name(), SUPER_MANAGER_LEVEL);
        if (StringUtils.isNotBlank(keyword)) {
            entityWrapper.andNew().like(FieldNameEnum.login_name.name(), keyword).or().like(FieldNameEnum.email.name(), keyword);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        Page<Account> accountPage = selectPage(page, entityWrapper);
        List<Account> accountList = accountPage.getRecords();
        accountList.forEach(account -> account.setPassword(""));
        List<String> accountIdList = accountList.stream().map(Account::getId).collect(Collectors.toList());
        List<Field> fields = getFieldsByAccounts(accountIdList);
        List<AccountDTO> list = new ArrayList<>();
        for (Account account : accountList) {
            AccountDTO accountDTO = CommonUtils.copyProperties(account, AccountDTO.class);
            Set<Field> collect = fields.stream().filter(field -> account.getId().equals(field.getAccountId())).collect(Collectors.toSet());
            accountDTO.setFields(collect);
            list.add(accountDTO);
        }
        Page<AccountDTO> pageDTO = new Page<>();
        BeanUtils.copyProperties(page, pageDTO);
        pageDTO.setRecords(list);
        return pageDTO;
    }

    private List<Field> getFieldsByAccounts(List<String> accountIdList) {
        Field entity = new Field();
        entity.setStatus(StatusValueEnum.normal.getValue());
        return fieldBusiness.selectList(new EntityWrapper<>(entity).in(FieldNameEnum.account_id.name(), accountIdList));
    }

    private List<Field> getFieldsByAccount(String accountId) {
        Field entity = new Field();
        entity.setStatus(StatusValueEnum.normal.getValue());
        return fieldBusiness.selectList(new EntityWrapper<>(entity).eq(FieldNameEnum.account_id.name(), accountId));
    }

    public Object getLevel(String id) {
        Account account = selectById(id);
        try {
            ParameterValidUtils.entityExist(id, account);
        } catch (Exception e) {
            return -1;
        }
        return account.getLevel();
    }

    public Object getAvailFields(String id) {
        Field field = new Field();
        field.setStatus(StatusValueEnum.normal.getValue());
        return fieldBusiness.selectList(new EntityWrapper<>(field).andNew().eq(FieldNameEnum.account_id.name(), id).or().isNull(FieldNameEnum.account_id.name()).or().eq(FieldNameEnum.account_id.name(), ""));
    }

    public Object login(String loginName, String password) {
        Account account = selectOne(new EntityWrapper<Account>().eq(FieldNameEnum.login_name.name(), loginName).or().eq(FieldNameEnum.email.name(), loginName));
        ParameterValidUtils.entityExist(account.getId(), account);
        PswUtils.vaild(password, account.getPassword());
        AccountDTO accountDTO = CommonUtils.copyProperties(account, AccountDTO.class);
        Field entity = new Field();
        entity.setStatus(StatusValueEnum.normal.getValue());
        entity.setAccountId(account.getId());
        List<Field> fields = fieldBusiness.selectList(new EntityWrapper<>(entity));
        accountDTO.setFields(fields);
        return accountDTO;
    }

    public void modifyPswById(String accountId, String psw) {
        Account account = new Account();
        account.setId(accountId);
        account.setPassword(PswUtils.encrypt(psw));
        updateById(account);
    }

    public void vaildFormat(String psw)  {
        if (StringUtils.isBlank(psw)) {
            throw new YudaoException(Result.ERROR, "密码不能为空");
        }
        if (!psw.matches("^(?=.*[A-z])(?=.*\\d)(?=.*[.?!@#$%^&*`~()\\-_+=])[A-z\\d.?!@#$%^&*`~()\\-_+=]{8,20}$")) {
            throw new YudaoException(Result.ERROR, "密码格式不正确");
        }
    }

    private String token = "";
    private String sid;

    /*
    call python service
     */
    public String getToken(String token) {
        if (this.token.equals(token)) {
            String s;
            if ("https://memoq.united-imaging.com:8080/memoqserverhttpsapi/v1/".equals(memoqHost)) {
                s = NetUtils.net("http://10.6.2.91:9000/login", null, "GET", 500000);
            }
            else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserName", memoqUsername);
                jsonObject.put("Password", memoqPassword);
                jsonObject.put("LoginMode", 0);
                s = NetUtils.doPostJson(memoqHost + "auth/login", jsonObject.toJSONString());
            }
            log.info(s);
            JSONObject json = JSONObject.parseObject(s);
            this.sid = json.getString("Sid") == null ? "" : json.getString("Sid");
            this.token = json.getString("AccessToken") == null ? "" : json.getString("AccessToken");
        }
        return this.token;
    }

    public String getTerms2(String searchExpression) {
        for (int i = 0; i < 3; i++) {

            String lang = "eng-US";
            if (searchExpression.matches(".*[\\u4e00-\\u9fa5].*")) {
                lang = "zho-CN";
            }
            log.info("------" + lang + "-------");
            List<PluginSearchEntry> pluginSearchEntries=null;
            JSONArray jsonArray;
            try {
                pluginSearchEntries = entryBusiness.search4Plugin(searchExpression);
            } catch (Exception e) {
                throw e;
            }
            JSONArray list = new JSONArray();
            List<String> memoqIds = new ArrayList<>();
            Iterator<PluginSearchEntry> iterator = pluginSearchEntries.iterator();
            while (iterator.hasNext()) {
                PluginSearchEntry pluginSearchEntry = iterator.next();
                if (memoqIds.contains(pluginSearchEntry.getId())) {
                    iterator.remove();
                }
                else {
                    memoqIds.add(pluginSearchEntry.getId());
                }
            }
            for (PluginSearchEntry o : pluginSearchEntries) {
                for (PluginSearchLanguage language : o.getLanguages()) {
                    if (lang.equals(language.getLanguage())) {
                        for (PluginSearchItem termItem : language.getTermItems()) {
                            String text = termItem.getText();
                            if (StringUtils.isNotBlank(text) && text.toLowerCase().contains(searchExpression.toLowerCase())) {
                                list.add(text);
                                list.add(termItem.getForDoc().toString());
                                StringBuilder sb = new StringBuilder();
                                for (PluginSearchLanguage language2 : o.getLanguages()) {
                                    JSONObject language2Obj = (JSONObject) JSONObject.toJSON(language2);
                                    sb.append(language2.getLanguage()).append("<br>");
//                                    JSONArray items2 = language2Obj.getJSONArray("TermItems");
                                    for (PluginSearchItem item2 : language2.getTermItems()) {
//                                        JSONObject itemObj2 = (JSONObject) JSONObject.toJSON(item2);
                                        String t = item2.getText();
                                        if (StringUtils.isNotBlank(t)) {
                                            sb.append(t).append("<br>");
                                            String example = item2.getExample();
                                            if (StringUtils.isNotBlank(example)) {
                                                if ("eng-US".equals(language2.getLanguage())) {
                                                    sb.append("Example: ");
                                                } else {
                                                    sb.append("例句：");
                                                }
                                                sb.append(example).append("<br>");
                                            }
//                                            JSONArray cms = itemObj2.getJSONArray("CustomMetas");
                                            boolean fd = item2.getForDoc()==1;
                                            if (fd) {
                                                sb.append("[For doc: True]<br>");
                                            }
                                            else {
                                                sb.append("[For doc: False]<br>");
                                            }
                                        }
                                    }
                                    String definition = language2.getDefinition();
                                    if (StringUtils.isNotBlank(definition)) {
                                        if ("eng-US".equals(language2.getLanguage())) {
                                            sb.append("Definition: ");
                                        } else {
                                            sb.append("定义：");
                                        }
                                        sb.append(definition).append("<br>");
                                    }

                                    sb.append("<br>");
                                }

                                if (StringUtils.isNotEmpty(o.getFieldName())) {
                                    sb.append("产品线：").append(o.getFieldName()).append("<br>");
                                }
                                if (StringUtils.isNotEmpty(o.getApplicableToName())) {
                                    sb.append("产品：").append(o.getApplicableToName()).append("<br>");
                                    continue;
                                }
                                if (StringUtils.isNotEmpty(o.getPartOfSpeechName())) {
                                    sb.append("词性：").append(o.getPartOfSpeechName()).append("<br>");
                                    continue;
                                }
                                if (StringUtils.isNotEmpty(o.getNote())) {
                                    sb.append("备注：").append(o.getNote()).append("<br>");
                                }

                                JSONArray customMetas2 = null;

//                                sb.append("贡献人：").append(jsonObject.getString("Creator")).append("<br>");
//                                sb.append("评审人：").append(jsonObject.getString("Modifier")).append("<br>");
//                                String createTime = jsonObject.getString("Created");
//                                if (createTime != null) {
//                                    createTime = createTime.replace("T", " ").replace("Z", "");
//                                }
//                                sb.append("创建时间：").append(createTime);
                                list.add(sb.toString());
                            }
                        }
                    }
                }
            }
            List<TermDTO> termDTOS = new ArrayList<>();
            for (int j = 0; j < list.size() / 3; j++) {
                TermDTO termDTO = new TermDTO();
                termDTO.setName(list.getString(3 * j));
                termDTO.setForDoc(list.getString(3 * j + 1));
                termDTO.setDetail(list.getString(3 * j + 2));
                termDTOS.add(termDTO);
            }
            Comparator comparator = Collator.getInstance(Locale.CHINA);
            termDTOS.sort((p1, p2) -> comparator.compare(p1.getName(), p2.getName()));
            list.clear();
            for (TermDTO termDTO : termDTOS) {
                list.add(termDTO.getName());
                list.add(termDTO.getForDoc());
                list.add(termDTO.getDetail());
            }
            return list.toString();
        }
        throw new YudaoException(Result.ERROR, "InvalidOrExpiredToken");
    }

    public String getTerms(String searchExpression) {
        for (int i = 0; i < 3; i++) {
            log.info("------begin get token-------");
            String token = getToken("");
            log.info("------end get token-------");
            String lang = "eng-US";
            if (searchExpression.matches(".*[\\u4e00-\\u9fa5].*")) {
                lang = "zho-CN";
            }
            log.info("------" + lang + "-------");
            JSONArray list = new JSONArray();
            String result = null;
            if ("https://memoq.united-imaging.com:8080/memoqserverhttpsapi/v1/".equals(memoqHost)) {
                try {
                    result = NetUtils.net("http://10.6.2.91:9000/tbsearch?token=" + token + "&expression=" + URLEncoder.encode(searchExpression, "utf-8") + "&lang=" + lang, null, "GET", 500000);
                } catch (Exception e) {
                }
            }
            else {
                JSONObject json = new JSONObject();
                json.put("FilteringConditions", new ArrayList<>());
                json.put("Limit", 120);
                json.put("SearchExpression", searchExpression);
                json.put("Condition", 1);
                json.put("TargetLanguage", lang);
                String url = memoqHost + "tbs/" + tbGuid + "/search?authToken=" + token;
                result = NetUtils.doPostJson(url, json.toString());
            }
            log.info("------result-------");
            log.info(result);
            JSONArray jsonArray;
            try {
                jsonArray = JSONArray.parseArray(result);
            } catch (Exception e) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("InvalidOrExpiredToken".equals(jsonObject.getString("ErrorCode"))) {
                    continue;
                }
                throw e;
            }
            List<String> memoqIds = new ArrayList<>();
            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                String id = jsonObject.getString("Id");
                if (memoqIds.contains(id)) {
                    iterator.remove();
                }
                else {
                    memoqIds.add(id);
                }
            }
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(o);
                JSONArray languages = jsonObject.getJSONArray("Languages");
                for (Object language : languages) {
                    JSONObject langObj = (JSONObject) JSONObject.toJSON(language);
                    if (lang.equals(langObj.getString("Language"))) {
                        JSONArray termItems = langObj.getJSONArray("TermItems");
                        for (Object termItem : termItems) {
                            JSONObject itemObj = (JSONObject) JSONObject.toJSON(termItem);
                            String text = itemObj.getString("Text");
                            if (StringUtils.isNotBlank(text) && text.toLowerCase().contains(searchExpression.toLowerCase())) {
                                list.add(text);
                                JSONArray customMetas = itemObj.getJSONArray("CustomMetas");
                                boolean forDoc = false;
                                for (Object customMeta : customMetas) {
                                    JSONObject customMetaObj = (JSONObject) JSONObject.toJSON(customMeta);
                                    if ("For doc".equals(customMetaObj.getString("Name")) && "True".equals(customMetaObj.getString("Value"))) {
                                        forDoc = true;
                                        break;
                                    }
                                }
                                if (forDoc) {
                                    list.add("1");
                                }
                                else {
                                    list.add("0");
                                }

                                StringBuilder sb = new StringBuilder();
                                for (Object language2 : languages) {
                                    JSONObject language2Obj = (JSONObject) JSONObject.toJSON(language2);
                                    sb.append(language2Obj.getString("Language")).append("<br>");
                                    JSONArray items2 = language2Obj.getJSONArray("TermItems");
                                    for (Object item2 : items2) {
                                        JSONObject itemObj2 = (JSONObject) JSONObject.toJSON(item2);
                                        String t = itemObj2.getString("Text");
                                        if (StringUtils.isNotBlank(t)) {
                                            sb.append(t).append("<br>");
                                            String example = itemObj2.getString("Example");
                                            if (StringUtils.isNotBlank(example)) {
                                                if ("eng-US".equals(language2Obj.getString("Language"))) {
                                                    sb.append("Example: ");
                                                } else {
                                                    sb.append("例句：");
                                                }
                                                sb.append(example).append("<br>");
                                            }
                                            JSONArray cms = itemObj2.getJSONArray("CustomMetas");
                                            boolean fd = false;
                                            for (Object cm : cms) {
                                                JSONObject cmObj = (JSONObject) JSONObject.toJSON(cm);
                                                if ("For doc".equals(cmObj.getString("Name")) && "True".equals(cmObj.getString("Value"))) {
                                                    fd = true;
                                                    break;
                                                }
                                            }
                                            if (fd) {
                                                sb.append("[For doc: True]<br>");
                                            }
                                            else {
                                                sb.append("[For doc: False]<br>");
                                            }
                                        }
                                    }
                                    String definition = language2Obj.getString("Definition");
                                    if (StringUtils.isNotBlank(definition)) {
                                        if ("eng-US".equals(language2Obj.getString("Language"))) {
                                            sb.append("Definition: ");
                                        } else {
                                            sb.append("定义：");
                                        }
                                        sb.append(definition).append("<br>");
                                    }

                                    sb.append("<br>");
                                }
                                JSONArray customMetas2 = jsonObject.getJSONArray("CustomMetas");
                                for (Object customMeta : customMetas2) {
                                    JSONObject customMetaObj = (JSONObject) JSONObject.toJSON(customMeta);
                                    if ("Field".equals(customMetaObj.getString("Name"))) {
                                        sb.append("产品线：").append(customMetaObj.getString("Value")).append("<br>");
                                        continue;
                                    }
                                    if ("Applicable to".equals(customMetaObj.getString("Name"))) {
                                        sb.append("产品：").append(customMetaObj.getString("Value")).append("<br>");
                                        continue;
                                    }
                                    if ("Part of speech".equals(customMetaObj.getString("Name"))) {
                                        sb.append("词性：").append(customMetaObj.getString("Value")).append("<br>");
                                        continue;
                                    }
                                    if ("Notes".equals(customMetaObj.getString("Name"))) {
                                        sb.append("备注：").append(customMetaObj.getString("Value")).append("<br>");
                                    }
                                }
                                sb.append("贡献人：").append(jsonObject.getString("Creator")).append("<br>");
                                sb.append("评审人：").append(jsonObject.getString("Modifier")).append("<br>");
                                String createTime = jsonObject.getString("Created");
                                if (createTime != null) {
                                    createTime = createTime.replace("T", " ").replace("Z", "");
                                }
                                sb.append("创建时间：").append(createTime);
                                list.add(sb.toString());
                            }
                        }
                    }
                }
            }
            List<TermDTO> termDTOS = new ArrayList<>();
            for (int j = 0; j < list.size() / 3; j++) {
                TermDTO termDTO = new TermDTO();
                termDTO.setName(list.getString(3 * j));
                termDTO.setForDoc(list.getString(3 * j + 1));
                termDTO.setDetail(list.getString(3 * j + 2));
                termDTOS.add(termDTO);
            }
            Comparator comparator = Collator.getInstance(Locale.CHINA);
            termDTOS.sort((p1, p2) -> comparator.compare(p1.getName(), p2.getName()));
            list.clear();
            for (TermDTO termDTO : termDTOS) {
                list.add(termDTO.getName());
                list.add(termDTO.getForDoc());
                list.add(termDTO.getDetail());
            }
            return list.toString();
        }
        throw new YudaoException(Result.ERROR, "InvalidOrExpiredToken");
    }

    /**
     * mysql存储过程
     *
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;   //数据库连接
        try {
            Class.forName("com.mysql.jdbc.Driver"); //加载数据库驱动，注册到驱动管理器
            /*数据库链接地址*/
            String url = "jdbc:mysql://192.168.0.212:13306/lianying?useUnicode=true&characterEncoding=UTF-8";
            String username = "root";
            String password = "yudao@123456";
            /*创建Connection链接*/
            conn = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;  //返回数据库连接

    }

    public List<Account> findAll() {
        List<Account> list = new ArrayList<>(); //实例化List对象
        Connection conn = getConnection();  //创建数据库连接
        try {
            //调用存储过程
            CallableStatement cs = conn.prepareCall("{call NewProc()}");
            ResultSet rs = cs.executeQuery(); //执行查询操作，并获取结果集
            while (rs.next()) {
                Account book = new Account(); //实例化Book对象
                book.setId(rs.getString("id"));  //对name属性赋值
                book.setLoginName(rs.getString("login_name")); //对price属性赋值
                book.setEmail(rs.getString("email")); //对bookCount属性赋值
                list.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;     //返回list
    }


    /**
     * sqlserver存储过程
     *
     * @return
     */
    public Object fuzzySelectUser(String keyword) {
        Connection connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://10.6.2.79;DatabaseName=ecology", "user_shuyu", "123@qweASD!23");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("", e);
        }
        try {
            // 设置调用的存储过程名及参数情况
            CallableStatement proc = connection.prepareCall("{ call dbo.shuyuzhikong_fuzzySelectLoginEmail(?) }");
            // 设置输入参数值1的值
            proc.setString(1, keyword);
            proc.execute();

            ResultSet rs = proc.getResultSet();
            while (rs.next()) {
                log.info("-----------" + rs.getString("email"));
                log.info("-----------" + rs.getString("loginid"));
            }


            CallableStatement proc2 = connection.prepareCall("{ call dbo.shuyuzhikong_selectEmail(?) }");
            // 设置输入参数值1的值
            proc2.setString(1, keyword);
            proc2.execute();

            ResultSet rs2 = proc2.getResultSet();
            while (rs2.next()) {
                log.info("-----------" + rs2.getString("email"));
            }


            connection.close();
            return rs;
        } catch (SQLException e) {
            log.error("", e);
        }
        return "";
    }

    public String getUserEmail(String createBy) {
        return baseMapper.getUserEmail(createBy);
    }
}