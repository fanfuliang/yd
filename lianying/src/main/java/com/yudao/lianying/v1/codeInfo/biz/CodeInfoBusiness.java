package com.yudao.lianying.v1.codeInfo.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.CommonUtils;
import com.yudao.common.yudaocommon.utils.DateUtils;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.v1.codeInfo.dao.CodeInfo;
import com.yudao.lianying.v1.codeInfo.dao.CodeKind;
import com.yudao.lianying.v1.codeInfo.dao.mapper.CodeInfoMapper;
import com.yudao.lianying.v1.codeInfo.dto.CodeDTO;
import com.yudao.lianying.v1.codeInfo.vo.CodeInfoVO;
import com.yudao.lianying.v1.field.dao.ApplicableTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author
 * @since 2020-07-06
 */
@Service
@Slf4j
public class CodeInfoBusiness extends ServiceImpl<CodeInfoMapper, CodeInfo> {

    @Autowired
    private CodeKindBusiness codeKindBusiness;

    public CodeInfo save(CodeInfoVO entityVO) {
        CodeInfo entity = CommonUtils.copyPropertiesAndCreateId(entityVO, CodeInfo.class);
        entity.setSort("1000");
        entity.setValue(entityVO.getName());
        entity.setValueEn(entityVO.getName());
        entity.setValueZh(entityVO.getName());
        entity.setZhName(entityVO.getName());
        entity.setEnName(entityVO.getName());
        if (StringUtils.isNotBlank(entityVO.getId())) {//编辑
            CodeInfo oldData = selectById(entityVO.getId());
            entity.setId(oldData.getId());
            updateById(entity);
        }
        else {
            insert(entity);
        }
        return entity;
    }

    public void removeByIdAuto(String ids) {
        List<String> set = CommonUtils.splitToListUnique(ids, ",");
        if (!this.deleteBatchIds(set)) {
            throw new YudaoException(Result.ERROR, "Failed to remove");
        }
    }

    public CodeKind selectCodeKindByKindId(String codeKindId) {
        return codeKindBusiness.selectById(codeKindId);
    }

    public CodeKind selectCodeKindByKindCode(String codeKindCode) {
        CodeKind codeKind = new CodeKind();
        codeKind.setCode(codeKindCode);
        return codeKindBusiness.selectOne(new EntityWrapper<>(codeKind));
    }

    public CodeInfo selectCodeInfoByInfoId(String codeInfoId) {
        return selectById(codeInfoId);
    }

    public CodeInfo selectCodeInfoByBothCode(String codeInfoCode, String codeKindCode) {
        CodeKind codeKindParam = new CodeKind();
        codeKindParam.setCode(codeKindCode);
        CodeKind codeKind = codeKindBusiness.selectOne(new EntityWrapper<>(codeKindParam));
        if (null == codeKind) {
            throw new YudaoException(Result.ERROR, "未查询到codeKind");
        }
        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setCode(codeInfoCode);
        codeInfo.setCodeKindId(codeKind.getId());
        codeInfo = selectOne(new EntityWrapper<>(codeInfo));
        return codeInfo;
    }

    public Object selectAllCodeByKindCode(String codeKindCode) {
        CodeKind codeKindParam = new CodeKind();
        codeKindParam.setCode(codeKindCode);
        CodeKind codeKind = codeKindBusiness.selectOne(new EntityWrapper<>(codeKindParam));
        if (null != codeKind) {
            CodeInfo codeInfoParam = new CodeInfo();
            codeInfoParam.setCodeKindId(codeKind.getId());
            Wrapper<CodeInfo> wrapper = new EntityWrapper<>(codeInfoParam).orderBy("sort");
            List<CodeInfo> list = selectList(wrapper);
            CodeDTO codeDTO = new CodeDTO();
            codeDTO.setCodeKind(codeKind);
            codeDTO.setCodeInfoList(list);
            return ResultUtil.success(codeDTO);
        }
        else {
            return ResultUtil.error(Result.ERROR, "传入数据错误");
        }
    }

    public List<String> getAllCodeByKindCode(String codeKindCode) {
        List<String> res=new ArrayList<>();
        CodeKind codeKindParam = new CodeKind();
        codeKindParam.setCode(codeKindCode);
        CodeKind codeKind = codeKindBusiness.selectOne(new EntityWrapper<>(codeKindParam));
        if (null != codeKind) {
            CodeInfo codeInfoParam = new CodeInfo();
            codeInfoParam.setCodeKindId(codeKind.getId());
            Wrapper<CodeInfo> wrapper = new EntityWrapper<>(codeInfoParam).orderBy("sort");
            List<CodeInfo> list = selectList(wrapper);
            list.stream().forEach(c->res.add(c.getCode()));
        }
        return res;
    }


    public String getIdByName(String codeKindId,String name) {
        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setCodeKindId(codeKindId);
        codeInfo.setName(name);
        codeInfo = selectOne(new EntityWrapper<>(codeInfo));
        return codeInfo.getId();
    }

    public CodeInfo getById(String id) {
        CodeInfo entity = selectById(id);
        if (null == entity) {
            throw new YudaoException(Result.ERROR, "No Data");
        }
        else {
            return entity;
        }
    }
}
