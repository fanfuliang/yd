package com.yudao.lianying.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import com.yudao.lianying.v1.email.biz.EmailConfigBusiness;
import com.yudao.lianying.v1.email.dao.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author ：徐悦
 * @date ：Created in 2019/7/8 16:48
 * @description：
 * @modified By：
 * @version: $
 */
@Component
@Slf4j
public class MailUtils {



    public static String getMailhost() {
        return mailhost;
    }

    public static String getMailPort() {
        return mailPort;
    }

    public static String getFromMail() {
        return fromMail;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getNick() {
        return nick;
    }

    private static String mailhost;

    private static String mailPort;

    private static String fromMail;

    private static String username;

    private static String password;

    private static String nick;

    private static String filePathPrefix;

    private static EmailConfigBusiness emailConfigBusiness;

    private static String profile;

    public static String getProfile() {
        return profile;
    }

    @Value("${spring.profiles.active}")
    private void setProfile(String profile){MailUtils.profile=profile;}

    @Autowired
    public void setEmailConfigBusiness(EmailConfigBusiness emailConfigBusiness) {
        MailUtils.emailConfigBusiness = emailConfigBusiness;
    }

    @Value("${file.path}")
    public void setFilePathPrefix(String filePathPrefix) {
        MailUtils.filePathPrefix = filePathPrefix;
    }


    /**
     * 发送邮件
     *
     * @param to       收件人列表，以","分割
     * @param to       抄送人列表，以","分割
     * @param subject  标题
     * @param body     内容
     * @param filepath 附件列表,无附件传递null
     * @return
     * @throws MessagingException
     * @throws AddressException
     * @throws UnsupportedEncodingException
     */
    public static boolean sendMail1(String to, String cc, String subject, String body, List<String> filepath) {
        try {
            // 参数修饰
            if (body == null) {
                body = "";
            }
            if (subject == null) {
                subject = "无主题";
            }
            // 创建Properties对象
            Properties props = System.getProperties();
            // 创建信件服务器
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.auth", "true"); // 通过验证
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", mailPort);
            props.put("mail.smtp.starttls.enable", "true");
            //props.put("mail.debug", "true");
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException e1) {
                e1.printStackTrace();
            }
            props.put("mail.smtp.ssl.socketFactory", sf);
            // 得到默认的对话对象
            Session session = Session.getDefaultInstance(props, null);
            // 创建一个消息，并初始化该消息的各项元素
            MimeMessage msg = new MimeMessage(session);
            nick = MimeUtility.encodeText(nick);
            msg.setFrom(new InternetAddress(nick + "<" + fromMail + ">"));

            if (StringUtils.isNotBlank(cc)) {
                String[] arr = cc.trim().split(",");
                InternetAddress[] ccAddress = new InternetAddress[arr.length];
                for (int k = 0; k < arr.length; k++) {
                    String emailAddress = arr[k];
                    new InternetAddress(emailAddress);
                    ccAddress[k] = new InternetAddress(emailAddress);
                }
                msg.addRecipients(Message.RecipientType.CC, ccAddress);
            }
            // 创建收件人列表
            if (to != null && to.trim().length() > 0) {
                String[] arr = to.split(",");
                int receiverCount = arr.length;
                if (receiverCount > 0) {
                    InternetAddress[] address = new InternetAddress[receiverCount];
                    for (int i = 0; i < receiverCount; i++) {
                        address[i] = new InternetAddress(arr[i]);
                    }
                    msg.addRecipients(Message.RecipientType.TO, address);
                    msg.setSubject(subject);
                    // 后面的BodyPart将加入到此处创建的Multipart中
                    Multipart mp = new MimeMultipart();
                    // 附件操作
                    if (filepath != null && filepath.size() > 0) {
                        for (String filename : filepath) {
                            MimeBodyPart mbp = new MimeBodyPart();
                            // 得到数据源
                            FileDataSource fds = new FileDataSource(filename);
                            // 得到附件本身并至入BodyPart
                            mbp.setDataHandler(new DataHandler(fds));
                            // 得到文件名同样至入BodyPart
                            mbp.setFileName(fds.getName());
                            mp.addBodyPart(mbp);
                        }
                        MimeBodyPart mbp = new MimeBodyPart();
                        mbp.setText(body);
                        mp.addBodyPart(mbp);
                        // 移走集合中的所有元素
                        filepath.clear();
                        // Multipart加入到信件
                        msg.setContent(mp);
                    }
                    else {
                        // 设置邮件正文
                        msg.setText(body);
                    }
                    msg.setText(body);
                    // 设置信件头的发送日期
                    msg.setSentDate(new Date());
                    msg.saveChanges();
                    // 发送信件
                    Transport transport = session.getTransport("smtp");
                    transport.connect(username, password);
                    transport.sendMessage(msg,
                            msg.getRecipients(Message.RecipientType.TO));
                    transport.close();
                    return true;
                }
                else {
                    log.warn("接收邮箱为空！");
//                    System.out.println("None receiver!");
                    return false;
                }

            }
            else {
                log.warn("接收邮箱为空！");
//                System.out.println("None receiver!");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送邮件出错", e);
            return false;
        }
    }

    public static void asynSendMail(String to, String cc, String subject, String body, String filePaths) {
        if(!MailUtils.getProfile().equals("ly"))
        {
            return;
        }
        setEmailConfig();
        Thread thread = new Thread(() -> {
            try {
                MailUtils.sendMail(to, cc, subject, body, filePaths);
            } catch (Exception e) {
                log.error("邮件发送失败", e);
            }
        });
        thread.start();
    }

    private static void setEmailConfig() {
        EmailConfig emailConfig = emailConfigBusiness.getDefaultEmailConfig();
        MailUtils.mailhost = emailConfig.getHost();
        MailUtils.mailPort = emailConfig.getPort();
        MailUtils.fromMail = emailConfig.getFromEmail();
        MailUtils.username = emailConfig.getUsername();
        MailUtils.password = emailConfig.getPassword();
        MailUtils.nick = emailConfig.getNickname();
    }

    /**
     * @param to        收件箱，逗号分隔
     * @param cc        抄送人，逗号分隔
     * @param subject   主题
     * @param body      内容
     * @param filePaths 附件地址，逗号拼接
     * @return
     * @throws Exception
     */
    public static boolean sendMail(String to, String cc, String subject, String body, String filePaths) throws Exception {
        Properties prop = new Properties();
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //协议
        prop.setProperty("mail.transport.protocol", "smtp");
        //服务器
        prop.setProperty("mail.smtp.host", mailhost);
        //端口
        prop.setProperty("mail.smtp.port", mailPort);
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            log.error("", e);
        }
        prop.put("mail.smtp.ssl.enable", "false");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        //
        //获取Session对象
        Session s = Session.getDefaultInstance(prop, new Authenticator() {
            //此访求返回用户和密码的对象
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                PasswordAuthentication pa = new PasswordAuthentication(username, password);
                return pa;
            }
        });
        //设置session的调试模式，发布时取消
        s.setDebug(false);
        MimeMessage mimeMessage = new MimeMessage(s);

