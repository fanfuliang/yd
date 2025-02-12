package com.yudao.lianying.v1.process.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yudao.lianying.v1.process.dao.Process;
import com.yudao.lianying.v1.process.dto.CommentProcessManageDTO;
import com.yudao.lianying.v1.process.dto.TermProcessManageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程表 Mapper 接口
 * </p>
 *
 * @author liudong
 * @since 2020-12-12
 */
public interface ProcessMapper extends BaseMapper<Process> {

    void modifyByIdAuto(@Param("idList") List<String> idList, @Param("modifyFieldNameArray") String[] modifyFieldNameArray, @Param("modifyFieldValueArray") String[] modifyFieldValueArray);

    List<Object> pageMySubmittedTerms(Page<Object> page, @Param("keyword") String keyword, @Param("beginTime") Date beginTime,
                                      @Param("endTime") Date endTime, @Param("orderBy") String orderBy, @Param("fieldList") List<String> fieldList,
                                      @Param("state") String state, @Param("account") String account);

    List<Object> pageMySubmittedComment(Page<Object> page, @Param("keyword") String keyword, @Param("beginTime") Date beginTime,
                                        @Param("endTime") Date endTime, @Param("orderBy") String orderBy, @Param("fieldList") List<String> fieldList,
                                        @Param("place") String place, @Param("state") String state, @Param("account") String account);

    List<Object> pageMyForwardTerms(Page<Object> page, @Param("keyword") String keyword, @Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime, @Param("orderBy") String orderBy, @Param("fieldList") List<String> fieldList,
                                    @Param("state") String state, @Param("account") String account,@Param("creator") String creator);

    List<Object> pageMyForwardComment(Page<Object> page, @Param("keyword") String keyword, @Param("beginTime") Date beginTime,
                                      @Param("endTime") Date endTime, @Param("orderBy") String orderBy, @Param("fieldList") List<String> fieldList,
                                      @Param("place") String place, @Param("state") String state, @Param("account") String account,@Param("creator") String creator);

    List<TermProcessManageDTO> pageManagerTerms(Page<TermProcessManageDTO> page, @Param("keyword") String keyword, @Param("beginTime") Date beginTime,
                                  @Param("endTime") Date endTime, @Param("orderBy") String orderBy, @Param("fieldList") List<String> fieldList,
                                  @Param("state") String state, @Param("account") String account,@Param("creator") String creator);

    List<TermProcessManageDTO> getForwardProcessTerm(@Param("processIdList") List<String> processIdList);

    List<CommentProcessManageDTO> pageManagerComment(Page<CommentProcessManageDTO> page, @Param("keyword") String keyword, @Param("beginTime") Date beginTime,
                                                     @Param("endTime") Date endTime, @Param("orderBy") String orderBy, @Param("fieldList") List<String> fieldList,
                                                     @Param("place") String place, @Param("state") String state, @Param("account") String account, @Param("creator") String creator);

    List<CommentProcessManageDTO> getForwardProcessComment(@Param("processIdList") List<String> processIdList);

    List<Object> contributeTermList();

    Integer myUnDealedCommentNum(@Param("account") String account);

    Integer myUnDealedTermNum(@Param("account") String account);

    List<Object> getNewTerms(@Param("type") String type, @Param("state") String state, @Param("num") Integer num);

    List<Map<String, Object>> getAllCreatorByAccount(@Param("accountId") String accountId, @Param("type") String type);
    List<Map<String, Object>> getManagerAllCreator(@Param("accountId") String accountId, @Param("type") String type);
}
