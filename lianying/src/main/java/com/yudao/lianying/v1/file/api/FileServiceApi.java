package com.yudao.lianying.v1.file.api;

import com.yudao.common.yudaocommon.annotation.ParameterValidtion;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.lianying.v1.file.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 会议资源  服务类
 * </p>
 *
 * @author wufei
 * @since 2019-04-26
 */
@Api(value="文件",tags={"文件操作接口"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping("/file")
public interface FileServiceApi {
    @ApiOperation(value = "上传文件")
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.POST}, origins="*")
    @RequestMapping(value = "/insertFile", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", required = true, value = "文件上传的形式，1-文件 2-base64加密字符串"),
            @ApiImplicitParam(name = "file", value = "接收上传文件的参数，MultipartFile获取文件的名字"),
            @ApiImplicitParam(name = "imgBaseStr",value = "图片base64字符串"),
            @ApiImplicitParam(name = "appName",required = true,value = "app名称"),
            @ApiImplicitParam(name = "localPath",value = "本地路径"),
            @ApiImplicitParam(name = "originalname",value = "当传base64数据时，原文件名称"),
            @ApiImplicitParam(name = "wordNum",value = "字数"),
    })
    Result insertFile(HttpServletRequest request, HttpServletResponse response, FileVO fileVO);

    @ApiOperation(value = "下载")
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET}, origins="*")
    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "httpPath", required = true, value = ""),
    })
    Result download(@ParameterValidtion(notEmpty = true, name = "httpPath") String httpPath, HttpServletResponse response);

//    @ApiOperation(value = "下载")
//    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET}, origins="*")
//    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "fileId", required = true, value = ""),
//    })
//    Result download(@ParameterValidtion(notEmpty = true, name = "fileId") String fileId, HttpServletResponse response);

}

