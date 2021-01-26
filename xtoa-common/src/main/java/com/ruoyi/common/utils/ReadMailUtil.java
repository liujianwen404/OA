package com.ruoyi.common.utils;

import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * 读取邮箱邮件
 * **/
public class ReadMailUtil {
    final static String USER = "liujianwen.404@qq.com"; // 用户名
    final static String PASSWORD = "uyevzpdnhyyscbbc"; // 密码
    public final static String MAIL_SERVER_HOST = "pop.qq.com"; // 邮箱服务器
    public final static String TYPE_HTML = "text/html;charset=UTF-8"; // 文本内容类型
    public final static String MAIL_FROM = "[email protected]"; // 发件人
    public final static String MAIL_TO = "[email protected]"; // 收件人
    public final static String MAIL_CC = "[email protected]"; // 抄送人
    public final static String MAIL_BCC = "[email protected]"; // 密送人
    @SneakyThrows
    public static boolean read(String sendMail) {
        // 创建一个有具体连接信息的Properties对象
        Properties prop = new Properties();
        prop.setProperty("mail.debug", "true");
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", MAIL_SERVER_HOST);
        // 1、创建session
        Session session = Session.getInstance(prop);
        // 2、通过session得到Store对象
        Store store = session.getStore();
        // 3、连上邮件服务器,POP3服务器的登陆认证
        store.connect(MAIL_SERVER_HOST, USER, PASSWORD);
        // 4、获得邮箱内的邮件夹,注意通过pop3协议获取某个邮件夹的名称只能为inbox
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        // 获得邮件夹Folder内的所有邮件Message对象
        Message[] messages = folder.getMessages();
        System.out.println("收件箱中共" + messages.length + "封邮件!");
        System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
        System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
        System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");
        for (int i = 0; i < messages.length; i++) {
            String subject = messages[i].getSubject();
            String from = messages[i].getFrom()[0].toString();
            if(from.contains(sendMail)){
                return true;
            }
        }
        // 5、关闭
        folder.close(false);
        store.close();
        return false;
    }
}
