package com.yudao.lianying.v1.file.serviceImpl;

import com.yudao.common.yudaocommon.enums.ResultEnum;
import com.yudao.common.yudaocommon.enums.StatusValueEnum;
import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import com.yudao.common.yudaocommon.utils.ResultUtil;
import com.yudao.lianying.utils.FtpUtils;
import com.yudao.lianying.v1.file.api.FileServiceApi;
import com.yudao.lianying.v1.file.biz.FileBusiness;
import com.yudao.lianying.v1.file.dao.FileInfo;
import com.yudao.lianying.v1.file.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;


@RestController
@Service
@Slf4j
public class FileServiceImpl implements FileServiceApi {

    @Autowired
    private FileBusiness fileBusiness;


    @Value("${file.path}")
    private String prefixPath;
    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.port}")
    private String ftpPort;
    @Value("${ftp.httpAddress}")
    private String ftpHttpAddress;

    @Value("${ftp.httpAddress2}")
    private String ftpHttpAddress2;


    /**
     * 上传文件
     * @param request
     * @param response
     * @param fileVO
     * @return
     */
    @Override
    public Result insertFile(HttpServletRequest request, HttpServletResponse response, FileVO fileVO) {
        /*
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        */
        try {
            if (null == fileVO.getType()) {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getStatus(), "文件上传的形式不明确");
            }
            //应用名称会作为上传服务器的路径
            String appName = fileVO.getAppName();
            if (StringUtils.isBlank(appName)) {
                return ResultUtil.error(ResultEnum.PARAM_ERROR.getStatus(), "文件被使用的应用名称不能为空");
            }
            String fileId = UUID.randomUUID().toString();
            String originalname;
            long fileSize;
            String fileFormat;
            // 判断是否是加密之后的图片
            if (fileVO.getType() == 2) {
                fileSize = FtpUtils.imageSize(fileVO.getImgBaseStr());
                if (fileSize == 0) {
                    return ResultUtil.error(ResultEnum.PARAM_ERROR.getStatus(), "文件不能为空");
                }
                originalname = fileVO.getOriginalname();
                if (StringUtils.isBlank(originalname)) {
                    return ResultUtil.error(ResultEnum.PARAM_ERROR.getStatus(), "Base64图片缺少文件名");
                }
                fileFormat = originalname.substring(originalname.lastIndexOf(".") + 1);
                boolean b = FtpUtils.generateImage(fileVO.getImgBaseStr(), fileId + "." + fileFormat, appName);
                if (!b) {
                    log.info("上传文件失败，文件名为{}！", originalname);
                    return ResultUtil.error(Result.ERROR, "上传文件失败");
                }
            } else {
                MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
                log.info("上传文件入参：文件大小=" + multi.getFile("file").getSize());
                MultipartFile file = multi.getFile("file");
                if (file.getSize() < 0) {
                    return ResultUtil.error(ResultEnum.PARAM_ERROR.getStatus(), "文件不能为空");
                }
                fileSize = file.getSize();
                originalname = file.getOriginalFilename();
                fileFormat = originalname.substring(originalname.lastIndexOf(".") + 1);
                InputStream inputStream = file.getInputStream();

                // 连接ftp
                boolean boo = FtpUtils.uploadFileToFtp(inputStream, fileId + "." + fileFormat, appName);
                if (!boo) {
                    log.info("上传文件失败，文件名为{}！", originalname);
                    return ResultUtil.error(Result.ERROR, "上传文件失败");
                }

            }

            FileInfo fileInfo = new FileInfo();
            BeanUtils.copyProperties(fileVO, fileInfo);
            if (null == fileInfo.getWordNum() || fileInfo.getWordNum() == 0) {
                //todo 解析文档算出字数
                fileInfo.setWordNum(1000);
            }
            //上传服务器的文件名和文件表的主键用一个uuid
            fileInfo.setId(fileId);
            fileInfo.setOriginalname(originalname);
            fileInfo.setAppName(appName);
            fileInfo.setSize(fileSize);
            fileInfo.setName(fileId + "." + fileFormat);
            fileInfo.setFileSuffix(fileFormat);
            fileInfo.setCreateTime(new Date());
            fileInfo.setStatus(StatusValueEnum.normal.getValue());
            log.info("保存的文件参数{}", fileInfo);
            String httpPath = ftpHttpAddress + appName + "/" + fileId + "." + fileFormat;
            String ftpPath = "ftp://" + ftpHost + ":" + ftpPort + "/" + appName + "/" + fileId + "." + fileFormat;
            fileInfo.setFtpPath(ftpPath);
            fileInfo.setHttpPath(httpPath);
            boolean b = fileBusiness.insert(fileInfo);
            if (b) {
                return ResultUtil.success(fileInfo);
            } else {
                return ResultUtil.error(Result.ERROR, "上传文件失败");
            }
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return ResultUtil.error(Result.ERROR, "上传文件失败");
        }
    }

//    @Override
//    public Result download(String fileId, HttpServletResponse response) {
//        try {
//            fileBusiness.downloadById(fileId, response);
//            return ResultUtil.success();
//        } catch (YudaoException e) {
//            log.error("", e);
//            return ResultUtil.error(e);
//        } catch (Exception e) {
//            log.error("", e);
//            return ResultUtil.error(ResultEnum.SERVER_ERROR);
//        }
//    }

    @Override
    public Result download(String httpPath, HttpServletResponse response) {
        try {
            if(httpPath.startsWith(ftpHttpAddress) || httpPath.startsWith(ftpHttpAddress2)) {
                fileBusiness.download(httpPath, response);
                return ResultUtil.success();
            }
            else{
                return ResultUtil.error(400,"file error");
            }

        } catch (YudaoException e) {
            log.error("", e);
            return ResultUtil.error(e);
        } catch (Exception e) {
            log.error("", e);
            return ResultUtil.error(ResultEnum.SERVER_ERROR);
        }
    }
}
