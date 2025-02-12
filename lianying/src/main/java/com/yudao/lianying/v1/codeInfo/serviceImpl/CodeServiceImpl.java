package com.yudao.lianying.v1.codeInfo.serviceImpl;

import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.codeInfo.api.CodeServiceApi;
import com.yudao.lianying.v1.codeInfo.biz.CodeInfoBusiness;
import com.yudao.lianying.v1.codeInfo.vo.CodeInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liudong
 * @since 2019-06-13
 */
@Service
@RestController
@Slf4j
public class CodeServiceImpl implements CodeServiceApi {
    @Autowired
    private CodeInfoBusiness codeInfoBusiness;

    @Override
    public Result save(CodeInfoVO entityVO) {
        try {
            return ResultUtil.success(codeInfoBusiness.save(entityVO));
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
            codeInfoBusiness.removeByIdAuto(ids);
            return ResultUtil.success();
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    public Result selectAllCodeByKindCode(String codeKindCode) {
        try {
            return ResultUtil.success(codeInfoBusiness.selectAllCodeByKindCode(codeKindCode));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result selectCodeKindByKindId(String codeKindId) {
        try {
            return ResultUtil.success(codeInfoBusiness.selectCodeKindByKindId(codeKindId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result selectCodeKindByKindCode(String codeKindCode) {
        try {
            return ResultUtil.success(codeInfoBusiness.selectCodeKindByKindCode(codeKindCode));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }

    @Override
    public Result selectCodeInfoByInfoId(String codeInfoId) {
        try {
            return ResultUtil.success(codeInfoBusiness.selectCodeInfoByInfoId(codeInfoId));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }


}
