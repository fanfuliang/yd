package com.yudao.lianying.v1.file.biz;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yudao.common.yudaocommon.utils.DownloadUtil;
import com.yudao.lianying.v1.file.dao.FileInfo;
import com.yudao.lianying.v1.file.dao.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;


/**
 * @program: yudao
 * @description:
 * @author: liudong
 * @create: 2020-06-29 18:27:01
 */
@Service
@Slf4j
public class FileBusiness extends ServiceImpl<FileMapper, FileInfo> {

    public void downloadById(String fileId, HttpServletResponse response) throws Exception {
        FileInfo fileInfo = this.selectById(fileId);
        if(fileInfo!=null)
        {
            String fileHttpPath=fileInfo.getHttpPath();
            download(fileHttpPath,response);
        }

    }

    public void download(String httpPath, HttpServletResponse response) throws Exception {
        InputStream is = DownloadUtil.download(httpPath);
        //2、创建字节输出流
        ServletOutputStream sos = response.getOutputStream();
        //3、得到下载的文件名
        String filename = httpPath.substring(httpPath.lastIndexOf("/") + 1);//得到的文件名为TomCat.png
        //4、设置文件编码
        filename = URLEncoder.encode(filename, "UTF-8");//编码为UTF-8
        //5、告知客户端(浏览器)要下载文件
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        response.setHeader("content-type", "image/png");//文件类型
        //6、输出
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = is.read(b)) != -1) {
            sos.write(b, 0, len);
        }
        sos.close();
        is.close();
    }
}

