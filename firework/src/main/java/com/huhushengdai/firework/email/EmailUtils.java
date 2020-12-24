package com.huhushengdai.firework.email;

import android.os.AsyncTask;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * Date： 2020/4/2
 * Description:
 *
 * @author WangLizhi
 * @version 1.0
 */
public class EmailUtils {
    private static final String from = "huhushengdai@163.com";
    private static final String to = "yuexialaowang@163.com";
    private static final String password = "GRGSRBMAESDXNYKV";
    private static final String title = "月下的报告";

    public static void send(String content) {
        send(from, to, password, title, content);
    }

    public static void syncSend(String content){
        syncSend(from, to, password, title, content);
    }

    public static void send(final String from,
                            final String to,
                            final String password,
                            final String title,
                            final String content) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                syncSend(from, to, password, title, content);
            }
        });
    }

    public static void syncSend(final String from,
                                final String to,
                                final String password,
                                final String title,
                                final String content){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com");
        Session session = Session.getInstance(props, null);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setText(content);
            Transport.send(msg, from, password);
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }
}
