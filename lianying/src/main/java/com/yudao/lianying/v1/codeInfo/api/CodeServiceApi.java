package com.yudao.lianying.v1.codeInfo.api;


import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.codeInfo.dao.CodeInfo;
import com.yudao.lianying.v1.codeInfo.vo.CodeInfoVO;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author
 * @since 2020-07-06
 */
@Api(tags = "code")
@RequestMapping(value = "/code", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface CodeServiceApi {

    @ApiOperation(value = "保存")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    Result save(CodeInfoVO entityVO);

    @ApiOperation(value = "根据id删除，允许传入多个id-自动生成")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.POST}, origins = "*")
    @RequestMapping(value = "/removeByIdAuto", method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "产品线主键，多个id以逗号分隔", required = true),
    })
    Result removeByIdAuto(@ParameterValidtion(notEmpty = true, name = "ids") String ids);

    @GetMapping("/selectAllCodeByKindCode")
    @ApiOperation(value = "根据codeKind的code值查询所有code对象")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @ApiImplicitParam(name = "codeKindCode", required = true, value = "codeKind的code值")
    Result selectAllCodeByKindCode(@ParameterValidtion(name = "codeKindCode", notEmpty = true) String codeKindCode);

    @GetMapping("/selectCodeKindByKindId")
    @ApiOperation(value = "根据codeKindid查询codeKind对象")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @ApiImplicitParam(name = "codeKindId", required = true, value = "codeKind的id")
    Result selectCodeKindByKindId(@ParameterValidtion(name = "codeKindId", notEmpty = true) String codeKindId);

    @GetMapping("/selectCodeKindByKindCode")
    @ApiOperation(value = "根据codeKind的code查询codeKind对象")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @ApiImplicitParam(name = "codeKindCode", required = true, value = "codeKind的code值")
    Result selectCodeKindByKindCode(@ParameterValidtion(name = "codeKindCode", notEmpty = true) String codeKindCode);

    @GetMapping("/selectCodeInfoByInfoId")
    @ApiOperation(value = "根据codeInfo的id查询codeInfo对象")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET}, origins = "*")
    @ApiImplicitParam(name = "codeInfoId", required = true, value = "codeInfo的Id")
    Result selectCodeInfoByInfoId(@ParameterValidtion(name = "codeInfoId", notEmpty = true) String codeInfoId);

}

