package com.yudao.lianying.v1.process.biz;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.FieldNameEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ParameterValidUtils;
import com.yudao.lianying.utils.MailUtils;
import com.yudao.lianying.utils.ModuleFieldNameEnum;
import com.yudao.lianying.utils.ProcessStateEnum;
import com.yudao.lianying.utils.ProcessTypeEnum;
import com.yudao.lianying.v1.account.biz.AccountBusiness;
import com.yudao.lianying.v1.account.dao.Account;
import com.yudao.lianying.v1.comment.biz.CommentBusiness;
import com.yudao.lianying.v1.comment.dao.Comment;
import com.yudao.lianying.v1.notice.biz.NoticeBusiness;
import com.yudao.lianying.v1.notice.dao.Notice;
import com.yudao.lianying.v1.process.dao.Process;
import com.yudao.lianying.v1.process.dao.mapper.ProcessMapper;
import com.yudao.lianying.v1.process.dto.ProcessHistoryDTO;
import com.yudao.lianying.v1.process.dto.CommentProcessManageDTO;
import com.yudao.lianying.v1.process.dto.TermProcessManageDTO;
import com.yudao.lianying.v1.process.vo.ForwardBatchVO;
import com.yudao.lianying.v1.process.vo.ProcessModifyVO1;
import com.yudao.lianying.v1.process.vo.ProcessVO;
import com.yudao.lianying.v1.term.biz.*;
import com.yudao.lianying.v1.term.dao.Entry;
import com.yudao.lianying.v1.term.dao.EntryLang;
import com.yudao.lianying.v1.term.dao.Item;
import com.yudao.lianying.v1.term.dao.Termbase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程表 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Service
@Slf4j
public class ProcessBusiness extends ServiceImpl<ProcessMapper, Process> {

    @Autowired
    private TermbaseBusiness termbaseBusiness;

    @Autowired
    private EntryBusiness entryBusiness;

    @Autowired
    private EntryLangBusiness entryLangBusiness;

    @Autowired
    private ItemBusiness itemBusiness;

    @Autowired
    private CommentBusiness commentBusiness;


    @Autowired
    private NoticeBusiness noticeBusiness;

    @Autowired
    private AccountBusiness accountBusiness;

    @Autowired
    private FeiShuPluginBusiness feiShuPluginBusiness;

    @Value("${web.host}")
    private String webHost;

