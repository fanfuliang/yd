package com.yudao.lianying.utils;

import com.yudao.common.yudaocommon.exception.YudaoException;
import com.yudao.common.yudaocommon.model.Result;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FtpUtils {
    private static Logger log = LoggerFactory.getLogger(FtpUtils.class);

    private static final String ANONYMOUS_LOGIN = "root";
    /*
     private FTPClient ftpClient = new FTPClient();
     private boolean is_connected = false;*/
    private static String ftpUsername;

    private static String ftpPassword;

    private static String ftpHost;

    private static int ftpPort;

    @Value("${ftp.username}")
    public void setFtpUsername(String username) {
        ftpUsername = username;
    }

    @Value("${ftp.port}")
    public void setFtpPort(int port) {
        ftpPort = port;
    }

    @Value("${ftp.host}")
    public void setFtpServer(String host) {
        ftpHost = host;
    }

    @Value("${ftp.password}")
    public void setFtpPassword(String password) {
        ftpPassword = password;
    }

    private static Integer defaulttimeoutsecond;
    private static Integer datatimeoutsecond;

    @Value("${ftp.defaultTimeoutSecond}")
    public void setDefaultTimeoutSecond(int defaultTimeoutSecond) {
        defaulttimeoutsecond = defaultTimeoutSecond;
    }

    @Value("${ftp.dataTimeoutSecond}")
    public void setDataTimeoutSecond(int dataTimeoutSecond) {
        datatimeoutsecond = dataTimeoutSecond;
    }

    private static FTPClient getFTPClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setDefaultTimeout(defaulttimeoutsecond * 1000);
            ftpClient.setDataTimeout(datatimeoutsecond * 1000);
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
        } catch (UnknownHostException ex) {
            throw new IOException("Can't find FTP server '" + ftpHost + "'");
        }

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect(ftpClient);
            throw new IOException("Can't connect to server '" + ftpHost + "'");
        }

        if (ftpUsername == "") {
            ftpUsername = ANONYMOUS_LOGIN;
        }
        // 登陆FTP服务器
        if (!ftpClient.login(ftpUsername, ftpPassword)) {
//            is_connected = false;
            disconnect(ftpClient);
            throw new IOException("Can't login to server '" + ftpHost + "'");
        } else {
//            is_connected = true;
        }
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        return ftpClient;
    }

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    private static void disconnect(FTPClient ftpClient) throws IOException {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String fileName, String path) {
        if (imgStr == null) {
            return false;
        }
        try {
            File file = new File(path);
            InputStream inputStream = null;
            // 判断文件夹是否存在，不存在则创建
            if (!file.exists()) {
                file.mkdirs();
            }
            if (imgStr.startsWith("data:image/")) {
                imgStr = imgStr.substring(imgStr.indexOf(",") + 1); // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。
            }
//        BASE64Decoder decoder = new BASE64Decoder();
            FTPClient ftpClient = getFTPClient();
            try {
                // 解密
                byte[] b = Base64.decodeBase64(imgStr);
                // 处理数据
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }

                inputStream = new ByteArrayInputStream(b);
                Boolean sucPath = createDirecroty(ftpClient, path);
                // 设置工作路径
                Boolean sucDirectory = setWorkingDirectory(ftpClient, path);
                if (sucPath && sucDirectory) {
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.storeFile(fileName, inputStream);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    disconnect(ftpClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 加密
//        BASE64Encoder encoder = new BASE64Encoder();
        return Base64.encodeBase64String(data);
    }

    /**
     * 通过图片base64流判断图片等于多少字节
     * image 图片流
     */
    public static long imageSize(String image) {
        try {
            if (image.startsWith("data:image/")) {
                image = image.substring(image.indexOf(",") + 1); // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。
            }
            Integer equalIndex = image.indexOf("=");//2.找到等号，把等号也去掉
            if (image.indexOf("=") > 0) {
                image = image.substring(0, equalIndex);
            }
            Integer strLength = image.length();//3.原来的字符流大小，单位为字节
            Integer size = strLength - (strLength / 8) * 2;//4.计算后得到的文件流大小，单位为字节
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean uploadFileToFtp(InputStream inputStream, String fileName, String ftp_path) {
        try {
            FTPClient ftpClient = getFTPClient();
            try {
                Boolean sucPath = createDirecroty(ftpClient, ftp_path);
                // 设置工作路径
                Boolean sucDirectory = setWorkingDirectory(ftpClient, ftp_path);

                // 上传文件 参数：上传后的文件名，输入流
                if (sucPath && sucDirectory) {
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.storeFile(fileName, inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    disconnect(ftpClient);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /*public static boolean uploadFileToFtp1(InputStream inputStream, String fileName, String ftp_path) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接ftp服务器 参数填服务器的ip
            ftpClient.connect(ftpHost, ftpPort);

            // 进行登录 参数分别为账号 密码
            ftpClient.login(ftpUsername, ftpPassword);

            // 设置文件类型为二进制文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 开启被动模式（按自己如何配置的ftp服务器来决定是否开启）
            ftpClient.enterLocalPassiveMode();
            *//*String [] paths = ftp_path.split("/");

            // 只能一级级创建，每创建完成就进一层
            for (String path : paths) {
                if (StringUtils.isNotBlank(path)) {
                    ftpClient.makeDirectory(path);
                    // 改变工作目录（按自己需要是否改变）,进入到文件保存的目录,只能选择local_root下已存在的目录
                    ftpClient.changeWorkingDirectory(path);
                }
            }*//*
            String[] dirs = ftp_path.split("/");
            // 切换到 FTP 根目录
            ftpClient.changeWorkingDirectory("/");
            // 按顺序检查目录是否存在，不存在则创建目录
            for (int i = 0; dirs != null && i < dirs.length; i++) {
                // 目录不存在则创建目录
                if (!ftpClient.changeWorkingDirectory(dirs[i])) {
                    if (ftpClient.makeDirectory(dirs[i])) {
                        // 目录创建成功切换到新创建的目录
                        if (!ftpClient.changeWorkingDirectory(dirs[i])) {
//                            return false;
                        }
                    } else {
//                        return false;
                    }
                }
            }

            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.changeWorkingDirectory("/");
            ftpClient.changeWorkingDirectory(ftp_path);
            // 上传文件 参数：上传后的文件名，输入流
            ftpClient.storeFile(fileName, inputStream);
        } catch (IOException e) {
            return false;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ioe) {

                }
            }
        }
        return true;
    }*/

    /**
     * 将ftp文件读成流进行再封装
     * @param ftpPath
     * @return
     */
    public static InputStream download(String ftpPath) {
        String path = ftpPath.substring(ftpPath.indexOf("/", 6), ftpPath.lastIndexOf("/") + 1);
        String name = ftpPath.substring(ftpPath.lastIndexOf("/") + 1);
        return download(path, name);
    }

    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }

    private static InputStream download(String path, String name) {
        // 连接ftp服务器 参数填服务器的ip
        InputStream inputStream = null;
        try {
            FTPClient ftpClient = getFTPClient();
            try {
                // 设置工作路径
                ftpClient.setRemoteVerificationEnabled(false);
                ftpClient.changeWorkingDirectory("/");
                if (ftpClient.changeWorkingDirectory(path)) {
                    // 从 FTP 服务器获取文件流
                    ftpClient.setBufferSize(2048);
                    ftpClient.setControlEncoding("UTF-8");
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    // 开启被动模式（按自己如何配置的ftp服务器来决定是否开启）
                    ftpClient.enterLocalPassiveMode();
                    InputStream is = ftpClient.retrieveFileStream(name);
                    // 复制一个流
                    ByteArrayOutputStream baos = cloneInputStream(is);
                    inputStream = new ByteArrayInputStream(baos.toByteArray());
                    // 完成文件传输必须调用completePendingCommand，completePendingCommand会一直在等FTP Server返回226 Transfer complete，但是FTP Server只有在接受到InputStream 执行close方法时，才会返回
                    is.close();
                    ftpClient.completePendingCommand();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    disconnect(ftpClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 实现文件打包压缩下载
     *
     * @param response            服务器响应对象HttpServletResponse
     * @param zipname             压缩包的文件名
     * @param ftpAbsolutePathList ftp文件路径
     * @param ftpFileList         要下载的文件名集合
     * @param namelist            压缩后的文件名集合
     */
    public static boolean zipDownloadFile(HttpServletResponse response, String zipname, List<String> ftpAbsolutePathList, List<String> ftpFileList, List<String> namelist) {
        String location = "1";
        byte[] buf = new byte[1024];
        File zipFile = new File(zipname);
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient();
            try {
//            FTPClient ftp = getFTPClient();
                ftpClient.setControlEncoding("UTF-8");
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftpClient.enterLocalPassiveMode();
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
                for (int i = 0; i < ftpFileList.size(); i++) {
                    boolean b = ftpClient.changeWorkingDirectory(ftpAbsolutePathList.get(i));
                    if (!b) {
                        log.error("切换工作目录失败");
                        zipOut.close();
                        throw new YudaoException(Result.ERROR, "文件下载失败");
                    }
                    ZipEntry entry = new ZipEntry(namelist.get(i));
                    zipOut.putNextEntry(entry);
                    // 开启被动模式，否则在linux环境下会被阻塞
                    ftpClient.enterLocalPassiveMode();
//                    InputStream bis = ftpClient.retrieveFileStream(new String(ftpFileList.get(i).getBytes("UTF-8"), "iso-8859-1"));
                    InputStream bis = ftpClient.retrieveFileStream(ftpFileList.get(i));
                    if (bis != null) {
                        int readLen = -1;
                        while ((readLen = bis.read(buf, 0, 1024)) != -1) {
                            zipOut.write(buf, 0, readLen);
                        }
                        zipOut.closeEntry();
                        bis.close();
                        ftpClient.completePendingCommand();
                        //调用ftp.retrieveFileStream这个接口后，一定要手动close掉返回的InputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则会导致后面对FTPClient的操作都失败
                    } else {
                        log.error("流为空");
//                        bis.close();
                        zipOut.close();
                        throw new YudaoException(Result.ERROR, "文件下载失败");
                    }
                }
                zipOut.close();
//            disconnect();
                //下载
                int len;
                FileInputStream zipInput = new FileInputStream(zipFile);
                OutputStream out = response.getOutputStream();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(zipname, "UTF-8") + ".zip");
                while ((len = zipInput.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                zipInput.close();
                out.flush();
                out.close();
                //删除压缩包
                zipFile.delete();
                return true;
            } catch (Exception e) {
//            logger.error("文件打包下载有误: " + e.getLocalizedMessage());
                //删除压缩包
                zipFile.delete();
                return false;
            } finally {
                disconnect(ftpClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 创建目录
     *
     * @param remote
     * @return
     * @throws IOException
     */
    private static boolean createDirecroty(FTPClient ftpClient, String remote) throws IOException {
        boolean success = true;
        try {
            String[] dirs = remote.split("/");
            // 切换到 FTP 根目录
            ftpClient.changeWorkingDirectory("/");
            // 按顺序检查目录是否存在，不存在则创建目录
            for (int i = 0; dirs != null && i < dirs.length; i++) {
                // 目录不存在则创建目录
                if (!ftpClient.changeWorkingDirectory(dirs[i])) {
                    if (ftpClient.makeDirectory(dirs[i])) {
                        // 目录创建成功切换到新创建的目录
                        if (!ftpClient.changeWorkingDirectory(dirs[i])) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 设置工作路径
     *
     * @param dir
     * @return
     */
    private static boolean setWorkingDirectory(FTPClient ftpClient, String dir) {
        // 如果目录不存在创建目录
        try {
            if (createDirecroty(ftpClient, dir)) {
                // 先切换到根目录再切换到目标目录
                ftpClient.setRemoteVerificationEnabled(false);
                ftpClient.changeWorkingDirectory("/");
                return ftpClient.changeWorkingDirectory(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
