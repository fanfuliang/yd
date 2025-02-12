package com.yudao.lianying.v1.basicConfig.serviceImpl;

import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.model.page.PageParam2;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.basicConfig.api.SysConfigServiceApi;
import com.yudao.lianying.v1.basicConfig.biz.SysConfigBusiness;
import com.yudao.lianying.v1.basicConfig.vo.SysConfigVO;
import com.yudao.lianying.v1.basicConfig.vo.SysConfigModifyVO;
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
 * @author fanfl
 * @since 2024-04-06
 */
@RestController
@Slf4j
public class SysConfigServiceImpl implements SysConfigServiceApi {

    @Autowired
    private SysConfigBusiness sysConfigBusiness;

    @Override
    public Result save(SysConfigVO entityVO) {
        try {
            return ResultUtil.success(sysConfigBusiness.save(entityVO));
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
            sysConfigBusiness.removeByIdAuto(ids);
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
    public Result modifyByIdAuto(SysConfigModifyVO modifyVO) {
        try {
            sysConfigBusiness.modifyByIdAuto(modifyVO);
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
            return ResultUtil.success(sysConfigBusiness.getByIdAuto(id));
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
            return ResultUtil.success(sysConfigBusiness.page(pageParam));
        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