    public Object saveAuto(ProcessVO entityVO) {
        Process entity = CommonUtils.copyPropertiesAndCreateId(entityVO, Process.class);
        if (insert(entity)) {
            return entity;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Process> list = new ArrayList<>();
        for (String id : set) {
            Process entity = new Process(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(ProcessModifyVO1 modifyVO1) {
        Process entity = selectById(modifyVO1.getId());
        ParameterValidUtils.entityExist(modifyVO1.getId(), entity);
        BeanUtils.copyProperties(modifyVO1, entity);
        entity.setUpdateTime(DateUtils.getNow());
        if (!(updateAllColumnById(entity))) {
            throw new YudaoException(Result.ERROR, "Failed to modify");
        }
    }

    public void modifyByIdAuto(List<String> idList, String[] modifyFieldNameArray, String[] modifyFieldValueArray) {
        baseMapper.modifyByIdAuto(idList, modifyFieldNameArray, modifyFieldValueArray);
    }

    public Object getByIdAuto(String id) {
        Process entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Process> page, String keyword, Date beginTime, Date endTime, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Process> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(FieldNameEnum.status.name(), StatusValueEnum.normal.getValue());
        for (int i = 0; i < filterFieldNameArray.length; i++) {
            entityWrapper.in(filterFieldNameArray[i], filterFieldValueArray[i]);
        }
        if (null != beginTime) {
            entityWrapper.ge(FieldNameEnum.create_time.name(), beginTime);
        }
        if (null != beginTime) {
            entityWrapper.lt(FieldNameEnum.create_time.name(), endTime);
        }
        if (StringUtils.isNotBlank(keyword)) {
            entityWrapper.andNew().eq(ModuleFieldNameEnum.term_text.name(), keyword)
                    .or().eq(ModuleFieldNameEnum.term2_text.name(), keyword)
                    .or().eq(ModuleFieldNameEnum.comment_text.name(), keyword);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            entityWrapper.orderBy(orderBy);
        }
        // 不管有没有排序，最后都对创建时间倒序
        entityWrapper.orderBy(FieldNameEnum.create_time.name(), false);
        return selectPage(page, entityWrapper);
    }

    public void forward(String processId, String accountId, String email, String remark) {
        Date now = DateUtils.getNow();
        Process process = selectById(processId);
        ParameterValidUtils.entityExist(processId, process);
//        process.setState(ProcessStateEnum.forwarded.getId());
//        process.setUpdateTime(now);
        String managerRealName = getManagerRealName(process);

        ParameterValidUtils.entityExist(processId, process);
        Process forword = CommonUtils.copyPropertiesAndCreateId(process, Process.class);
        forword.setState(ProcessStateEnum.to_process.getId());
        forword.setRemark(remark);
        forword.setReaded(0);
        forword.setCreateBy(accountId);
        forword.setParentId(processId);
        forword.setCreateTime(now);
        forword.setUpdateTime(now);

//        updateAllColumnById(process);
        insert(forword);
//        if (EmailUtils.validateEmail(email)) {
        if (false) {
            StringBuilder bodyBuilder = new StringBuilder();
            String titleStr;
            if (ProcessTypeEnum.term.name().equals(process.getType())) {
                titleStr = "【联影词典】新术语待评审；New terms to be reviewed";
                bodyBuilder.append("管理员[").append(managerRealName).append("]已将新术语转发给您评审：<br/>").append("The administrator [").append(managerRealName).append("] has sent the following terms to you for review:").append("<br/>")
                        .append("<br/>")
                        .append("术语信息/Term info：").append(process.getTermText()).append("<br/>")
                        .append("<br/>")
                        .append("请您及时查看、添加评审意见。点此<a href=\"").append(webHost).append("/user/process/unDeal?tabIndex=1\">查看详情</a>。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/process/unDeal?tabIndex=1\">here</a> to review the terms and share your comments.");
            }
            else {
                String commentId = process.getCommentId();
                Comment comment = commentBusiness.selectById(commentId);
                ParameterValidUtils.entityExist(commentId, comment);
                String place = commentBusiness.getPlaceStr4Email(comment);
                titleStr = "【联影词典】新的纠错意见待评审；Submitted suggestion to be reviewed";
                bodyBuilder.append("用户[").append(comment.getCreateBy()).append("]已为术语添加纠错意见：").append("<br/>")
                        .append("The user [").append(comment.getCreateBy()).append("] has suggested an edit for a term entry:").append("<br/>")
                        .append("<br/>")
                        .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                        .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                        .append("<br/>")
                        .append("管理员[").append(managerRealName).append("]已将此纠错意见转发给您评审。请您及时查看、添加评审意见。点此<a href=\"").append(webHost).append("/user/process/unDeal?tabIndex=2\">查看详情</a>。").append("<br/>")
                        .append("The administrator [").append(managerRealName).append("] has sent this to you for review. Please click <a href=\"").append(webHost).append("/user/process/unDeal?tabIndex=2\">here</a> to review the suggestion and share your comments.");
            }
            MailUtils.asynSendMail(email, null, titleStr, bodyBuilder.toString(), null);
        }
    }

    public void forwardBatch(ForwardBatchVO forwardBatchVO){
        if(CollectionUtils.isNotEmpty(forwardBatchVO.getForwardList()))
        {
            forwardBatchVO.getForwardList().forEach(f->{
                forward(forwardBatchVO.getProcessId(),f.getAccountId(),f.getEmail(),forwardBatchVO.getRemark());
            });
        }
    }
    public void suggest(String processId, String suggestion) {
        Process process = selectById(processId);
        ParameterValidUtils.entityExist(processId, process);
        Process parentProcess = selectById(process.getParentId());
        ParameterValidUtils.entityExist(process.getParentId(), parentProcess);
        process.setSuggestion(suggestion);
        process.setState(ProcessStateEnum.approved.getId());
        process.setUpdateTime(DateUtils.getNow());
//        parentProcess.setState(ProcessStateEnum.to_process.getId());
//        parentProcess.setReaded(0);
//        parentProcess.setUpdateTime(DateUtils.getNow());
        updateAllColumnById(process);
//        updateAllColumnById(parentProcess);
        String suggest;
        String enSuggest;
        if (suggestion.startsWith("[同意]")) {
            suggest = "[同意]";
            enSuggest = "[Accept]";
        }
        else if (suggestion.startsWith("[不同意]")) {
            suggest = "[不同意]";
            enSuggest = "[Reject]";
        }
        else {
            suggest = "[其他意见]";
            enSuggest = "[Other comments]";
        }
        Account account = accountBusiness.selectById(parentProcess.getCreateBy());
//        if (null != account && EmailUtils.validateEmail(account.getEmail())) {
//        if (null != account)
        if(false)
        {
            StringBuilder bodyBuilder = new StringBuilder();
            String titleStr;
            if (ProcessTypeEnum.term.name().equals(process.getType())) {
                titleStr = "【联影词典】请查看新术语的评审意见；Review result of new terms";
                bodyBuilder.append("用户[").append(process.getCreateBy()).append("]已为新术语添加评审意见：").append(suggest).append("<br/>")
                        .append("The user [").append(process.getCreateBy()).append("] has reviewed the new terms.<br/>Review result: ").append(enSuggest).append("<br/>")
                        .append("<br/>")
                        .append("术语信息/Term info：").append(process.getTermText()).append("<br/>")
                        .append("<br/>")
                        .append("请您及时查看、处理。点此<a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=1\">查看详情</a>。<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=1\">here</a> to review the result.");
            }
            else {
                String commentId = process.getCommentId();
                Comment comment = commentBusiness.selectById(commentId);
                ParameterValidUtils.entityExist(commentId, comment);
                String place = commentBusiness.getPlaceStr4Email(comment);
                titleStr = "【联影词典】请查看纠错的评审意见；Review result of submitted suggestion";
                bodyBuilder.append("用户[").append(process.getCreateBy()).append("]已为纠错添加评审意见：").append(suggest).append("<br/>")
                        .append("The user [").append(process.getCreateBy()).append("] has reviewed the submitted suggestion.<br/>Review result: ").append(enSuggest).append("<br/>")
                        .append("<br/>")
                        .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                        .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                        .append("<br/>")
                        .append("请您及时查看、处理。点此<a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=2\">查看详情</a>。<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=2\">here</a> to review the result.");
            }
            MailUtils.asynSendMail(account.getEmail(), null, titleStr, bodyBuilder.toString(), null);
        }
    }

    public void processed(String processId, String state, Boolean publicShow, String suggestion,String tbId) {
        String stateStr;
        if (ProcessStateEnum.accepted.getId().equals(state)) {
            stateStr = "】被接受";
        }
        else if (ProcessStateEnum.refused.getId().equals(state)) {
            stateStr = "】被拒绝";
        }
        else if (ProcessStateEnum.to_back.getId().equals(state)) {
            stateStr = "】被退回";
        }
        else {
            throw new YudaoException(Result.ERROR, "评审状态错误");
        }

        Process manageProcess = selectById(processId);
        ParameterValidUtils.entityExist(processId, manageProcess);
        manageProcess.setSuggestion(suggestion);
        // 查询创建流程
        Process submittedProcess = selectById(manageProcess.getParentId());
        submittedProcess.setReaded(0);
        ParameterValidUtils.entityExist(processId, manageProcess);
        // 查询所有转发流程
        Process entity = new Process();
        entity.setParentId(processId);
        entity.setStatus(StatusValueEnum.normal.getValue());
        List<Process> forwardList = selectList(new EntityWrapper<>(entity));
        Set<String> forwarderIdList = forwardList.stream().map(Process::getCreateBy).collect(Collectors.toSet());

        List<Notice> list = new ArrayList<>();
        StringBuilder bodyBuilder = new StringBuilder();
        String titleStr;
        String managerRealName = getManagerRealName(manageProcess);
        String createBy;
        if (ProcessTypeEnum.term.name().equals(manageProcess.getType())) {
            String entryId = manageProcess.getEntryId();
            Entry entry = entryBusiness.selectById(entryId);
            if (null == entry) {
                throw new YudaoException(Result.ERROR, "entry不存在");
            }
            createBy = entry.getCreator();

            createNotice("您提交的术语【" + submittedProcess.getTermText() + stateStr, createBy, entryId, null, list);
            for (String accountId : forwarderIdList) {
                createNotice("您评审过的术语【" + submittedProcess.getTermText() + stateStr, accountId, entryId, null, list);
            }

            entry.setState(state);
            entry.setModified(DateUtils.getNow());
            entry.setTbGuid(tbId);



            if (ProcessStateEnum.accepted.getId().equals(state)) {
                titleStr = "【联影词典】新术语已被接受；New terms accepted";
                bodyBuilder.append("您提交的术语已被接受：<br/>The terms have been accepted:").append("<br/>")
                        .append("<br/>")
                        .append("术语信息/Term info：").append(manageProcess.getTermText()).append("<br/>")
                        .append("<br/>")
                        .append("点此<a href=\"").append(webHost).append("/user/selfTerm\">查看详情</a>。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/selfTerm\">here</a> to review the result.").append("<br/>")
                        .append("<br/>")
                        .append("感谢您的支持和贡献！<br/>Thanks for your contribution!");
            }
            else if(ProcessStateEnum.to_back.getId().equals(state)) {
                titleStr = "【联影词典】新术语已被退回；New terms backed";
                bodyBuilder.append("您提交的术语已被退回：<br/>The terms have been backed:").append("<br/>")
                        .append("<br/>")
                        .append("术语信息/Term info：").append(manageProcess.getTermText()).append("<br/>")
                        .append("<br/>")
                        .append("退回理由/Comments：").append(suggestion).append("<br/>")
                        .append("<br/>")
                        .append("点此<a href=\"").append(webHost).append("/user/selfTerm\">查看详情</a>。您可以重新提交术语，或者直接与管理员[").append(managerRealName).append("]联系。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/selfTerm\">here</a> to view details. You can submit the terms again or contact the administrator [").append(managerRealName).append("] for further discussion.").append("<br/>")
                        .append("<br/>")
                        .append("感谢您的支持和贡献！<br/>Thanks for your support!");
            }
            else {
                titleStr = "【联影词典】新术语已被拒绝；New terms rejected";
                bodyBuilder.append("您提交的术语已被拒绝：<br/>The terms have been rejected:").append("<br/>")
                        .append("<br/>")
                        .append("术语信息/Term info：").append(manageProcess.getTermText()).append("<br/>")
                        .append("<br/>")
                        .append("拒绝理由/Comments：").append(suggestion).append("<br/>")
                        .append("<br/>")
                        .append("点此<a href=\"").append(webHost).append("/user/selfTerm\">查看详情</a>。您可以重新提交术语，或者直接与管理员[").append(managerRealName).append("]联系。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/selfTerm\">here</a> to view details. You can submit the terms again or contact the administrator [").append(managerRealName).append("] for further discussion.").append("<br/>")
                        .append("<br/>")
                        .append("感谢您的支持和贡献！<br/>Thanks for your support!");
            }

            if (ProcessStateEnum.accepted.getId().equals(state)){
                List<EntryLang> entryLangList = entryLangBusiness.selectListByEntryId(entryId);
                Termbase tb=termbaseBusiness.selectById(tbId);
                String localIds=tb.getLocalId();
                List<String> localIdList = Arrays.asList(localIds.split(","));
                boolean change=false;
                List<String> elIdList=new ArrayList<>();
                for (EntryLang el:  entryLangList) {
                    elIdList.add(el.getId());
                    if(!localIdList.contains(el.getLanguageId())){
                        if(!change){
                            change=true;
                            if(localIds.endsWith(","))
                            {
                                localIds.substring(0,localIds.length()-1);
                            }
                        }
                        localIds+=String.format(",%s",el.getLanguageId());
                    }
                }
                if(change)
                {
                    tb.setLocalId(localIds);
                    termbaseBusiness.updateAllColumnById(tb);
                }

                List<Item> itemList = itemBusiness.selectList(new EntityWrapper<Item>().in("term_entry_lang_id", elIdList));
                feiShuPluginBusiness.saveFeiShu(entry,entryLangList,itemList,null);
            }

            entryBusiness.updateAllColumnById(entry);
        }
        else if (ProcessTypeEnum.comment.name().equals(manageProcess.getType())) {
            String commentId = manageProcess.getCommentId();
            Comment comment = commentBusiness.selectById(commentId);
            ParameterValidUtils.entityExist(commentId, comment);
            createBy = comment.getCreateBy();
            String publicShowStr;
            if (publicShow) {
                publicShowStr = "，并被公开显示";
            }
            else {
                publicShowStr = "";
            }
            createNotice("您提交的纠错【" + submittedProcess.getCommentText() + stateStr + publicShowStr, createBy, null, commentId, list);
            for (String accountId : forwarderIdList) {
                createNotice("您评审过的纠错【" + submittedProcess.getCommentText() + stateStr + publicShowStr, accountId, accountId, null, list);
            }
            comment.setState(state);
            comment.setPublicShow(publicShow);
            comment.setUpdateTime(DateUtils.getNow());
            String place = commentBusiness.getPlaceStr4Email(comment);
            if (ProcessStateEnum.accepted.getId().equals(state)) {
                titleStr = "【联影词典】纠错已被接受；Suggestion accepted";
                bodyBuilder.append("您提交的纠错已被接受：<br/>The submitted suggestion has been accepted:").append("<br/>")
                        .append("<br/>")
                        .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                        .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                        .append("<br/>")
                        .append("点此<a href=\"").append(webHost).append("/user/selfECTerm\">查看详情</a>。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/selfECTerm\">here</a> to review details.").append("<br/>")
                        .append("<br/>")
                        .append("感谢您的支持和贡献！<br/>Thanks for your contribution!");
            }
            else if (ProcessStateEnum.to_back.getId().equals(state)) {
                titleStr = "【联影词典】纠错已被退回；Suggestion backed";
                bodyBuilder.append("您提交的纠错已被退回：<br/>The submitted suggestion has been rejected:").append("<br/>")
                        .append("<br/>")
                        .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                        .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                        .append("<br/>")
                        .append("退回理由/Comments：").append(suggestion).append("<br/>")
                        .append("<br/>")
                        .append("点此<a href=\"").append(webHost).append("/user/selfECTerm\">查看详情</a>。您可以重新提交纠错，或者直接与管理员[").append(managerRealName).append("]联系。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/selfECTerm\">here</a> to view details. You can submit the suggestion again or contact the administrator [").append(managerRealName).append("] for further discussion.").append("<br/>")
                        .append("<br/>")
                        .append("感谢您的支持和贡献！<br/>Thanks for your support!");
            }
            else {
                titleStr = "【联影词典】纠错已被拒绝；Suggestion rejected";
                bodyBuilder.append("您提交的纠错已被拒绝：<br/>The submitted suggestion has been rejected:").append("<br/>")
                        .append("<br/>")
                        .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                        .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                        .append("<br/>")
                        .append("拒绝理由/Comments：").append(suggestion).append("<br/>")
                        .append("<br/>")
                        .append("点此<a href=\"").append(webHost).append("/user/selfECTerm\">查看详情</a>。您可以重新提交纠错，或者直接与管理员[").append(managerRealName).append("]联系。").append("<br/>")
                        .append("Please click <a href=\"").append(webHost).append("/user/selfECTerm\">here</a> to view details. You can submit the suggestion again or contact the administrator [").append(managerRealName).append("] for further discussion.").append("<br/>")
                        .append("<br/>")
                        .append("感谢您的支持和贡献！<br/>Thanks for your support!");
            }
            commentBusiness.updateAllColumnById(comment);
        }
        else {
            throw new YudaoException(Result.ERROR, "流程类型不符");
        }
        forwardList.add(manageProcess); // 借用转发流程集合装一下管理员流程，最后用来批量修改
        forwardList.add(submittedProcess); // 借用转发流程集合装一下提交流程，最后用来批量修改
        forwardList.forEach(obj -> obj.setState(state)); // 所有流程的状态和最终评审状态相同
        forwardList.forEach(obj -> obj.setUpdateTime(DateUtils.getNow()));
        updateAllColumnBatchById(forwardList);
        noticeBusiness.insertBatch(list);
        String userEmail = accountBusiness.getUserEmail(createBy);
        if (StringUtils.isNotBlank(userEmail)) {
            MailUtils.asynSendMail(userEmail, null, titleStr, bodyBuilder.toString(), null);
        }
    }

    private String getManagerRealName(Process manageProcess) {
        String managerId = manageProcess.getCreateBy();
        Account manager = accountBusiness.selectById(managerId);
        String managerRealName = "";
        if (null != manager){
            if(StringUtils.isNotBlank(manager.getRealname())){
                managerRealName = manager.getRealname();
            }
            else if(StringUtils.isNotBlank(manager.getLoginName())){
                managerRealName = manager.getLoginName();
            }

        }
        return managerRealName;
    }

    private void createNotice(String content, String accountId, String entryId, String commentId, List<Notice> list) {
        Notice notice = new Notice();
        notice.setId(CommonUtils.uuid());
        notice.setCreateBy(accountId);
        notice.setEntryId(entryId);
        notice.setCommentId(commentId);
        notice.setName(content);
        notice.setContent(content);
        list.add(notice);
    }

    public Object processHistory(String processId) {
        String typeTerm = "术语";
        String typeComment = "纠错";
        String typeForward = "转发";
        String typeForwardManage = "转发评审";
        String typeManage = "评审";
        String managerStr = "（管理员）";

        Process process = selectById(processId);
        ParameterValidUtils.entityExist(processId, process);
        Process whereEntity = new Process();
        whereEntity.setStatus(StatusValueEnum.normal.getValue());
        List<Process> sumbitProcessList;
        List<Process> manageProcessList;
        List<Process> forwordProcessList;

        if (ProcessTypeEnum.term.name().equals(process.getType())) {
            String entryId = process.getEntryId();
            if (StringUtils.isBlank(entryId)) {
                throw new YudaoException(Result.ERROR, "流程历史数据错误");
            }
            sumbitProcessList = selectList(new EntityWrapper<>(whereEntity)
                    .isNull(FieldNameEnum.parent_id.name())
                    .eq(ModuleFieldNameEnum.entry_id.name(), entryId)
                    .eq("type","term"));
        }
        else if (ProcessTypeEnum.comment.name().equals(process.getType())) {
            String commentId = process.getCommentId();
            if (StringUtils.isBlank(commentId)) {
                throw new YudaoException(Result.ERROR, "流程历史数据错误");
            }
            sumbitProcessList = selectList(new EntityWrapper<>(whereEntity)
                    .isNull(FieldNameEnum.parent_id.name())
                    .eq(ModuleFieldNameEnum.comment_id.name(), commentId)
                    .eq("type","comment"));
        }
        else {
            throw new YudaoException(Result.ERROR, "流程类型错误");
        }
        if (sumbitProcessList.size() != 1) {
            throw new YudaoException(Result.ERROR, "提交术语/纠错的流程错误");
        }
        Process sumbitProcess = sumbitProcessList.get(0);
        String sumbitProcessId = sumbitProcess.getId();
        manageProcessList = selectList(new EntityWrapper<>(whereEntity).eq(FieldNameEnum.parent_id.name(), sumbitProcessId));
        if (sumbitProcessList.size() != 1) {
            throw new YudaoException(Result.ERROR, "管理审批流程错误");
        }
        Process manageProcess = manageProcessList.get(0);
        String manageProcessId = manageProcess.getId();
        Account account = accountBusiness.selectById(manageProcess.getCreateBy());
        String loginName = null != account ? account.getLoginName() : "";
        loginName = loginName + managerStr;
        forwordProcessList = selectList(new EntityWrapper<>(whereEntity)
                .eq(FieldNameEnum.parent_id.name(), manageProcessId)
                .orderBy(FieldNameEnum.create_time.name(),false));

        JSONArray result = new JSONArray();
        if (ProcessStateEnum.refused.getId().equals(manageProcess.getState()) || ProcessStateEnum.accepted.getId().equals(manageProcess.getState())) {
            String manageDetail;
            if (ProcessStateEnum.refused.getId().equals(manageProcess.getState())) {
                manageDetail = "拒绝";
            }
            else {
                manageDetail = "接受";
            }
            String suggestion = manageProcess.getSuggestion();
            if (StringUtils.isNotBlank(suggestion)) {
                manageDetail = manageDetail + "：" + suggestion;
            }
            ProcessHistoryDTO processHistoryDTO = new ProcessHistoryDTO(typeManage, manageDetail, loginName, manageProcess.getUpdateTime());
            result.add(processHistoryDTO);
        }

        for (Process fp : forwordProcessList) {
            ProcessHistoryDTO processHistoryDTO = new ProcessHistoryDTO(typeForward, "[转发给" + fp.getCreateBy() + "]：" + fp.getRemark(), loginName, fp.getCreateTime());
            result.add(processHistoryDTO);
            if (!ProcessStateEnum.to_process.getId().equals(fp.getState())) {
                ProcessHistoryDTO processHistoryDTO2 = new ProcessHistoryDTO(typeForwardManage, fp.getSuggestion(), fp.getCreateBy(), fp.getUpdateTime());
                result.add(processHistoryDTO2);
            }
        }
        if (ProcessTypeEnum.term.name().equals(process.getType())) {
            result.add(new ProcessHistoryDTO(typeTerm, sumbitProcess.getTermText(), sumbitProcess.getCreateBy(), sumbitProcess.getCreateTime()));
        }
        else {
            result.add(new ProcessHistoryDTO(typeComment, commentBusiness.selectById(sumbitProcess.getCommentId()), sumbitProcess.getCreateBy(), sumbitProcess.getCreateTime()));
        }
        return result;
    }

    public Object processHistory4Comment(String commentId) {
        String typeComment = "纠错";
        String typeForward = "转发";
        String typeForwardManage = "转发评审";
        String typeManage = "评审";
        String managerStr = "（管理员）";
        Process whereEntity = new Process();
        whereEntity.setStatus(StatusValueEnum.normal.getValue());
        List<Process> sumbitProcessList = selectList(new EntityWrapper<>(whereEntity).isNull(FieldNameEnum.parent_id.name()).eq(ModuleFieldNameEnum.comment_id.name(), commentId));
        if (sumbitProcessList.size() != 1) {
            throw new YudaoException(Result.ERROR, "提交纠错的流程错误");
        }
        Process sumbitProcess = sumbitProcessList.get(0);
        String sumbitProcessId = sumbitProcess.getId();
        List<Process> manageProcessList = selectList(new EntityWrapper<>(whereEntity).eq(FieldNameEnum.parent_id.name(), sumbitProcessId));
        if (sumbitProcessList.size() != 1) {
            throw new YudaoException(Result.ERROR, "管理审批流程错误");
        }
        Process manageProcess = manageProcessList.get(0);
        String manageProcessId = manageProcess.getId();
        Account account = accountBusiness.selectById(manageProcess.getCreateBy());
        String loginName = null != account ? account.getLoginName() : "";
        loginName = loginName + managerStr;
        List<Process> forwordProcessList = selectList(new EntityWrapper<>(whereEntity).eq(FieldNameEnum.parent_id.name(), manageProcessId).orderBy(FieldNameEnum.create_time.name()));

        JSONArray result = new JSONArray();
        result.add(new ProcessHistoryDTO(typeComment, commentBusiness.selectById(sumbitProcess.getCommentId()), sumbitProcess.getCreateBy(), sumbitProcess.getCreateTime()));
        for (Process fp : forwordProcessList) {
            ProcessHistoryDTO processHistoryDTO = new ProcessHistoryDTO(typeForward, "[转发给" + fp.getCreateBy() + "]：" + fp.getRemark(), loginName, fp.getCreateTime());
            result.add(processHistoryDTO);
            if (!ProcessStateEnum.to_process.getId().equals(fp.getState())) {
                ProcessHistoryDTO processHistoryDTO2 = new ProcessHistoryDTO(typeForwardManage, fp.getSuggestion(), fp.getCreateBy(), fp.getUpdateTime());
                result.add(processHistoryDTO2);
            }
        }
        if (ProcessStateEnum.refused.getId().equals(manageProcess.getState()) || ProcessStateEnum.accepted.getId().equals(manageProcess.getState())) {
            String manageDetail;
            if (ProcessStateEnum.refused.getId().equals(manageProcess.getState())) {
                manageDetail = "拒绝";
            }
            else {
                manageDetail = "接受";
            }
            String suggestion = manageProcess.getSuggestion();
            if (StringUtils.isNotBlank(suggestion)) {
                manageDetail = manageDetail + "：" + suggestion;
            }
            ProcessHistoryDTO processHistoryDTO = new ProcessHistoryDTO(typeManage, manageDetail, loginName, manageProcess.getUpdateTime());
            result.add(processHistoryDTO);
        }
        return result;
    }

    public Object pageMySubmittedTerms(Page<Object> page, String keyword, Date beginTime, Date endTime, String orderBy, String field, String state, String account) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        state = CommonUtils.quotesSeparatedStr(state);
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        return page.setRecords(baseMapper.pageMySubmittedTerms(page, keyword, beginTime, endTime, orderBy, fieldList, state, account));
    }

    public Object pageMySubmittedComment(Page<Object> page, String keyword, Date beginTime, Date endTime, String orderBy, String field, String place, String state, String account) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        state = CommonUtils.quotesSeparatedStr(state);
        place = CommonUtils.quotesSeparatedStr(place);
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        return page.setRecords(baseMapper.pageMySubmittedComment(page, keyword, beginTime, endTime, orderBy, fieldList, place, state, account));
    }

    public Object pageMyForwardTerms(Page<Object> page, String keyword, Date beginTime, Date endTime, String orderBy, String field, String state, String account, String creator) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        creator = CommonUtils.quotesSeparatedStr(creator);
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        return page.setRecords(baseMapper.pageMyForwardTerms(page, keyword, beginTime, endTime, orderBy, fieldList, state, account, creator));
    }

    public Object pageMyForwardComment(Page<Object> page, String keyword, Date beginTime, Date endTime, String orderBy, String field, String place, String state, String account, String creator) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        place = CommonUtils.quotesSeparatedStr(place);
        creator = CommonUtils.quotesSeparatedStr(creator);
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        return page.setRecords(baseMapper.pageMyForwardComment(page, keyword, beginTime, endTime, orderBy, fieldList, place, state, account, creator));
    }

