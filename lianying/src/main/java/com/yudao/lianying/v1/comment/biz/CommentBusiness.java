package com.yudao.lianying.v1.comment.biz;

import com.alibaba.fastjson.JSONObject;
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
import com.yudao.lianying.utils.CommentPlaceEnum;
import com.yudao.lianying.utils.MailUtils;
import com.yudao.lianying.utils.ProcessStateEnum;
import com.yudao.lianying.utils.ProcessTypeEnum;
import com.yudao.lianying.v1.account.biz.AccountBusiness;
import com.yudao.lianying.v1.account.dao.Account;
import com.yudao.lianying.v1.comment.dao.Comment;
import com.yudao.lianying.v1.comment.dao.mapper.CommentMapper;
import com.yudao.lianying.v1.comment.vo.CommentBathVO;
import com.yudao.lianying.v1.comment.vo.CommentModifyVO1;
import com.yudao.lianying.v1.comment.vo.CommentSmallVO;
import com.yudao.lianying.v1.comment.vo.CommentVO;
import com.yudao.lianying.v1.field.biz.FieldBusiness;
import com.yudao.lianying.v1.process.biz.ProcessBusiness;
import com.yudao.lianying.v1.process.dao.Process;
import com.yudao.lianying.v1.term.biz.EntryBusiness;
import com.yudao.lianying.v1.term.dao.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 纠错表 服务类
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
@Service
@Slf4j
public class CommentBusiness extends ServiceImpl<CommentMapper, Comment> {

    @Autowired
    private FieldBusiness fieldBusiness;

    @Autowired
    private ProcessBusiness processBusiness;

    @Autowired
    private AccountBusiness accountBusiness;

    @Autowired
    private EntryBusiness entryBusiness;

    @Value("${web.host}")
    private String webHost;

    @Transactional
    public Object save(CommentVO entityVO) {
        return saveOne(entityVO);
    }


    public Object saveOne(CommentVO entityVO) {
        Comment comment = CommonUtils.copyPropertiesAndCreateId(entityVO, Comment.class);
        Entry entry = entryBusiness.selectById(comment.getEntryId());

        comment.setState(ProcessStateEnum.to_process.getId());

        comment.setTermText(entryBusiness.getTermText(comment.getEntryId()));
        comment.setField(entry.getFieldName());

        Process submitted = new Process();
        submitted.setId(CommonUtils.uuid());
        submitted.setType(ProcessTypeEnum.comment.name());
        submitted.setState(ProcessStateEnum.to_approve.getId());
        submitted.setCommentId(comment.getId());
        submitted.setEntryId(comment.getEntryId());
        submitted.setField(entry.getField());
        submitted.setFieldName(entry.getFieldName());
        submitted.setTermText(comment.getTermText());

        submitted.setPlace(comment.getPlace());
        submitted.setCommentText(comment.getText());
        submitted.setCreateBy(comment.getCreateBy());
        Set<String> managerIdList = fieldBusiness.getManagerId(comment.getField());
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
                    if (false) {
                        String titleStr = "【联影词典】新的纠错意见待评审；Submitted suggestion to be reviewed";
                        String place = getPlaceStr4Email(comment);
                        StringBuilder bodyBuilder = new StringBuilder();
                        bodyBuilder.append("用户[").append(comment.getCreateBy()).append("]已为术语添加纠错意见：").append("<br/>")
                                .append("The user [").append(comment.getCreateBy()).append("] has suggested an edit for a term entry:").append("<br/>")
                                .append("<br/>")
                                .append("<table border=\"1px\" style=\"border-collapse: collapse;\"><tr><td>术语信息/Term info</td><td>").append(comment.getTermText()).append("</td></tr><tr><td>错误位置/Type</td><td>").append(place).append("</td></tr><tr><td>问题/Problem</td><td>")
                                .append(comment.getText()).append("</td></tr><tr><td>建议/Suggestion</td><td>").append(comment.getAdvice()).append("</td></tr></table>").append("<br/>")
                                .append("<br/>")
                                .append("请及时查看、处理。点此<a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=2\">查看详情</a>。").append("<br/>")
                                .append("Please click <a href=\"").append(webHost).append("/manage/process/unDeal?tabIndex=2\">here</a> to review the result.");
                        MailUtils.asynSendMail(email, cc, titleStr, bodyBuilder.toString(), null);
                    }
                    processBusiness.insert(managerProcess);
                }
            }

        }
        processBusiness.insert(submitted);
        if (insert(comment)) {
            return comment;
        }
        else {
            throw new YudaoException(Result.ERROR, "Failed to save");
        }
    }

    @Transactional
    public void saveBatch(CommentBathVO entityVO) {
        for (CommentSmallVO o : entityVO.getCommentVOList()) {
            CommentVO commentVO = new CommentVO();
            commentVO.setCreateBy(entityVO.getCreateBy());
            commentVO.setEntryId(entityVO.getEntryId());
            commentVO.setState(entityVO.getState());
            commentVO.setText(o.getText());
            commentVO.setPlace(o.getPlace());
            //commentVO.setQuestion(o.getQuestion());
            //commentVO.setLanguage(entityVO.getLanguage());
            commentVO.setAdvice(o.getAdvice());
            saveOne(commentVO);
        }
    }

    public String getPlaceStr4Email(Comment comment) {
        String place = "";
        if (CommentPlaceEnum.entry.getId().equals(comment.getPlace())) {
            place = "词条/Entry";
        }
        else if (CommentPlaceEnum.example.getId().equals(comment.getPlace())) {
            place = "例句/Example";
        }
        else if (CommentPlaceEnum.note.getId().equals(comment.getPlace())) {
            place = "备注/Notes";
        }
        return place;
    }


    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        List<Comment> list = new ArrayList<>();
        for (String id : set) {
            Comment entity = new Comment(id, StatusValueEnum.deleted.getValue());
            list.add(entity);
        }
        if (!updateBatchById(list)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public void modifyAllColumnInObjectAuto(CommentModifyVO1 modifyVO1) {
        Comment entity = selectById(modifyVO1.getId());
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
        Comment entity = selectById(id);
        if (null == entity || !StatusValueEnum.normal.getValue().equals(entity.getStatus())) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }

    public Object pageAuto(Page<Comment> page, String keyword, String[] filterFieldNameArray, String[] filterFieldValueArray, String orderBy) {
        EntityWrapper<Comment> entityWrapper = new EntityWrapper<>();
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

    public List<Comment> getCommentByEntry(String entryId) {
        Comment comment = new Comment();
        comment.setEntryId(entryId);
        comment.setPublicShow(true);
        comment.setStatus(StatusValueEnum.normal.getValue());
        return selectList(new EntityWrapper<>(comment).orderBy(FieldNameEnum.create_time.name()));
    }

    public Object tempConfigInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("webHost", webHost);
        jsonObject.put("mailPsw", MailUtils.getPassword());
        jsonObject.put("mailUsername", MailUtils.getUsername());
        jsonObject.put("fromMail", MailUtils.getFromMail());
        jsonObject.put("mailHost", MailUtils.getMailhost());
        jsonObject.put("mailPort", MailUtils.getMailPort());
        jsonObject.put("mailNickname", MailUtils.getNick());
        jsonObject.put("memoqHost", accountBusiness.getMemoqHost());
        jsonObject.put("memoqUsername", accountBusiness.getMemoqUsername());
        jsonObject.put("memoqPassword", accountBusiness.getMemoqPassword());
        return jsonObject;
    }
}
