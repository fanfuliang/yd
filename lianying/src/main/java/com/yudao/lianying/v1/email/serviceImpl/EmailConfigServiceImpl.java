package com.yudao.lianying.v1.email.serviceImpl;

import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.PageParam2;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.utils.ModifyVO;
import com.yudao.lianying.v1.email.api.EmailConfigServiceApi;
import com.yudao.lianying.v1.email.biz.EmailConfigBusiness;
import com.yudao.lianying.v1.email.vo.EmailConfigModifyVO;
import com.yudao.lianying.v1.email.vo.EmailConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liudong
 * @since 2021-10-11
 */
@RestController
@Slf4j
public class EmailConfigServiceImpl implements EmailConfigServiceApi {

    @Autowired
    private EmailConfigBusiness emailConfigBusiness;

    @Override
    public Result saveAuto(EmailConfigVO entityVO) {
        try {
            return ResultUtil.success(emailConfigBusiness.saveAuto(entityVO));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result removeByIdAuto(String ids) {
        try {
            emailConfigBusiness.removeByIdAuto(ids);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result modifyByIdAuto(EmailConfigModifyVO modifyVO) {
        try {
            emailConfigBusiness.modifyByIdAuto(modifyVO);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result modifyBatchByIdAuto(@RequestBody List<ModifyVO> modifyVOList) {
        try {
            emailConfigBusiness.modifyBatchByIdAuto(modifyVOList);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result getByIdAuto(String id) {
        try {
            return ResultUtil.success(emailConfigBusiness.getByIdAuto(id));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result page(@RequestBody PageParam2 pageParam) {
        try {
            return ResultUtil.success(emailConfigBusiness.page(pageParam));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