    public Object pageManagerTerms(Page<TermProcessManageDTO> page, String keyword, Date beginTime, Date endTime, String orderBy, String field, String state, String account, String creator) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        state = CommonUtils.quotesSeparatedStr(state);
        creator = CommonUtils.quotesSeparatedStr(creator);
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);


        List<TermProcessManageDTO> processManageDTOS = baseMapper.pageManagerTerms(page, keyword, beginTime, endTime, orderBy, fieldList, state, account, creator);
        List<String> processIdList = processManageDTOS.stream().map(p -> p.getId()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(processIdList)) {
            List<TermProcessManageDTO> forwardProcessTermDTOS = baseMapper.getForwardProcessTerm(processIdList);
            Map<String, List<TermProcessManageDTO>> stringListMap = forwardProcessTermDTOS.stream().collect(Collectors.groupingBy(TermProcessManageDTO::getParentId));
            processManageDTOS.forEach(p -> {
                if (!CollectionUtils.isEmpty(stringListMap.get(p.getId()))) {
                    p.setForwardProcessList(stringListMap.get(p.getId()));
                }
            });
        }
        return page.setRecords(processManageDTOS);
    }

    public Object pageManagerComment(Page<CommentProcessManageDTO> page, String keyword, Date beginTime, Date endTime, String orderBy, String field, String place, String state, String account, String creator) {
        keyword = CommonUtils.blankToEmpty(keyword);
        orderBy = CommonUtils.blankToEmpty(orderBy);
        state = CommonUtils.quotesSeparatedStr(state);
        creator = CommonUtils.quotesSeparatedStr(creator);
        place = CommonUtils.quotesSeparatedStr(place);
        ParameterValidUtils.beginTimeBeforeEqualsEndTime(beginTime, endTime);
        endTime = DateUtils.tomorrowStartDate(endTime);
        List<String> fieldList = CommonUtils.splitByCommaToListUnique(field);
        List<CommentProcessManageDTO> processManageDTOS = baseMapper.pageManagerComment(page, keyword, beginTime, endTime, orderBy, fieldList, place, state, account, creator);
        List<String> processIdList = processManageDTOS.stream().map(p -> p.getId()).collect(Collectors.toList());
        List<CommentProcessManageDTO> forwardProcessCommentDTOS = baseMapper.getForwardProcessComment(processIdList);
        Map<String, List<CommentProcessManageDTO>> stringListMap = forwardProcessCommentDTOS.stream().collect(Collectors.groupingBy(CommentProcessManageDTO::getParentId));
        processManageDTOS.forEach(p->{
            if (!CollectionUtils.isEmpty(stringListMap.get(p.getId()))) {
                p.setForwardProcessList(stringListMap.get(p.getId()));
            }
        });
        return page.setRecords(processManageDTOS);
    }

    public Object contributeTermList() {
        return baseMapper.contributeTermList();
    }

    public Object myUnDealedTermNum(String account) {
        return baseMapper.myUnDealedTermNum(account);
    }

    public Object myUnDealedCommentNum(String account) {
        return baseMapper.myUnDealedCommentNum(account);
    }

    public Object getAllCreatorByAccount(String accountId, String type) {
        return baseMapper.getAllCreatorByAccount(accountId, type);
    }

    public Object getManagerAllCreator(String accountId, String type) {
        return baseMapper.getManagerAllCreator(accountId, type);
    }

    public Object getNewTerms(Integer num) {
        return baseMapper.getNewTerms(ProcessTypeEnum.term.name(), ProcessStateEnum.accepted.getId(), num);
    }

    public void unprocessed() {
        Process entity = new Process();
        entity.setStatus(StatusValueEnum.normal.getValue());
        entity.setState(ProcessStateEnum.to_process.getId());
        long begin = DateUtils.presentStartTimestamp(System.currentTimeMillis()) - 3600 * 24 * 15 * 1000;
        long end = DateUtils.presentStartTimestamp(System.currentTimeMillis()) - 3600 * 24 * 14 * 1000;
        List<Process> processes = selectList(new EntityWrapper<>(entity).gt("update_time", begin).le("update_time", end));
        for (Process process : processes) {
            String createBy = process.getCreateBy();
            if (StringUtils.isBlank(createBy)) {
                log.error("数据错误，流程缺少创建人，processId: " + createBy);
            }
            else {
                Account manager = accountBusiness.selectById(createBy);
                if (null == manager) {
                    // 被转发评审的流程
                    String parentId = process.getParentId();
                    Process mProcess = selectById(parentId);
                    if (null == mProcess) {
                        log.error("数据错误，缺少管理员流程，processId: " + parentId);
                    }
                    else {
                        manager = accountBusiness.selectById(mProcess.getCreateBy());
                        if (null == manager) {
                            log.error("数据错误，管理员流程缺少管理员，processId: " + parentId);
                        }
                        else {
                            Process manageProcess = new Process();
                            manageProcess.setId(parentId);
                            manageProcess.setState(ProcessStateEnum.to_process.getId());
                            updateById(manageProcess);
                            Process updateProcess = new Process();
                            updateProcess.setId(process.getId());
                            updateProcess.setState(ProcessStateEnum.approved.getId());
                            updateById(updateProcess);
                        }
                    }
                }
                else {
                    // 管理员的流程
                    Notice notice = new Notice();
                    String content;
                    if (StringUtils.isBlank(process.getCommentId())) {
                        notice.setEntryId(process.getEntryId());
                        content = "您有超过14天未处理的术语【" + process.getTermText() + "】，请及时处理";
                    }
                    else {
                        notice.setCommentId(process.getCommentId());
                        content = "您有超过14天未处理的纠错【" + process.getCommentText() + "】，请及时处理";
                    }
                    notice.setId(CommonUtils.uuid());
                    notice.setName(content);
                    notice.setContent(content);
                    notice.setCreateBy(process.getCreateBy());
                    noticeBusiness.insert(notice);
                }

                if (null != manager) {
                    String titleStr = null;
                    StringBuilder bodyBuilder = new StringBuilder();
                    if (StringUtils.isBlank(process.getCommentId())) {
                        titleStr = "【联影词典】待处理流程提醒（新术语）；Reminder of review process (new terms)";
                        bodyBuilder.append("新术语流程已超过14天未处理。<br/>")
                                .append("There has been no update on the review process of these new terms for 14 days.<br/>")
                                .append("<br/>")
                                .append("术语信息/Term info：").append(process.getTermText()).append("<br/>")
                                .append("<br/>")
                                .append("请您及时处理流程或重新转发流程。点此<a href=\"").append(webHost).append("/manage/process/unDeal\">查看详情</a>。").append("<br/>")
                                .append("Please click <a href=\"").append(webHost).append("/manage/process/unDeal\">here</a> to review the terms or assign the task to someone else.");
                    }
                    else {
                        String commentId = process.getCommentId();
                        Comment comment = commentBusiness.selectById(commentId);
                        if (null == comment) {
                            log.error("数据错误，缺少纠错，commentId: " + commentId);
                        }
                        else {
                            String place = commentBusiness.getPlaceStr4Email(comment);
                            titleStr = "【联影词典】待处理流程提醒（纠错）；Reminder of review process (suggestion)";
                            bodyBuilder.append("纠错流程已超过14天未处理。<br/>")
                                    .append("There has been no update on the review process of this suggestion for 14 days.\n<br/>")
                                    .append("<br/>")
                                    .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                                    .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                                    .append("<br/>")
                                    .append("请您及时处理流程或重新转发流程。点此<a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=2\">查看详情</a>。").append("<br/>")
                                    .append("Please click <a href=\"").append(webHost).append("/user/selfECTerm\">here</a> to review the suggestion or assign the task to someone else.").append("<br/>");

                        }
                    }
                    if (null != titleStr) {
                        log.info("发邮件给管理员，邮箱：" + manager.getEmail());
                        MailUtils.asynSendMail(manager.getEmail(), null, titleStr, bodyBuilder.toString(), null);
                    }
                }
            }
        }
    }
}