        if (StringUtils.isNotBlank(cc)) {
            String[] arr = cc.trim().split(",");
            InternetAddress[] ccAddress = new InternetAddress[arr.length];
            for (int k = 0; k < arr.length; k++) {
                String emailAddress = arr[k];
                new InternetAddress(emailAddress);
                ccAddress[k] = new InternetAddress(emailAddress);
            }
            mimeMessage.addRecipients(Message.RecipientType.CC, ccAddress);
        }
        MimeBodyPart content = new MimeBodyPart();
        content.setContent(body, "text/html;charset=UTF-8");
        mimeMessage.setFrom(new InternetAddress(nick + "<" + fromMail + ">"));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        //设置主题
        mimeMessage.setSubject(subject);
        mimeMessage.setSentDate(new Date());
//            MimeMultipart mp = new MimeMultipart();
        // 附件操作
        List<String> filepath = new ArrayList<>();
        if (StringUtils.isNotBlank(filePaths)) {
            String[] filePathArr = filePaths.split(",");
            filepath = new ArrayList<>(Arrays.asList(filePathArr));
        }
        List<String> filenames = new ArrayList<>();
        //整封邮件的MINE消息体
//            MimeMultipart msgMultipart = new MimeMultipart("mixed");//混合的组合关系
        MimeMultipart mixed = new MimeMultipart();
        mixed.addBodyPart(content);
        if (filepath.size() > 0) {
            for (String filename : filepath) {
                MimeBodyPart mbp = new MimeBodyPart();
                String name = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
                String localPath = filePathPrefix + "email/";
                boolean download = download(filename, name, localPath);
//                    log.info("\n\n"+download);
                if (!download) {
                    continue;
                }
                // 得到数据源
                FileDataSource fds = new FileDataSource(localPath + name);
                // 得到附件本身并至入BodyPart
                mbp.setDataHandler(new DataHandler(fds));
                // 得到文件名同样至入BodyPart
                mbp.setFileName(fds.getName());
//                    mp.addBodyPart(mbp);
                mixed.addBodyPart(mbp);
                filenames.add(localPath + name);
                // 之后删除文件
                    /*File file = new File(localPath + name);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }*/
            }
//                MimeBodyPart mbp = new MimeBodyPart();
////                mbp.setText(body);
//                mp.addBodyPart(mbp);
//                mp.setSubType("mixed");
            // 移走集合中的所有元素
//                filepath.clear();
            // Multipart加入到信件
        }
        mimeMessage.setContent(mixed);

        //设置内容
//            mimeMessage.setText(body);
        mimeMessage.saveChanges();
        //发送
        Transport.send(mimeMessage);
        if (!CollectionUtils.isEmpty(filenames)) {
            for (String filename : filenames) {
                File file = new File(filename);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
        }
        return true;
    }

    public static boolean download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
//        System.out.println("\n\n" +filename);
        OutputStream os = new FileOutputStream(sf.getPath() + "/" + filename);
//        log.info(sf.getPath() + filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return true;
    }
}
